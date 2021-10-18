package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.Node;
import com.tfsc.ilabs.selfservice.workflow.models.NodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

public interface NodeRepository extends JpaRepository<Node,Integer> {
    @QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    List<Node> findAllByParentId(Integer id);

    Node findByNodeIdAndNodeType(String nodeId, NodeType nodeType);
}
