package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionWorkflow;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

public class ActionWorkFlowContainer {

	public static ActionWorkflow getActionWorkFlowInstance(Action action, WorkflowTemplate workflowTemplate) {
		ActionWorkflow actionWorkFlowInstance = new ActionWorkflow();
		actionWorkFlowInstance.setAction(action);
		actionWorkFlowInstance.setWorkflowTemplate(workflowTemplate);
		return actionWorkFlowInstance;
	}
}
