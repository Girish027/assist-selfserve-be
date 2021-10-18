package com.tfsc.ilabs.selfservice.common.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by ravi.b on 03-06-2019.
 */
@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Service extends AuditableEntity {

    @Id
    @NotNull
    @Column(unique = true)
    private String id;

    @NotNull
    private String name;

    @Column(length = 1024)
    private String description;

    public Service() {
    }

    public Service(@NotNull String id, @NotNull String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
