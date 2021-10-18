package com.tfsc.ilabs.selfservice.action.repositories;

import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by ravi.b on 05-06-2019.
 */
@RepositoryRestResource()
public interface ActionExecutionMonitorRepository extends JpaRepository<ActionExecutionMonitor, Integer> {

    List<ActionExecutionMonitor> findAllByActionAndWorkflowInstanceAndEnv(Action action, WorkflowInstance workflowInstance , Environment environment);

    ActionExecutionMonitor findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(Action action, WorkflowInstance workflowInstance, EntityInstance entityInstance,Environment environment);
}
