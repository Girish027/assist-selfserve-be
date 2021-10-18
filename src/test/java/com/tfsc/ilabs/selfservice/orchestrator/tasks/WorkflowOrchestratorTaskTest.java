package com.tfsc.ilabs.selfservice.orchestrator.tasks;

import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityIdLookup;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityIdLookupRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.orchestrator.dependencyhandler.DependencyGraph;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.testcontainer.ActionExecutionMonitorContainer;
import com.tfsc.ilabs.selfservice.testcontainer.EntityInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseUtil.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class WorkflowOrchestratorTaskTest {

    private final DependencyGraph<ActionExecutionMonitor> dependencyGraph = getDependencyGraph();
    @InjectMocks
    WorkflowOrchestratorTask workflowOrchestratorTask;
    @Mock
    WorkflowInstance workflowInstance;
    @Mock
    Set<DependencyGraph<ActionExecutionMonitor>> graphSet;
    @Mock
    ExecutorService executorService;
    @Mock
    DBConfigService dbConfigService;
    @Mock
    WorkflowService workflowService;
    @Mock
    EntityService entityService;
    @Mock
    EntityIdLookupRepository entityIdLookupRepository;
    @Mock
    ActionExecutorService actionExecutorService;

    private DependencyGraph<ActionExecutionMonitor> getDependencyGraph() {
        ActionExecutionMonitor actionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        Set<ActionExecutionMonitor> all = new HashSet<>();
        all.add(actionExecutionMonitor);
        Map<ActionExecutionMonitor, Set<ActionExecutionMonitor>> itemToDependenciesMap = new HashMap<>();
        return new DependencyGraph<>(itemToDependenciesMap, all);
    }

    @Before
    public void init() {
        PowerMockito.mockStatic(BaseUtil.class);
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(null, "queue", 1, "queue");
        workflowInstance = WorkflowInstanceContainer.getWorkflowInstance(entityInstance);
        workflowInstance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_LIVE);
        graphSet = Set.of(dependencyGraph);

        PowerMockito.when(BaseUtil.isNullOrBlank(any())).thenReturn(false);
        PowerMockito.when(BaseUtil.isNotNullOrBlank(any())).thenReturn(true);
        PowerMockito.when(BaseUtil.shouldStubLivePublish(any(), any())).thenReturn(false);
        Mockito.when(executorService.submit(any(ActionTask.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        Mockito.when(entityService.getInstance(any())).thenReturn(Optional.of(entityInstance));
        Mockito.when(entityIdLookupRepository.findByEntityTemplateAndTestEntityId(any(), any()))
                .thenReturn(null);
        Mockito.when(entityIdLookupRepository.save(any(EntityIdLookup.class))).thenReturn(null);
        Mockito.when(workflowService.getInstance(2))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getParentWorkflowInstance()));
        Mockito.doNothing().when(workflowService).save(any(WorkflowInstance.class));
        Mockito.doNothing().when(entityService).save(any(EntityInstance.class));
        Mockito.doNothing().when(actionExecutorService).save(any(ActionExecutionMonitor.class));

        String accountId = "accountX";
        String clientId = "clientX";
        workflowOrchestratorTask = new WorkflowOrchestratorTask(workflowService, entityService,
                actionExecutorService, executorService, dbConfigService, entityIdLookupRepository,
                workflowInstance, graphSet, clientId, accountId, new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList("")));
    }

    @Test
    public void test_run() {
        workflowOrchestratorTask.run();
        Mockito.verify(executorService, Mockito.times(1)).submit(any(ActionTask.class));
    }

    @Test
    public void test_onSuccess() {
        ActionExecutionMonitor actionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        actionExecutionMonitor.getWorkflowInstance().setStatus(WorkflowInstanceStatus.PROMOTED_TO_LIVE);
        PowerMockito.when(BaseUtil.translateAPIResponse(any(), any(), any()))
                .thenReturn(new ApiResponse(PublishState.SUCCESS, "entityId"));
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any()))
                .thenReturn(Environment.TEST);

        workflowOrchestratorTask.onActionComplete(actionExecutionMonitor, dependencyGraph);
        Mockito.verify(workflowService, Mockito.times(1)).save(any(WorkflowInstance.class));
        Mockito.verify(workflowService, Mockito.times(1)).markWorkflowInstanceStatusCompleted(any(WorkflowInstance.class));
        Mockito.verify(entityService, Mockito.times(1)).markEntityInstanceStatusCompleted(any(EntityInstance.class));
    }

    @Test
    public void test_onFailure() {
        ActionExecutionMonitor actionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        PowerMockito.when(BaseUtil.translateAPIResponse(any(), any(), any()))
                .thenReturn(new ApiResponse(PublishState.ERROR, "ERROR"));
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any()))
                .thenReturn(Environment.TEST);

        workflowOrchestratorTask.onFailureEvent(actionExecutionMonitor, dependencyGraph);
        Mockito.verify(workflowService, Mockito.times(1)).save(any(WorkflowInstance.class));
        Mockito.verify(workflowService, Mockito.times(1)).markWorkflowInstanceStatusFailed(any(WorkflowInstance.class));
        Mockito.verify(entityService, Mockito.times(1)).markEntityInstanceStatusFailed(any(EntityInstance.class));
    }

    @Test
    public void test_onFailure_inOnActionComplete() {
        ActionExecutionMonitor actionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        PowerMockito.when(BaseUtil.translateAPIResponse(any(), any(), any()))
                .thenReturn(new ApiResponse(PublishState.ERROR, "ERROR"));
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any()))
                .thenReturn(Environment.TEST);

        workflowOrchestratorTask.onActionComplete(actionExecutionMonitor, dependencyGraph);
        Mockito.verify(workflowService, Mockito.times(2)).save(any(WorkflowInstance.class));
        Mockito.verify(workflowService, Mockito.times(1)).markWorkflowInstanceStatusFailed(any(WorkflowInstance.class));
        Mockito.verify(entityService, Mockito.times(1)).markEntityInstanceStatusFailed(any(EntityInstance.class));
    }
}
