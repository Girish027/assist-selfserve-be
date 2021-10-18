package com.tfsc.ilabs.selfservice.workflow.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.utils.ConfigConverter;
import com.tfsc.ilabs.selfservice.common.utils.UiSchemaConverter;
import com.tfsc.ilabs.selfservice.common.utils.WorkflowConfigConverter;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.workflow.dto.response.ActivityDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowTemplateDTO;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "activity_template")
public class WorkflowTemplate extends AuditableEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "entity_template_id")
    private EntityTemplate entityTemplate;

    @NotNull
    @Column(columnDefinition = "json")
    @Convert(converter = ConfigConverter.class)
    private JsonNode entityLocation;

    @Id
    @NotNull
    @Column(unique = true)
    private String id;

    @NotNull
    private String title;

    @Column(length = 1024)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private WorkflowModel model;

    @NotNull
    private boolean promotionApproval;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private WorkflowType type;

    @Column(columnDefinition = "mediumtext")
    @Convert(converter = UiSchemaConverter.class)
    private UiSchema uiSchema;

    @NotNull
    @Column(columnDefinition = "json")
    @Convert(converter = WorkflowConfigConverter.class)
    private WorkflowConfig configs;

    public WorkflowTemplate() {
        // for jackson purpose only
    }

    public WorkflowTemplate(@NotNull String id, @NotNull String title, String description, @NotNull WorkflowModel model, @NotNull boolean promotionApproval, @NotNull WorkflowType type, @NotNull EntityTemplate entityTemplate, JsonNode entityLocation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.model = model;
        this.promotionApproval = promotionApproval;
        this.type = type;
        this.entityTemplate = entityTemplate;
        this.entityLocation = entityLocation;
    }

    public WorkflowTemplate(@NotNull String id, @NotNull String title, String description, @NotNull WorkflowModel model, @NotNull boolean promotionApproval, @NotNull WorkflowType type, @NotNull EntityTemplate entityTemplate, JsonNode entityLocation, UiSchema uiSchema) {
        this(id, title, description, model, promotionApproval, type, entityTemplate, entityLocation);
        this.uiSchema = uiSchema;
    }

    public WorkflowTemplateDTO toWorkflowTemlateDTO(Set<DefinitionType> pageTypes) {
        return new ActivityDTO(this.id, this.title, this.description, this.model,
                this.promotionApproval, this.entityLocation, this.entityTemplate, pageTypes, uiSchema, this.configs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkflowTemplate that = (WorkflowTemplate) o;
        return promotionApproval == that.promotionApproval && Objects.equals(id, that.id)
                && Objects.equals(title, that.title) && Objects.equals(description, that.description)
                && model == that.model && Objects.equals(entityLocation, that.entityLocation)
                && Objects.equals(configs, that.configs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, model, promotionApproval, entityLocation, configs);
    }

    @Override
    public String toString() {
        return "WorkflowTemplate{" + "id='" + id + '\'' + ", label='" + title + '\'' + ", description='" + description
                + '\'' + ", model=" + model + ", promotionApproval=" + promotionApproval + ", configs=" + configs + '}';
    }
}
