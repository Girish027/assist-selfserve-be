package com.tfsc.ilabs.selfservice.workflow.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ravi.b on 10-06-2019.
 */
@Data
@EqualsAndHashCode
public class WorkflowEntity implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private WorkflowEntityType type;

    private String title;

    private List<String> pages;

    public WorkflowEntity() {
        //for jackson only
        type = WorkflowEntityType.HOME_PAGE;
    }

    public WorkflowEntity(@NotNull WorkflowEntityType type, String title) {
        this.type = type;
        this.title = title;
    }
}
