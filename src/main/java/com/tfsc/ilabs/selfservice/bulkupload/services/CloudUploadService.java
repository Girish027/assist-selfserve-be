package com.tfsc.ilabs.selfservice.bulkupload.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.tfsc.ilabs.selfservice.bulkupload.config.SaveInformation;
import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.FileUtils;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;


@Service
public class CloudUploadService implements UploadService{
    Logger logger = LoggerFactory.getLogger(CloudUploadService.class);

    @Autowired
    private Environment env;

    @Autowired
    SaveInformation saveInformation;

    @Autowired
    WorkflowService workflowService;

    @Override
    public UploadFile upload(MultipartFile file, String clientId, String accountId, Integer workflowInstanceId, String pageTemplateId,String uploadKey) throws IOException {
        String objectName = getUploadDirectory(file, clientId, accountId, workflowInstanceId);
        if(BaseUtil.isNullOrBlank(objectName)) {
            throw new SelfServeException("WorkflowInstance not found");
        }
        String projectId = env.getProperty(Constants.GCP_PROJECT_ID);
        String bucketName = env.getProperty(Constants.GCP_BUCKET_NAME);
        String credentialsFilePath = env.getProperty(Constants.CREDENTIALS_FILE_PATH);

        try {
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFilePath));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, file.getBytes());
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        logger.info(String.format("File %s uploaded to bucket %s as %s",file.getName(), bucketName, objectName));

        String filePath = "gs://"+bucketName+"/"+objectName;

        return saveInformation.saveInformation(file,clientId,accountId,workflowInstanceId,pageTemplateId,filePath,uploadKey,objectName);
    }

    private String getUploadDirectory(MultipartFile file, String clientId, String accountId, Integer workflowInstanceId) {
        Optional<WorkflowInstance> instance =  workflowService.getInstance(workflowInstanceId);
        if (instance.isPresent()) {
            return Constants.GCP_UPLOAD_DIR + "/" + clientId + "/" + instance.get().getWorkflowTemplate().getId() + "/" + FileUtils.getFileName(clientId, accountId, file);
        }
        return null;
    }
}

