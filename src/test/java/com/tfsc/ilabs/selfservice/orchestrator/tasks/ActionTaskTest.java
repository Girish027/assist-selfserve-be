package com.tfsc.ilabs.selfservice.orchestrator.tasks;

import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.ResponseCallback;
import com.tfsc.ilabs.selfservice.testcontainer.ActionExecutionMonitorContainer;
import com.tfsc.ilabs.selfservice.testcontainer.EntityInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.EntityTemplateContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ActionTaskTest {
    @InjectMocks
    ActionTask actionTask;

    @Mock
    EntityService entityService;
    @Mock
    ActionExecutorService actionExecutorService;
    @Mock
    ActionExecutionMonitor actionExecutionMonitor;
    @Mock
    ResponseCallback listener;

    @Test
    public void test_run_withFailedAction() {
        ActionExecutionMonitor currentActionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(
                EntityTemplateContainer.createEntityTemplate("queue", 1), "queue", 1, "queue");

        Mockito.when(actionExecutionMonitor.getEntityInstance())
                .thenReturn(entityInstance);
        Mockito.when(entityService.getInstance(any()))
                .thenReturn(java.util.Optional.of(entityInstance));
        Mockito.when(actionExecutorService.execute(any(), any(), any(), any(), any()))
                .thenReturn(ExecutionStatus.FAILED);
        Mockito.when(actionExecutorService.getActionExecutionMonitor(any()))
                .thenReturn(currentActionExecutionMonitor);

        actionTask.run();
        Mockito.verify(actionExecutorService, Mockito.times(1))
                .save(any());
        Mockito.verify(listener, Mockito.times(1))
                .onResponseFailure();
    }

    @Test
    public void test_run_withCompletedAction() {
        ActionExecutionMonitor currentActionExecutionMonitor = ActionExecutionMonitorContainer.createActionExecutionMonitorInstance();
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(
                EntityTemplateContainer.createEntityTemplate("queue", 1), "queue", 1, "queue");

        Mockito.when(actionExecutionMonitor.getEntityInstance())
                .thenReturn(entityInstance);
        Mockito.when(entityService.getInstance(any()))
                .thenReturn(java.util.Optional.of(entityInstance));
        Mockito.when(actionExecutorService.execute(any(), any(), any(), any(), any()))
                .thenReturn(ExecutionStatus.COMPLETED);
        Mockito.when(actionExecutorService.getActionExecutionMonitor(any()))
                .thenReturn(currentActionExecutionMonitor);

        actionTask.run();
        Mockito.verify(actionExecutorService, Mockito.times(1))
                .save(any());
        Mockito.verify(listener, Mockito.times(1))
                .onResponseSuccess();
    }
}
