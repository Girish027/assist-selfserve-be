package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.common.models.Role;
import com.tfsc.ilabs.selfservice.common.models.RolePermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RoleContainer {
    public static Optional<Role> getRole(Integer id) {
        List<Integer> permissionIds = Arrays.asList(1,2);
        List<RolePermission> rolePermissions = RolePermissionContainer.getRolePermission(1, permissionIds);
        Role role = new Role("appRole" + id, "standardRole" + id, rolePermissions);
        role.setId(id);
        return Optional.of(role);
    }

    public static List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(getRole(1).get());
        return roles;
    }
}
