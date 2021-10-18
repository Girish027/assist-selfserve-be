package com.tfsc.ilabs.selfservice.security.service.api;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;

import java.util.Set;

/**
 * Created by jonathan.paul on 28/05/2020
 */
public interface PermissionService {
    Set<PermissionType> getPermissions(String accessToken, String clientId, String accountId);
}
