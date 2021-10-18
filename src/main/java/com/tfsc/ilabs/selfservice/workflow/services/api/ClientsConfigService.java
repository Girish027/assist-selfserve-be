package com.tfsc.ilabs.selfservice.workflow.services.api;

import com.tfsc.ilabs.selfservice.workflow.models.ConfigNames;

import com.tfsc.ilabs.selfservice.workflow.models.Node;
import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import java.util.List;

public interface ClientsConfigService {

    List<Node> filterNodesByConfig(ScopeType scopeType, String scopeId, List<Node> nodes, ConfigNames configName);

    List<WorkflowTemplate> filterWorkflowTemplatesByConfig(ScopeType scopeType, String scopeId, List<WorkflowTemplate> workflowTemplates, ConfigNames configName);

}
