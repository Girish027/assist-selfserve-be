package com.tfsc.ilabs.selfservice.configpuller.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ParamConfigTemplate {
    private ArrayNode contexts;
    private JsonNode constants;
    private  JsonNode queryParams;
}