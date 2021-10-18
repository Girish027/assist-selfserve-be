package com.tfsc.ilabs.selfservice.configpuller.services.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.Config;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.configpuller.repositories.DBConfigRepository;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DBConfigServiceImpl implements DBConfigService {
    private static final Logger logger = LoggerFactory.getLogger(DBConfigServiceImpl.class);

    @Autowired
    private DBConfigRepository dbConfigRepository;

    @Override
    public List<ConfigDTO> findAllByType(ConfigType configType) {
        return dbConfigRepository.findAllByType(configType).stream().map(Config::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName, #code}")
    private Config getByCode(String code) {
        Optional<Config> config = dbConfigRepository.findById(code);
        if (config.isPresent()) {
            return config.get();
        } else {
            throw new NoSuchResourceException(new ErrorObject("Config not found {0}", code));
        }
    }

    public ConfigDTO findByCode(String code) {
        return this.findByCode(code, ConfigDTO.class);
    }

    public <T> T findByCode(String code, JavaType javaType) {
        T value = null;
        try {
            Config config = this.getByCode(code);
            if (Config.isValid(config)) {
                value = new ObjectMapper().readValue(config.getValue(), javaType);
            }
        } catch (NoSuchResourceException e) {
            logger.warn("config: {} not available", code);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return value;
    }

    public <T> T findByCode(String code, Class<T> classType) {
        T value = null;
        try {
            Config config = this.getByCode(code);
            if (Config.isValid(config)) {
                value = new ObjectMapper().readValue(config.getValue(), classType);
            }
        } catch (NoSuchResourceException e) {
            logger.warn("config: {} not available", code);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return value;
    }
}
