package com.tfsc.ilabs.selfservice.configpuller.controllers;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tfsc.ilabs.selfservice.common.config.DbConfig;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class DBConfigController {

    @Autowired
    private DBConfigService dbConfigService;

    @Autowired
    private DbConfig dbConfig;

    @GetMapping
    public List<ConfigDTO> fetchAllConfigs() {
        return dbConfigService.findAllByType(ConfigType.UI_APP);
    }

    @GetMapping(Constants.GET_CODE)
    public ConfigDTO findByCode(@PathVariable String code) {
        return dbConfigService.findByCode(code);
    }

    @PostMapping(Constants.RELOAD_CONFIG)
    public void reloadConfig(){
        dbConfig.evictConfigCacheAndPopulate();
    }
}
