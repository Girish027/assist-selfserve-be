package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import lombok.Data;

@Data
public class EntityDataResponseDTO {

    private Integer workflowInstanceId;
    private DefinitionType templateType;

    public EntityDataResponseDTO() {
        // for jackson
    }

    public EntityDataResponseDTO(Integer workflowInstanceId, DefinitionType templateType) {
        this.workflowInstanceId = workflowInstanceId;
        this.templateType = templateType;
    }
}
