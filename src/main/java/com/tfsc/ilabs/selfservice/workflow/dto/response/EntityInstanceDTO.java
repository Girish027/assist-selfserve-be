package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class EntityInstanceDTO implements Serializable {
    private Integer id;

    private String name;

    private String label;

    private EntityInstanceStatus status;

    private String lastModifiedBy;

    private Date lastUpdatedOn;

    public EntityInstanceDTO(Integer id, String name, String label, EntityInstanceStatus status) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.status = status;
    }

    public EntityInstanceDTO(Integer id, String name, String label, EntityInstanceStatus status, String lastModifiedBy, Date lastUpdatedOn) {
        this(id, name, label, status);
        this.lastModifiedBy = lastModifiedBy;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
