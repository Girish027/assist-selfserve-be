package com.tfsc.ilabs.selfservice.workflow.controllers;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.*;
import static org.mockito.Mockito.verify;

/**
 * Created by prasada on 19-09-2019.
 */

@RunWith(MockitoJUnitRunner.class)
public class WorkflowControllerTest {
	@InjectMocks
	private WorkflowController workflowController;

	@Mock
	private WorkflowService workflowService;

	@Mock
	private SessionValidator sessionValidator;

	@Test
	public void testGetWorkflowTemplates() throws Exception {
		workflowController.getWorkflowTemplates(ScopeType.client, CLIENT_ID);
		verify(workflowService).getActivities(ScopeType.client, CLIENT_ID);
	}

	@Test
	public void testGetWorkflowInstances() throws Exception {
		workflowController.getWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
		verify(workflowService).getWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
	}

	@Test
	public void getWorkflowInstancesCountSummary() throws Exception {
		workflowController.getWorkflowInstancesCountSummary(CLIENT_ID, ACCOUNT_ID);
		verify(workflowService).getWorkflowInstancesCountSummary(CLIENT_ID, ACCOUNT_ID);
	}

	@Test
	public void testGetOrderedWorkflowInstancesWithWorkflow() throws Exception {
		workflowController.getOrderedWorkflowInstances(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID);
		verify(workflowService).getWorkflowInstancesOrderByStatus(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID);
	}

	@Test
	public void testGetWorkflowInstancesWithWorkflow() throws Exception {
		workflowController.getWorkflowInstances(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID);
		verify(workflowService).getWorkflowInstances(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID);
	}

	@Test
	public void testGetWorkflowInstancesWithWorkflowAndStatus() throws Exception {
		workflowController.getWorkflowInstances(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID, STATUS);
		verify(workflowService).getWorkflowInstancesForStatus(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID, STATUS);
	}

	@Test
	public void testGetAll() throws Exception {
		workflowController.getWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
		verify(workflowService).getWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
	}

	@Test
	public void testDeleteWorkflowInstance() throws Exception {
		workflowController.deleteWorkflowInstanceDraft(WORKFLOW_INSTANCE_ID);
		verify(workflowService).deleteWorkflowInstance(WORKFLOW_INSTANCE_ID, Environment.DRAFT);
		workflowController.deleteWorkflowInstanceTest(WORKFLOW_INSTANCE_ID);
		verify(workflowService).deleteWorkflowInstance(WORKFLOW_INSTANCE_ID, Environment.TEST);
	}

	@Test
	public void testAcquireEditLock() throws Exception {
		workflowController.acquireEditLock(WORKFLOW_INSTANCE_ID);
		verify(workflowService).acquireEditLock(WORKFLOW_INSTANCE_ID);
	}

	@Test
	public void testReleaseEditLock() throws Exception {
		workflowController.releaseEditLock(WORKFLOW_INSTANCE_ID);
		verify(workflowService).releaseEditLock(WORKFLOW_INSTANCE_ID);
	}

	@Test
	public void testGetWorkflowDefintion() throws Exception {
		workflowController.getWorkflowDefintion(ID, TYPE);
		verify(workflowService).getWorkflowDefintion(ID, TYPE);
	}

	@Test
	public void testGetWorkflowMetaData() throws Exception {
		workflowController.getWorkflowMetadata(ID);
		verify(workflowService).getWorkflowMetaData(ID);
	}

	@Test
	public void testCreateWorkflowInstance() throws Exception {
		WorkflowInstanceRequestDTO payload = new WorkflowInstanceRequestDTO(PAGE_ID, ENTITIES);
		List<NameLabel> entities = payload.getEntities();
		String pageTemplateId = payload.getPageId();
		workflowController.createWorkflowInstance(payload, CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID, DefinitionType.CREATE);
		verify(workflowService).createWorkflowInstance(CLIENT_ID, ACCOUNT_ID, WORKFLOW_ID, payload, DefinitionType.CREATE);
	}

	@Test
	public void test_getParentNodes() throws Exception {
		workflowController.getParentNodes(ScopeType.client, CLIENT_ID);
		verify(workflowService).getParentNodes(ScopeType.client, CLIENT_ID);
	}

	@Test
	public void test_getAllNodes() throws Exception {
		workflowController.getAllNodes(MENU_GROUP_NAME, ScopeType.client, null);
		verify(workflowService).getAllNodes(MENU_GROUP_NAME, ScopeType.client, null);
	}

	@Test
	public void test_pollWorkflowInstances() throws  Exception {
		workflowController.pollWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
		verify(workflowService).pollWorkflowInstances(CLIENT_ID, ACCOUNT_ID);
	}
}
