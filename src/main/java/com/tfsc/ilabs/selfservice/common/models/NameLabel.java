package com.tfsc.ilabs.selfservice.common.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ravi.b on 25-09-2019.
 */
@Data
@EqualsAndHashCode
public class NameLabel {
    private String name;

    private String label;

    public NameLabel() {
        //for jackson
    }

    public NameLabel(String name, String label) {
        this.name = name;
        this.label = label;
    }


    public JsonNode toJsonNode() throws IOException {
        String jsonString = "{\"name\":\""+name+"\",\"label\":\""+label+"\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return  jsonNode;

    }
}
