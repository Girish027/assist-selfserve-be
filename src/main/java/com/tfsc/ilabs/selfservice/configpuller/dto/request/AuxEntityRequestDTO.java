package com.tfsc.ilabs.selfservice.configpuller.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AuxEntityRequestDTO {
    private Integer entityTemplateId;
    private String type;
    private JsonNode fetchParams;
}
