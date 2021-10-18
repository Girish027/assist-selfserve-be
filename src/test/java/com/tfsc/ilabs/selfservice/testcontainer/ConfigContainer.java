package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.dto.ApiConfig;
import io.swagger.models.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigContainer {
    public static List<ApiConfig> getCacheConfig(String code) throws IOException {
        List<ApiConfig> cacheConfig = new ArrayList<>();
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setUrl("testUrl");
        apiConfig.setMethod(HttpMethod.POST);
        apiConfig.setHeaders(new HashMap<String, String>());
        apiConfig.setBody(new ObjectMapper().createObjectNode());
        cacheConfig.add(apiConfig);
        return cacheConfig;
    }
}
