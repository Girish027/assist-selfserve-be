package com.tfsc.ilabs.selfservice.common.models;

import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
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
public class Audit extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @NotNull
    private String userId;


    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private AuditEventType eventType;


    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private ExecutionStatus status;

    @NotNull
    @Column(columnDefinition = "json")
    private String eventData;

    @NotNull
    private String hostName;

    public Audit(@NotNull String userId, @NotNull AuditEventType eventType, @NotNull ExecutionStatus status, @NotNull String eventData, @NotNull String hostName) {
        this.userId = userId;
        this.eventType = eventType;
        this.status = status;
        this.eventData = eventData;
        this.hostName = hostName;
    }
}
