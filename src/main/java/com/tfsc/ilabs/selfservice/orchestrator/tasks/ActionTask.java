package com.tfsc.ilabs.selfservice.orchestrator.tasks;

import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.ResponseCallback;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by ravi.b on 31-07-2019.
 */
public class ActionTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ActionTask.class);
    protected WorkflowInstance workflowInstance;
    protected String clientId;
    protected String accountId;
    private ActionExecutorService actionExecutorService;
    private EntityService entityService;
    private ActionExecutionMonitor actionExecutionMonitor;
    private ResponseCallback listener;
    private SSAuthenticationToken authenticationToken;

    ActionTask(ActionExecutorService actionExecutorService, EntityService entityService, WorkflowInstance workflowInstance, ActionExecutionMonitor actionExecutionMonitor, ResponseCallback listener, String clientId, String accountId, SSAuthenticationToken authenticationToken) {
        this.actionExecutorService = actionExecutorService;
        this.entityService = entityService;
        this.workflowInstance = workflowInstance;
        this.actionExecutionMonitor = actionExecutionMonitor;
        this.listener = listener;
        this.clientId = clientId;
        this.accountId = accountId;
        this.authenticationToken = authenticationToken;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting action execution of actionExecutionMonitor:: {}", actionExecutionMonitor.getId());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // update with latest entityInstance
            entityService.getInstance(actionExecutionMonitor.getEntityInstance().getId()).ifPresent(instance -> {
                actionExecutionMonitor.setEntityInstance(instance);
            });

            ExecutionStatus executionStatus = actionExecutorService.execute(actionExecutionMonitor.getAction(), workflowInstance,
                    actionExecutionMonitor.getEntityInstance(), clientId, accountId);
            if (ExecutionStatus.FAILED.equals(executionStatus)) {
                throw new SelfServeException("Action execution failed for action {} and entity {}", actionExecutionMonitor.getAction(),
                        actionExecutionMonitor.getEntityInstance().getName());
            }

            logger.info("Completed action execution of actionExecutionMonitor:: {}", actionExecutionMonitor.getId());
            setActionExecutionMeta();
            listener.onResponseSuccess();
        } catch (Exception e) {
            logger.error(e.getMessage());
            setActionExecutionMeta();
            listener.onResponseFailure();
        }
    }

    private void setActionExecutionMeta() {
        // get previous result
        ActionExecutionMonitor currentActionExecutionMonitor = actionExecutorService
                .getActionExecutionMonitor(actionExecutionMonitor);

        ActionExecutionMeta actionExecutionMeta = currentActionExecutionMonitor.getExecutionMeta();

        if (actionExecutionMeta == null) {
            actionExecutionMeta = new ActionExecutionMeta();
        }
        int executionCount = actionExecutionMeta.getExecutionCount();

        // update previous result
        executionCount++;
        actionExecutionMeta.setExecutionCount(executionCount);
        currentActionExecutionMonitor.setExecutionMeta(actionExecutionMeta);

        actionExecutorService.save(currentActionExecutionMonitor);
    }
}
