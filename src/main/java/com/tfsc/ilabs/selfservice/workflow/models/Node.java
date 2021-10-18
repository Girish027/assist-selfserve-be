package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "node")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Node extends AuditableEntity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String nodeId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NodeType nodeType;

    @NotNull
    private int parentId;

    public Node() {
    }

    public Node(@NotNull String nodeId, @NotNull NodeType nodeType, @NotNull int parentId) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.parentId = parentId;
    }
}