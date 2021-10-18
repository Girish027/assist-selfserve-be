package com.tfsc.ilabs.selfservice.configpuller.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class AuxDataResponseDTO implements Serializable {
    private JsonNode page;
    private JsonNode list;

    public AuxDataResponseDTO() {
    }

    public AuxDataResponseDTO(JsonNode page, JsonNode list) {
        this.page = page;
        this.list = list;
    }
}