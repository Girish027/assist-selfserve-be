package com.tfsc.ilabs.selfservice.bulkupload.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.FileUtils;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageDataStatus;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
import com.tfsc.ilabs.selfservice.page.repositories.PageTemplateRepository;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class SaveInformation {
    @Autowired
    WorkflowService workflowService;

    @Autowired
    PageDataRepository pageDataRepository;

    @Autowired
    PageTemplateRepository pageTemplateRepository;

    public UploadFile saveInformation(MultipartFile file, String clientId, String accountId, Integer workflowInstanceId, String pageTemplateId, String targetLocation,String uploadKey, String fileName) throws IOException {
        UploadFile uploadFile = new UploadFile(fileName,clientId,accountId,targetLocation,
                file.getContentType(), file.getSize(),FileUtils.getTimeStamp());
        // Use the params to check if a pageData exists
        PageData savedPageData = pageDataRepository
                .findByWorkflowInstanceIdAndPageTemplateId(workflowInstanceId, pageTemplateId);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        PageData newPageData = null;
        if(savedPageData == null){
            JsonNode update = objectNode.set(uploadKey,uploadFile.toJsonNode());
            Optional<WorkflowInstance> workflowInstance = workflowService.getInstance(workflowInstanceId);
            Optional<PageTemplate> pageTemplate = pageTemplateRepository.findById(pageTemplateId);
            if (workflowInstance.isPresent() && pageTemplate.isPresent()) {
                newPageData = new PageData(workflowInstance.get(), pageTemplate.get(),update, PageDataStatus.NOT_COMPLETE);
            }
            pageDataRepository.save(newPageData);
        }else{
            JsonNode data = savedPageData.getData();
            ((ObjectNode) data).replace(uploadKey, uploadFile.toJsonNode());
            savedPageData.setData(data);
            pageDataRepository.save(savedPageData);
        }
        return uploadFile;
    }
}
