package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowType;

import java.util.ArrayList;
import java.util.List;

public class WorkflowTemplateContainer {

    public static WorkflowTemplate getSingletonWorkflow(String entityLocation) {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setId("1");
        workflowTemplate.setEntityTemplate(EntityTemplateContainer.createEntityTemplate("name", 1));
        workflowTemplate.setPromotionApproval(false);
        workflowTemplate.setType(WorkflowType.SINGLETON);
        workflowTemplate.setEntityLocation(getEntityLocation(entityLocation));
        WorkflowConfig workflowConfig =new WorkflowConfig();
        workflowConfig.setPublishType(PublishType.DEFAULT);
        workflowTemplate.setConfigs(workflowConfig);
        return workflowTemplate;
    }

    public static JsonNode getEntityLocation(String location) {
        JsonNode entityLocation = new ObjectMapper().createObjectNode();
        ((ObjectNode)entityLocation).put(String.valueOf(DefinitionType.CREATE), location);
        return entityLocation;
    }

    public static List<NameLabel> getNameLabelList(){
        List<NameLabel> nameLabelList = new ArrayList<>();
        NameLabel nameLabel = new NameLabel();
        nameLabel.setLabel("entity12");
        nameLabel.setName("entity12");
        nameLabelList.add(nameLabel);
        return nameLabelList;
    }

    public static WorkflowTemplate getWorkflow(String entityLocation, PublishType publishType) {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setId("1");
        workflowTemplate.setEntityTemplate(EntityTemplateContainer.createEntityTemplate("name", 1));
        workflowTemplate.setPromotionApproval(false);
        workflowTemplate.setType(WorkflowType.SINGLETON);
        workflowTemplate.setEntityLocation(getEntityLocation(entityLocation));
        WorkflowConfig workflowConfig =new WorkflowConfig();
        workflowConfig.setPublishType(publishType);
        workflowTemplate.setConfigs(workflowConfig);
        return workflowTemplate;
    }

    public static WorkflowTemplate getWorkflowTemplate(String id, String entityLocation, PublishType publishType) {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setId(id);
        workflowTemplate.setEntityTemplate(EntityTemplateContainer.createEntityTemplate(id, 1));
        workflowTemplate.setPromotionApproval(false);
        workflowTemplate.setType(WorkflowType.SINGLETON);
        workflowTemplate.setEntityLocation(getEntityLocation(entityLocation));
        WorkflowConfig workflowConfig =new WorkflowConfig();
        workflowConfig.setPublishType(publishType);
        workflowTemplate.setConfigs(workflowConfig);
        return workflowTemplate;
    }
}
