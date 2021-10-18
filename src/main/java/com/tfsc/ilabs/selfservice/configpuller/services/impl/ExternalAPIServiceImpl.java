package com.tfsc.ilabs.selfservice.configpuller.services.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.tfsc.ilabs.selfservice.common.dto.ExcludedFetchConfig;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthConfigService;
import com.tfsc.ilabs.selfservice.common.services.impl.ExternalServiceAuthFactory;
import com.tfsc.ilabs.selfservice.common.utils.*;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.ParamConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;
import com.tfsc.ilabs.selfservice.configpuller.model.TranslatorType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.configpuller.services.api.ExternalAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

@Service
public class ExternalAPIServiceImpl implements ExternalAPIService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalAPIServiceImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExternalServiceAuthFactory externalServiceAuthFactory;

    @Autowired
    private ExternalAuthConfigService externalAuthConfigService;

    @Autowired
    private DBConfigService dbConfigService;

    @Override
    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName, #fullURL, #fetchConfigTemplate.type, #fetchConfigTemplate.fetchFor, #searchConfig.pageSize, #searchConfig.pageNumber}",
            condition = "!@dbConfig.getExcludeListForCaching().contains(#fetchConfigTemplate.fetchFor) && !#searchConfig.hasFilter")
    public String externalServiceGetApiCall(String fullURL, ServiceUrls serviceUrls, FetchConfigTemplate fetchConfigTemplate,
                                            JsonNode contextConfig, JsonNode uiResponse, Environment env, Properties properties,
                                            String auxiliaryResponse, SearchConfig searchConfig) {
        String apiResponseString = "";

        JsonNode headers = buildHeader(serviceUrls.getHeaders(), fetchConfigTemplate.getHeaders(),
                fetchConfigTemplate.getService().getId(), env);
        ParamConfigTemplate paramConfig = fetchConfigTemplate.getParams();
        TranslatorType translatorType = fetchConfigTemplate.getTranslatorType();
        JsonNode respApiToRespUi = fetchConfigTemplate.getRespApiToRespUi();

        JsonNode queryParams = getQueryParams(properties, paramConfig);

        if (!fullURL.contains(Constants.PLACE_HOLDER_PREFIX)) {
            long startMillis = System.currentTimeMillis();
            HttpResponse<String> apiResponse = HttpUtils.makeGetCall(fullURL, headers, queryParams);
            int statusCode = apiResponse.statusCode();

            boolean isErrorAcceptableInFetchedConfig = isFetchErrorAllowed(fetchConfigTemplate, statusCode);
            if (statusCode != 200 && !isErrorAcceptableInFetchedConfig) {
                throw new SelfServeException("Got exception while making get call:" + fullURL);
            } else {
                apiResponseString = apiResponse.body();
            }
            logger.debug("URL:{} - API Response:{} ", fullURL, apiResponseString);
            long totalTime = startMillis - System.currentTimeMillis();
            logger.info("Response received for :{} - Total Time Taken in Millis:{}", fullURL, totalTime);
        }
        if (respApiToRespUi != null && !respApiToRespUi.isNull()) {
            try {
                switch (translatorType) {
                    case JSON:
                        getJsonUiResponse(uiResponse, apiResponseString, respApiToRespUi);
                        break;
                    case JS:
                        uiResponse = getJsUiResponse(contextConfig, apiResponseString, respApiToRespUi, auxiliaryResponse);
                        break;
                    default:
                }
            } catch (Exception e) {
                logger.error("Standard exception received: Response Data: {}", apiResponseString, e);
                throw new SelfServeException("Standard exception received: {0} {1}", e.getMessage(), e);
            }
        }
        logger.debug("For URL {} - Parsed API Response {}", fullURL, uiResponse);
        return StringUtils.valueAsString(uiResponse);
    }

    private JsonNode getQueryParams(Properties properties, ParamConfigTemplate paramConfig) {
        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(Constants.PLACE_HOLDER_PREFIX,
                Constants.PLACE_HOLDER_SUFFIX);
        JsonNode queryParams = paramConfig.getQueryParams();
        if (queryParams != null) {
            String queryParamsString = placeholderHelper.replacePlaceholders(paramConfig.getQueryParams().toString(),
                    properties);
            if (queryParamsString.contains(Constants.PLACE_HOLDER_PREFIX)) {
                logger.warn("expected placeholders are missing, so making null");
                queryParamsString = null;
            }
            try {
                queryParams = objectMapper.readTree(queryParamsString);
            } catch (Exception e) {
                logger.error("Error parsing query params");
            }
        }
        return queryParams;
    }

    public byte[] externalServiceGetApiCallAsByteArray(String fullURL, ServiceUrls serviceUrls, FetchConfigTemplate fetchConfigTemplate,
                                                       JsonNode contextConfig, JsonNode uiResponse, Environment env) {
        byte[] apiResponseString = null;

        JsonNode headers = buildHeader(serviceUrls.getHeaders(), fetchConfigTemplate.getHeaders(),
                fetchConfigTemplate.getService().getId(), env);

        if (!fullURL.contains(Constants.PLACE_HOLDER_PREFIX)) {
            long startMillis = System.currentTimeMillis();
            HttpResponse<byte[]> apiResponse = HttpUtils.makeGetCallOfArray(fullURL, headers);
            int statusCode = apiResponse.statusCode();

            boolean isErrorAcceptableInFetchedConfig = isFetchErrorAllowed(fetchConfigTemplate, statusCode);
            if (statusCode != 200 && !isErrorAcceptableInFetchedConfig) {
                throw new SelfServeException("Got exception while making get call:" + fullURL);
            } else {
                apiResponseString = apiResponse.body();
            }
            logger.debug("URL:{} - API Response:{} ", fullURL, apiResponseString);
            long totalTime = startMillis - System.currentTimeMillis();
            logger.info("Response received for :{} - Total Time Taken in Millis:{}", fullURL, totalTime);
        }
        return apiResponseString;
    }

    private void getJsonUiResponse(JsonNode uiResponse, String apiResponseString, JsonNode respApiToRespUi) throws IOException {
        JsonNode apiResponseJson = objectMapper.readTree(apiResponseString);
        String stringifiedApiResponseJson = apiResponseJson.asText();
        logger.debug("Json Response: {}", stringifiedApiResponseJson);
        ResponseUtils.evaluateContract(respApiToRespUi, apiResponseJson, uiResponse);
    }

    private JsonNode getJsUiResponse(JsonNode contextConfig, String apiResponseString,
                                     JsonNode respApiToRespUi, String auxiliaryResponse) throws IOException {
        // We expect respApiToRespUi to be a json object with 'objectTranslator' key of
        // type string with a function stringified
        String configEntity = contextConfig.has(Constants.ENTITY) ? contextConfig.get(Constants.ENTITY).asText() : null;
        String objectTranslator = respApiToRespUi.get("objectTranslator").asText();
        String translated = ScriptUtil.invokeFunction(objectTranslator,
                Constants.OBJECT_TRANSLATOR_METHOD, apiResponseString, configEntity, contextConfig, auxiliaryResponse);
        return objectMapper.readTree(translated);
    }

    private JsonNode buildHeader(JsonNode header, JsonNode configHeader, String serviceId, Environment env) {
        Iterator<String> fieldNames = configHeader.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = header.get(fieldName);
            JsonNode updatedNode = configHeader.get(fieldName);
            if (Constants.AUTH_TOKEN_TYPE.equals(fieldName)) {
                String authTokenHeaderKey = externalAuthConfigService.getValue(Constants.DB_CONFIG_TOKEN_HEADER_KEY, serviceId);
                ((ObjectNode) header).set(authTokenHeaderKey,
                        TextNode.valueOf(updatedNode.asText() + externalServiceAuthFactory.getBuilder(serviceId).generateSignedToken(env)));
            } else {
                if (fieldNode == null || fieldNode.isNull()) {
                    ((ObjectNode) header).set(fieldName, updatedNode);
                }
            }
        }
        return header;
    }

    private boolean isFetchErrorAllowed(FetchConfigTemplate fetchConfigTemplate, int statusCode) {
        boolean isFetchConfigErrorAllowed = false;

        JavaType type = new ObjectMapper().getTypeFactory().constructMapType(Map.class, String.class, ExcludedFetchConfig.class);
        Map<String, ExcludedFetchConfig> excludedFetchConfigs = dbConfigService.findByCode(Constants.EXCLUDED_FETCHFOR_LIST, type);

        if (excludedFetchConfigs != null) {
            ExcludedFetchConfig excludedFetchConfig = excludedFetchConfigs.get(fetchConfigTemplate.getFetchFor());
            if (excludedFetchConfig != null && (excludedFetchConfig.getStatus().contains(statusCode) || excludedFetchConfig.getStatus().size() == 0)) {
                isFetchConfigErrorAllowed = true;
            }
        }

        return isFetchConfigErrorAllowed;
    }
}
