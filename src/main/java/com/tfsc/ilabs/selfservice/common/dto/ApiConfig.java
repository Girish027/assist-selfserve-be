package com.tfsc.ilabs.selfservice.common.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.HttpMethod;
import lombok.Data;

import java.util.Map;

@Data
public class ApiConfig {
    String url;
    HttpMethod method;
    Map<String, String> headers;
    JsonNode body;
}
