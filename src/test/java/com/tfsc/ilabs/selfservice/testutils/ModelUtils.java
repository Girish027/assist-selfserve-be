package com.tfsc.ilabs.selfservice.testutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.page.models.PageDefinition;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowModel;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowType;

import java.io.IOException;
import java.util.HashMap;

public class ModelUtils {

    public static EntityTemplate getEntityTemplate() {
        EntityTemplate entityTemplate = new EntityTemplate();
        entityTemplate.setFetchFor("test-data");
        entityTemplate.setId(1);
        entityTemplate.setName("test-name");
        return entityTemplate;
    }

    public static PageTemplate getPageTemplate() throws IOException {
        return new PageTemplate("1", "test-title", "test-description", getPageDefinition(),
                DefinitionType.CREATE);
    }

    public static PageDefinition getPageDefinition() throws IOException {
        return new PageDefinition(getTestJsonNode(), getTestJsonNode(), getTestJsonNode(), getTestJsonNode(), getTestJsonNode());
    }

    public static WorkflowTemplate getWorkflowTemplate() throws IOException {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate("1", "test-title", "test-description", WorkflowModel.MENU, true, WorkflowType.NON_SINGLETON, getEntityTemplate(), getTestJsonNode());
        WorkflowConfig configs = new WorkflowConfig(PublishType.DEFAULT, false, new HashMap<>(), null, null, null, null, false, false, true, null);
        workflowTemplate.setConfigs(configs);
        return workflowTemplate;
    }

    public static JsonNode getTestJsonNode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String testStr = "{\"name\":\"value\"}";
        return objectMapper.readTree(testStr);
    }
}