package com.tfsc.ilabs.selfservice.page.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.utils.ConfigConverter;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by ravi.b on 31-05-2019.
 */
@Data
@Entity
public class PageData extends AuditableEntity {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "activity_instance_id")
    private WorkflowInstance workflowInstance;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "page_template_id")
    private PageTemplate pageTemplate;

    @NotNull
    @Column(columnDefinition = "json")
    @Convert(converter = ConfigConverter.class)
    private JsonNode data;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private PageDataStatus status;

    public PageData() {
    }

    public PageData(@NotNull WorkflowInstance workflowInstance, @NotNull PageTemplate pageTemplate,
                    @NotNull JsonNode data, @NotNull PageDataStatus status) {
        this.workflowInstance = workflowInstance;
        this.pageTemplate = pageTemplate;
        this.data = data;
        this.status = status;
    }

    public PageDataResponseDTO toDTO() {
        return new PageDataResponseDTO(this.id, this.workflowInstance.getId(), this.pageTemplate.getId(), this.data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageData pageData = (PageData) o;
        return Objects.equals(workflowInstance, pageData.workflowInstance)
                && Objects.equals(pageTemplate, pageData.pageTemplate)
                && Objects.equals(data, pageData.data) && status == pageData.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowInstance, pageTemplate, data, status);
    }
}
