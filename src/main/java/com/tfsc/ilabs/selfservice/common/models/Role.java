package com.tfsc.ilabs.selfservice.common.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"appRole", "standardRole"})
})
public class Role extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @NotNull
    private String appRole;

    @NotNull
    private String standardRole;

    @OneToMany
    @JoinColumn(name = "role_id")
    private List<RolePermission> rolePermissions;

    public Role() {
    }

    public Role(@NotNull String appRole, @NotNull String standardRole, List<RolePermission> rolePermissions) {
        this.appRole = appRole;
        this.standardRole = standardRole;
        this.rolePermissions = rolePermissions;
    }
}
