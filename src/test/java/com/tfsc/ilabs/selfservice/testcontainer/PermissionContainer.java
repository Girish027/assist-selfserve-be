package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.common.models.Permission;
import com.tfsc.ilabs.selfservice.common.models.PermissionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionContainer {
    private static Map<Integer, PermissionType> permissions;

    static {
        permissions = new HashMap<>();
        permissions.put(1, PermissionType.ACTIVITY_DRAFT_CREATE);
        permissions.put(2, PermissionType.ACTIVITY_DRAFT_DISCARD);
        permissions.put(3, PermissionType.ACTIVITY_DRAFT_EDIT);
        permissions.put(4, PermissionType.ACTIVITY_TEST_PUBLISH);
        permissions.put(5, PermissionType.ACTIVITY_LIVE_PUBLISH);
    }

    public static Permission getPermission(Integer id) {
        Permission permission = new Permission(permissions.get(id));
        permission.setId(id);
        return permission;
    }

    public static List<Permission> getAllPermissions() {
        List<Permission> allPermissions = permissions.values().stream()
                .map(permission -> new Permission(permission))
                .collect(Collectors.toList());
        return allPermissions;
    }
}
