package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.configpuller.model.ParamConfigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class ParamConfigConverter implements AttributeConverter<ParamConfigTemplate, String> {
    private static final Logger logger = LoggerFactory.getLogger(ParamConfigConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ParamConfigTemplate paramConfig) {
        try {
            return paramConfig != null ? objectMapper.writeValueAsString(paramConfig) : null;
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in ParamConfigConverter::convertToDatabaseColumn, full stack trace follows: ", e);
        }
        return null;
    }

    @Override
    public ParamConfigTemplate convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, ParamConfigTemplate.class) : null;
        } catch (IOException e) {
            logger.error("Threw a IOException in ParamConfigConverter::convertToEntityAttribute, full stack trace follows: ", e);
        }
        return null;
    }
}
