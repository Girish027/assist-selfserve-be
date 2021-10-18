package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by ravi.b on 05-06-2019.
 */
@CrossOrigin
@RepositoryRestResource()
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Integer> {
    List<WorkflowInstance> findAllByWorkflowTemplate(Optional<WorkflowTemplate> workflowTemplate);

    List<WorkflowInstance> findByWorkflowTemplateAndClientIdAndAccountId(Optional<WorkflowTemplate> workflowTemplate,String clientId, String accountId);

    List<WorkflowInstance> findByClientIdAndAccountId(String clientId, String accountId);

    List<WorkflowInstance> findByClientIdAndAccountIdOrderByLastUpdatedOnDesc(String clientId, String accountId);

    List<WorkflowInstance> findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(
            String clientId, String accountId, List<WorkflowInstanceStatus> statuses, String entityName, String workflowTemplateId);
    List<WorkflowInstance> findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(String clientId, String accountId, boolean hidden, Date createdOn);
}
