package com.tfsc.ilabs.selfservice.page.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.utils.StringUtils;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDefinitionDTO;
import lombok.Data;

@Data
public class PageDefinition {
    private JsonNode schema;
    private JsonNode uiSchema;
    private JsonNode viewUiSchema;
    private JsonNode fetch;
    private JsonNode validation;

    public PageDefinition() {
    }

    public PageDefinition(JsonNode schema, JsonNode uiSchema, JsonNode viewUiSchema, JsonNode fetch, JsonNode validation) {
        this.schema = schema;
        this.uiSchema = uiSchema;
        this.viewUiSchema = viewUiSchema;
        this.fetch = fetch;
        this.validation = validation;
    }

    public PageDefinitionDTO toDTO() {
        PageDefinitionDTO dto = new PageDefinitionDTO();
        dto.setSchema(StringUtils.valueAsString(this.schema));
        dto.setUiSchema(StringUtils.valueAsString(this.uiSchema));
        dto.setViewUiSchema(StringUtils.valueAsString(this.viewUiSchema));
        dto.setFetch(StringUtils.valueAsString(this.fetch));
        dto.setValidation(StringUtils.valueAsString(this.validation));
        return dto;
    }
}