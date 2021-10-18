package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.workflow.models.UiSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class UiSchemaConverter implements AttributeConverter<UiSchema, String> {
    private static final Logger logger = LoggerFactory.getLogger(UiSchemaConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(UiSchema template) {
        try {
            return template != null ? objectMapper.writeValueAsString(template) : null;
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public UiSchema convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, UiSchema.class) : null;
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}
