package com.tfsc.ilabs.selfservice.common.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Permission extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PermissionType name;

    public Permission() {
    }

    public Permission(PermissionType name) {
        this.name = name;
    }
}
