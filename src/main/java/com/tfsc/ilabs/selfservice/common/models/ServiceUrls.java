package com.tfsc.ilabs.selfservice.common.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.utils.ConfigConverter;
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
public class ServiceUrls extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private Environment env;

    @Column(columnDefinition = "headers")
    @Convert(converter = ConfigConverter.class)
    private JsonNode headers;

    @NotNull
    private String baseURL;

    public ServiceUrls() {

    }

    public ServiceUrls(@NotNull Service service, @NotNull Environment env, @NotNull String baseURL, @NotNull JsonNode headers) {
        this.service = service;
        this.env = env;
        this.headers = headers;
        this.baseURL = baseURL;
    }
}
