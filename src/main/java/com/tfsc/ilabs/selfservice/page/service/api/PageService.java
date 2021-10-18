package com.tfsc.ilabs.selfservice.page.service.api;

import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Created by ravi.b on 07-06-2019.
 */
public interface PageService {
    List<PageTemplate> getAllPagesOfWorkflow(WorkflowTemplate workflowTemplate);

    PageDataResponseDTO savePageData(PageDataRequestDTO dataToSave, Integer workflowInstanceId, String pageTemplateId);

    PageDataResponseDTO getPageDataByWorkflowInstanceAndPageTemplate(Integer workflowInstanceId, String pageTemplateId);

    List<PageData> getPageDataByWorkflowInstace(WorkflowInstance workflowInstance);

    Optional<PageTemplate> findById(String pageTemplateId);
}
