package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.workflow.models.NodeGroup;
import com.tfsc.ilabs.selfservice.workflow.models.UiSchema;

public class NodeGroupContainer {

    public static NodeGroup createNodeGroup(String id, String title) {
        NodeGroup nodeGroup = new NodeGroup();
        nodeGroup.setId(id);
        nodeGroup.setTitle(title);
        nodeGroup.setUiSchema(new UiSchema());
        return nodeGroup;
    }
}
