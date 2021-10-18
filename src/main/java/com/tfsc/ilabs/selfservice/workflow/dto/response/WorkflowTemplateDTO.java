package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTileType;

import java.io.Serializable;

/**
 * Created by ravi.b on 07-06-2019.
 */
public abstract class WorkflowTemplateDTO implements Serializable {
    private String id;

    private String title;

    private String description;

    private WorkflowTileType type;

    public WorkflowTemplateDTO() {
        // for jackson
    }

    public WorkflowTemplateDTO(String id, String title, String description, WorkflowTileType workflowTileType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = workflowTileType;
    }

    public WorkflowTemplateDTO(WorkflowTemplate workflowTemplate) {
        this.setId(workflowTemplate.getId());
        this.setTitle(workflowTemplate.getTitle());
        this.setDescription(workflowTemplate.getDescription());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowTileType getType() {
        return type;
    }

    public void setType(WorkflowTileType type) {
        this.type = type;
    }
}
