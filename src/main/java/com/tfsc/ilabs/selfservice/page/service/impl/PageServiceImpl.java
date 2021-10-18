package com.tfsc.ilabs.selfservice.page.service.impl;

import com.tfsc.ilabs.selfservice.common.exception.InvalidArgumentsException;
import com.tfsc.ilabs.selfservice.common.exception.InvalidResourceException;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.exception.ResourceException;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageDataStatus;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
import com.tfsc.ilabs.selfservice.page.repositories.PageTemplateRepository;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ravi.b on 07-06-2019.
 */
@Service
@Transactional
public class PageServiceImpl implements PageService {

    @Value("${workflow_instance.timeout}")
    private Integer configuredTimeout;

    @Autowired
    private EntityService entityService;

    @Autowired
    private PageDataRepository pageDataRepository;

    @Autowired
    private PageTemplateRepository pageTemplateRepository;

    @Autowired
    private WorkflowService workflowService;

    @Override
    public List<PageTemplate> getAllPagesOfWorkflow(WorkflowTemplate workflowTemplate) {
        return workflowService.getAllPagesOfWorkflow(workflowTemplate);
    }

    private WorkflowInstance validateRequest(PageDataRequestDTO pageDataRequestDTO, Integer workflowInstanceId, String pageTemplateId) {
        Optional<PageTemplate> optionalPageTemplate = pageTemplateRepository.findById(pageTemplateId);
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowService.getInstance(workflowInstanceId);
        WorkflowInstance workflowInstance = new WorkflowInstance();
        if (optionalWorkflowInstance.isEmpty()) {
            throw new InvalidResourceException(new ErrorObject("workflow instance provided is not valid", workflowInstanceId));
        }
        DefinitionType pageType = DefinitionType.CREATE;
        if (optionalPageTemplate.isPresent()) {
            pageType = optionalPageTemplate.get().getType();
        }
        List<String> duplicateEntities=new ArrayList<>();
        if (optionalWorkflowInstance.isPresent()) {
            workflowInstance = optionalWorkflowInstance.get();
            List<String> entities = pageDataRequestDTO.getEntities().stream().map(NameLabel::getName).collect(Collectors.toList());
            duplicateEntities = workflowService.getEntitiesInUseByOtherWorkflows(
                    workflowInstance.getWorkflowTemplate(), entities, workflowInstance);
        }


        if (!duplicateEntities.isEmpty() && DefinitionType.CREATE.equals(pageType)) {
            throw new InvalidResourceException(new ErrorObject("Found duplicate entities", duplicateEntities));
        }
        if (!workflowInstance.isLockAvailable(SecurityContextUtil.getCurrentUserId(), configuredTimeout)) {
                // If this workflowInstance is locked by another user
                // Then do not save the page information
                ResourceException resourceException = new ResourceException(new ErrorObject(Constants.RESOURCE_LOCKED));
                resourceException.setHttpStatus(HttpStatus.LOCKED);
                throw resourceException;
        }
        if (!WorkflowInstanceStatus.DRAFT_EDIT.equals(workflowInstance.getStatus())
                    && !WorkflowInstanceStatus.DRAFT_SAVE.equals(workflowInstance.getStatus())) {
                throw new InvalidArgumentsException(new ErrorObject("workflow instance is not in valid state to save data", workflowInstanceId));
        }

        return workflowInstance;
    }

    @Override
    public PageDataResponseDTO savePageData(PageDataRequestDTO pageDataRequestDTO, Integer workflowInstanceId, String pageTemplateId) {
        // Params: workflowInstanceId and pageTemplateId
        WorkflowInstance workflowInstance = validateRequest(pageDataRequestDTO, workflowInstanceId, pageTemplateId);

        entityService.save(workflowInstance, pageDataRequestDTO.getEntities());

        // Use the params to check if a pageData exists
        PageData savedPageData = pageDataRepository
                .findByWorkflowInstanceIdAndPageTemplateId(workflowInstanceId, pageTemplateId);

        // If it does not exist, then we should create a new page
        if (savedPageData == null) {
            Optional<PageTemplate> optionalPageTemplate = pageTemplateRepository.findById(pageTemplateId);
            if (optionalPageTemplate.isPresent()) {
                PageData newPageData = new PageData(workflowInstance, optionalPageTemplate.get(), pageDataRequestDTO.getData(),
                        PageDataStatus.NOT_COMPLETE);
                return pageDataRepository.save(newPageData).toDTO();
            }
        } else {
            // Else we should update existing pageData
            savedPageData.setData(pageDataRequestDTO.getData());
            return pageDataRepository.save(savedPageData).toDTO();
        }
        return null;
    }

    @Override
    public PageDataResponseDTO getPageDataByWorkflowInstanceAndPageTemplate(Integer workflowInstanceId, String pageTemplateId) {
        PageData pageData = pageDataRepository.findByWorkflowInstanceIdAndPageTemplateId(workflowInstanceId,
                pageTemplateId);
        if (pageData != null) {
            return pageData.toDTO();
        } else {
            throw new NoSuchResourceException(new ErrorObject("Page data not found for the workflow instance", workflowInstanceId));
        }
    }

    @Override
    public List<PageData> getPageDataByWorkflowInstace(WorkflowInstance workflowInstance) {
        return pageDataRepository.findAllByWorkflowInstance(workflowInstance);
    }

    @Override
    public Optional<PageTemplate> findById(String pageTemplateId) {
        return pageTemplateRepository.findById(pageTemplateId);
    }
}
