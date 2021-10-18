package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.utils.UiSchemaConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class NodeGroup extends AuditableEntity {

    @Id
    @NotNull
    @Column(unique = true)
    private String id;

    @NotNull
    private String title;

    @Column(columnDefinition = "mediumtext")
    @Convert(converter = UiSchemaConverter.class)
    private UiSchema uiSchema;

    public NodeGroup() {
    }

    public NodeGroup(@NotNull String title, UiSchema uiSchema) {
        this.title = title;
        this.uiSchema = uiSchema;
    }
}