package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * Created by ravi.b on 17-10-2019.
 */
@Data
@EqualsAndHashCode
public class ActivityDTO extends WorkflowTemplateDTO {
    private List<String> schema;

    private WorkflowModel model;

    private boolean promotionApproval;

    private JsonNode entityLocation;

    private Integer entityTemplateId;

    private String entityTemplateName;

    private Set<DefinitionType> pageTypes;

    private UiSchema uiSchema;

    private WorkflowConfig configs;

    public ActivityDTO(String id, String title, String description, WorkflowModel model, boolean promotionApproval, JsonNode entityLocation, EntityTemplate entityTemplate, WorkflowConfig configs) {
        super(id, title, description, WorkflowTileType.ACTIVITY);
        this.model = model;
        this.promotionApproval = promotionApproval;
        this.entityLocation = entityLocation;
        this.entityTemplateId = entityTemplate.getId();
        this.entityTemplateName = entityTemplate.getName();
        this.configs = configs;
    }

    public ActivityDTO(String id, String title, String description, WorkflowModel model, boolean promotionApproval, JsonNode entityLocation, EntityTemplate entityTemplate, Set<DefinitionType> pageTypes, UiSchema uiSchema, WorkflowConfig configs) {
        this(id, title, description, model, promotionApproval, entityLocation, entityTemplate, configs);
        this.pageTypes = pageTypes;
        this.uiSchema = uiSchema;
    }

    public ActivityDTO(WorkflowTemplate workflowTemplate) {
        super(workflowTemplate.getId(), workflowTemplate.getTitle(), workflowTemplate.getDescription(), WorkflowTileType.ACTIVITY);
        this.model = workflowTemplate.getModel();
        this.promotionApproval = workflowTemplate.isPromotionApproval();
        this.entityTemplateId = workflowTemplate.getEntityTemplate().getId();
        this.entityTemplateName = workflowTemplate.getEntityTemplate().getName();
        this.configs = workflowTemplate.getConfigs();
    }
}
