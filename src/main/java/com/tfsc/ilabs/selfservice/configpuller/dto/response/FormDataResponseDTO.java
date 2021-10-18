package com.tfsc.ilabs.selfservice.configpuller.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FormDataResponseDTO {
    private int id;
    private Integer workflowInstanceId;
    private String pageTemplateId;
    private JsonNode data;
    private JsonNode list;
}
