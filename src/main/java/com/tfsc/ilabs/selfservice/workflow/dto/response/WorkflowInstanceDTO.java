package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class WorkflowInstanceDTO implements Serializable {
    private Integer id;

    private Integer nodeId;

    private WorkflowInstanceStatus status;

    private String workflowId;

    private String workflowTitle;

    private String entityType;

    private List<EntityInstanceDTO> entityInstances;

    private DefinitionType type;

    private ApiResponse processedResponse;

    private String lastModifiedBy;

    private Date lastUpdatedOn;

    private Date createdOn;

    private String createdBy;

    public WorkflowInstanceDTO() {
        // for jackson
    }
}
