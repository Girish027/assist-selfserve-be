package com.tfsc.ilabs.selfservice.orchestrator.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;
import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionPollingConfig;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.dto.UserAudit;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.utils.*;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityIdLookup;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityIdLookupRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler.DependencyGraph;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.ResponseCallback;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Created by ravi.b on 01-08-2019.
 */
public class WorkflowOrchestratorTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowOrchestratorTask.class);
    protected WorkflowInstance workflowInstance;
    protected String clientId;
    protected String accountId;
    private WorkflowService workflowService;
    private EntityService entityService;
    private ActionExecutorService actionExecutorService;
    private ExecutorService executorService;
    private DBConfigService dbConfigService;
    private Set<DependencyGraph<ActionExecutionMonitor>> graphSet;
    private SSAuthenticationToken authenticationToken;
    private ResponseCallback responseCallback;
    private EntityIdLookupRepository entityIdLookupRepository;

    public WorkflowOrchestratorTask(WorkflowService workflowService, EntityService entityService, ActionExecutorService actionExecutorService,
                                    ExecutorService executorService, DBConfigService dbConfigService, EntityIdLookupRepository entityIdLookupRepository,
                                    WorkflowInstance workflowInstance, Set<DependencyGraph<ActionExecutionMonitor>> graphSet,
                                    String clientId, String accountId, SSAuthenticationToken authenticationToken) {
        this.workflowService = workflowService;
        this.entityService = entityService;
        this.actionExecutorService = actionExecutorService;
        this.executorService = executorService;
        this.dbConfigService = dbConfigService;
        this.workflowInstance = workflowInstance;
        this.graphSet = graphSet;
        this.clientId = clientId;
        this.accountId = accountId;
        this.authenticationToken = authenticationToken;
        this.entityIdLookupRepository = entityIdLookupRepository;
    }

    @Override
    public void run() {
        graphSet.forEach(graph -> executeActions(graph.start(), graph));
    }

    private void executeActions(Set<ActionExecutionMonitor> actionExecutionMonitors, DependencyGraph<ActionExecutionMonitor> graph) {
        logger.info("Starting execution for action ");
        actionExecutionMonitors.forEach(getActionExecutionMonitorConsumer(graph));
    }

    private Consumer<ActionExecutionMonitor> getActionExecutionMonitorConsumer(DependencyGraph<ActionExecutionMonitor> graph) {
        return actionExecutionMonitor -> {
            ActionTask actionTask = new ActionTask(actionExecutorService, entityService, workflowInstance, actionExecutionMonitor,
                    new ResponseCallback() {
                        @Override
                        public void onResponseSuccess() {
                            logger.info("{} ::  Calling on Success callback for actionExecutionMonitor :: {}",
                                    Thread.currentThread().getName(), actionExecutionMonitor.getId());
                            onActionComplete(getCurrentActionExecutionMonitor(), graph);
                        }

                        @Override
                        public void onResponseFailure() {
                            onFailureEvent(getCurrentActionExecutionMonitor(), graph);
                        }

                        private ActionExecutionMonitor getCurrentActionExecutionMonitor() {
                            return actionExecutorService.getActionExecutionMonitor(actionExecutionMonitor);
                        }
                    },
                    clientId, accountId, authenticationToken
            );
            executorService.submit(actionTask);
        };
    }

    /**
     * Action to perform on failure
     */
    public void onFailureEvent(ActionExecutionMonitor actionExecutionMonitor, DependencyGraph<ActionExecutionMonitor> graph) {
        try {
            ApiResponse apiResponse = translateAPIResponse(actionExecutionMonitor);
            boolean hasRetried = retryAction(actionExecutionMonitor, graph, apiResponse);
            if (hasRetried) {
                return;
            }
            setTranslatedResponse(apiResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
            actionExecutionMonitor.setStatus(ExecutionStatus.FAILED);
            actionExecutorService.save(actionExecutionMonitor);
        }

        entityService.markEntityInstanceStatusFailed(actionExecutionMonitor.getEntityInstance());
        workflowService.markWorkflowInstanceStatusFailed(workflowInstance);

        logger.info("{} ::  Calling on Failure callback for actionExecutionMonitor:: {}", Thread.currentThread().getName(),
                actionExecutionMonitor.getId());
        logPublishUserAudit(actionExecutionMonitor.getEntityInstance(), workflowInstance.getType());
        if (responseCallback != null) {
            responseCallback.onResponseFailure();
        }
    }

    private void setTranslatedResponse(ApiResponse apiResponse) {
        workflowInstance.setProcessedResponse(apiResponse);

        // update with latest entityInstances
        Set<EntityInstance> entityInstances = new HashSet<>();
        workflowInstance.getEntityInstances().forEach(entityInstance -> {
            entityService.getInstance(entityInstance.getId()).ifPresent(entityInstances::add);
        });
        workflowInstance.setEntityInstances(entityInstances);

        workflowService.save(workflowInstance);
    }

    public void onActionComplete(ActionExecutionMonitor actionExecutionMonitor, DependencyGraph<ActionExecutionMonitor> graph) {
        // It will update the entityId received from API response and update the entityInstance,
        // which is again used while createNewInstance to find for any pending EntityInstance based on name
        ApiResponse apiResponse = translateAPIResponse(actionExecutionMonitor);
        setTranslatedResponse(apiResponse);
        if (PublishState.ERROR.equals(apiResponse.getStatus()) || PublishState.ERROR_STOP.equals(apiResponse.getStatus())) {
            actionExecutionMonitor.setStatus(ExecutionStatus.FAILED);
            actionExecutorService.save(actionExecutionMonitor);

            onFailureEvent(actionExecutionMonitor, graph);
            return;
        }

        if (BaseUtil.isNotNullOrBlank(apiResponse.getMessage())) {
            EntityInstance entityInstance = actionExecutionMonitor.getEntityInstance();
            Environment environment = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(entityInstance.getWorkflowInstance());
            saveEntityIdInformation(apiResponse, entityInstance, environment);
        }

        executeActions(graph.itemCompleted(actionExecutionMonitor), graph);

        DefinitionType entityAction = workflowInstance.getType();
        // mark entity complete if done
        if (graph.hasFinished()) {
            entityService.markEntityInstanceStatusCompleted(actionExecutionMonitor.getEntityInstance());
            logPublishUserAudit(actionExecutionMonitor.getEntityInstance(), entityAction);
        }
        // check if all graph are done to complete the workflow
        if (isAllGraphCompletedSuccessfully()) {
            logger.info(" Completed Workflow Execution:: {}, status: {}", workflowInstance.getId(), workflowInstance.getStatus());
            onWorkflowComplete(actionExecutionMonitor.getWorkflowInstance());
        }
    }

    private boolean isAllGraphCompletedSuccessfully() {
        return graphSet.stream().allMatch(DependencyGraph::hasFinished);
    }

    /**
     * Save EntityId from TEST and LIVE into both Lookup table and EntityInstance table
     */
    private void saveEntityIdInformation(ApiResponse apiResponse, EntityInstance entityInstance, Environment environment) {
        EntityIdLookup entityIdLookup = entityIdLookupRepository.findByEntityTemplateAndTestEntityId(entityInstance.getEntityTemplate(), entityInstance.getName());
        entityInstance.setName(apiResponse.getMessage());
        entityInstance.setPollingId(apiResponse.getMessage());
        entityService.save(entityInstance);
        if (Environment.TEST.equals(environment)) {
            if (entityIdLookup == null) {
                entityIdLookup = new EntityIdLookup();
                entityIdLookup.setTestEntityId(apiResponse.getMessage());
                entityIdLookup.setEntityTemplate(entityInstance.getEntityTemplate());
                entityIdLookupRepository.save(entityIdLookup);
            }
        } else if (Environment.LIVE.equals(environment) && entityIdLookup != null) {
            entityIdLookup.setLiveEntityId(apiResponse.getMessage());
            entityIdLookupRepository.save(entityIdLookup);
        }
    }

    /**
     * Returns ApiResponse object from responseBody based on translate function defined in responseDefinition
     */
    private ApiResponse translateAPIResponse(ActionExecutionMonitor actionExecutionMonitor) {
        if (BaseUtil.shouldStubLivePublish(workflowInstance, dbConfigService)) {
            return new ApiResponse(PublishState.SUCCESS, "");
        }

        String responseBody = actionExecutionMonitor.getResponse().getResponseBody();
        Integer responseCode = actionExecutionMonitor.getResponse().getResponseCode();
        JsonNode actionResponseDefinition = actionExecutionMonitor.getAction().getResponseDefinition();
        return BaseUtil.translateAPIResponse(actionResponseDefinition, responseBody, responseCode);
    }

    private void onWorkflowComplete(WorkflowInstance workflowInstance) {
        workflowService.markWorkflowInstanceStatusCompleted(workflowInstance);
        if (responseCallback != null) {
            responseCallback.onResponseSuccess();
        }

        // Mark all parents as Discarded since they are either not required or are already published
        if (WorkflowInstanceStatus.PROMOTED_TO_LIVE.equals(workflowInstance.getStatus())) {
            markParentAsDiscarded(workflowInstance);
        }

        // evict cache to remove stale data after successful action completion
        CacheUtils.evictCache(dbConfigService, Constants.CACHE_EXPIRE_CONFIG);
    }

    private void markParentAsDiscarded(WorkflowInstance workflowInstance) {
        if (BaseUtil.isNullOrBlank(workflowInstance.getPendingParentId())) {
            return;
        }
        Integer parentInstanceId = Integer.parseInt(workflowInstance.getPendingParentId());
        Optional<WorkflowInstance> optionalParentInstance = workflowService.getInstance(parentInstanceId);
        if (optionalParentInstance.isPresent()) {
            WorkflowInstance parentInstance = optionalParentInstance.get();
            if (!BaseUtil.isWorkflowInstanceInTerminalState(parentInstance)) {
                workflowService.markWorkflowInstanceStatusDiscarded(parentInstance);
                Set<EntityInstance> entityInstances = parentInstance.getEntityInstances();
                entityInstances.forEach(entityInstance -> entityService.markEntityInstanceStatusDiscarded(entityInstance));
                markParentAsDiscarded(parentInstance);
            }
        }
    }

    private void logPublishUserAudit(EntityInstance entityInstance, DefinitionType entityAction) {
        UserAudit userAudit = UserAuditLogUtils.constructPublishUserAudit(entityInstance, authenticationToken.getUserId(),
                clientId, accountId, workflowInstance.getWorkflowTemplate().getTitle(), entityAction);
        UserAuditLogUtils.logUserAudit(userAudit);
    }

    public void attachResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    private boolean retryAction(ActionExecutionMonitor actionExecutionMonitor, DependencyGraph<ActionExecutionMonitor> graph, ApiResponse apiResponse) {
        Map<ActionConfigType, ActionConfig> actionConfigs = actionExecutionMonitor.getAction().getConfigs();
        if(PublishState.ERROR_STOP == apiResponse.getStatus()) {
            return false;
        }
        if (actionConfigs == null) {
            return false;
        }

        ActionPollingConfig pollingConfig = (ActionPollingConfig) actionConfigs.get(ActionConfigType.POLL);
        if (pollingConfig == null) {
            return false;
        }

        ActionExecutionMeta actionExecutionMeta = actionExecutionMonitor.getExecutionMeta();
        long retryInterval = pollingConfig.getRetryInterval();
        int executionCount = actionExecutionMeta.getExecutionCount();

        if (executionCount >= pollingConfig.getRetryCount()) {
            return false;
        }

        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException e) {
            logger.error("Error while attempting sleep on thread", e);
            Thread.currentThread().interrupt();
            throw new SelfServeException("Error while attempting sleep on thread", e);
        }

        // retry
        Consumer<ActionExecutionMonitor> consumer = getActionExecutionMonitorConsumer(graph);
        consumer.accept(actionExecutionMonitor);

        return true;
    }
}
