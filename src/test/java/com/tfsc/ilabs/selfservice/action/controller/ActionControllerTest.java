package com.tfsc.ilabs.selfservice.action.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Api;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.services.api.CommonService;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.security.service.api.PermissionService;
import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.ACCOUNT_ID;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.CLIENT_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActionControllerTest {
    @InjectMocks
    ActionController actionController = new ActionController();

    @Mock
    CommonService commonService;
    @Mock
    PermissionService permissionService;
    @Mock
    ActionExecutorService actionExecutorService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(actionController).build();
    }

    @Test
    public void test_executeAction() throws Exception {
        ApiResponse retVal = new ApiResponse(PublishState.SUCCESS, "SUCCESS");
        Integer actionId = 1;
        when(actionExecutorService.execute(anyInt(), any(JsonNode.class), any(NameLabel.class), anyString(), anyString(), any(Environment.class)))
                .thenReturn(retVal);

        MvcResult results = mockMvc.perform(post(Constants.BASE_URI +
                Constants.EXECUTE_ACTION_URI, CLIENT_ID, ACCOUNT_ID, Environment.TEST, "testEntity", actionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn();
        String result = results.getResponse().getContentAsString();

        assertEquals(retVal.toString(), result);
    }
}