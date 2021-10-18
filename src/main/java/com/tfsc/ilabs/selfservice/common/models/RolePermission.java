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
public class RolePermission extends AuditableEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    public RolePermission() {
    }

    public RolePermission(@NotNull Role role, @NotNull Permission permission) {
        this.role = role;
        this.permission = permission;
    }
}
