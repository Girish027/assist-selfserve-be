package com.tfsc.ilabs.selfservice.bulkupload.services;

import com.tfsc.ilabs.selfservice.bulkupload.exception.FileStorageException;
import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.FileUtils;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowInstanceRepository;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileUploader {
    @Autowired
    Environment env;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    WorkflowInstanceRepository workflowInstanceRepository;

    public UploadFile upload(MultipartFile file, Integer workflowInstanceId, String pageTemplateId,String uploadKey) throws IOException {
        String accountId = null;
        String clientId = null;
        Optional<WorkflowInstance> instance =  workflowInstanceRepository.findById(workflowInstanceId);
        if (instance.isPresent()) {
            accountId = instance.get().getAccountId();
            clientId = instance.get().getAccountId();
        }

        if(!FileUtils.isValidFile(FileUtils.getFileName(clientId,accountId,file),env.getProperty(Constants.FILE_TYPES))  || !FileUtils.checkMimeType(file,env.getProperty(Constants.FILE_MIME_TYPES))){
            throw new FileStorageException(new ErrorObject("File format (."+FileUtils.getFileExtension(FileUtils.getFileName(clientId,accountId,file))+") not supported "));
        }
        else if(file.isEmpty()){
            throw new FileStorageException(new ErrorObject("File is empty"));
        }

        else if(!FileUtils.isValidFileName(file.getOriginalFilename())){
            throw new FileStorageException(new ErrorObject("File name contains unsupported special characters"));
        }
        else if (!FileUtils.isValidContent(file)) {
            throw new FileStorageException(new ErrorObject("File content is not valid"));
        }
        UploadService service = uploadFileService.upload(env.getProperty(Constants.STORAGE_TYPE));
        return service.upload(file,clientId,accountId,workflowInstanceId,pageTemplateId,uploadKey);
    }
}
