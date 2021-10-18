package com.tfsc.ilabs.selfservice.configpuller.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.dto.ApiConfig;
import com.tfsc.ilabs.selfservice.configpuller.model.Config;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.configpuller.repositories.DBConfigRepository;
import com.tfsc.ilabs.selfservice.configpuller.services.impl.DBConfigServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DBConfigServiceTest {
    @InjectMocks
    DBConfigServiceImpl dbConfigService;
    @Mock
    DBConfigRepository dbConfigRepository;

    private void setUpForWrapperClass(String code) {
        Mockito.when(dbConfigRepository.findById(code))
                .thenReturn(Optional.of(new Config(code, "true", true, ConfigType.BACKEND)));
    }

    @Test
    public void test_getByCode_withWrapperClass() {
        String code = "testCode";
        setUpForWrapperClass(code);
        Boolean value = dbConfigService.findByCode(code, Boolean.class);
        Assert.assertEquals(true, value);
    }

    private void setUpForClass(String code) {
        Mockito.when(dbConfigRepository.findById(code))
                .thenReturn(Optional.of(new Config(
                                code,
                                "{\"url\": \"url\", \"method\":\"POST\", \"body\":\"\", \"headers\":{}}",
                                true,
                                ConfigType.BACKEND
                        ))
                );
    }

    @Test
    public void test_getByCode_withClass() {
        String code = "testCode";
        setUpForClass(code);
        ApiConfig value = dbConfigService.findByCode(code, ApiConfig.class);
        Assert.assertEquals("url", value.getUrl());
    }

    private void setUpForType(String code) {
        Mockito.when(dbConfigRepository.findById(code))
                .thenReturn(Optional.of(new Config(
                                code,
                                "[{\"url\": \"url\", \"method\":\"POST\", \"body\":\"\", \"headers\":{}}]",
                                true,
                                ConfigType.BACKEND
                        ))
                );
    }

    @Test
    public void test_getByCode_withJavaType() {
        String code = "testCode";
        setUpForType(code);
        JavaType type = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ApiConfig.class);
        List<ApiConfig> values = dbConfigService.findByCode(code, type);
        Assert.assertEquals("url", values.get(0).getUrl());
    }
}
