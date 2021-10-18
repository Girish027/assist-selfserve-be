package com.tfsc.ilabs.selfservice.security.service.impl;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.common.models.Role;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.repositories.PermissionRepository;
import com.tfsc.ilabs.selfservice.common.repositories.RoleRepository;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import com.tfsc.ilabs.selfservice.security.service.api.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jonathan.paul on 28/05/2020
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Set<PermissionType> getPermissions(String accessToken, String clientId, String accountId) {
        Map<String, List<String>> userRoles = getUserRoles(accessToken);
        List<Role> currRole = getCurrRole(userRoles, clientId, accountId);
        return getCurrentPermissions(currRole);
    }

    private Map<String, List<String>> getUserRoles(String accessToken) {
        Map<String, List<String>> roles = new HashMap<>();
        UserAccessTokenInfo userAccessTokenInfo = SessionValidatorWithOKTA.getTokenMap().get(accessToken);
        if (userAccessTokenInfo != null) {
            roles = userAccessTokenInfo.getRoles();
        }
        return roles;
    }

    private List<Role> getCurrRole(Map<String, List<String>> roles, String clientId, String accountId) {
        List<Role> currRoles = new ArrayList<>();
        if (roles != null && roles.get(clientId + accountId) != null) {
            roles.get(clientId + accountId).forEach(standardRole -> {
                Optional<Role> optionalRole = roleRepository.findByStandardRole(standardRole);
                optionalRole.ifPresent(currRoles::add);
            });
        }
        return currRoles;
    }

    private Set<PermissionType> getCurrentPermissions(List<Role> currRoles) {
        Set<PermissionType> currentPermissions = new HashSet<>();
        currRoles.forEach(role ->
                currentPermissions.addAll(role.getRolePermissions().stream()
                        .map(rolePermission -> rolePermission.getPermission().getName())
                        .collect(Collectors.toSet()))
        );
        return currentPermissions;
    }
}
