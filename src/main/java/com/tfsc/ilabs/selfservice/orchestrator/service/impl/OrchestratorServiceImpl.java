package com.tfsc.ilabs.selfservice.orchestrator.service.impl;

import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.repositories.ActionExecutionMonitorRepository;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.exception.InvalidResourceException;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityIdLookupRepository;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler.DependencyGraph;
import com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler.DependencyGraphBuilder;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.OrchestratorService;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.ResponseCallback;
import com.tfsc.ilabs.selfservice.orchestrator.tasks.WorkflowOrchestratorTask;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by ravi.b on 01-08-2019.
 */
@Service
public class OrchestratorServiceImpl implements OrchestratorService {
    private static final Logger logger = LoggerFactory.getLogger(OrchestratorServiceImpl.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private ActionExecutorService actionExecutorService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private EntityInstanceRepository entityInstanceRepository;
    @Autowired
    private PageDataRepository pageDataRepository;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ActionExecutionMonitorRepository actionExecutionMonitorRepository;
    @Autowired
    private DBConfigService dbConfigService;
    @Autowired
    private EntityIdLookupRepository entityIdLookupRepository;

    @Override
    @CacheEvict(value = Constants.CACHE_NAME, allEntries = true)
    public void orchestrate(String clientId, String accountId, WorkflowInstance workflowInstance, Environment publishToEnv) {

        if (!isWorkflowReadyForOrchestration(workflowInstance)) {
            if (workflowInstance == null) {
                throw new InvalidResourceException(new ErrorObject("Instance is null. Cannot promote"));
            }
            throw new InvalidResourceException(
                    new ErrorObject("Inconsistent state of instance. Cannot promote ", workflowInstance.toDTO()));
        }

        List<PageTemplate> pageTemplates = pageDataRepository.findAllByWorkflowInstance(workflowInstance).stream()
                .map(PageData::getPageTemplate).collect(Collectors.toList());
        if (pageTemplates.isEmpty()) {
            logger.error("Page Template not found with workflowInstanceId: {}", workflowInstance.getId());
            throw new NoSuchResourceException(
                    new ErrorObject("Page Template not found. Cannot promote", workflowInstance.toDTO()));
        }

        submitWorkflowOrchestratorTask(clientId, accountId, publishToEnv, workflowInstance);
    }

    /**
     * This method submits workflow task for processing. When workflow is pushed to LIVE, it checks if any of its parent workflow is pending to
     * be pushed to LIVE (CREATE) and it first pushes parent workflow to LIVE and on success of parent it pushes other workflow to LIVE
     * Eg. IF CREATE is pending and we are trying to push UPDATE to LIVE then first PUSH CREATE to LIVE and then push UPDATE to LIVE
     */
    private void submitWorkflowOrchestratorTask(String clientId, String accountId, Environment publishToEnv, WorkflowInstance workflowInstance) {
        WorkflowInstance parentInstance = getParentInstanceToPromote(workflowInstance, publishToEnv);

        WorkflowOrchestratorTask workflowOrchestratorTask = parentInstance != null
                ? buildParentTask(clientId, accountId, workflowInstance, publishToEnv, parentInstance)
                : buildWorkflowOrchestratorTask(clientId, accountId, workflowInstance, publishToEnv);

        executorService.submit(workflowOrchestratorTask);
    }

    private WorkflowInstance getParentInstanceToPromote(WorkflowInstance workflowInstance, Environment publishToEnv) {
        WorkflowInstance parentToPromote = null;

        if (isParentPublishNeeded(workflowInstance, publishToEnv)) {
            Optional<WorkflowInstance> optionalParentInstance = workflowService.getInstance(Integer.parseInt(
                    workflowInstance.getPendingParentId()));
            if (optionalParentInstance.isPresent()) {
                WorkflowInstance parentInstance = optionalParentInstance.get();

                if (isInstanceWithCreateTemplateNotTerminated(parentInstance)) {
                    parentToPromote = parentInstance;
                } else {
                    parentToPromote = getParentInstanceToPromote(parentInstance, publishToEnv);
                }
            }
        }

        return parentToPromote;
    }

    private boolean isParentPublishNeeded(WorkflowInstance workflowInstance, Environment publishToEnv) {
        return Environment.LIVE.equals(publishToEnv) && !BaseUtil.isNullOrBlank(workflowInstance.getPendingParentId())
                && DefinitionType.UPDATE.equals(workflowInstance.getType());
    }

    private boolean isInstanceWithCreateTemplateNotTerminated(WorkflowInstance workflowInstance) {
        return DefinitionType.CREATE.equals(workflowInstance.getType()) && !BaseUtil.isWorkflowInstanceInTerminalState(workflowInstance);
    }

    private WorkflowOrchestratorTask buildParentTask(String clientId, String accountId, WorkflowInstance workflowInstance,
                                                     Environment publishToEnv, WorkflowInstance parentInstance) {
        WorkflowOrchestratorTask parentWorkflowOrchestratorTask = buildWorkflowOrchestratorTask(clientId, accountId,
                parentInstance, publishToEnv);
        parentWorkflowOrchestratorTask.attachResponseCallback(new ResponseCallback() {
            @Override
            public void onResponseSuccess() {
                logger.info("ParentInstance {} Promoted to Live successfully. Starting child instance now", parentInstance.getId());
                // On success of Parent Create instance, execute update instance
                WorkflowOrchestratorTask workflowOrchestratorTask = buildWorkflowOrchestratorTask(clientId, accountId,
                        workflowInstance, publishToEnv);
                executorService.submit(workflowOrchestratorTask);
            }

            @Override
            public void onResponseFailure() {
                logger.error("Live promotion failed for PendingParentInstance {}", workflowInstance.getPendingParentId());
                // copy parent's failure status
                Optional<WorkflowInstance> optionalParentInstance = workflowService.getInstance(parentInstance.getId());
                optionalParentInstance.ifPresent(currentParentInstance -> {
                    workflowInstance.setProcessedResponse(currentParentInstance.getProcessedResponse());
                    workflowInstance.setStatus(currentParentInstance.getStatus());
                    workflowService.save(workflowInstance);
                });
            }
        });
        return parentWorkflowOrchestratorTask;
    }

    private WorkflowOrchestratorTask buildWorkflowOrchestratorTask(String clientId, String accountId,
                                                                   WorkflowInstance workflowInstance, Environment publishToEnv) {
        // make all action execution monitor records only iff not present
        List<Action> actions = actionExecutorService.createActionExecutionMonitorIfNotExist(workflowInstance, publishToEnv);
        Set<DependencyGraph<ActionExecutionMonitor>> dependencyGraphsForEachEntity = new HashSet<>();
        if (actions.isEmpty()) {
            logger.error("Actions not found with workflowInstanceId: {}", workflowInstance.getId());
            throw new NoSuchResourceException(
                    new ErrorObject("Action not found. Cannot promote", workflowInstance.toDTO()));
        }
        List<EntityInstance> entityInstances = entityInstanceRepository
                .findAllByWorkflowInstance(workflowInstance).stream()
                .filter(entityInstance -> !EntityInstanceStatus.DISCARDED.equals(entityInstance.getStatus()))
                .collect(Collectors.toList());
        entityInstances.forEach(entityInstance -> dependencyGraphsForEachEntity.add(buildDependencies(actions,
                workflowInstance, entityInstance, publishToEnv)));

        if (!dependencyGraphsForEachEntity.isEmpty())
            workflowService.markWorkflowInstanceStatusRunning(workflowInstance);

        return new WorkflowOrchestratorTask(workflowService,
                entityService, actionExecutorService, executorService, dbConfigService, entityIdLookupRepository,
                workflowInstance, dependencyGraphsForEachEntity, clientId, accountId, SecurityContextUtil.getAuthenticationToken());
    }

    @Override
    public WorkflowInstanceDTO getStatus(String clientId, String accountId, Integer workflowInstanceId) {
        WorkflowInstance workflowInstance = workflowService.validateAndGetWorkflowInstance(clientId, accountId, workflowInstanceId);

        return workflowInstance.toDTO();
    }

    private DependencyGraph<ActionExecutionMonitor> buildDependencies(Collection<Action> actionToBeExecuted, WorkflowInstance workflowInstance,
                                                                      EntityInstance entityInstance, Environment environmentToRun) {

        DependencyGraphBuilder<ActionExecutionMonitor> graphBuilder = new DependencyGraphBuilder<>();

        for (Action action : actionToBeExecuted) {
            ActionExecutionMonitor actionExecutionMonitor = actionExecutionMonitorRepository
                    .findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(action, workflowInstance, entityInstance,
                            environmentToRun);

            // ignore if action is completed
            if (ExecutionStatus.COMPLETED.equals(actionExecutionMonitor.getStatus())) {
                continue;
            }

            graphBuilder.add(actionExecutionMonitor);

            // Add dependency action in graph only if its not completed for workflowinstance
            Set<Action> actionDependency = action.getActionDependencies();
            for (Action dependency : actionDependency) {
                ActionExecutionMonitor dependentActionExecutionMonitor = actionExecutionMonitorRepository
                        .findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(dependency, workflowInstance,
                                entityInstance, environmentToRun);

                if (ExecutionStatus.COMPLETED.equals(dependentActionExecutionMonitor.getStatus())) {
                    continue;
                }

                graphBuilder.addDependency(actionExecutionMonitor, dependentActionExecutionMonitor);
            }
        }

        return graphBuilder.build();
    }

    private boolean isWorkflowReadyForOrchestration(WorkflowInstance workflowInstance) {
        if (workflowInstance != null) {
            switch (workflowInstance.getStatus()) {
                case DRAFT_EDIT:
                case PROMOTING_TO_LIVE:
                case PROMOTING_TO_TEST:
                case PROMOTED_TO_LIVE:
                    return false;

                case DRAFT_SAVE:
                case LIVE_PROMOTION_FAILED:
                case TEST_PROMOTION_FAILED:
                case PROMOTED_TO_TEST:
                    return true;
                case DISCARDED:
                default:
                    break;
            }
        }
        return false;
    }
}
