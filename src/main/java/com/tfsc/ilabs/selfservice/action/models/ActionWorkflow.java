package com.tfsc.ilabs.selfservice.action.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by ravi.b on 03-06-2019.
 */
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"action_id", "activity_template_id"})
})
public class ActionWorkflow extends AuditableEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action  action;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "activity_template_id")
    private WorkflowTemplate workflowTemplate;

    public Integer getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public WorkflowTemplate getWorkflowTemplate() {
        return workflowTemplate;
    }

    public void setWorkflowTemplate(WorkflowTemplate workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionWorkflow that = (ActionWorkflow) o;
        return Objects.equals(action, that.action) &&
                Objects.equals(workflowTemplate, that.workflowTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, workflowTemplate);
    }

    @Override
    public String toString() {
        return "ActionWorkflow{" +
                "id=" + id +
                ", action=" + action +
                ", workflowTemplate=" + workflowTemplate +
                '}';
    }
}

