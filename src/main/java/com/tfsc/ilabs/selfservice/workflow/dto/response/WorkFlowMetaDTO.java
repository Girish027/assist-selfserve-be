package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;

import java.util.Set;

public class WorkFlowMetaDTO {

    private ActivityDTO workflow;

    private Set<DefinitionType> pageTypes;

    public ActivityDTO getWorkflow() {
        return workflow;
    }

    public void setWorkflow(ActivityDTO workflow) {
        this.workflow = workflow;
    }

    public Set<DefinitionType> getPageTypes() {
        return pageTypes;
    }

    public void setPageTypes(Set<DefinitionType> pageTypes) {
        this.pageTypes = pageTypes;
    }
}
