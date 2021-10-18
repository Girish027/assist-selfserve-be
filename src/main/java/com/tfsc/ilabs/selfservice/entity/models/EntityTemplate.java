package com.tfsc.ilabs.selfservice.entity.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
public class EntityTemplate extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String name;
    @Column
    private String fetchFor;

    public EntityTemplate() {
        // default constructor
    }
}
