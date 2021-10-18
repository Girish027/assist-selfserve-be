package com.tfsc.ilabs.selfservice.security.service.api;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;

public interface AuthorizationService {
    boolean hasInstancePermission(SSAuthenticationToken authentication, Integer workflowInstanceId, PermissionType ...permissionTypes);
    boolean hasPermission(SSAuthenticationToken authentication, String clientId, String accountId, PermissionType ...permissionTypes);
    boolean hasUploadPermission(SSAuthenticationToken authentication, Integer workflowInstanceId, PermissionType... permissionTypes);
}
