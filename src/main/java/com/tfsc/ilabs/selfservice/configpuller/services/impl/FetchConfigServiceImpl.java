package com.tfsc.ilabs.selfservice.configpuller.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.*;
import com.tfsc.ilabs.selfservice.common.repositories.ServiceUrlRepository;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.StringUtils;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxDataRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.Fetch;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.AuxDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.model.ParamConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;
import com.tfsc.ilabs.selfservice.configpuller.repositories.FetchConfigTemplateRepository;
import com.tfsc.ilabs.selfservice.configpuller.services.api.ExternalAPIService;
import com.tfsc.ilabs.selfservice.configpuller.services.api.FetchConfigService;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowPage;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowPageRepository;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@org.springframework.stereotype.Service
public class FetchConfigServiceImpl implements FetchConfigService {

    private static final Logger logger = LoggerFactory.getLogger(FetchConfigService.class);

    @Autowired
    private FetchConfigTemplateRepository fetchConfigTemplateRepository;

    @Autowired
    private ServiceUrlRepository serviceUrlRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExternalAPIService externalAPIService;

    @Autowired
    private WorkflowPageRepository workflowPageRepository;

    @Autowired
    private WorkflowTemplateRepository workflowTemplateRepository;

    @Autowired
    private SessionValidator sessionValidator;

    /**
     * Structure of fetch: { page: 'p1', list: { testList:
     * 'path.in.page.to.testList' } }
     */
    @Override
    public AuxDataResponseDTO getAuxData(AuxDataRequestDTO auxDataRequestDTO, String clientId, String accountId, Environment env,
                                         Boolean isWorkflowInstancePresent) {
        Fetch fetchConfig = auxDataRequestDTO.getFetch();
        List<NameLabel> entities = auxDataRequestDTO.getEntities();
        JsonNode contextConfig = BaseUtil.getContext(clientId, accountId, entities);

        AuxDataResponseDTO auxData = new AuxDataResponseDTO();
        if (!isWorkflowInstancePresent && fetchConfig.getPage() != null) {
            JsonNode value = getEntityData(fetchConfig, contextConfig, env);
            auxData.setPage(value);
        }
        if (fetchConfig.getList() != null) {
            JsonNode listValues = getListData(fetchConfig, contextConfig, env);
            auxData.setList(listValues);
        }
        return auxData;
    }

    public byte[] getDataFromServiceAsString(String fetchFor, JsonNode contextConfig, FetchType fetchType, Environment env) {
        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(Constants.PLACE_HOLDER_PREFIX,
                Constants.PLACE_HOLDER_SUFFIX);

        // Get data from db corresponding to the fetchFor identifier
        Optional<FetchConfigTemplate> optionalFetchConfigTemplate = fetchConfigTemplateRepository.findOne(getQueryInputByCondition(fetchFor, fetchType));
        byte[] uiResponse = null;

        if (optionalFetchConfigTemplate.isPresent()) {
            FetchConfigTemplate fetchConfigTemplate = optionalFetchConfigTemplate.get();
            ParamConfigTemplate params = fetchConfigTemplate.getParams();

            // Create properties from context
            Properties properties = new Properties();
            ArrayNode contexts = params.getContexts();
            Service service = fetchConfigTemplate.getService();

            // Create properties from constantParams
            JsonNode constants = params.getConstants();
            Iterator<String> keys = constants.fieldNames();
            while (keys.hasNext()) {
                String currKey = keys.next();
                String currValue = constants.get(currKey).asText();
                properties.put(currKey, UriUtils.encode(currValue, StandardCharsets.UTF_8));
            }
            buildProperties(service.getId(), contextConfig, contexts, properties);

            // Get relative URL
            String relativeURL = fetchConfigTemplate.getRelativeURL();

            // Get service to call from
            ServiceUrls serviceUrls = serviceUrlRepository.findByServiceIdAndEnv(service.getId(), env);
            if (serviceUrls != null) {
                String baseURL = serviceUrls.getBaseURL();
                String fullURL = placeholderHelper.replacePlaceholders(baseURL + relativeURL, properties);

                // Make service call
                uiResponse = externalAPIService.externalServiceGetApiCallAsByteArray(
                        fullURL, serviceUrls, fetchConfigTemplate, contextConfig, objectMapper.createObjectNode(), env);
            } else {
                logger.error("Service URL for environment {} not registered", env);
            }
        }

        return uiResponse;
    }

    @Override
    public JsonNode getDataFromService(String fetchFor, JsonNode contextConfig, FetchType fetchType, Environment env) {
        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(Constants.PLACE_HOLDER_PREFIX,
                Constants.PLACE_HOLDER_SUFFIX);

        // Get data from db corresponding to the fetchFor identifier
        List<FetchConfigTemplate> fetchConfigTemplates = fetchConfigTemplateRepository.findByFetchForAndTypeInOrderByExecutionOrderAsc(fetchFor, List.of(fetchType, FetchType.AUXILIARY_RESPONSE));
        JsonNode uiResponse = null;
        String auxiliaryResponse = "";
        // Iterate over each contract with same fetchFor (id)
        for (FetchConfigTemplate fetchConfigTemplate : fetchConfigTemplates) {
            ParamConfigTemplate params = fetchConfigTemplate.getParams();
            Service service = fetchConfigTemplate.getService();

            // Create properties from context
            Properties properties = new Properties();
            ArrayNode contexts = params.getContexts();

            // Create properties from constantParams
            JsonNode constants = params.getConstants();
            Iterator<String> keys = constants.fieldNames();
            while (keys.hasNext()) {
                String currKey = keys.next();
                String currValue = constants.get(currKey).asText();
                properties.put(currKey, UriUtils.encode(currValue, StandardCharsets.UTF_8));
            }
            buildProperties(service.getId(), contextConfig, contexts, properties);

            // Get relative URL
            String relativeURL = fetchConfigTemplate.getRelativeURL();
            // Get service to call from

            ServiceUrls serviceUrls = serviceUrlRepository.findByServiceIdAndEnv(service.getId(), env);
            if (serviceUrls != null) {
                String baseURL = serviceUrls.getBaseURL();
                String fullURL = placeholderHelper.replacePlaceholders(baseURL + relativeURL, properties);
                SearchConfig searchConfig = getSearchConfigParameters(contextConfig);
                // Make service call
                String externalServiceApiResponse = externalAPIService.externalServiceGetApiCall(
                        fullURL, serviceUrls, fetchConfigTemplate, contextConfig, uiResponse, env, properties, auxiliaryResponse, searchConfig);
                JsonNode tempApiResponse = StringUtils.valueAsJsonNode(externalServiceApiResponse);
                logger.info("For URL : {} - Response Size : {}", fullURL, tempApiResponse.size());
                // Get FETCH CONFIG TYPE and if AUXILARY_DATA then set contextConfig or create a new variable and send to next call.
                if(!fetchConfigTemplate.getType().name().equals(FetchType.AUXILIARY_RESPONSE.name())) {
                    uiResponse = BaseUtil.mergeResponse(uiResponse, tempApiResponse);
                } else {
                    auxiliaryResponse = tempApiResponse.toString();
                }
            } else {
                logger.error("Service URL for environment {} not registered", env);
            }
        }
        return uiResponse;
    }

    private SearchConfig getSearchConfigParameters(JsonNode contextConfig) {
        SearchConfig searchConfig = new SearchConfig();
        searchConfig.setHasFilter(contextConfig.get(Constants.SEARCH_VALUE) != null);
        searchConfig.setPageNumber(contextConfig.get(Constants.PAGE_NUMBER) != null?contextConfig.get(Constants.PAGE_NUMBER).asInt():0);
        searchConfig.setPageSize(contextConfig.get(Constants.PAGE_SIZE) != null?contextConfig.get(Constants.PAGE_SIZE).asInt():0);
        return searchConfig;
    }

    @Override
    public String getAuxEntityData(String fetchFor, String type, JsonNode contextConfig, Environment env) {
        return String.valueOf(getDataFromService(fetchFor, contextConfig, BaseUtil.getFetchType(type), env));
    }

    private Example<FetchConfigTemplate> getQueryInputByCondition(String fetchFor, FetchType fetchType) {
        FetchConfigTemplate inputTemplate = new FetchConfigTemplate();
        inputTemplate.setFetchFor(fetchFor);
        inputTemplate.setType(fetchType);
        return Example.of(inputTemplate);
    }

    private JsonNode getListData(Fetch fetchConfig, JsonNode contextConfig, Environment env) {
        // For each item in the 'list' get list values
        JsonNode list = fetchConfig.getList();
        Iterator<String> listKeys = list.fieldNames();
        ObjectNode listValues = objectMapper.createObjectNode();
        while (listKeys.hasNext()) {
            String fieldId = listKeys.next();
            listValues.set(fieldId, getDataFromService(fieldId, contextConfig, FetchType.FIELD_OPTIONS, env));
        }
        return listValues;
    }

    private JsonNode getEntityData(Fetch fetchConfig, JsonNode contextConfig, Environment env) {
        String pageId = fetchConfig.getPage().asText();
        ObjectNode value = objectMapper.createObjectNode();
        value.set(pageId, getDataFromService(pageId, contextConfig, FetchType.PAGE_DATA_VALUE, env));
        return value;
    }

    @Override
    public PublishType getPublishTypeFromPageTemplateId(String pageTemplateId) {
        Optional<List<WorkflowPage>> workflowPage = workflowPageRepository.findByPageTemplateId(pageTemplateId);
        WorkflowConfig config = null;
        if (workflowPage.isEmpty()) {
            throw new NoSuchResourceException(new ErrorObject("Workflow not found for pagetemplate", workflowPage));
        }
        if (workflowPage.isPresent()) {
            Optional<WorkflowTemplate> workflowTemplate =  workflowTemplateRepository.findById(workflowPage.get().get(0).getWorkflowTemplate().getId());
            if(workflowTemplate.isPresent()) {
                config = workflowTemplate.get().getConfigs();
            }
        }
        return config!=null ? config.getPublishType() : null;
    }

    private void buildProperties(String serviceId, JsonNode contextConfig, ArrayNode contexts, Properties properties) {
        String clientId = contextConfig.get("clientId").asText();
        String accountId = contextConfig.get("accountId").asText();
        String searchValue = contextConfig.get(Constants.SEARCH_VALUE) == null ? null : contextConfig.get(Constants.SEARCH_VALUE).asText();
        ComponentInfo component = sessionValidator.getComponentInfoFromTokenMap(clientId, serviceId);

        for (int iter = 0; iter < contexts.size(); iter++) {
            String currKey = contexts.get(iter).asText();
            if (Constants.SEARCH_VALUE.equals(currKey)) {
                if(searchValue == null || searchValue.isBlank()) {
                    properties.setProperty(currKey, UriUtils.encode(clientId, StandardCharsets.UTF_8));
                } else {
                    String currValue = contextConfig.get(currKey).asText();
                    properties.setProperty(currKey, UriUtils.encode(currValue, StandardCharsets.UTF_8));
                }
            } else if (Constants.COMPONENT_CLIENT_ID.equals(currKey)) {
                if (component != null && component.getComponentClientId() != null) {
                    properties.setProperty(currKey, UriUtils.encode(component.getComponentClientId(), StandardCharsets.UTF_8));
                } else {
                    properties.setProperty(currKey, UriUtils.encode(clientId, StandardCharsets.UTF_8));
                }
            } else if (Constants.COMPONENT_ACCOUNT_ID.equals(currKey)) {
                if (component != null && component.getComponentAccountId() != null) {
                    properties.setProperty(currKey, UriUtils.encode(component.getComponentAccountId(), StandardCharsets.UTF_8));
                } else {
                    properties.setProperty(currKey, UriUtils.encode(accountId, StandardCharsets.UTF_8));
                }
            } else {
                if (contextConfig.get(currKey) != null) {
                    String currValue = contextConfig.get(currKey).asText();
                    properties.setProperty(currKey, UriUtils.encode(currValue, StandardCharsets.UTF_8));
                }
            }
        }
    }
}