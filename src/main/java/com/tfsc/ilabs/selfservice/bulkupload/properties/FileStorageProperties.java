package com.tfsc.ilabs.selfservice.bulkupload.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String fileTypes;

    public String getUploadDir() {
        return uploadDir;
    }

    public String getTypes() {
        return fileTypes;
    }

    public void setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
