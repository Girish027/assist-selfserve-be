package com.tfsc.ilabs.selfservice.bulkupload.services;

import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    public UploadFile upload(MultipartFile file, String clientId, String accountId,Integer workflowInstanceId,String pageTemplateId,String uploadKey) throws IOException;
}
