package com.tfsc.ilabs.selfservice.page.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.page.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by ravi.b on 21-08-2019.
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PageDataResponseDTO extends PageDTO {
    private int id;

    private Integer workflowInstanceId;

    private String pageTemplateId;

    public PageDataResponseDTO(int id, Integer workflowInstanceId, String pageTemplateId, JsonNode data) {
        super(data);
        this.id = id;
        this.workflowInstanceId = workflowInstanceId;
        this.pageTemplateId = pageTemplateId;
    }
}
