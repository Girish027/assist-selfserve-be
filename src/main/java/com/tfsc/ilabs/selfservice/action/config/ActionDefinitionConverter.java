package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionRestDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * Created by ravi.b on 04-06-2019.
 */

@Converter
public class ActionDefinitionConverter implements AttributeConverter<ActionDefinition, String> {
    private static final Logger logger = LoggerFactory.getLogger(ActionDefinitionConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ActionDefinition actionDefinition) {
        try {
            return actionDefinition != null ? objectMapper.writeValueAsString(actionDefinition) : "";
        } catch (JsonProcessingException e) {
            logger.error(String.format("Threw a JsonProcessingException in ActionDefinitionConverter::convertToDatabaseColumn, full stack trace follows: %s", e.getMessage()));
        }
        return "";
    }

    @Override
    public ActionDefinition convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, ActionRestDefinition.class) : null;
        } catch (IOException e) {
            logger.error(String.format("Threw a IOException in ActionDefinitionConverter::convertToEntityAttribute, full stack trace follows: %s", e.getMessage()));
        }
        return null;
    }
}
