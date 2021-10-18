package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;

public class ActionExecutionMonitorContainer {
    public static ActionExecutionMonitor createActionExecutionMonitorInstance() {
        EntityTemplate entityTemplate = EntityTemplateContainer.createEntityTemplate("queue", 1);
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(entityTemplate, "name", 1, "label");

        ActionExecutionMonitor actionExecutionMonitor =  new ActionExecutionMonitor(ActionContainer.createActionInstance(),
                WorkflowInstanceContainer.getWorkflowInstance(), entityInstance, Environment.TEST);
        actionExecutionMonitor.setId(1);
        actionExecutionMonitor.setResponse(getResponse());
        actionExecutionMonitor.setRequest(getRequest());
        return actionExecutionMonitor;
    }

    private static ResponseDefinition getResponse() {
        ResponseDefinition responseDefinition = new ResponseDefinition();
        responseDefinition.setResponseBody("success");
        return responseDefinition;
    }

    private static RequestDefinition getRequest() {
        RequestDefinition requestDefinition = new RequestDefinition();
        requestDefinition.setBody("");
        requestDefinition.setUrl("http://sample");
        return requestDefinition;
    }
}
