package com.tfsc.ilabs.selfservice.bulkupload.controller;


import com.tfsc.ilabs.selfservice.bulkupload.controllers.FileUploadController;

import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import com.tfsc.ilabs.selfservice.bulkupload.services.FileUploader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    @InjectMocks
    FileUploadController fileController = new FileUploadController();

    @Mock
    FileUploader fileUploader;

    @Test
    public void uploadFileTest() throws IOException {
        String clientId = "test-id";
        String accountId ="test-id";
        String uploadKey ="key";
        String pageTemplateId ="test-id";
        Integer workflowInstanceId = 5;
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test","test.txt","text/plain","test data".getBytes());
        UploadFile result = fileController.uploadFile(mockMultipartFile,workflowInstanceId,pageTemplateId,uploadKey);
        Assert.assertNull(result);
    }
}
