package com.tfsc.ilabs.selfservice.action.repositories;

import com.tfsc.ilabs.selfservice.action.models.ActionWorkflow;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ravi.b on 23-07-2019.
 */
public interface ActionWorkflowRepository extends JpaRepository<ActionWorkflow, Integer> {
    List<ActionWorkflow> findAllByWorkflowTemplate(WorkflowTemplate workflowTemplate);

    List<ActionWorkflow> findAllByWorkflowTemplateAndActionDefinitionType(WorkflowTemplate workflowTemplate,
                                                                          DefinitionType type);
}
