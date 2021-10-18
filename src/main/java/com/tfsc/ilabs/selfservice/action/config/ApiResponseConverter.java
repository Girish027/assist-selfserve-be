package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class ApiResponseConverter implements AttributeConverter<ApiResponse, String> {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponseConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ApiResponse definition) {
        try {
            return definition != null ? objectMapper.writeValueAsString(definition) : "{}";
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in ApiResponseConverter::convertToDatabaseColumn, full stack trace follows: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResponse convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, ApiResponse.class) : null;
        } catch (IOException e) {
            logger.error("Threw a JsonProcessingException in ApiResponseConverter::convertToEntityAttribute, full stack trace follows: {}", e.getMessage());
        }
        return null;
    }
}
