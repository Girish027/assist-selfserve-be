package com.tfsc.ilabs.selfservice.page.repositories;

import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by ravi.b on 05-06-2019.
 */
@RepositoryRestResource()
public interface PageDataRepository extends JpaRepository<PageData, Integer> {
    PageData findByWorkflowInstanceIdAndPageTemplateId(Integer workflowInstanceId, String pageTemplateId);

    List<PageData> findAllByWorkflowInstance(WorkflowInstance workflowInstance);


}
