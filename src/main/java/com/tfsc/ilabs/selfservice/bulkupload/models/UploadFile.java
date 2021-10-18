package com.tfsc.ilabs.selfservice.bulkupload.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
public class UploadFile {
    private String fileName;
    private  String clientId;
    private String accountId;
    private String filePath;
    private String fileType;
    private long size;
    private String timestamp;

    public UploadFile(String fileName,String clientId,String accountId,String filePath,  String fileType, long size,String timestamp) {
        this.fileName = fileName;
        this.clientId = clientId;
        this.accountId = accountId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.size = size;
        this.timestamp=timestamp;
    }

    public JsonNode toJsonNode() throws IOException {
        String jsonString = "{\"fileName\":\""+fileName+"\",\"fileLocation\":\""+filePath+"\",\"fileType\":\""+fileType+"\",\"size\":"+size+"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return  jsonNode;
    }
}
