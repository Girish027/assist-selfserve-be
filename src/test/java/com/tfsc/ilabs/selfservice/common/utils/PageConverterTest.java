package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.config.RequestDefinitionConverter;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import com.tfsc.ilabs.selfservice.page.models.PageDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class PageConverterTest {

    @InjectMocks
    PageConverter converter;

    @Mock
    PageDefinition requestDefinition;

    @Mock
    ObjectMapper objectMapper;

    @Test
    public void test_convertToDatabaseColumn(){
        String result = converter.convertToDatabaseColumn(requestDefinition);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute(){
        String json = "{\"key\":\"value\"}";
        PageDefinition result = converter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToDatabaseColumn_Ex() throws JsonProcessingException {
        Mockito.when(objectMapper.writeValueAsString(Mockito.<TypeReference<?>>any())).thenThrow(JsonProcessingException.class);
        String result = converter.convertToDatabaseColumn(requestDefinition);
        Assert.assertEquals(null, result);
    }

    @Test
    public void test_convertToEntityAttribute_Ex() throws IOException {
        String json = "{\"key\":\"value\"}";
        Mockito.lenient().when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(new IOException());
        PageDefinition result = converter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}