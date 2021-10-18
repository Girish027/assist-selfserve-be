package com.tfsc.ilabs.selfservice.configpuller.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.ComponentInfo;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.Service;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import com.tfsc.ilabs.selfservice.common.repositories.ServiceUrlRepository;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxEntityRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.AuxDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;
import com.tfsc.ilabs.selfservice.configpuller.repositories.FetchConfigTemplateRepository;
import com.tfsc.ilabs.selfservice.configpuller.services.api.ExternalAPIService;
import com.tfsc.ilabs.selfservice.configpuller.services.impl.FetchConfigServiceImpl;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.testcontainer.FetchConfigContainer;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * @author prasada
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigPullerServicesTest {
    private final String clientId = "test-default";
    private final String accountId = "test-default";
    @InjectMocks
    FetchConfigServiceImpl fetchConfigServiceImpl;
    @Mock
    FetchConfigTemplateRepository fetchConfigTemplateRepository;
    @Mock
    SessionValidator sessionValidator;
    @Mock
    ServiceUrlRepository serviceUrlRepository;
    @Mock
    WorkflowService workflowService;
    @Spy
    ObjectMapper objectMapper;
    @Mock
    WorkflowTemplate mockWTemplate;
    @Mock
    EntityTemplate mockETemplate;
    @Mock
    ExternalAPIService externalAPIService;

    //  @Test
    public void testGetAuxData_DeleteIt() throws IOException {
        AuxDataResponseDTO auxData = new AuxDataResponseDTO();

        JsonNode pageValue = new ObjectMapper().readTree("{\"p1\" : 1, \"p2\" : 2}");
        auxData.setPage(pageValue);

        assertNotNull(auxData.getList());
        assertNotNull(auxData.getPage());
    }

    @Test
    public void testAuxEntityData_ExternalAPI_Pagination_Page_1() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = new AuxEntityRequestDTO();
        auxEntityRequestDTO.setEntityTemplateId(2);
        auxEntityRequestDTO.setType("ENTITY_LISTING");
        JsonNode fetchParam = new ObjectMapper().readTree("{\"pageNo\":1,\"pageSize\":\"20\" }");
        auxEntityRequestDTO.setFetchParams(fetchParam);
        Mockito.when(fetchConfigTemplateRepository.findByFetchForAndTypeInOrderByExecutionOrderAsc(Mockito.anyString(), Mockito.anyList()))
                .thenReturn(FetchConfigContainer.fetchConfigTemplateListForExternalAPIForPagination());
        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST, "https://gorest.co.in", new ObjectMapper().readTree("{}"));
        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), Mockito.any()))
                .thenReturn(serviceUrls);
        Mockito.when(externalAPIService.externalServiceGetApiCall(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(SearchConfig.class)))
                .thenReturn("{\"currentPage\":1,\"perPage\":20,\"totalPage\":1,\"totalCount\":380,\"list\":{\"enum\":[\"test-default-account-default-queue-aatest\",\"test-default-account-default-queue-ac\"],\"enumNames\":[\"API-Queue2\",\"ac\",\"central-services-auto-a0ecf\"]}}");

        String jsonResponse = fetchConfigServiceImpl.getAuxEntityData("queue_entity", "ENTITY_LISTING", BaseUtil.getContext(clientId, accountId, auxEntityRequestDTO.getFetchParams()), Environment.TEST);

        JsonNode node = new ObjectMapper().readTree(jsonResponse);
        Assert.assertNotNull(jsonResponse);
        Assert.assertEquals(fetchParam.get("pageNo"), node.get("currentPage"));
        Assert.assertNotNull(node.get("list"));
    }

    @Test
    public void testAuxEntityData_ComponentAPI_OHS() throws Exception {
        AuxEntityRequestDTO auxEntityRequestDTO = new AuxEntityRequestDTO();
        auxEntityRequestDTO.setEntityTemplateId(2);
        auxEntityRequestDTO.setType("ENTITY_SEARCH");
        JsonNode fetchParam = new ObjectMapper().readTree("{\"pageNo\":1,\"pageSize\":\"20\" }");
        auxEntityRequestDTO.setFetchParams(fetchParam);
        Mockito.when(sessionValidator.getComponentInfoFromTokenMap(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new ComponentInfo());
        Mockito.when(fetchConfigTemplateRepository.findByFetchForAndTypeInOrderByExecutionOrderAsc(Mockito.anyString(), Mockito.anyList()))
                .thenReturn(FetchConfigContainer.fetchConfigTemplateListForOHSEntityList());
        ServiceUrls serviceUrls = new ServiceUrls(new Service("1", "test", "test"), Environment.TEST, "http://qa.api.sv2.247-inc.net", new ObjectMapper().readTree("{}"));
        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), Mockito.any()))
                .thenReturn(serviceUrls);
        Mockito.when(externalAPIService.externalServiceGetApiCall(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(SearchConfig.class)))
                .thenReturn("{\"currentPage\":1,\"perPage\":20,\"totalPage\":1,\"totalCount\":380,\"list\":{\"enum\":[\"test-default-account-default-queue-aatest\",\"test-default-account-default-queue-ac\"],\"enumNames\":[\"API-Queue2\",\"ac\",\"central-services-auto-a0ecf\"]}}");

        String jsonResponse = fetchConfigServiceImpl.getAuxEntityData("queue_entity", "ENTITY_SEARCH", BaseUtil.getContext(clientId, accountId, auxEntityRequestDTO.getFetchParams()), Environment.TEST);
        JsonNode node = new ObjectMapper().readTree(jsonResponse);
        Assert.assertNotNull(node);
        Assert.assertEquals(fetchParam.get("pageNo"), node.get("currentPage"));
        Assert.assertNotNull(node.get("list"));
    }
}
