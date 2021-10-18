package com.tfsc.ilabs.selfservice.bulkupload.controllers;



import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;

import com.tfsc.ilabs.selfservice.bulkupload.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.ws.rs.Consumes;
import java.io.IOException;


@RestController
@RequestMapping("/api")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    FileUploader fileUploader;

    @PreAuthorize("@authorization.hasUploadPermission(authentication, #workflowInstanceId, 'ACTIVITY_LIVE_PUBLISH', 'ACTIVITY_TEST_PUBLISH')")
    @PostMapping("/workflow/instance/{workflowInstanceId}/page/{pageTemplateId}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFile uploadFile(@RequestParam(name = "file") MultipartFile uploadFile , @PathVariable Integer workflowInstanceId,
                                 @PathVariable String pageTemplateId, @RequestParam(value = "uploadKey") String uploadKey) throws IOException {
         return fileUploader.upload(uploadFile, workflowInstanceId,pageTemplateId,"file");

    }
}

