package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.definition.RequestDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.security.service.UserSession;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.workflow.dto.response.SummaryStatus;
import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class BaseUtil {
    private static final Logger logger =
            LoggerFactory.getLogger(BaseUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private BaseUtil() {
        // hide implicit public constructor
    }

    public static boolean isNullOrBlank(String str) {
        return (str == null || ("").equals(str.trim()));
    }

    public static boolean isNotNullOrBlank(String str) {
        return !isNullOrBlank(str);
    }

    public static boolean isNullOrBlankCollection(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    private static String trimTrailingSlashes(String url) {
        return url.replaceAll("/+$", "").replaceAll("\\\\+$", "");
    }

    private static String trimLeadingSlashes(String url) {
        return url.replaceAll("^/+", "").replaceAll("^\\\\+", "");
    }

    public static String joinUrlParts(String prefix, String suffix) {
        return trimTrailingSlashes(prefix) + "/" + trimLeadingSlashes(suffix);
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static CloseableHttpClient getOktaHttpClient(
            String oktaConnectionTimeoutMultiply,
            String oktaConnectionTimeout,
            String oktaConnectionProxy,
            String oktaConnectionProxyHost,
            String oktaConnectionProxyPort,
            String oktaConnectionProxyProtocol

    ) {
        // Increase timeout for okta requests
        CloseableHttpClient httpclient;
        int timeout = Integer.parseInt(oktaConnectionTimeoutMultiply);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * Integer.parseInt(oktaConnectionTimeout))
                .setConnectionRequestTimeout(timeout * Integer.parseInt(oktaConnectionTimeout))
                .setSocketTimeout(timeout * Integer.parseInt(oktaConnectionTimeout)).build();
        if (Boolean.parseBoolean(oktaConnectionProxy)) {
            // If the application provides proxy settings as application properties, than use below code to initialize the httpclient
            HttpHost proxy = new HttpHost(oktaConnectionProxyHost, Integer.parseInt(oktaConnectionProxyPort), oktaConnectionProxyProtocol);
            DefaultProxyRoutePlanner routePlaner = new DefaultProxyRoutePlanner(proxy);
            httpclient = HttpClients.custom().useSystemProperties().setRoutePlanner(routePlaner).setDefaultRequestConfig(config).build();
            System.setProperty("http.proxyHost", oktaConnectionProxyHost);
            System.setProperty("http.proxyPort", oktaConnectionProxyPort);
            System.setProperty("https.proxyHost", oktaConnectionProxyHost);
            System.setProperty("https.proxyPort", oktaConnectionProxyPort);
            logger.info("Created httpclient with proxy");
        } else {
            // If the application needs to read the proxy settings from system properties than use below code to initialize the httpclient
            httpclient = HttpClients.custom().useSystemProperties().setDefaultRequestConfig(config).build();
            logger.info("Created httpclient without proxy");
        }
        return httpclient;
    }

    public static String convertSetToString(Set set) {
        String s = Optional.ofNullable(set)
                .orElse(Collections.emptySet())
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","))
                .toString();
        return String.format("[%s]", s);
    }

    public static String convertListToString(List list) {
        String s = Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","))
                .toString();
        return String.format("[%s]", s);
    }

    public static String convertMapToString(Map map) {
        String s = Optional.ofNullable(map)
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","))
                .toString();
        return String.format("[%s]", s);
    }

    public static Environment getEnvironmentToFetchFrom(PublishType publishType) {
        Environment retVal = Environment.TEST;
        if (PublishType.LIVE_ONLY == publishType) {
            retVal = Environment.LIVE;
        }
        return retVal;
    }

    public static Environment getEnvironmentToExecuteForWorkflowInstance(WorkflowInstance workflowInstance) {
        PublishType publishType = workflowInstance.getWorkflowTemplate().getConfigs().getPublishType();
        Environment env = null;
        if (PublishType.DEFAULT.equals(publishType)) {
            switch (workflowInstance.getStatus()) {
                case PROMOTED_TO_TEST:
                case LIVE_PROMOTION_FAILED:
                case PROMOTING_TO_LIVE:
                    env = Environment.LIVE;
                    break;
                case DRAFT_SAVE:
                case TEST_PROMOTION_FAILED:
                case PROMOTING_TO_TEST:
                    env = Environment.TEST;
                    break;
                case DRAFT_EDIT:
                case PROMOTED_TO_LIVE:
                default:
                    throw new SelfServeException(Constants.NO_ENVIRONMENT_TO_EXECUTE_FROM_THIS_STATE + workflowInstance.getStatus().name());
            }
        } else if (PublishType.TEST_ONLY.equals(publishType)) {
            switch (workflowInstance.getStatus()) {
                case DRAFT_SAVE:
                case TEST_PROMOTION_FAILED:
                case PROMOTING_TO_TEST:
                    env = Environment.TEST;
                    break;
                case PROMOTED_TO_TEST:
                case DRAFT_EDIT:
                default:
                    throw new SelfServeException(Constants.NO_ENVIRONMENT_TO_EXECUTE_FROM_THIS_STATE + workflowInstance.getStatus().name());
            }
        } else if (PublishType.LIVE_ONLY.equals(publishType)) {
            switch (workflowInstance.getStatus()) {
                case DRAFT_SAVE:
                case LIVE_PROMOTION_FAILED:
                case PROMOTING_TO_LIVE:
                    env = Environment.LIVE;
                    break;
                case DRAFT_EDIT:
                case PROMOTED_TO_LIVE:
                default:
                    throw new SelfServeException(Constants.NO_ENVIRONMENT_TO_EXECUTE_FROM_THIS_STATE + workflowInstance.getStatus().name());
            }
        }
        return env;
    }

    public static boolean isWorkflowInstanceInTerminalState(WorkflowInstance workflowInstance) {
        WorkflowInstanceStatus workflowInstanceStatus = workflowInstance.getStatus();
        PublishType publishType = workflowInstance.getWorkflowTemplate().getConfigs().getPublishType();

        WorkflowInstanceStatus[] terminalWorkflowInstanceStatus = Constants.TERMINAL_STATUS.get(publishType);
        return Arrays.asList(terminalWorkflowInstanceStatus).contains(workflowInstanceStatus);
    }

    public static boolean isEntityInstanceInTerminalState(EntityInstance entityInstance) {
        return EntityInstanceStatus.PROMOTED_TO_LIVE.equals(entityInstance.getStatus())
                || EntityInstanceStatus.DISCARDED.equals(entityInstance.getStatus())
                || (EntityInstanceStatus.PROMOTED_TO_TEST.equals(entityInstance.getStatus())
                && PublishType.TEST_ONLY.equals(entityInstance.getWorkflowInstance().getWorkflowTemplate().getConfigs().getPublishType()));
    }

    public static JsonNode getContext(String clientId, String accountId, List<NameLabel> entities) {
        JsonNode contextConfig = objectMapper.createObjectNode();
        ((ObjectNode) contextConfig).put(Constants.CLIENT_ID, clientId);
        ((ObjectNode) contextConfig).put(Constants.ACCOUNT_ID, accountId);
        if (!entities.isEmpty()) {
            ((ObjectNode) contextConfig).put(Constants.ENTITY, entities.get(0).getName());
        }
        return contextConfig;
    }

    public static JsonNode getContext(String clientId, String accountId, JsonNode contextConfig) {
        ((ObjectNode) contextConfig).put(Constants.CLIENT_ID, clientId);
        ((ObjectNode) contextConfig).put(Constants.ACCOUNT_ID, accountId);
        return contextConfig;
    }

    public static FetchType getFetchType(String fetchType) {
        return FetchType.valueOf(fetchType);
    }

    public static JsonNode mergeResponse(JsonNode mainResponse, JsonNode updateJson) {
        if (updateJson.isArray()) {
            if (mainResponse == null) {
                mainResponse = updateJson;
            } else {
                if (updateJson.size() <= 1) {
                    ((ArrayNode) mainResponse).add(updateJson);
                } else {
                    BaseUtil.merge(mainResponse, updateJson);
                }
            }
        } else {
            if (mainResponse == null) {
                mainResponse = objectMapper.createObjectNode();
            }
            BaseUtil.merge(mainResponse, updateJson);
        }
        return mainResponse;
    }

    private static JsonNode mergeArray(JsonNode mainNode, JsonNode updateNode) {
        if (updateNode.size() <= 1) {
            ((ArrayNode) mainNode).addAll((ArrayNode) updateNode);
        } else {
            for (int index = 0; index < updateNode.size() && index < mainNode.size(); index++) {
                JsonNode updateNodeValue = updateNode.get(index);
                JsonNode mainNodeValue = mainNode.get(index);
                JsonNode mergedVal = merge(mainNodeValue, updateNodeValue);
                ((ArrayNode) mainNode).set(index, mergedVal);
            }
        }
        return mainNode;
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        if (updateNode == null) {
            return mainNode != null
                    ? merge(null, mainNode)
                    : null;
        }

        mainNode = initWithCompatibleType(mainNode, updateNode);

        if (mainNode instanceof ArrayNode && updateNode instanceof ArrayNode) {
            mergeArray(mainNode, updateNode);
        } else if (mainNode != null) {
            Iterator<String> fieldNames = updateNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();

                JsonNode updateNodeFieldValue = updateNode.get(fieldName);
                JsonNode mainNodeFieldValue = mainNode.get(fieldName);
                if (mainNodeFieldValue != null)
                    mergeNodeValues(mainNodeFieldValue, updateNodeFieldValue);
                else if (mainNode instanceof ObjectNode)
                    ((ObjectNode) mainNode).set(fieldName, updateNodeFieldValue);
            }
        }
        return mainNode;
    }

    private static void mergeNodeValues(JsonNode mainNodeFieldValue, JsonNode updateNodeFieldValue) {
        if (mainNodeFieldValue.isObject()) {
            merge(mainNodeFieldValue, updateNodeFieldValue);
        } else if (mainNodeFieldValue.isArray() && updateNodeFieldValue.isArray()) {
            mergeArray(mainNodeFieldValue, updateNodeFieldValue);
        }
    }

    private static JsonNode initWithCompatibleType(JsonNode mainNode, JsonNode updateNode) {
        if (mainNode == null) {
            if (updateNode instanceof ObjectNode)
                mainNode = objectMapper.createObjectNode();
            else if (updateNode instanceof ArrayNode)
                mainNode = objectMapper.createArrayNode();
        }
        return mainNode;
    }

    public static SSAuthenticationToken toToken(UserSession userSession) {
        SSAuthenticationToken token = new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        token.setUserId(userSession.getUserId());
        token.setUserName(userSession.getUserName());
        token.setAccessToken(userSession.getAccessToken());
        return token;
    }

    public static boolean isInvalidateSessionRequest(HttpServletRequest request) {
        return request.getRequestURI().endsWith(Constants.INVALIDATE_SESSION_URI);
    }

    public static boolean isErrorRequest(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/api/error");
    }

    /**
     * It categorizes Status into TEST,LIVE,DRAFT,DISCARDED
     *
     * @param workflowInstance
     * @return
     */
    public static SummaryStatus extractSummaryStatus(WorkflowInstance workflowInstance) {
        if (Constants.LIVE_STATUS.contains(workflowInstance.getStatus())) {
            return SummaryStatus.LIVE;
        } else if (Constants.TEST_STATUS.contains(workflowInstance.getStatus())) {
            return SummaryStatus.TEST;
        } else if (Constants.DRAFT_STATUS.contains(workflowInstance.getStatus())) {
            return SummaryStatus.DRAFT;
        }
        return SummaryStatus.DISCARDED;
    }

    public static String getJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(obj);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static boolean shouldStubLivePublish(WorkflowInstance workflowInstance, DBConfigService dbConfigService) {
        Environment environmentToExecute = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
        Boolean skipLiveEnv = dbConfigService.findByCode(Constants.SKIP_LIVE_PROMOTIONS, Boolean.class);
        PublishType publishType = workflowInstance.getWorkflowTemplate().getConfigs().getPublishType();

        return (skipLiveEnv != null) && skipLiveEnv && Environment.LIVE.equals(environmentToExecute) && !PublishType.LIVE_ONLY.equals(publishType);
    }

    public static ActionExecutorService.RequestResponseDefinition getSuccessRequestResponseDefinitionStub(DBConfigService dbConfigService) {
        JsonNode responseBody = dbConfigService.findByCode(Constants.MOCK_SUCCESS_RESPONSE, JsonNode.class);
        return new ActionExecutorService.RequestResponseDefinition(new RequestDefinition(),
                new ResponseDefinition(200, responseBody.toString()));
    }

    public static ApiResponse translateAPIResponse(JsonNode actionResponseDefinition, String responseBody, Integer statusCode) {
        if (actionResponseDefinition != null) {
            JsonNode responseKey =  actionResponseDefinition.get(Constants.ENTITY_RESPONSE_TRANSLATOR_KEY);
            if (responseKey != null) {
                String objectTranslator = responseKey.asText();
                return ScriptUtil.invokeFunction(objectTranslator,
                        Constants.OBJECT_TRANSLATOR_METHOD, ApiResponse.class, responseBody, statusCode);
            }
        }
        return new ApiResponse();
    }
}