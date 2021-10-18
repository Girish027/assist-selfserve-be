package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.workflow.models.Node;
import com.tfsc.ilabs.selfservice.workflow.models.NodeType;

public class NodeContainer {

    public static Node createNode(String nodeId, NodeType nodeType, Integer parentId) {
        Node node = new Node();
        node.setId(Integer.parseInt(nodeId));
        node.setNodeId(nodeId);
        node.setNodeType(nodeType);
        node.setParentId(parentId);
        return node;
    }

    public static Node createNode(int id, String nodeId, NodeType nodeType, Integer parentId) {
        Node node = new Node();
        node.setId(id);
        node.setNodeId(nodeId);
        node.setNodeType(nodeType);
        node.setParentId(parentId);
        return node;
    }
}
