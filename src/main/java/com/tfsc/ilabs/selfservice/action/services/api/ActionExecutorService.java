package com.tfsc.ilabs.selfservice.action.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * Created by ravi.b on 23-07-2019.
 */
public interface ActionExecutorService {
    ExecutionStatus execute(Action action, WorkflowInstance workflowInstance, EntityInstance entityInstance, String clientId, String accountId);

    List<Action> createActionExecutionMonitorIfNotExist(WorkflowInstance workflowInstance, Environment publishToEnv);

    ApiResponse execute(Integer actionId, JsonNode pageData, NameLabel entityInstance, String clientId, String accountId,
                        Environment environmentToExecute);

    void save(ActionExecutionMonitor actionExecutionMonitor);

    ActionExecutionMonitor getActionExecutionMonitor(Action action, WorkflowInstance workflowInstance,
                                                                      EntityInstance entityInstance, Environment env);

    ActionExecutionMonitor getActionExecutionMonitor(ActionExecutionMonitor actionExecutionMonitor);

    @AllArgsConstructor
    @Data
    static class RequestResponseDefinition {
        RequestDefinition requestDefinition;
        ResponseDefinition responseDefinition;
    }
}
