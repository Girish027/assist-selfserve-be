package com.tfsc.ilabs.selfservice.workflow.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode
public class NodeResponseDTO implements Serializable {

    private Map<Integer, NodeDTO> nodes;
    private List<Integer> list;

    public NodeResponseDTO() {
    }

    public NodeResponseDTO(Map<Integer, NodeDTO> nodes) {
        this.nodes = nodes;
    }
}
