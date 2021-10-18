package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ravi.b on 31-05-2019.
 */

@Data
@EqualsAndHashCode
@Entity
@Table(name = "activity_page", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"page_template_id", "activity_template_id"})
})
public class WorkflowPage extends AuditableEntity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "activity_template_id")
    private WorkflowTemplate workflowTemplate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "page_template_id")
    private PageTemplate pageTemplate;

    @NotNull
    private int pageOrder;

    private String sectionName;
}
