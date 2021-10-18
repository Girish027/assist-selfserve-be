package com.tfsc.ilabs.selfservice.bulkupload.services;


import com.tfsc.ilabs.selfservice.bulkupload.config.SaveInformation;
import com.tfsc.ilabs.selfservice.bulkupload.exception.FileStorageException;
import com.tfsc.ilabs.selfservice.bulkupload.models.UploadFile;
import com.tfsc.ilabs.selfservice.bulkupload.properties.FileStorageProperties;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.springframework.core.env.Environment;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.utils.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class LocalFileStorageService implements UploadService{

    @Autowired
    SaveInformation saveInformation;

    @Autowired
    private Environment env;

    @Override
    public UploadFile upload(MultipartFile file, String clientId, String accountId, Integer workflowInstanceId, String pageTemplateId,String uploadKey) throws IOException {


        try {
            String uploadPath = env.getProperty(Constants.LOCAL_FILE_UPLOAD_PATH);
            String fileName = FileUtils.getFileName(clientId,accountId,file);
            Path targetLocation = Path.of(uploadPath + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileLocation = targetLocation.toAbsolutePath().toString().replace("\\", "/");
            return saveInformation.saveInformation(file,clientId,accountId,workflowInstanceId,pageTemplateId,fileLocation,uploadKey,fileName);

        } catch (IOException ex) {
            throw new FileStorageException(new ErrorObject("Could not store file " + FileUtils.getFileName(clientId,accountId,file) + ". Please try again!", ex));
        }
    }
}
