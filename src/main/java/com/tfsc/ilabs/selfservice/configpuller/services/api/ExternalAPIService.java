package com.tfsc.ilabs.selfservice.configpuller.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;

import java.util.Properties;

public interface ExternalAPIService {
    /**
     *
     * @param fullURL
     * @param serviceUrls
     * @param fetchConfigTemplate
     * @param contextConfig
     * @param uiResponse
     * @param env
     * @param properties
     * @param auxiliaryResponse
     * @param searchConfig - used for Api Caching key and condition.<br>
     *                     <blockquote>{@code pageSize} and {@code pageNumber} helps caching page results<br>
     *                     {@code hasFilter} calculated based on the {@link com.tfsc.ilabs.selfservice.common.utils.Constants SEARCH_VALUE} field</blockquote>
     * @return
     * @throws SelfServeException
     */
    String externalServiceGetApiCall(String fullURL, ServiceUrls serviceUrls, FetchConfigTemplate fetchConfigTemplate,
                                     JsonNode contextConfig, JsonNode uiResponse, Environment env, Properties properties,
                                     String auxiliaryResponse, SearchConfig searchConfig) throws SelfServeException;

    byte[] externalServiceGetApiCallAsByteArray(String fullURL, ServiceUrls serviceUrls, FetchConfigTemplate fetchConfigTemplate,
                                     JsonNode contextConfig, JsonNode uiResponse, Environment env) throws SelfServeException;

}
