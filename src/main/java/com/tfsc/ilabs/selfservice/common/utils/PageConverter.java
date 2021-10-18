package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.page.models.PageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class PageConverter implements AttributeConverter<PageDefinition, String> {
    private static final Logger logger = LoggerFactory.getLogger(PageConverter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(PageDefinition pageDefinition) {
        try {
            return pageDefinition != null ? objectMapper.writeValueAsString(pageDefinition) : null;
        } catch (JsonProcessingException e) {
            logger.error("Threw a JsonProcessingException in PageConverter::convertToDatabaseColumn, full stack trace follows: ", e);
        }
        return null;
    }

    @Override
    public PageDefinition convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? objectMapper.readValue(jsonString, PageDefinition.class) : null;
        } catch (IOException e) {
            logger.error("Threw a IOException in PageConverter::convertToEntityAttribute, full stack trace follows: ", e);
        }
        return null;
    }
}
