package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ActionDefinitionConverterTest {

    @Mock
    ActionDefinition actionDefinition;
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ActionDefinitionConverter actionDefinitionConverter;

    @Test
    public void test_convertToDatabaseColumn() {
        String result = actionDefinitionConverter.convertToDatabaseColumn(actionDefinition);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute() {
        String json = "{\"key\":\"value\"}";
        ActionDefinition result = actionDefinitionConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToDatabaseColumn_Ex() throws JsonProcessingException {
        Mockito.when(objectMapper.writeValueAsString(Mockito.<TypeReference<?>>any())).thenThrow(JsonProcessingException.class);
        String result = actionDefinitionConverter.convertToDatabaseColumn(actionDefinition);
        Assert.assertEquals("", result);
    }

    @Test
    public void test_convertToEntityAttribute_Ex() throws IOException {
        String json = "{\"key\":\"value\"}";
        Mockito.lenient().when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(IOException.class);
        ActionDefinition result = actionDefinitionConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}
