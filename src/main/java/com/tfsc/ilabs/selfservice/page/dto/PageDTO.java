package com.tfsc.ilabs.selfservice.page.dto;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by ravi.b on 25-09-2019.
 */
public abstract class PageDTO {
    private JsonNode data;

    protected PageDTO() {
    }

    public PageDTO(JsonNode data) {
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
