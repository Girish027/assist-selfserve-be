package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.NodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeGroupRepository extends JpaRepository<NodeGroup,String> {
}
