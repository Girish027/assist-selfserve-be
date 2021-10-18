package com.tfsc.ilabs.selfservice.orchestrator.service;

import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ActionType;
import com.tfsc.ilabs.selfservice.action.repositories.ActionExecutionMonitorRepository;
import com.tfsc.ilabs.selfservice.action.repositories.ActionWorkflowRepository;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.exception.InvalidResourceException;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.orchestrator.service.impl.OrchestratorServiceImpl;
import com.tfsc.ilabs.selfservice.orchestrator.tasks.WorkflowOrchestratorTask;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageDataStatus;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrchestratorServiceTest {
    @InjectMocks
    OrchestratorServiceImpl orchestratorService;

    @Mock
    ActionWorkflowRepository actionWorkflowRepository;

    @Mock
    ActionExecutorService actionExecutorService;

    @Mock
    ExecutorService executorService;

    @Mock
    EntityInstanceRepository entityInstanceRepository;

    @Mock
    PageDataRepository pageDataRepository;

    @Mock
    WorkflowService workflowService;

    @Mock
    ActionExecutionMonitorRepository actionExecutionMonitorRepository;

    @Mock
    EntityInstance entityInstance;

    private String clientId = "test-client";
    private String accountId = "test-account";
    private Integer testWorkflowInstanceId = 1;
    private WorkflowInstance workflowInstance = null;

    @Before
    public void init() throws IOException {
        Set<EntityInstance> set = new HashSet<>();
        set.add(getEntityInstance());

        workflowInstance = new WorkflowInstance(getWorkflowTemplate(), getPageTemplate(), clientId, accountId, WorkflowInstanceStatus.DRAFT_SAVE, DefinitionType.CREATE);
        workflowInstance.setEntityInstances(set);
        Mockito.when(workflowService.validateAndGetWorkflowInstance(clientId, accountId, testWorkflowInstanceId))
                .thenReturn(workflowInstance);
    }

    @Test
    public void test_getStatus() {
        WorkflowInstanceDTO result = orchestratorService.getStatus(clientId, accountId, testWorkflowInstanceId);

        Assert.assertEquals(workflowInstance.toDTO(), result);
    }

    @Test
    public void test_orchestrate() throws IOException {
        PageData pageData = new PageData(workflowInstance, getPageTemplate(), getTestJsonNode(), PageDataStatus.NOT_COMPLETE);
        List<PageData> pageDataList = new ArrayList<>();
        pageDataList.add(pageData);

        Action action = new Action("test-description", null, ActionType.REST, DefinitionType.CREATE);

        ActionExecutionMonitor actionExecutionMonitor = new ActionExecutionMonitor(action, workflowInstance,
                getEntityInstance(), Environment.DRAFT);

        Mockito.when(pageDataRepository.findAllByWorkflowInstance(workflowInstance)).thenReturn(pageDataList);
        Mockito.when(actionExecutorService.createActionExecutionMonitorIfNotExist(any(), any()))
                .thenReturn(List.of(action));
        Mockito.when(entityInstanceRepository.findAllByWorkflowInstance(workflowInstance))
                .thenReturn(List.of(getEntityInstance()));
        Mockito.when(actionExecutionMonitorRepository.findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(any(), any(), any(), any()))
                .thenReturn(actionExecutionMonitor);
        Mockito.when(executorService.submit(any(WorkflowOrchestratorTask.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        orchestratorService.orchestrate(clientId, accountId, workflowInstance, Environment.TEST);

        Mockito.verify(executorService, times(1))
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    private EntityInstance getEntityInstance() {
        entityInstance = new EntityInstance("test-name", "test-label", workflowInstance, getEntityTemplate(), EntityInstanceStatus.DRAFT, null);
        return entityInstance;
    }

    @Test(expected = InvalidResourceException.class)
    public void test_orchestrate_withInvalidWorkflowInstanceId() {
        orchestratorService.orchestrate(clientId, accountId, null, Environment.TEST);

        Mockito.verify(executorService, times(0))
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    @Test(expected = InvalidResourceException.class)
    public void test_orchestrate_withInvalidWorkflowInstanceStatus() {
        workflowInstance.setStatus(WorkflowInstanceStatus.PROMOTING_TO_TEST);
        orchestratorService.orchestrate(clientId, accountId, workflowInstance, Environment.LIVE);

        Mockito.verify(executorService, times(0))
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_orchestrate_withEmptyPageTemplates() {
        List<PageData> pageDataList = new ArrayList<>();
        Mockito.when(pageDataRepository.findAllByWorkflowInstance(workflowInstance)).thenReturn(pageDataList);

        orchestratorService.orchestrate(clientId, accountId, workflowInstance, Environment.LIVE);

        Mockito.verify(executorService, times(0))
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_orchestrate_withEmptyActions() throws IOException {
        PageData pageData = new PageData(workflowInstance, getPageTemplate(), getTestJsonNode(), PageDataStatus.NOT_COMPLETE);
        List<PageData> pageDataList = new ArrayList<>();
        pageDataList.add(pageData);

        Mockito.when(pageDataRepository.findAllByWorkflowInstance(workflowInstance)).thenReturn(pageDataList);
        Mockito.when(actionExecutorService.createActionExecutionMonitorIfNotExist(any(), any()))
                .thenReturn(List.of());

        orchestratorService.orchestrate(clientId, accountId, workflowInstance, Environment.LIVE);

        Mockito.verify(executorService, times(0))
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    @Test
    public void test_orchestrate_withParentInstance() throws IOException {
        PageData pageData = new PageData(workflowInstance, getPageTemplate(), getTestJsonNode(), PageDataStatus.NOT_COMPLETE);
        List<PageData> pageDataList = new ArrayList<>();
        pageDataList.add(pageData);

        Action action = new Action("test-description", null, ActionType.REST, DefinitionType.CREATE);

        ActionExecutionMonitor actionExecutionMonitor = new ActionExecutionMonitor(action, workflowInstance,
                getEntityInstance(), Environment.DRAFT);

        Mockito.when(pageDataRepository.findAllByWorkflowInstance(workflowInstance)).thenReturn(pageDataList);
        Mockito.when(actionExecutorService.createActionExecutionMonitorIfNotExist(any(), any()))
                .thenReturn(List.of(action));
        Mockito.when(executorService.submit(any(WorkflowOrchestratorTask.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        WorkflowInstance parentInstance = getParentInstance();
        workflowInstance.setPendingParentId("2");
        workflowInstance.setType(DefinitionType.UPDATE);
        Mockito.when(workflowService.getInstance(anyInt())).thenReturn(Optional.of(parentInstance));

        orchestratorService.orchestrate(clientId, accountId, workflowInstance, Environment.LIVE);

        Mockito.verify(executorService, times(1)) // should be 2. We should handle Futures
                .submit(Mockito.any(WorkflowOrchestratorTask.class));
    }

    private WorkflowInstance getParentInstance() throws IOException {
        Set<EntityInstance> set = new HashSet<>();
        set.add(getEntityInstance());

        WorkflowInstance parentInstance = new WorkflowInstance(getWorkflowTemplate(), getPageTemplate(), clientId, accountId, WorkflowInstanceStatus.PROMOTED_TO_TEST, DefinitionType.CREATE);
        parentInstance.setEntityInstances(set);
        return parentInstance;
    }
}