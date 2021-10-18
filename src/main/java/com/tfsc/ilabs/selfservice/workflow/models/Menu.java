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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Menu extends AuditableEntity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String menuGroupName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node nodeParentChild;

    @NotNull
    private int seq;

    public Menu() {
    }

    public Menu(@NotNull String menuGroupName, @NotNull Node nodeParentChild, @NotNull int seq) {
        this.menuGroupName = menuGroupName;
        this.nodeParentChild = nodeParentChild;
        this.seq = seq;
    }
}