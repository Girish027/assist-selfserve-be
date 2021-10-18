package com.tfsc.ilabs.selfservice.security.service.api;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.repositories.PermissionRepository;
import com.tfsc.ilabs.selfservice.common.repositories.RoleRepository;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import com.tfsc.ilabs.selfservice.security.service.impl.PermissionServiceImpl;
import com.tfsc.ilabs.selfservice.testcontainer.PermissionContainer;
import com.tfsc.ilabs.selfservice.testcontainer.RoleContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;

import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SessionValidatorWithOKTA.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class PermissionServiceTest {
    @InjectMocks
    PermissionServiceImpl permissionService;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PermissionRepository permissionRepository;

    @Test
    public void testGetPermissions() {
        String clientId = "testclient";
        String accountId = "testclient";
        String testRole = "developer";
        String accessToken = "sample";
        UserAccessTokenInfo userAccessTokenInfo = new UserAccessTokenInfo();
        Map<String, List<String>> roles = new HashMap<>();
        roles.put(clientId+accountId, Collections.singletonList(testRole));
        userAccessTokenInfo.setRoles(roles);

        SessionValidatorWithOKTA.getTokenMap().put(accessToken, userAccessTokenInfo);
        Mockito.when(permissionRepository.findAll()).thenReturn(PermissionContainer.getAllPermissions());
        Mockito.when(roleRepository.findByStandardRole("developer")).thenReturn(RoleContainer.getRole(1));

        Set<PermissionType> permissions = permissionService.getPermissions(accessToken, clientId, accountId);
        assertEquals(2, permissions.size());
    }
}
