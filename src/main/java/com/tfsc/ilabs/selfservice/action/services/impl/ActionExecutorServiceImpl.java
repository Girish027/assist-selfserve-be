package com.tfsc.ilabs.selfservice.action.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.tfsc.ilabs.selfservice.action.models.*;
import com.tfsc.ilabs.selfservice.action.models.definition.*;
import com.tfsc.ilabs.selfservice.action.repositories.ActionExecutionMonitorRepository;
import com.tfsc.ilabs.selfservice.action.repositories.ActionRepository;
import com.tfsc.ilabs.selfservice.action.repositories.ActionWorkflowRepository;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.*;
import com.tfsc.ilabs.selfservice.common.repositories.ServiceUrlRepository;
import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthConfigService;
import com.tfsc.ilabs.selfservice.common.services.impl.ExternalServiceAuthFactory;
import com.tfsc.ilabs.selfservice.common.utils.*;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.configpuller.services.api.FetchConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityIdLookup;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityIdLookupRepository;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import io.swagger.models.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ravi.b on 23-07-2019.
 */
@Service
@Transactional
public class ActionExecutorServiceImpl implements ActionExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(ActionExecutorServiceImpl.class);
    private static JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ActionRepository actionRepository;
    @Autowired
    private ServiceUrlRepository serviceUrlRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private ActionExecutionMonitorRepository actionExecutionMonitorRepository;
    @Autowired
    private EntityInstanceRepository entityInstanceRepository;
    @Autowired
    private ActionWorkflowRepository actionWorkflowRepository;
    @Autowired
    private EntityIdLookupRepository entityIdLookupRepository;
    @Autowired
    private FetchConfigService fetchConfigService;
    @Autowired
    private ExternalServiceAuthFactory externalServiceAuthFactory;
    @Autowired
    private ExternalAuthConfigService externalAuthConfigService;
    @Autowired
    private DBConfigService dbConfigService;
    @Autowired
    private org.springframework.core.env.Environment environment;
    @Autowired
    private SessionValidator sessionValidator;
    /**
     * Create action execution monitor record for all action w.r.t entity instances if they don't exists
     */
    @Override
    public List<Action> createActionExecutionMonitorIfNotExist(WorkflowInstance workflowInstance, Environment publishToEnv) {
        List<EntityInstance> entityInstances = entityInstanceRepository.findAllByWorkflowInstance(workflowInstance);
        List<Action> actions = actionWorkflowRepository.findAllByWorkflowTemplateAndActionDefinitionType(
                workflowInstance.getWorkflowTemplate(), workflowInstance.getType())
                .stream()
                .map(ActionWorkflow::getAction)
                .collect(Collectors.toList());

        saveActionExecutions(actions, entityInstances, workflowInstance, publishToEnv);

        return actions;
    }

    private void saveActionExecutions(List<Action> actions, List<EntityInstance> entityInstances, WorkflowInstance workflowInstance, Environment environmentToExecute) {
        for (Action action : actions) {
            for (EntityInstance entityInstance : entityInstances) {
                ActionExecutionMonitor actionExecutionMonitor = actionExecutionMonitorRepository
                        .findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(action, workflowInstance,
                                entityInstance, environmentToExecute);
                if (actionExecutionMonitor == null) {
                    actionExecutionMonitor = new ActionExecutionMonitor(action, workflowInstance, entityInstance,
                            environmentToExecute);
                }

                // reset execution meta
                actionExecutionMonitor.setExecutionMeta(new ActionExecutionMeta());

                actionExecutionMonitorRepository.save(actionExecutionMonitor);
            }
        }
    }

    /**
     * This method execute the action's sub action w.r.t one entity (in case action needs to be executed on multiple entities.)
     */
    @Override
    public ExecutionStatus execute(Action action, WorkflowInstance workflowInstance, EntityInstance entityInstance,
                                   String clientId, String accountId) {
        RequestResponseDefinition requestResponseDefinition = null;
        logger.info("Starting action execution for :: actionId: {}, workflowInstanceId: {}, entityInstanceId: {} ",
                action.getId(), workflowInstance.getId(), entityInstance.getId());

        try {
            // if workflow is already at end state. do nothing
            if (WorkflowInstanceStatus.PROMOTED_TO_LIVE.equals(workflowInstance.getStatus())) {
                return ExecutionStatus.COMPLETED;
            }

            // Getting Service to call (Based on workflow instance state)
            Environment environmentToExecute = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
            logger.info("env to execute: {}", environmentToExecute);

            if (BaseUtil.shouldStubLivePublish(workflowInstance, dbConfigService)) {
                requestResponseDefinition = BaseUtil.getSuccessRequestResponseDefinitionStub(dbConfigService);
                logger.info("request res definition: {}", requestResponseDefinition);
            } else {
                JsonNode pageData = getAllPageDataForWorkflowInstance(workflowInstance);
                requestResponseDefinition = makeActionCall(action, entityInstance.toNameLabel(), clientId,
                        accountId, environmentToExecute, pageData, entityInstance.getPollingId());
                logger.info("request res definition: {}", requestResponseDefinition);
            }

            return handleResponse(requestResponseDefinition.getRequestDefinition(),
                    requestResponseDefinition.getResponseDefinition(), environmentToExecute, action, workflowInstance, entityInstance);
        } catch (Exception ex) {
            logger.info("Error: {}", ex.getMessage());
            throw new SelfServeException("Couldn't execute action call with id: "+ action.getId());
        }
    }

    @Override
    public ApiResponse execute(Integer actionId, JsonNode pageData, NameLabel entityInstance, String clientId, String accountId,
                               Environment environmentToExecute) {
        Optional<Action> optionalAction = actionRepository.findById(actionId);
        if (optionalAction.isEmpty()) {
            throw new NoSuchResourceException(new ErrorObject("Action not found", actionId));
        }
        Action action = null;
        if (optionalAction.isPresent()) {
            action = optionalAction.get();
        }
        ActionExecutorService.RequestResponseDefinition requestResponseDefinition = makeActionCall(
                action, entityInstance, clientId, accountId, environmentToExecute, pageData, null);
        ResponseDefinition responseDefinition = requestResponseDefinition.getResponseDefinition();
        JsonNode actionResponseDefinition=null;
        if(action!=null){
            actionResponseDefinition=action.getResponseDefinition();
        }
        return BaseUtil.translateAPIResponse(actionResponseDefinition, responseDefinition.getResponseBody(),
                responseDefinition.getResponseCode());
    }

    public String getBaseServiceUrl(String serviceId, Environment environmentToExecute) {
        ServiceUrls serviceUrls = serviceUrlRepository.findByServiceIdAndEnv(serviceId,
                environmentToExecute);
        if (serviceUrls == null) {
            throw new SelfServeException("Couldn't find service registered with Self Serve");
        }
        return serviceUrls.getBaseURL();
    }

    private RequestResponseDefinition makeActionCall(Action action, NameLabel entityInstance, String clientId, String accountId,
                                                     Environment environmentToExecute, JsonNode pageData, String pollingId) {

        ResponseDefinition responseDefinition = new ResponseDefinition();
        ActionRestDefinition actionRestDefinition = (ActionRestDefinition) action.getDefinition();
        Map<String, String> headers = new HashMap<>();
        String body = "";
        String url = "";
        try {
            ObjectNode liveEntityMap = getEntityIdMap(actionRestDefinition, pageData, environmentToExecute);

            ObjectNode prefetchRequestData = objectMapper.createObjectNode();
            if (!isEntityIdMapped(actionRestDefinition, pageData, environmentToExecute, liveEntityMap)) {
                JsonNode preFetchDefinition = actionRestDefinition.getPreFetchDefinition();
                if (preFetchDefinition != null && !preFetchDefinition.isNull() && ((Environment.LIVE.name().equals(environmentToExecute.name())) || (!actionRestDefinition.isPrefetchRequiredForLiveOnly() && Environment.TEST.name().equals(environmentToExecute.name()))))
                {
                    prefetchRequestData = getPrefetchRequestData(entityInstance, clientId, accountId, preFetchDefinition, environmentToExecute);

                }
                logger.info("preFetchDefinition: {}", preFetchDefinition);
            }
            MultipartInputStreamFileResource bodyWithFormData = null;
            String baseUrl = getBaseServiceUrl(actionRestDefinition.getServiceId(), environmentToExecute);
            Properties properties = constructProperties(entityInstance.getName(), clientId, accountId, actionRestDefinition.getServiceId(),
                    environmentToExecute, baseUrl);
            headers = buildHeaders(actionRestDefinition, environmentToExecute, properties);
            logger.info("Headers: {}", headers);
            Map<ActionConfigType, ActionConfig> actionConfigs = action.getConfigs();
            ActionPathParamsConfig paramsConfig = (ActionPathParamsConfig) actionConfigs.get(ActionConfigType.PATH_PARAMS);
            String pathParam = "";
            if (actionRestDefinition.isUploadFile()) {
                bodyWithFormData = getBodyForActionCallWithFormData(pageData);
                body = bodyWithFormData == null ? null : bodyWithFormData.toString();
                if(actionRestDefinition.isGetPathParamFromPageData()) {
                    pathParam = getBodyForActionCall(actionRestDefinition, pageData,entityInstance.getName(), clientId,
                            accountId, environmentToExecute, prefetchRequestData.toString(), liveEntityMap);
                }
            } else {
                body = getBodyForActionCall(actionRestDefinition, pageData, entityInstance.getName(), clientId,
                        accountId, environmentToExecute, prefetchRequestData.toString(), liveEntityMap);
            }
            logger.info("body: {}", body);

            if (pollingId != null) {
                properties.put(Constants.UPLOAD_ID, pollingId);
            }
            if (paramsConfig != null && !pathParam.isBlank()) {
                for (Map<String, String> param : paramsConfig.getPathParams()) {
                    if (param.get("key").equals(Constants.UPLOAD_OPTION)) {
                        properties.put((String) param.get("key"), pathParam);
                    }
                }
            }
            properties.put(Constants.BODY, body);

            url = buildURL(actionRestDefinition, properties, baseUrl);
            logger.info("url: {}", url);

            long startMillis = System.currentTimeMillis();
            Boolean isEncoded = actionRestDefinition.isUrlParamsEncoded();
            if (RestClientType.REST_TEMPLATE.equals(actionRestDefinition.getRestClientType())
                    && BaseUtil.isNotNullOrBlank(actionRestDefinition.getRequestBodyKey())) {
                ResponseEntity<String> responseEntity = HttpUtils.sendPOST(url, body, headers, actionRestDefinition.getRequestBodyKey(), isEncoded);
                long timeTakenMillis = System.currentTimeMillis() - startMillis;
                logger.info("{} Response received URL: {} ; Total Response time in Milliseconds: {}, calling sendPOST",
                        actionRestDefinition.getMethod(), url, timeTakenMillis);
                responseDefinition.setResponseBody(responseEntity.getBody());
                responseDefinition.setResponseCode(responseEntity.getStatusCodeValue());
            } else if (RestClientType.MULTIPART_FILE.equals(actionRestDefinition.getRestClientType())) {
                ResponseEntity<String> responseEntity = HttpUtils.sendPOST(url, bodyWithFormData, headers, actionRestDefinition.getRequestBodyKey(), isEncoded);
                long timeTakenMillis = System.currentTimeMillis() - startMillis;
                logger.info("{} Response received URL: {} ; Total Response time in Milliseconds: {}, calling sendPOST",
                        actionRestDefinition.getMethod(), url, timeTakenMillis);
                responseDefinition.setResponseBody(responseEntity.getBody());
                responseDefinition.setResponseCode(responseEntity.getStatusCodeValue());
            } else {
                HttpResponse<String> response = null;
                HttpMethod httpMethod = actionRestDefinition.getMethod();

                switch (httpMethod) {
                    case GET: {
                        response = HttpUtils.makeGetCall(url, objectMapper.valueToTree(headers));
                        break;
                    }
                    case POST:
                    case PUT: {
                        response = HttpUtils.makePostOrPutCall(url, httpMethod, headers, body, isEncoded);
                        break;
                    }
                    default:
                }
                long timeTakenMillis = System.currentTimeMillis() - startMillis;
                logger.info("{} Response received URL: {} ; Total Response time in Milliseconds: {}, calling makePostOrPutCall",
                        actionRestDefinition.getMethod(), url, timeTakenMillis);
                if (response != null) {
                    responseDefinition.setResponseBody(response.body());
                    responseDefinition.setResponseCode(response.statusCode());
                }
            }
        } catch (SelfServeException ex) {
            responseDefinition.setResponseBody(ex.getMessage());
            logger.error(ex.getMessage());
            responseDefinition.setResponseCode(500);
        }

        return new RequestResponseDefinition(new RequestDefinition(actionRestDefinition.getMethod(), headers, url, body),
                responseDefinition);
    }

    @Override
    public void save(ActionExecutionMonitor actionExecutionMonitor) {
        actionExecutionMonitorRepository.save(actionExecutionMonitor);
    }

    private ObjectNode getPrefetchRequestData(NameLabel entityInstance, String clientId, String accountId, JsonNode preFetchDefinition, Environment environmentToExecute) {
        ObjectNode prefetchRequestData = objectMapper.createObjectNode();

        List<NameLabel> entities = new ArrayList<>();
        entities.add(new NameLabel(entityInstance.getName(), entityInstance.getLabel()));
        JsonNode contextConfig = BaseUtil.getContext(clientId, accountId, entities);

        if (!preFetchDefinition.isNull()) {
            for (Iterator<String> it = preFetchDefinition.fieldNames(); it.hasNext(); ) {
                String key = it.next();
                Environment finalEnv = environmentToExecute;

                JsonNode values = preFetchDefinition.get(key);
                String fetchFor = values.get("fetchFor").asText();
                FetchType fetchType = FetchType.valueOf(values.get("fetchType").asText());
                if (values.get("env") != null) {
                    finalEnv = Environment.valueOf(values.get("env").asText());
                }
                JsonNode fetchData = fetchConfigService.getDataFromService(fetchFor, contextConfig, fetchType, finalEnv);
                prefetchRequestData.set(key, fetchData);
            }
        }

        return prefetchRequestData;
    }

    /**
     * This methods handle the response of each sub action and update the same in sub action monitor table.
     *
     * @return returns sub action status for actual action status calculation.
     */
    private ExecutionStatus handleResponse(RequestDefinition request, ResponseDefinition response, Environment environment,
                                           Action action, WorkflowInstance workflowInstance, EntityInstance entityInstance) {
        ActionExecutionMonitor actionExecutionMonitor = actionExecutionMonitorRepository
                .findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(action, workflowInstance, entityInstance, environment);
        actionExecutionMonitor = actionExecutionMonitor == null
                ? new ActionExecutionMonitor(action, workflowInstance, entityInstance, environment)
                : actionExecutionMonitor;

        actionExecutionMonitor.setRequest(request);
        actionExecutionMonitor.setResponse(response);

        ExecutionStatus status = HttpStatus.valueOf(response.getResponseCode()).is2xxSuccessful()
                ? ExecutionStatus.COMPLETED
                : ExecutionStatus.FAILED;
        actionExecutionMonitor.setStatus(status);
        actionExecutionMonitorRepository.save(actionExecutionMonitor);

        return status;
    }

    private Properties constructProperties(String entityId, String clientId, String accountId, String serviceId,
                                           Environment environment, String serviceUrl) {
        Properties properties = new Properties();
        properties.put(Constants.ENTITY, UriUtils.encode(entityId, StandardCharsets.UTF_8));
        properties.put(Constants.CLIENT_ID, clientId);
        properties.put(Constants.ACCOUNT_ID, accountId);
        properties.put(PropertyConstants.SERVICE_DOMAIN, serviceUrl);
        ComponentInfo component = null;
        if(!StringUtils.isEmpty(serviceId)) {
           component = sessionValidator.getComponentInfoFromTokenMap(clientId, serviceId);
        }
        if(component != null && component.getComponentClientId() != null){
            properties.put(Constants.COMPONENT_CLIENT_ID, component.getComponentClientId());
        }else{
            properties.put(Constants.COMPONENT_CLIENT_ID, clientId);
        }
        if(component != null && component.getComponentAccountId() != null){
            properties.put(Constants.COMPONENT_ACCOUNT_ID, component.getComponentAccountId());
        }else{
            properties.put(Constants.COMPONENT_ACCOUNT_ID, accountId);
        }
        if(environment.name().equals(PropertyConstants.TEST)) {
            properties.put(PropertyConstants.DOT_DOMAIN_ENVIRONMENT, PropertyConstants.DOT_STAGING);
            properties.put(PropertyConstants.DOMAIN_ENVIRONMENT, PropertyConstants.STAGING);
            properties.put(PropertyConstants.DOMAIN_ENVIRONMENT_DOT, PropertyConstants.STAGING_DOT);
        } else {
            properties.put(PropertyConstants.DOT_DOMAIN_ENVIRONMENT, "");
            properties.put(PropertyConstants.DOMAIN_ENVIRONMENT, "");
            properties.put(PropertyConstants.DOMAIN_ENVIRONMENT_DOT, "");
        }
        return properties;
    }
    /**
     * This method is used to build the URL for making action call with correct service and and its url based on environment.
     */
    private String buildURL(ActionRestDefinition actionRestDefinition, Properties properties,
                            String serviceUrl) {
        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(Constants.PLACE_HOLDER_PREFIX,
                Constants.PLACE_HOLDER_SUFFIX);
        String relativePath = actionRestDefinition.getRelativePath();
        return placeholderHelper.replacePlaceholders(serviceUrl + relativePath, properties);
    }

    /**
     * This method merges the base service & config headers
     */
    private Map<String, String> buildHeaders(ActionRestDefinition actionRestDefinition, Environment environmentToExecute,
                                             Properties properties) {
        Map<String, String> configHeadersMap = actionRestDefinition.getHeaders();

        ServiceUrls serviceUrls = serviceUrlRepository.findByServiceIdAndEnv(actionRestDefinition.getServiceId(),
                environmentToExecute);
        if (serviceUrls == null) {
            throw new SelfServeException("Couldn't find service registered with Self Serve");
        }

        JsonNode baseHeaders = serviceUrls.getHeaders();
        ObjectNode mergedHeaders = new ObjectMapper().createObjectNode();
        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(Constants.PLACE_HOLDER_PREFIX,
                Constants.PLACE_HOLDER_SUFFIX);
        for (Map.Entry<String, String> configHeader : configHeadersMap.entrySet()) {
            String fieldName = configHeader.getKey();
            JsonNode baseHeader = baseHeaders.get(fieldName);
            if (Constants.AUTH_TOKEN_TYPE.equals(fieldName)) {
                String authTokenHeaderKey = externalAuthConfigService.getValue(Constants.DB_CONFIG_TOKEN_HEADER_KEY, actionRestDefinition.getServiceId());
                mergedHeaders.set(authTokenHeaderKey,
                        TextNode.valueOf(baseHeader.asText() + externalServiceAuthFactory.getBuilder(actionRestDefinition.getServiceId()).generateSignedToken(environmentToExecute)));
            } else {
                if (baseHeader == null || baseHeader.isNull()) {
                    String headerValue = placeholderHelper.replacePlaceholders(configHeader.getValue(), properties);
                    mergedHeaders.set(fieldName, TextNode.valueOf(headerValue));
                } else {
                    String headerValue = placeholderHelper.replacePlaceholders(baseHeader.asText(), properties);
                    mergedHeaders.put(fieldName, headerValue);
                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> baseHeadersMap = mapper.convertValue(mergedHeaders, new TypeReference<Map<String, String>>() {
        });

        configHeadersMap.forEach((key, value) -> baseHeadersMap.merge(key, value, (v1, v2) -> v1));

        return baseHeadersMap;
    }


    /**
     * This method will collect all page data for workflow instance and create a json object with keys as page id and value as page data
     *
     * @return json object with keys as pageid and valus as pagedata
     */
    private JsonNode getAllPageDataForWorkflowInstance(WorkflowInstance workflowInstance) {
        ObjectNode objectNode = jsonNodeFactory.objectNode();

        List<PageData> pageDataList = pageService.getPageDataByWorkflowInstace(workflowInstance);
        pageDataList.forEach(pageData -> objectNode.set(pageData.getPageTemplate().getId(), pageData.getData()));

        return objectNode;
    }

    /**
     * This methods gets the body of action from either objectTranWorkflowControllerslator JS function or bodyDefinition mapper
     *
     * @return String json body for action call
     */
    private String getBodyForActionCall(ActionRestDefinition actionRestDefinition, JsonNode pageData,
                                        String entityId, String clientId, String accountId, Environment env, String prefetchRequestData, ObjectNode liveEntityIdMap) {
        String javascriptFunctionCode = actionRestDefinition.getObjectTranslator();
        ComponentInfo component = sessionValidator.getComponentInfoFromTokenMap(clientId,actionRestDefinition.getServiceId());
        if (BaseUtil.isNullOrBlank(javascriptFunctionCode)) {
            throw new SelfServeException("Object translator function not defined .");
        }

        return ScriptUtil.invokeFunction(javascriptFunctionCode, Constants.OBJECT_TRANSLATOR_METHOD,
                pageData.toString(), entityId, clientId, accountId, env.name(), prefetchRequestData, liveEntityIdMap,component.getComponentClientId(), component.getComponentAccountId());
    }

    private boolean isEntityIdMapped(ActionRestDefinition actionRestDefinition, JsonNode pageData, Environment env, ObjectNode entityIdMap) {
        int mapSize = entityIdMap.size();
        if (actionRestDefinition.isIdLookupRequired() && Environment.LIVE.name().equals(env.name())) {
            for (String entityKeyName : actionRestDefinition.getIdLookupKeys().split(",")) {
                JsonNode testEntityIdValues = pageData.findValue(entityKeyName);
                if (testEntityIdValues != null && testEntityIdValues.isArray()) {
                    return mapSize == testEntityIdValues.size();
                } else {
                    return mapSize > 0;
                }
            }
        }
        return false;
    }
    private ObjectNode getEntityIdMap(ActionRestDefinition actionRestDefinition, JsonNode pageData, Environment env) {
        ObjectNode liveEntityIdMap = objectMapper.createObjectNode();
        if (actionRestDefinition.isIdLookupRequired() && Environment.LIVE.name().equals(env.name())) {
            for (String entityKeyName : actionRestDefinition.getIdLookupKeys().split(",")) {
                JsonNode testEntityIdValues = pageData.findValue(entityKeyName);
                if (testEntityIdValues != null && testEntityIdValues.isArray()) {
                    List<String> entityIdList = new ArrayList<>();
                    for (JsonNode testEntityIdValue : testEntityIdValues) {
                        EntityIdLookup entityIdLookup = entityIdLookupRepository.findByTestEntityId(testEntityIdValue.asText());
                        entityIdList.add(entityIdLookup.getLiveEntityId());
                    }
                    liveEntityIdMap.put(entityKeyName, String.join(",", entityIdList));
                } else {
                    JsonNode testEntityIdValue = pageData;
                    for(String path : entityKeyName.split("\\.") ){
                        testEntityIdValue =  testEntityIdValue.findValue(path);
                    }
                    EntityIdLookup entityIdLookup = entityIdLookupRepository.findByTestEntityId(testEntityIdValue.asText());
                    if(entityIdLookup != null){
                        String liveEntityId = entityIdLookup.getLiveEntityId();
                        if(liveEntityId != null){
                            liveEntityIdMap.put(entityKeyName, entityIdLookup.getLiveEntityId());
                        }
                    }

                }
            }
        }
        return liveEntityIdMap;
    }

    private MultipartInputStreamFileResource getBodyForActionCallWithFormData(JsonNode pageData) {

        MultipartInputStreamFileResource file = null;
        String fileName = pageData.findValue("file").get("fileName").asText();

        try {
            if (environment.getProperty(Constants.STORAGE_TYPE).equals("gcp")) {
                String projectId = environment.getProperty(Constants.GCP_PROJECT_ID);
                String bucketName = environment.getProperty(Constants.GCP_BUCKET_NAME);
                String credentialsFilePath = environment.getProperty(Constants.CREDENTIALS_FILE_PATH);
                Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFilePath));
                Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();

                Blob blob = storage.get(bucketName, fileName);
                ReadChannel reader = blob.reader();
                InputStream inputStream = Channels.newInputStream(reader);
                file = new MultipartInputStreamFileResource(inputStream, fileName);
            } else {
                String localFileLocation = pageData.findValue("file").get("fileLocation").asText();
                File f = new File(localFileLocation);
                file = new MultipartInputStreamFileResource(new FileInputStream(f), fileName);
            }
        } catch (Exception ex) {
            logger.error(String.format("Unable to load file: %s", ex.getMessage()));
        }
        return file;
    }

    @Override
    public ActionExecutionMonitor getActionExecutionMonitor(Action action, WorkflowInstance workflowInstance,
                                                            EntityInstance entityInstance, Environment env) {
        return actionExecutionMonitorRepository.findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(action,
                workflowInstance, entityInstance, env);
    }

    @Override
    public ActionExecutionMonitor getActionExecutionMonitor(ActionExecutionMonitor actionExecutionMonitor) {
        return getActionExecutionMonitor(actionExecutionMonitor.getAction(), actionExecutionMonitor.getWorkflowInstance(),
                actionExecutionMonitor.getEntityInstance(), actionExecutionMonitor.getEnv());
    }
}

class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return -1;
    }
}
