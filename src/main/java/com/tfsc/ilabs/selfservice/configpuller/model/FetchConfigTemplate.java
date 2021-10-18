package com.tfsc.ilabs.selfservice.configpuller.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.Service;
import com.tfsc.ilabs.selfservice.common.utils.ConfigConverter;
import com.tfsc.ilabs.selfservice.common.utils.ParamConfigConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ravi.b on 03-06-2019.
 */
@Data
@EqualsAndHashCode
@Entity
public class FetchConfigTemplate extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private FetchType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private TranslatorType translatorType;

    @NotNull
    private String fetchFor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @NotNull
    private String relativeURL;

    @Column(columnDefinition = "json")
    @Convert(converter = ConfigConverter.class)
    private JsonNode headers;

    // This will contain information regarding request params of the service url
    // The params can be constant or pre-determined else they can be dynamic or
    // context driven
    @Column(columnDefinition = "json")
    @Convert(converter = ParamConfigConverter.class)
    private ParamConfigTemplate params;

    @Column(columnDefinition = "json")
    @Convert(converter = ConfigConverter.class)
    private JsonNode respApiToRespUi;

    @Column(name = "executionOrder")
    private Integer executionOrder;

    public FetchConfigTemplate() {
        // for jackson
    }
}
