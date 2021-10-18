package com.tfsc.ilabs.selfservice.configpuller.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.dto.ExcludedFetchConfig;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.Service;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.utils.ScriptUtil;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxEntityRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;
import com.tfsc.ilabs.selfservice.configpuller.model.TranslatorType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.configpuller.services.impl.ExternalAPIServiceImpl;
import com.tfsc.ilabs.selfservice.testcontainer.FetchConfigContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.ACCOUNT_ID;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.CLIENT_ID;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class, ScriptUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ExternalAPIServiceTest {
    @Mock
    HttpResponse<String> httpResponse;
    @Mock
    HttpResponse<byte[]> httpResponseByteArray;
    @Mock
    DBConfigService dbConfigService;
    @InjectMocks
    private ExternalAPIServiceImpl externalAPIService;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.mockStatic(ScriptUtil.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Field fieldServices = externalAPIService.getClass().getDeclaredField("objectMapper");
        fieldServices.setAccessible(true);
        fieldServices.set(externalAPIService, objectMapper);

        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(JavaType.class))).thenReturn(null);
    }

    @Test
    public void testAuxEntityData_ComponentAPI_OHS() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();
        String body = "{\"status\":\"SUCCESS\"}";
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String contextConfigString = "{\"pageNo\":1,\"pageSize\":\"20\",\"clientId\":\"test_client\",\"accountId\":\"test_account\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode contextConfig = mapper.readTree(contextConfigString);

        String objectTranslator = fetchConfigTemplate.getRespApiToRespUi().get("objectTranslator").asText();
        PowerMockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(body);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));

        JsonNode node = new ObjectMapper().readTree(jsonResponse);
        Assert.assertNotNull(node);
        Assert.assertEquals("SUCCESS", node.get("status").asText());
    }

    @Test
    public void testAuxEntityData_ComponentAPI_OHS_withMissingQueryParam() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);
        fetchConfigTemplate.getParams().setQueryParams(new ObjectMapper().readTree("{\"pageNumber\":\"${pageNumber}\"}"));
        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();
        String body = "{\"status\":\"SUCCESS\"}";
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String contextConfigString = "{\"pageNo\":1,\"pageSize\":\"20\",\"clientId\":\"test_client\",\"accountId\":\"test_account\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode contextConfig = mapper.readTree(contextConfigString);

        String objectTranslator = fetchConfigTemplate.getRespApiToRespUi().get("objectTranslator").asText();
        PowerMockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(body);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));

        JsonNode node = new ObjectMapper().readTree(jsonResponse);
        Assert.assertNotNull(node);
        Assert.assertEquals("SUCCESS", node.get("status").asText());
    }

    @Test(expected = SelfServeException.class)
    public void testAuxEntityData_WithApiResponseAsError() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        String baseUrl = "http://test-host";
        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;
        JsonNode uiResponse = new ObjectMapper().createObjectNode();
        String body = "{\"status\":\"SUCCESS\"}";
        Mockito.when(httpResponse.statusCode()).thenReturn(400);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));
    }

    @Test
    public void test_externalServiceGetApiCall_withFetchErrorAllowedWithStatusCodes() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        ExcludedFetchConfig excludedFetchConfig = new ExcludedFetchConfig();
        excludedFetchConfig.setFetchFor("queue_entities");
        excludedFetchConfig.setStatus(List.of(404, 500));
        Map<String, ExcludedFetchConfig> excludedFetchConfigs = new HashMap<>();
        excludedFetchConfigs.put("queue_entities", excludedFetchConfig);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(JavaType.class))).thenReturn(excludedFetchConfigs);

        Mockito.when(httpResponse.statusCode()).thenReturn(500);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String body = "{\"status\":\"SUCCESS\"}";
        PowerMockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(body);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));

        Assert.assertEquals(body, jsonResponse);
    }

    @Test
    public void test_externalServiceGetApiCall_withFetchErrorAllowedWithoutStatusCodes() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        ExcludedFetchConfig excludedFetchConfig = new ExcludedFetchConfig();
        excludedFetchConfig.setFetchFor("queue_entities");
        Map<String, ExcludedFetchConfig> excludedFetchConfigs = new HashMap<>();
        excludedFetchConfigs.put("queue_entities", excludedFetchConfig);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(JavaType.class))).thenReturn(excludedFetchConfigs);

        Mockito.when(httpResponse.statusCode()).thenReturn(500);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String body = "{\"status\":\"SUCCESS\"}";
        PowerMockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(body);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));

        Assert.assertEquals(body, jsonResponse);
    }

    @Test
    public void test_externalServiceGetApiCall_withJSON() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);
        fetchConfigTemplate.setTranslatorType(TranslatorType.JSON);
        fetchConfigTemplate.setRespApiToRespUi(new ObjectMapper().readTree("[]")); // forcing error with incorrect structure

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        String body = "{}";
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(),"",new SearchConfig(0,0,false));
        Assert.assertEquals(jsonResponse, body);
    }

    @Test(expected = SelfServeException.class)
    public void test_externalServiceGetApiCall_withJSONParseException() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);
        fetchConfigTemplate.setTranslatorType(TranslatorType.JSON);
        fetchConfigTemplate.setRespApiToRespUi(new ObjectMapper().readTree("[{}]")); // forcing error with incorrect structure

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        String body = "{}";
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCall(fullUrl, fetchConfigTemplate.getHeaders(), fetchConfigTemplate.getParams().getQueryParams()))
                .thenReturn(httpResponse);

        String jsonResponse = externalAPIService.externalServiceGetApiCall(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST, new Properties(), "",new SearchConfig(0,0,false));
    }

    @Test(expected = SelfServeException.class)
    public void test_externalServiceGetApiCallAsByteArray_WithApiResponseAsError() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        String baseUrl = "http://test-host";
        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;
        JsonNode uiResponse = new ObjectMapper().createObjectNode();
        Mockito.when(httpResponseByteArray.statusCode()).thenReturn(400);
        PowerMockito.when(HttpUtils.makeGetCallOfArray(fullUrl, fetchConfigTemplate.getHeaders()))
                .thenReturn(httpResponseByteArray);

        byte[] jsonResponse = externalAPIService.externalServiceGetApiCallAsByteArray(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST);
    }

    @Test
    public void test_externalServiceGetApiCallAsByteArray_withFetchErrorAllowedWithStatusCodes() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        ExcludedFetchConfig excludedFetchConfig = new ExcludedFetchConfig();
        excludedFetchConfig.setFetchFor("queue_entities");
        excludedFetchConfig.setStatus(List.of(404, 500));
        Map<String, ExcludedFetchConfig> excludedFetchConfigs = new HashMap<>();
        excludedFetchConfigs.put("queue_entities", excludedFetchConfig);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(JavaType.class))).thenReturn(excludedFetchConfigs);

        byte[] body = "{\"status\":\"SUCCESS\"}".getBytes();
        Mockito.when(httpResponseByteArray.statusCode()).thenReturn(500);
        Mockito.when(httpResponseByteArray.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCallOfArray(fullUrl, fetchConfigTemplate.getHeaders()))
                .thenReturn(httpResponseByteArray);

        byte[] jsonResponse = externalAPIService.externalServiceGetApiCallAsByteArray(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST);

        Assert.assertEquals(body, jsonResponse);
    }

    @Test
    public void test_externalServiceGetApiCallAsByteArray_withFetchErrorAllowedWithoutStatusCodes() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = getAuxEntityRequestDTO();

        String baseUrl = "http://test-host";
        String fullUrl = baseUrl + "/listByAccount?entityType=queue&accountId=" + ACCOUNT_ID;

        List<FetchConfigTemplate> fetchConfigTemplates = FetchConfigContainer.fetchConfigTemplateListForOHSEntityList();
        FetchConfigTemplate fetchConfigTemplate = fetchConfigTemplates.get(0);

        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST,
                baseUrl, new ObjectMapper().readTree("{}"));

        JsonNode uiResponse = new ObjectMapper().createObjectNode();

        ExcludedFetchConfig excludedFetchConfig = new ExcludedFetchConfig();
        excludedFetchConfig.setFetchFor("queue_entities");
        Map<String, ExcludedFetchConfig> excludedFetchConfigs = new HashMap<>();
        excludedFetchConfigs.put("queue_entities", excludedFetchConfig);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(JavaType.class))).thenReturn(excludedFetchConfigs);

        byte[] body = "{\"status\":\"SUCCESS\"}".getBytes();
        Mockito.when(httpResponseByteArray.statusCode()).thenReturn(500);
        Mockito.when(httpResponseByteArray.body()).thenReturn(body);
        PowerMockito.when(HttpUtils.makeGetCallOfArray(fullUrl, fetchConfigTemplate.getHeaders()))
                .thenReturn(httpResponseByteArray);

        byte[] jsonResponse = externalAPIService.externalServiceGetApiCallAsByteArray(fullUrl, serviceUrls, fetchConfigTemplate,
                BaseUtil.getContext(CLIENT_ID, ACCOUNT_ID, auxEntityRequestDTO.getFetchParams()), uiResponse, Environment.TEST);

        Assert.assertEquals(body, jsonResponse);
    }

    private AuxEntityRequestDTO getAuxEntityRequestDTO() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = new AuxEntityRequestDTO();
        auxEntityRequestDTO.setEntityTemplateId(2);
        auxEntityRequestDTO.setType("ENTITY_SEARCH");
        JsonNode fetchParam = new ObjectMapper().readTree("{\"pageNo\":1,\"pageSize\":\"20\" }");
        auxEntityRequestDTO.setFetchParams(fetchParam);
        return auxEntityRequestDTO;
    }
}
