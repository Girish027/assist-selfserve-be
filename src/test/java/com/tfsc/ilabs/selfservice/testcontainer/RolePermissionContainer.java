package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.common.models.RolePermission;

import java.util.ArrayList;
import java.util.List;

public class RolePermissionContainer {
    public static List<RolePermission> getRolePermission(Integer roleId, List<Integer> permissionIds) {
        List<RolePermission> rolePermissions = new ArrayList<>();
        int[] counter = new int[1];
        permissionIds.forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(++counter[0]);
            rolePermission.setPermission(PermissionContainer.getPermission(permissionId));
            rolePermissions.add(rolePermission);
        });
        return rolePermissions;
    }
}
