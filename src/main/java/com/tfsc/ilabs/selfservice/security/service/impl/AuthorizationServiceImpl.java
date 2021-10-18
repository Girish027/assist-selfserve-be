package com.tfsc.ilabs.selfservice.security.service.impl;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.security.service.api.AuthorizationService;
import com.tfsc.ilabs.selfservice.security.service.api.PermissionService;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service("authorization")
public class AuthorizationServiceImpl implements AuthorizationService {
    @Value("${role_permission.enable}")
    private boolean isAuthorizationEnabled;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Override
    public boolean hasInstancePermission(SSAuthenticationToken authentication, Integer workflowInstanceId, PermissionType... permissionTypes) {
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowInstanceRepository.findById(workflowInstanceId);
        if (optionalWorkflowInstance.isPresent()) {
            WorkflowInstance workflowInstance = optionalWorkflowInstance.get();
            String clientId = workflowInstance.getClientId();
            String accountId = workflowInstance.getAccountId();
            return hasPermission(authentication, clientId, accountId, permissionTypes);
        }
        return false;
    }

    @Override
    public boolean hasPermission(SSAuthenticationToken authentication, String clientId, String accountId, PermissionType... permissionTypes) {
        boolean isPermitted = false;
        if (isAuthorizationEnabled) {
            String accessToken = authentication.getAccessToken();
            Set<PermissionType> permissions = permissionService.getPermissions(accessToken, clientId, accountId);

            isPermitted = validatePermissions(permissionTypes, permissions);
        } else {
            isPermitted = true;
        }
        return isPermitted;
    }

    @Override
    public boolean hasUploadPermission(SSAuthenticationToken authentication, Integer workflowInstanceId, PermissionType... permissionTypes) {
        boolean isPermitted = false;
        if (isAuthorizationEnabled) {
            String accessToken = authentication.getAccessToken();
            Optional<WorkflowInstance> workflowInstance =  workflowInstanceRepository.findById(workflowInstanceId);
            if(workflowInstance.isPresent()) {
                Set<PermissionType> permissions = permissionService.getPermissions(accessToken, workflowInstance.get().getClientId(), workflowInstance.get().getAccountId());
                isPermitted = validatePermissions(permissionTypes, permissions);
            }
        } else {
            isPermitted = true;
        }
        return isPermitted;
    }

    private boolean validatePermissions(PermissionType[] permissionTypes, Set<PermissionType> permissions) {
        boolean isPermitted = true;
        for (PermissionType permissionType : permissionTypes) {
            isPermitted = permissions.contains(permissionType) && isPermitted;
        }
        return isPermitted;
    }
}
