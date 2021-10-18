package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class ActionExecutionMetaConverter implements AttributeConverter<ActionExecutionMeta, String> {
    private static final Logger logger = LoggerFactory.getLogger(ActionExecutionMetaConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ActionExecutionMeta actionConfig) {
        try {
            return actionConfig != null ? objectMapper.writeValueAsString(actionConfig) : "{}";
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in ActionExecutionMetaConverter::convertToDatabaseColumn, full stack trace follows", e);
        }
        return "";
    }

    @Override
    public ActionExecutionMeta convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, ActionExecutionMeta.class) : null;
        } catch (IOException e) {
            logger.error("Threw a IOException in ActionExecutionMetaConverter::convertToEntityAttribute, full stack trace follows", e);
        }
        return null;
    }
}
