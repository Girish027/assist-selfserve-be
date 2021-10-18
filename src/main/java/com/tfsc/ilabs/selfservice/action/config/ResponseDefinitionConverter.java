package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
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
public class ResponseDefinitionConverter implements AttributeConverter<ResponseDefinition, String> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseDefinitionConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ResponseDefinition definition) {
        try {
            return definition != null ? objectMapper.writeValueAsString(definition) : "{}";
        } catch (JsonProcessingException e) {
            logger.error(String.format("Threw a JsonProcessingException in ResponseDefinitionConverter::convertToDatabaseColumn, full stack trace follows: %s", e.getMessage()));
        }
        return "";
    }

    @Override
    public ResponseDefinition convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, ResponseDefinition.class) : null;
        } catch (IOException e) {
            logger.error(String.format("Threw a IOException in ResponseDefinitionConverter::convertToEntityAttribute, full stack trace follows: %s", e.getMessage()));
        }
        return null;
    }
}
