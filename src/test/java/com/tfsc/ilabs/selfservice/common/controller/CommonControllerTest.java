package com.tfsc.ilabs.selfservice.common.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.common.services.api.CommonService;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.security.service.api.PermissionService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Set;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CommonControllerTest {
    @InjectMocks
    CommonController commonController = new CommonController();

    @Mock
    CommonService commonService;

    @Mock
    PermissionService permissionService;

    @Mock
    ResponseEntity<String> responseEntity;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(commonController).build();
    }

    @Test
    public void test_invalidateCache() throws Exception {
        mockMvc.perform(post(Constants.BASE_URI +
                Constants.EXPIRE_API_CACHE_URI)).andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void test_invalidateSession() throws Exception {
        mockMvc.perform(post(Constants.BASE_URI +
                Constants.INVALIDATE_SESSION_URI).header(Constants.ACCESS_TOKEN, TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    public void test_evictEntityCache() throws Exception {
        doNothing().when(commonService).evictCache(isA(String.class));
        mockMvc.perform(post(Constants.BASE_URI +
                Constants.EXPIRE_ENTITY_CACHE_URI)
                .param("name", "testName")
        ).andExpect(status().isOk());

        verify(commonService, times(1)).evictCache("testName");
    }

    @Test
    @Ignore
    public void test_getPermissions() throws Exception {
        when(permissionService.getPermissions(TOKEN, CLIENT_ID, ACCOUNT_ID))
                .thenReturn(Collections.singleton(PermissionType.ACTIVITY_DRAFT_CREATE));

        MvcResult results = mockMvc.perform(get(Constants.BASE_URI + Constants.GET_PERMISSIONS_URI, CLIENT_ID, ACCOUNT_ID).header(Constants.ACCESS_TOKEN, TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String permissionString = results.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(Set.class, PermissionType.class);
        Set<PermissionType> permissions = objectMapper.readValue(permissionString, type);

        assertEquals(Collections.singleton(PermissionType.ACTIVITY_DRAFT_CREATE), permissions);
    }

    @Test
    public void test_publishUILog() throws Exception {
        mockMvc.perform(post(Constants.BASE_URI + "/log").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
    }
}
