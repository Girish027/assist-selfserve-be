package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
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
public class RequestDefinitionConverter implements AttributeConverter<RequestDefinition, String> {
    private static final Logger logger = LoggerFactory.getLogger(RequestDefinitionConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(RequestDefinition definition) {
        try {
            return definition != null ? objectMapper.writeValueAsString(definition) : "{}";
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in RequestDefinitionConverter::convertToDatabaseColumn, full stack trace follows: {}", e.getMessage());
        }
        return "";
    }

    @Override
    public RequestDefinition convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, RequestDefinition.class) : null;
        } catch (IOException e) {
            logger.error("Threw a JsonProcessingException in RequestDefinitionConverter::convertToEntityAttribute, full stack trace follows: {}", e.getMessage());
        }
        return null;
    }
}
