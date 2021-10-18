package com.tfsc.ilabs.selfservice.action.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ConfigValuesConverterTest {

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ConfigValuesConverter configValuesConverter;

    @Test
    public void test_convertToDatabaseColumn_withEmpty() throws Exception {
        List<String> input = new ArrayList<>();
        String expected = "[]";
        Mockito.when(objectMapper.writeValueAsString(Mockito.anyList())).thenReturn("[]");
        String result = configValuesConverter.convertToDatabaseColumn(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void test_convertToDatabaseColumn_withNull() throws Exception {
        String result = configValuesConverter.convertToDatabaseColumn(null);
        Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsString(Mockito.anyList());
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToDatabaseColumn() throws Exception {
        List<String> input = new ArrayList<>() {{
            add("1");
            add("2");
        }};
        String expected = "[\"1\", \"2\"]";
        Mockito.when(objectMapper.writeValueAsString(Mockito.anyList())).thenReturn(expected);
        String result = configValuesConverter.convertToDatabaseColumn(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void test_convertToEntityAttribute_withNull() throws Exception {
        Mockito.verify(objectMapper, Mockito.times(0)).readValue(Mockito.anyString(), Mockito.any(TypeReference.class
        ));
        List<String> result = configValuesConverter.convertToEntityAttribute(null);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute_withEmpty() throws Exception {
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(new ArrayList<String>());
        List<String> result = configValuesConverter.convertToEntityAttribute("[]");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test_convertToEntityAttribute() throws Exception {
        String input = "[\"1\", \"2\"]";
        List<String> expected = new ArrayList<>() {{
            add("1");
            add("2");
        }};
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(expected);
        List<String> result = configValuesConverter.convertToEntityAttribute(input);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("2", result.get(1));
    }
}
