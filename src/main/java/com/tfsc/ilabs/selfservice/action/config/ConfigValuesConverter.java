package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ConfigValuesConverter implements AttributeConverter<List<String>, String> {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponseConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error(String.format("Error occurred while trying to map List to JSON String in ConfigValuesConverter::convertToDatabaseColumn, message: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            logger.error(String.format("Error occurred while trying to map JSON String to List in ConfigValuesConverter::convertToEntityAttribute, message: %s", e.getMessage()));
            return new ArrayList<String>();
        }
    }
}