package com.tfsc.ilabs.selfservice.action.models;

import com.tfsc.ilabs.selfservice.action.config.ActionExecutionMetaConverter;
import com.tfsc.ilabs.selfservice.action.config.RequestDefinitionConverter;
import com.tfsc.ilabs.selfservice.action.config.ResponseDefinitionConverter;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by ravi.b on 03-06-2019.
 */
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"action_id", "activity_instance_id", "env", "entity_instance_id"})
})
public class ActionExecutionMonitor extends AuditableEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "activity_instance_id")
    private WorkflowInstance workflowInstance;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "entity_instance_id")
    private EntityInstance entityInstance;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private Environment env;

    @Column(columnDefinition = "json")
    @Convert(converter = RequestDefinitionConverter.class)
    private RequestDefinition request;

    @Column(columnDefinition = "json")
    @Convert(converter = ResponseDefinitionConverter.class)
    private ResponseDefinition response;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private ExecutionStatus status;

    @Column(columnDefinition = "json")
    @Convert(converter = ActionExecutionMetaConverter.class)
    private ActionExecutionMeta executionMeta;

    public ActionExecutionMonitor() {
    }

    public ActionExecutionMonitor(@NotNull Action action, @NotNull WorkflowInstance workflowInstance, @NotNull EntityInstance entityInstance, @NotNull Environment env) {
        this.action = action;
        this.workflowInstance = workflowInstance;
        this.entityInstance = entityInstance;
        this.env = env;
        this.status = ExecutionStatus.NEW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionExecutionMonitor that = (ActionExecutionMonitor) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(action.getId(), that.action.getId()) &&
                Objects.equals(workflowInstance.getId(), that.workflowInstance.getId()) &&
                //taking only entityInstanceId for comparison as name updates with entityId after execution
                Objects.equals(entityInstance.getId(), that.entityInstance.getId()) &&
                env == that.env;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action.getId(), workflowInstance.getId(), entityInstance.getId(), env);
    }

    @Override
    public String toString() {
        return "ActionExecutionMonitor{" +
                "id=" + id +
                ", action=" + action +
                ", workflowInstance=" + workflowInstance +
                ", entityInstance=" + entityInstance +
                ", env=" + env +
                ", request=" + request +
                ", response=" + response +
                ", status=" + status +
                '}';
    }
}