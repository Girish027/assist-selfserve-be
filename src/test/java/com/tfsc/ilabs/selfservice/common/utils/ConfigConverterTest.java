package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
public class ConfigConverterTest {
    @InjectMocks
    ConfigConverter configConverter;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    JsonNode jsonNode;

    @Test
    public void test_convertToDatabaseColumn(){
        String result = configConverter.convertToDatabaseColumn(jsonNode);
        Assert.assertNotNull(result);
    }

    @Test
    public void test_convertToEntityAttribute(){
        String json = "{\"key\":\"value\"}";
        JsonNode result = configConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute_Ex() throws IOException {
        String json = "{\"key\":\"value\"}";
        Mockito.lenient().when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(new IOException());
        JsonNode result = configConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}
