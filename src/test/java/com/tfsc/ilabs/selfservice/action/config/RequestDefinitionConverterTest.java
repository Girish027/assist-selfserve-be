package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class RequestDefinitionConverterTest {
    @InjectMocks
    RequestDefinitionConverter requestDefinitionConverter;

    @Mock
    RequestDefinition requestDefinition;

    @Mock
    ObjectMapper objectMapper;

    @Test
    public void test_convertToDatabaseColumn() {
        String result = requestDefinitionConverter.convertToDatabaseColumn(requestDefinition);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute() {
        String json = "{\"key\":\"value\"}";
        RequestDefinition result = requestDefinitionConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToDatabaseColumn_Ex() throws JsonProcessingException {
        Mockito.when(objectMapper.writeValueAsString(Mockito.<TypeReference<?>>any())).thenThrow(JsonProcessingException.class);
        String result = requestDefinitionConverter.convertToDatabaseColumn(requestDefinition);
        Assert.assertEquals("", result);
    }

    @Test
    public void test_convertToEntityAttribute_Ex() throws IOException {
        String json = "{\"key\":\"value\"}";
        Mockito.lenient().when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(new IOException());
        RequestDefinition result = requestDefinitionConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}