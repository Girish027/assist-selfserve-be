package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.action.config.ConfigValuesConverter;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "clients_config", uniqueConstraints = { @UniqueConstraint(columnNames =
        { "scope_id", "scope_type", "config_name" }) })
@Getter
@Setter
@NoArgsConstructor
public class ClientsConfig extends AuditableEntity {
    private static final Logger logger = LoggerFactory.getLogger(ClientsConfig.class);

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type")
    private ScopeType scopeType;

    @NotNull
    @Column(name = "scope_id")
    private String scopeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_name")
    private ConfigNames configName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "disable")
    private String disable;

    @Column(columnDefinition = "mediumtext")
    @Convert (converter = ConfigValuesConverter.class)
    private List<String> values;

    public ClientsConfig(Integer id, @NotNull ScopeType scopeType, @NotNull String scopeId, ConfigNames configName, String description, @NotNull String disable, List<String> values) {
        this.id = id;
        this.scopeType = scopeType;
        this.scopeId = scopeId;
        this.configName = configName;
        this.description = description;
        this.disable = disable;
        this.values = values;
    }
}