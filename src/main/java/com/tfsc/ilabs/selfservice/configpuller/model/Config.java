package com.tfsc.ilabs.selfservice.configpuller.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Config extends AuditableEntity {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @NotNull
    @Column(unique = true)
    private String code;
    @NotNull
    @Column(name = "value", columnDefinition = "LONGTEXT")
    private String value;
    @NotNull
    @Column(columnDefinition = "boolean default true")
    private boolean status;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ConfigType type;

    private String classType;

    public Config() {
    }

    public Config(@NotNull String code, @NotNull String value, @NotNull boolean status, @NotNull ConfigType type) {
        this.code = code;
        this.value = value;
        this.status = status;
        this.type = type;
    }

    public ConfigDTO toDTO() {
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setCode(this.code);
        try {
            Object updatedValue = (this.classType == null)  ? this.value :
             mapper.readValue(this.value, Class.forName(this.classType));
            configDTO.setValue(updatedValue);
        } catch (Exception e) {
            logger.warn("Config :: toDTO :: Error while converting value");
        }
        configDTO.setStatus(this.status);
        configDTO.setType(this.type);
        return configDTO;
    }

    public static boolean isValid(Config config) {
        if (config == null) {
            logger.warn("Config :: isValid:: config is missing in db");
            return false;
        }

        if (config.getValue() == null) {
            logger.warn("Config :: isValid:: config is empty");
            return false;
        }

        if (!config.isStatus()) {
            logger.warn("Config :: isValid:: {} config has status disabled", config.getCode());
            return false;
        }
        return true;
    }
}
