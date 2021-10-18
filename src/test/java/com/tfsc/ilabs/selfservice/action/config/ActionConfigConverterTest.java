package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionPathParamsConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionPollingConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ActionConfigConverterTest {

    @Mock
    Map<ActionConfigType, ActionConfig> actionConfigs;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ActionConfigConverter actionConfigConverter;

    @Test
    public void test_convertToDatabaseColumn_withNull() {
        String result = actionConfigConverter.convertToDatabaseColumn(null);
        Assert.assertEquals("{}", result);
        try {
            Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsString(Mockito.any(Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertToEntityAttribute_withNull() {
        Map<ActionConfigType, ActionConfig> result = actionConfigConverter.convertToEntityAttribute(null);
        Assert.assertNull(result);
        try {
            Mockito.verify(objectMapper, Mockito.times(0)).readValue(Mockito.any(String.class), Mockito.any(TypeReference.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertToEntityAttribute() {
        Map<ActionConfigType, ActionConfig> configs = new HashMap<>();
        ActionPollingConfig config = new ActionPollingConfig(3, 1000);
        configs.put(ActionConfigType.POLL, config);
        String json = "{\"POLL\":\"{\"type\":\"POLL\", \"retryInterval\":1000, \"retryCount\": 3}\"}";
        try {
            Mockito.when(objectMapper.readValue(Mockito.any(String.class), Mockito.any(TypeReference.class))).thenReturn(configs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<ActionConfigType, ActionConfig> result = actionConfigConverter.convertToEntityAttribute(json);

        Assert.assertEquals(configs, result);
        try {
            Mockito.verify(objectMapper, Mockito.times(1)).readValue(Mockito.any(String.class), Mockito.any(TypeReference.class));
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
        String result = actionConfigConverter.convertToDatabaseColumn(actionConfigs);
        Assert.assertEquals("", result);
    }

    @Test
    public void test_convertToEntityAttribute_Exception() {
        String json = "{}";
        try {
            Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(IOException.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<ActionConfigType, ActionConfig> result = actionConfigConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute_PathConfig() {
        Map<ActionConfigType, ActionConfig> configs = new HashMap<>();
        ActionPathParamsConfig config = new ActionPathParamsConfig(List.of(Map.of("key", "uploadOption")));
        configs.put(ActionConfigType.POLL, config);
        String json = "{\"PATH_PARAMS\": {\"type\": \"PATH_PARAMS\", \"pathParams\": [{\"key\": \"uploadOption\"}]}}";
        try {
            Mockito.when(objectMapper.readValue(Mockito.any(String.class), Mockito.any(TypeReference.class))).thenReturn(configs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<ActionConfigType, ActionConfig> result = actionConfigConverter.convertToEntityAttribute(json);

        Assert.assertEquals(configs, result);
        try {
            Mockito.verify(objectMapper, Mockito.times(1)).readValue(Mockito.any(String.class), Mockito.any(TypeReference.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
