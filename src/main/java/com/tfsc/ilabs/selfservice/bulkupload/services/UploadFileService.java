package com.tfsc.ilabs.selfservice.bulkupload.services;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UploadFileService {

    @Autowired
    private LocalFileStorageService localFileStorageService;

    @Autowired
    private CloudUploadService cloudUploadService;

    public UploadService upload(String where){
        if(where==null){
            return null;
        }
        if(where.equalsIgnoreCase(Constants.LOCAL_STORAGE)){
            return localFileStorageService;
        }
        if(where.equalsIgnoreCase(Constants.GCP_STORAGE)){
            return cloudUploadService;
        }
        return null;
    }
}
