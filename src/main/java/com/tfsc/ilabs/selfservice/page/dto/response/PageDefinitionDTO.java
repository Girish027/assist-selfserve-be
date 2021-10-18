package com.tfsc.ilabs.selfservice.page.dto.response;

import java.io.Serializable;

public class PageDefinitionDTO implements Serializable {

    private Object schema;
    private Object uiSchema;
    private Object viewUiSchema;
    private Object fetch;
    private Object validation;

    public PageDefinitionDTO() {
    }

    public PageDefinitionDTO(Object schema, Object uiSchema, Object viewUiSchema, Object fetch, Object validation) {
        this.schema = schema;
        this.uiSchema = uiSchema;
        this.viewUiSchema = viewUiSchema;
        this.fetch = fetch;
        this.validation = validation;
    }

    public Object getSchema() {
        return schema;
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }

    public Object getUiSchema() {
        return uiSchema;
    }

    public void setUiSchema(Object uiSchema) {
        this.uiSchema = uiSchema;
    }

    public Object getViewUiSchema() {
        return viewUiSchema;
    }

    public void setViewUiSchema(Object viewUiSchema) {
        this.viewUiSchema = viewUiSchema;
    }

    public Object getFetch() {
        return fetch;
    }

    public void setFetch(Object fetch) {
        this.fetch = fetch;
    }

    public Object getValidation() {
        return validation;
    }

    public void setValidation(Object validation) {
        this.validation = validation;
    }
}
