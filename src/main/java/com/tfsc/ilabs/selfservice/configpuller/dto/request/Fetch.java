package com.tfsc.ilabs.selfservice.configpuller.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Fetch {
    private JsonNode page;
    private JsonNode list;

    public Fetch() {
    }

    public Fetch(JsonNode page, JsonNode list) {
        this.page = page;
        this.list = list;
    }
}