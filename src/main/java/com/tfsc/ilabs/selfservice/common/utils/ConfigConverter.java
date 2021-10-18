package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class ConfigConverter implements AttributeConverter<JsonNode, String> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(JsonNode jsonValue) {
        return jsonValue != null ? jsonValue.toString() : null;
    }

    @Override
    public JsonNode convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readTree(jsonString) : null;
        } catch (IOException e) {
            logger.error("Threw a IOException in ConfigConverter::convertToEntityAttribute, full stack trace follows: ", e);
        }
        return null;
    }
}
