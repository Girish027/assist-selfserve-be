package com.tfsc.ilabs.selfservice.workflow.dto.request;

import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class WorkflowInstanceRequestDTO {
    private String pageId;
    private List<NameLabel> entities;

    public WorkflowInstanceRequestDTO() {
    }

    public WorkflowInstanceRequestDTO(String pageId, List<NameLabel> entities) {
        this.pageId = pageId;
        this.entities = entities;
    }
}
