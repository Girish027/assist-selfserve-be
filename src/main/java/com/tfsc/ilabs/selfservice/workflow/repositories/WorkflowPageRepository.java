package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowPage;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Created by ravi.b on 05-06-2019.
 */
@RepositoryRestResource()
public interface WorkflowPageRepository extends JpaRepository<WorkflowPage, Integer> {
    List<WorkflowPage> findAllByWorkflowTemplate(WorkflowTemplate workflowTemplate);

    WorkflowPage findByWorkflowTemplateIdAndPageOrder(String workflowTemplate, int pageOrder);

    List<WorkflowPage> findAllByWorkflowTemplateAndPageTemplateType(WorkflowTemplate workflowTemplate, DefinitionType type);

    Optional<List<WorkflowPage>> findByPageTemplateId(String pageTemplateId);

}
