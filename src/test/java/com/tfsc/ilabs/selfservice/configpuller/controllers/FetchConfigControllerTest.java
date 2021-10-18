package com.tfsc.ilabs.selfservice.configpuller.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.controller.ControllerExceptionHandler;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxDataRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxEntityRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.Fetch;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.AuxDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.FetchConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.testcontainer.FetchConfigContainer;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.*;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.ACCOUNT_ID;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.CLIENT_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by prasada on 19-09-2019.
 */

@RunWith(MockitoJUnitRunner.class)
public class FetchConfigControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    FetchConfigController fetchConfigController;
    @Mock
    FetchConfigService fetchConfigService;
    @Mock
    WorkflowService workflowService;
    @Mock
    PageService pageService;
    @Mock
    EntityService entityService;
    @Mock
    org.springframework.core.env.Environment environment;

    private MockMvc mockMvc;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(fetchConfigController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        Mockito.when(environment.getProperty(Mockito.anyString())).thenReturn("aa");

        JsonNode auxData = new ObjectMapper().readTree("{\"testPage\": 5}");
        JsonNode list = new ObjectMapper().readTree("{\"items\":[]}");
        AuxDataResponseDTO auxDataResponseDTO = new AuxDataResponseDTO(auxData, list);
        Mockito.when(fetchConfigService.getAuxData(any(), anyString(), anyString(), any(Environment.class), anyBoolean()))
                .thenReturn(auxDataResponseDTO);

        PageDataResponseDTO pageDataResponseDTO = new PageDataResponseDTO();
        JsonNode pageData = new ObjectMapper().readTree("{\"value\": 6}");
        pageDataResponseDTO.setData(pageData);
        Mockito.when(pageService.getPageDataByWorkflowInstanceAndPageTemplate(anyInt(), anyString()))
                .thenReturn(pageDataResponseDTO);
    }

    @Test
    public void testAuxEntityData_whenEntityListing() throws IOException {
        AuxEntityRequestDTO auxEntityRequestDTO = new AuxEntityRequestDTO();
        auxEntityRequestDTO.setEntityTemplateId(2);
        auxEntityRequestDTO.setType("ENTITY_LISTING");
        auxEntityRequestDTO.setFetchParams(objectMapper.readTree("{\"pageNo\":\"2\",\"pageSize\":\"20\" }"));
        EntityTemplate template = new EntityTemplate();
        template.setId(2);
        template.setFetchFor("entity");
        template.setName("entity");
        Mockito.when(entityService.getTemplate(Mockito.anyInt())).thenReturn(template);
        Mockito.when(fetchConfigService.getAuxEntityData(Mockito.anyString(), Mockito.anyString(), Mockito.any(JsonNode.class), Mockito.any(Environment.class)))
                .thenReturn(FetchConfigContainer.getFetchEntityResponse());
        Mockito.when(environment.getProperty(Mockito.anyString())).thenReturn("aa");
        String response = fetchConfigController.fetchAuxEntityData(auxEntityRequestDTO, CLIENT_ID, ACCOUNT_ID, PublishType.DEFAULT);
        Assert.assertNotNull(response);
    }

    @Test
    public void test_getFetchData() throws Exception {
        ObjectNode retVal = new ObjectMapper().createObjectNode();
        retVal.put("response", "SUCCESS");
        when(fetchConfigService.getDataFromService(anyString(), any(JsonNode.class), any(FetchType.class), any(Environment.class)))
                .thenReturn(retVal);

        MvcResult results = mockMvc.perform(get(Constants.BASE_URI +
                Constants.GET_FETCH_DATA_URI, CLIENT_ID, ACCOUNT_ID, Environment.TEST,
                "testEntity", FetchType.ENTITY_LISTING, "listEntity"))
                .andExpect(status().isOk())
                .andReturn();
        String result = results.getResponse().getContentAsString();

        assertEquals(result, retVal.toString());
    }

    @Test
    public void test_getFormData() throws Exception {
        ObjectNode retVal = (ObjectNode) new ObjectMapper().readTree("{\"id\":0,\"workflowInstanceId\":1,\"pageTemplateId\":\"testPage\",\"data\":{\"value\":6},\"list\":{\"items\":[]}}");

        AuxDataRequestDTO auxDataRequestDTO = getAuxEntityRequestDTO(1);
        MvcResult results = mockMvc.perform(
                post(Constants.BASE_URI
                        +Constants.GET_FETCH_FORMDATA_URI, CLIENT_ID, ACCOUNT_ID, "testPage")
                        .content(auxDataRequestDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = results.getResponse().getContentAsString();

        assertEquals(retVal.toString(), result);
    }

    @Test
    public void test_getFormData_withoutWorkflowInstanceId() throws Exception {
        ObjectNode retVal = (ObjectNode) new ObjectMapper().readTree("{\"id\":0,\"workflowInstanceId\":null,\"pageTemplateId\":\"testPage\",\"data\":5,\"list\":{\"items\":[]}}");

        AuxDataRequestDTO auxDataRequestDTO = getAuxEntityRequestDTO(null);
        MvcResult results = mockMvc.perform(
                post(Constants.BASE_URI
                        +Constants.GET_FETCH_FORMDATA_URI, CLIENT_ID, ACCOUNT_ID, "testPage")
                        .content(auxDataRequestDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = results.getResponse().getContentAsString();

        assertEquals(retVal.toString(), result);
    }

    @Test
    public void test_fetchAuxEntityData_noPostBody() throws Exception {
        ObjectNode retVal = new ObjectMapper().createObjectNode();
        retVal.put("message", MESSAGE_NOT_READABLE_ERROR_MESSAGE);
        MvcResult results = mockMvc.perform(post(Constants.BASE_URI
                 + Constants.GET_FETCH_ENTITY_URI, CLIENT_ID, ACCOUNT_ID, PublishType.DEFAULT))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String result = results.getResponse().getContentAsString();
        assertEquals(result, retVal.toString());
    }

    @Test
    public void test_fetchAuxEntityData_bodyWithIncorrectDataType() throws Exception {
        ObjectNode retVal = new ObjectMapper().createObjectNode();
        retVal.put("message", MESSAGE_NOT_READABLE_ERROR_MESSAGE);

        MvcResult results = mockMvc.perform(post(Constants.BASE_URI
                + Constants.GET_FETCH_ENTITY_URI, CLIENT_ID, ACCOUNT_ID, PublishType.DEFAULT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"entityTemplateId\": \"<s>\", \"fetchParams\": {}, \"type\": \"\"}"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String result = results.getResponse().getContentAsString();
        assertEquals(result, retVal.toString());
    }

    @Test
    public void test_fetchAuxEntityData_incorrectParameterValue() throws Exception {
        ObjectNode retVal = new ObjectMapper().createObjectNode();
        retVal.put("message", METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE+" : publishType");
        MvcResult results = mockMvc.perform(post(Constants.BASE_URI
                + Constants.GET_FETCH_ENTITY_URI, CLIENT_ID, ACCOUNT_ID, "TEST")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"entityTemplateId\": \"1\", \"fetchParams\": {}, \"type\": \"\"}"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String result = results.getResponse().getContentAsString();
        assertEquals(result, retVal.toString());
    }

    @Test
    public void test_fetchAuxEntityData_internalError() throws Exception {
        ObjectNode retVal = new ObjectMapper().createObjectNode();
        retVal.put("message", DEFAULT_ERROR_MESSAGE);

        MvcResult results = mockMvc.perform(post(Constants.BASE_URI
                + Constants.GET_FETCH_ENTITY_URI, CLIENT_ID, ACCOUNT_ID, PublishType.DEFAULT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"entityTemplateId\": \"1\", \"fetchParams\": {}, \"type\": \"\"}"))
                .andExpect(status().is5xxServerError())
                .andReturn();
        String result = results.getResponse().getContentAsString();
        assertEquals(result, retVal.toString());
    }

    private AuxDataRequestDTO getAuxEntityRequestDTO(Integer workflowInstanceId) throws IOException {
        JsonNode page = new ObjectMapper().readTree("{}");
        JsonNode list = new ObjectMapper().readTree("{}");
        Fetch fetch = new Fetch(page, list);
        return new AuxDataRequestDTO(fetch, List.of(), workflowInstanceId, "");
    }
}
