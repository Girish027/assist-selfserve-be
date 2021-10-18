package com.tfsc.ilabs.selfservice.configpuller.services.api;

import com.fasterxml.jackson.databind.JavaType;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;

import java.util.List;

public interface DBConfigService {

    List<ConfigDTO> findAllByType(ConfigType configType);
    ConfigDTO findByCode(String code);
    <T> T findByCode(String code, JavaType javaType);
    <T> T findByCode(String code, Class<T> classType);
}
