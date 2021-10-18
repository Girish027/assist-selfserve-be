package com.tfsc.ilabs.selfservice.security.service.api;

import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import com.tfsc.ilabs.selfservice.security.service.impl.AuthorizationServiceImpl;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.testcontainer.PermissionContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowInstanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SessionValidatorWithOKTA.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class AuthorizationServiceTest {
    @InjectMocks
    AuthorizationServiceImpl authorizationService;

    @Mock
    WorkflowInstanceRepository workflowInstanceRepository;
    @Mock
    PermissionService permissionService;

    @Test
    public void testGetPermissions() {
        String clientId = "testclient";
        String accountId = "testclient";
        String accessToken = "sample";
        ReflectionTestUtils.setField(authorizationService, "isAuthorizationEnabled", true);

        PermissionType[] permissionTypes = new PermissionType[]{
                PermissionType.ACTIVITY_DRAFT_CREATE, PermissionType.ACTIVITY_DRAFT_DISCARD
        };

        SSAuthenticationToken authentication = new SSAuthenticationToken(null);
        authentication.setAccessToken(accessToken);

        Set<PermissionType> userPermissions = new HashSet<>();
        userPermissions.add(PermissionContainer.getPermission(1).getName());
        userPermissions.add(PermissionContainer.getPermission(2).getName());
        Mockito.when(permissionService.getPermissions(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(userPermissions);

        boolean isPermitted = authorizationService.hasPermission(authentication, clientId, accountId, permissionTypes);

        assertTrue(isPermitted);
    }

    @Test
    public void test_hasInstancePermission() {
        SSAuthenticationToken authentication = null;
        Integer workflowInstanceId = 1;
        PermissionType[] permissionTypes = new PermissionType[]{};

        Mockito.when(workflowInstanceRepository.findById(1))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getWorkflowInstance()));

        authorizationService.hasInstancePermission(authentication, workflowInstanceId, permissionTypes);
    }

    @Test
    public void testGetUploadPermissions_not_permitted() {
        String clientId = "testclient";
        String accountId = "testclient";
        String accessToken = "sample";
        ReflectionTestUtils.setField(authorizationService, "isAuthorizationEnabled", true);

        PermissionType[] permissionTypes = new PermissionType[]{
                PermissionType.ACTIVITY_TEST_PUBLISH, PermissionType.ACTIVITY_LIVE_PUBLISH
        };

        SSAuthenticationToken authentication = new SSAuthenticationToken(null);
        authentication.setAccessToken(accessToken);

        Set<PermissionType> userPermissions = new HashSet<>();
        userPermissions.add(PermissionContainer.getPermission(1).getName());
        userPermissions.add(PermissionContainer.getPermission(2).getName());
        Mockito.when(permissionService.getPermissions(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(userPermissions);
        Mockito.when(workflowInstanceRepository.findById(1))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getWorkflowInstance()));

        boolean isPermitted = authorizationService.hasUploadPermission(authentication, 1, permissionTypes);

        assertFalse(isPermitted);
    }

    @Test
    public void testGetUploadPermissions_permitted() {
        String clientId = "testclient";
        String accountId = "testclient";
        String accessToken = "sample";
        ReflectionTestUtils.setField(authorizationService, "isAuthorizationEnabled", true);

        PermissionType[] permissionTypes = new PermissionType[]{
                PermissionType.ACTIVITY_TEST_PUBLISH, PermissionType.ACTIVITY_LIVE_PUBLISH
        };

        SSAuthenticationToken authentication = new SSAuthenticationToken(null);
        authentication.setAccessToken(accessToken);

        Set<PermissionType> userPermissions = new HashSet<>();
        userPermissions.add(PermissionContainer.getPermission(4).getName());
        userPermissions.add(PermissionContainer.getPermission(5).getName());
        Mockito.when(permissionService.getPermissions(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(userPermissions);
        Mockito.when(workflowInstanceRepository.findById(1))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getWorkflowInstance()));

        boolean isPermitted = authorizationService.hasUploadPermission(authentication, 1, permissionTypes);

        assertTrue(isPermitted);
    }
}
