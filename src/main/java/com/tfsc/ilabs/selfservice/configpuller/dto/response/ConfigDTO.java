package com.tfsc.ilabs.selfservice.configpuller.dto.response;

import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ConfigDTO implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ConfigDTO.class);

    private String code;
    private Object value;
    private ConfigType type;
    private Boolean status;

    public ConfigDTO() {
    }

    public ConfigDTO(String code, Object value, ConfigType type, Boolean status) {
        this.code = code;
        this.value = value;
        this.type = type;
        this.status = status;
    }

    public static boolean isValid(ConfigDTO config) {
        if (config == null) {
            logger.warn("config is missing in db");
            return false;
        }

        if (config.getValue() == null) {
            logger.warn("config is empty");
            return false;
        }

        if (!config.getStatus()) {
            logger.warn("{} config has status disabled", config.getCode());
            return false;
        }
        return true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ConfigType getType() {
        return type;
    }

    public void setType(ConfigType type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
