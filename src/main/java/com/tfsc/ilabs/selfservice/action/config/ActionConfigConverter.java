package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Converter
public class ActionConfigConverter implements AttributeConverter<Map<ActionConfigType, ActionConfig>, String> {
    private static final Logger logger = LoggerFactory.getLogger(ActionConfigConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<ActionConfigType, ActionConfig> actionConfig) {
        try {
            return actionConfig != null ? objectMapper.writeValueAsString(actionConfig) : "{}";
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in ActionExecutionMetaConverter::convertToDatabaseColumn, full stack trace follows", e);
        }
        return "";
    }

    @Override
    public Map<ActionConfigType, ActionConfig> convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, new TypeReference<Map<ActionConfigType, ActionConfig>>() {
            }) : null;
        } catch (IOException e) {
            logger.error("Threw a IOException in ActionConfigConverter::convertToEntityAttribute, full stack trace follows", e);
        }
        return null;
    }
}
