package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ActionExecutionMetaConverterTest {

    @Mock
    ActionExecutionMeta actionExecutionMeta;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ActionExecutionMetaConverter actionExecutionMetaConverter;

    @Test
    public void test_convertToDatabaseColumn_withNull() {
        String result = actionExecutionMetaConverter.convertToDatabaseColumn(null);
        Assert.assertEquals("{}", result);
        try {
            Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsString(Mockito.any(Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertToEntityAttribute_withNull() {
        ActionExecutionMeta result = actionExecutionMetaConverter.convertToEntityAttribute(null);
        Assert.assertNull(result);
        try {
            Mockito.verify(objectMapper, Mockito.times(0)).readValue(Mockito.any(String.class), Mockito.any(TypeReference.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertToEntityAttribute() {
        ActionExecutionMeta meta = new ActionExecutionMeta();
        meta.setExecutionCount(2);
        String json = "{\"executionCount\": 2}";
        try {
            Mockito.when(objectMapper.readValue(json, ActionExecutionMeta.class))
                    .thenReturn(meta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActionExecutionMeta result = actionExecutionMetaConverter.convertToEntityAttribute(json);

        Assert.assertEquals(meta, result);
        try {
            Mockito.verify(objectMapper, Mockito.times(1)).readValue(json, ActionExecutionMeta.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertToDatabaseColumn_Exception() {
        try {
            Mockito.when(objectMapper.writeValueAsString(Mockito.<TypeReference<?>>any())).thenThrow(JsonProcessingException.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String result = actionExecutionMetaConverter.convertToDatabaseColumn(actionExecutionMeta);
        Assert.assertEquals("", result);
    }

    @Test
    public void test_convertToEntityAttribute_Exception() {
        String json = "{}";
        try {
            Mockito.when(objectMapper.readValue(json, ActionExecutionMeta.class)).thenThrow(IOException.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActionExecutionMeta result = actionExecutionMetaConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}
