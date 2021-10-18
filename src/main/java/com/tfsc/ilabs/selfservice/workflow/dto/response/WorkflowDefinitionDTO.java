package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.page.dto.response.PageTemplateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by ravi.b on 07-06-2019.
 */
@Data
@EqualsAndHashCode
public class WorkflowDefinitionDTO implements Serializable {
    private List<String> workflowSchema;

    private Map<String, PageTemplateDTO> pages;

    private Map<String, WorkflowEntity> schemaEntities;

    public WorkflowDefinitionDTO() {
        //for jackson
    }

    public WorkflowDefinitionDTO(Map<String, PageTemplateDTO> pages) {
        this.pages = pages;
    }
}
