package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.configpuller.model.ParamConfigTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.UiSchema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class UiSchemaConverterTest {

    @InjectMocks
    UiSchemaConverter uiSchemaConverter;

    @Mock
    UiSchema uiSchema;

    @Mock
    ObjectMapper objectMapper;

    @Test
    public void test_convertToDatabaseColumn(){
        String result = uiSchemaConverter.convertToDatabaseColumn(uiSchema);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute(){
        String json = "{\"key\":\"value\"}";
        UiSchema result = uiSchemaConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToDatabaseColumn_Ex() throws JsonProcessingException {
        Mockito.when(objectMapper.writeValueAsString(Mockito.<TypeReference<?>>any())).thenThrow(JsonProcessingException.class);
        String result = uiSchemaConverter.convertToDatabaseColumn(uiSchema);
        Assert.assertEquals(null, result);
    }

    @Test
    public void test_convertToEntityAttribute_Ex() throws IOException {
        String json = "{\"key\":\"value\"}";
        Mockito.lenient().when(objectMapper.readValue(Mockito.anyString(), Mockito.<TypeReference<?>>any())).thenThrow(new IOException());
        UiSchema result = uiSchemaConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}
