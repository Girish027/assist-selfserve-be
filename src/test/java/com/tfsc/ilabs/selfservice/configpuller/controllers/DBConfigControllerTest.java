package com.tfsc.ilabs.selfservice.configpuller.controllers;

import com.tfsc.ilabs.selfservice.common.config.DbConfig;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DBConfigControllerTest {

    @InjectMocks
    DBConfigController dbConfigController = new DBConfigController();
    @Mock
    DbConfig dbConfig;
    @Mock
    DBConfigService dbConfigService;
    @Mock
    List<ConfigDTO> configDTOList = new ArrayList<>();
    @Mock
    ConfigDTO configDTO;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dbConfigController).build();

        Mockito.when(dbConfigService.findAllByType(ConfigType.UI_APP)).thenReturn(configDTOList);
        Mockito.when(dbConfigService.findByCode("test-code")).thenReturn(configDTO);
    }

    @Test
    public void test_fetchAllConfigs() {
        List<ConfigDTO> result = dbConfigController.fetchAllConfigs();
        Assert.assertEquals(configDTOList, result);
    }

    @Test
    public void test_findByCode() {
        ConfigDTO result = dbConfigController.findByCode("test-code");
        Assert.assertEquals(configDTO, result);
    }

    @Test
    public void test_restConfig() throws Exception {
        mockMvc.perform(
                post(Constants.BASE_URI
                        +"/configurations" + Constants.RELOAD_CONFIG))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(dbConfig, Mockito.times(1))
                .evictConfigCacheAndPopulate();
    }
}