package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.workflow.models.NodeType;
import com.tfsc.ilabs.selfservice.workflow.models.UiSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class NodeDTO implements Serializable {

    private int id;

    private int parentId;

    private String title;

    private NodeType nodeType;

    private String nodeId;

    private UiSchema uiSchema;

    public NodeDTO() {
    }

    public NodeDTO(int id, int parentId, String title, NodeType nodeType, String nodeId, UiSchema uiSchema) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.nodeType = nodeType;
        this.nodeId = nodeId;
        this.uiSchema = uiSchema;
    }
}
