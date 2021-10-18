package com.tfsc.ilabs.selfservice.orchestrator.controller;

import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.OrchestratorService;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.ACCOUNT_ID;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.CLIENT_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class OrchestrationControllerTest {

    @InjectMocks
    OrchestrationController orchestrationController;

    @Mock
    OrchestratorService orchestratorService;
    @Mock
    WorkflowService workflowService;
    @Mock
    WorkflowInstanceDTO workflowInstanceDTO;

    private MockMvc mockMvc;
    private Integer testWorkflowInstanceId = 5;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(orchestrationController).build();

        Mockito.when(orchestratorService.getStatus(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId)).thenReturn(workflowInstanceDTO);
    }

    @Test
    public void test_getPublishedWorkflowStatus() throws Exception {
        WorkflowInstanceDTO result = orchestrationController.getPublishedWorkflowStatus(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId);
        Assert.assertEquals(result, workflowInstanceDTO);
    }

    @Test
    public void test_publishWorkflowTest() throws Exception {
        String endpoint = Constants.BASE_URI
        +"/client/" + CLIENT_ID + "/account/" + ACCOUNT_ID + "/workflow/instance/" + testWorkflowInstanceId + "/publish/test";
        Mockito.when(workflowService.validateAndGetWorkflowInstance(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId))
                .thenReturn(WorkflowInstanceContainer.getWorkflowInstance());

        mockMvc.perform(post(endpoint)).andExpect(status().isOk());

        verify(orchestratorService, times(1)).orchestrate(CLIENT_ID, ACCOUNT_ID,
                WorkflowInstanceContainer.getWorkflowInstance(), Environment.TEST);
    }

    @Test
    public void test_publishWorkflowLive() throws Exception {
        String endpoint = Constants.BASE_URI +
        "/client/" + CLIENT_ID + "/account/" + ACCOUNT_ID + "/workflow/instance/" + testWorkflowInstanceId + "/publish/live";
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        workflowInstance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_TEST);
        Mockito.when(workflowService.validateAndGetWorkflowInstance(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId))
                .thenReturn(workflowInstance);

        mockMvc.perform(post(endpoint)).andExpect(status().isOk());

        verify(orchestratorService, times(1)).orchestrate(CLIENT_ID, ACCOUNT_ID, workflowInstance, Environment.LIVE);
    }

    @Test
    public void test_publishWorkflowLive_fromTestUrl() throws Exception {
        String endpoint = Constants.BASE_URI
        + "/client/" + CLIENT_ID + "/account/" + ACCOUNT_ID + "/workflow/instance/" + testWorkflowInstanceId + "/publish/test";
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        workflowInstance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_TEST);
        Mockito.when(workflowService.validateAndGetWorkflowInstance(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId))
                .thenReturn(workflowInstance);

        mockMvc.perform(post(endpoint)).andExpect(status().isOk());

        verify(orchestratorService, times(0)).orchestrate(CLIENT_ID, ACCOUNT_ID, workflowInstance,
                Environment.LIVE);
    }

    @Test(expected = NestedServletException.class)
    public void test_publishWorkflowTest_withInvalidWorkflowId() throws Exception {
        String endpoint = Constants.BASE_URI
        + "/client/" + CLIENT_ID + "/account/" + ACCOUNT_ID + "/workflow/instance/" + testWorkflowInstanceId + "/publish/test";
        Mockito.when(workflowService.validateAndGetWorkflowInstance(CLIENT_ID, ACCOUNT_ID, testWorkflowInstanceId))
                .thenReturn(null);

        mockMvc.perform(post(endpoint)).andReturn();
    }
}