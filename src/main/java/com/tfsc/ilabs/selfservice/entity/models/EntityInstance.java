package com.tfsc.ilabs.selfservice.entity.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.workflow.dto.response.EntityInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "activity_instance_id", "entity_template_id", "polling_id"})
})
public class EntityInstance extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String label;

    @ManyToOne
    @JoinColumn(name = "activity_instance_id")
    private WorkflowInstance workflowInstance;

    @ManyToOne
    @JoinColumn(name = "entity_template_id")
    private EntityTemplate entityTemplate;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    @NotNull
    private EntityInstanceStatus status;

    @Column(name = "polling_id")
    private String pollingId;

    public EntityInstance() {
    }

    public EntityInstance(@NotNull String name, @NotNull String label, WorkflowInstance workflowInstance, EntityTemplate entityTemplate,
                          @NotNull EntityInstanceStatus status, String pollingId) {
        this.name = name;
        this.label = label;
        this.workflowInstance = workflowInstance;
        this.entityTemplate = entityTemplate;
        this.status = status;
        this.pollingId = pollingId;
    }


    public boolean isLocked() {
        return !(status == EntityInstanceStatus.DISCARDED || status == EntityInstanceStatus.PROMOTED_TO_LIVE);
    }

    public EntityInstanceDTO toDTO() {
        return new EntityInstanceDTO(this.id, this.name, this.label, this.status, this.getLastUpdatedBy(), this.getLastUpdatedOn());
    }

    public NameLabel toNameLabel() {
        return new NameLabel(this.name, this.label);
    }
}
