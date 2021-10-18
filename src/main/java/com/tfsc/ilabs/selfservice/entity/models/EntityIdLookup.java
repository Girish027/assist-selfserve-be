package com.tfsc.ilabs.selfservice.entity.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Sushil.Kumar
 */
@Data
@Entity
public class EntityIdLookup extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column
    private String testEntityId;
    @Column
    private String liveEntityId;

    @ManyToOne
    @JoinColumn(name = "entity_template_id")
    private EntityTemplate entityTemplate;
}
