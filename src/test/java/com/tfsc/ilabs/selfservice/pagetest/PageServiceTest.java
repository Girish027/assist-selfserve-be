package com.tfsc.ilabs.selfservice.pagetest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
import com.tfsc.ilabs.selfservice.page.repositories.PageTemplateRepository;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.page.service.impl.PageServiceImpl;
import com.tfsc.ilabs.selfservice.testcontainer.PageDataContainer;
import com.tfsc.ilabs.selfservice.testcontainer.PageTemplateContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowTemplateContainer;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;

@RunWith(MockitoJUnitRunner.class)
public class PageServiceTest {

    @InjectMocks
    PageService pageService = new PageServiceImpl();

    @Mock
    EntityService entityService;

    @Mock
    WorkflowService workflowService;

    @Mock
    PageTemplateRepository pageTemplateRepository;

    @Mock
    PageDataRepository pageDataRepository;

    @Before
    public void setUp() {

    }


    @Test
    public void testGetAllPagesForWorkflow() {
        Mockito.when(workflowService.getAllPagesOfWorkflow(Mockito.any(WorkflowTemplate.class)))
                .thenReturn(PageTemplateContainer.getPageTemplateList());
        List<PageTemplate> pageTemplates = pageService.getAllPagesOfWorkflow(WorkflowTemplateContainer.getSingletonWorkflow("a.b.c"));
        assertEquals(2, pageTemplates.size());
        assertEquals("Page1", pageTemplates.get(0).getTitle());
        assertEquals("Page2", pageTemplates.get(1).getTitle());
    }

    @Test
    public void testFindById() {
        String id = "page1";
        Mockito.when(pageTemplateRepository.findById(id)).thenReturn(PageTemplateContainer.getPageTemplateById(id));
        Optional<PageTemplate> pageTemplateOptional = pageService.findById(id);
        assertEquals(true, pageTemplateOptional.isPresent());
        PageTemplate pageTemplate = pageTemplateOptional.get();
        assertEquals(id, pageTemplate.getId());
    }

    @Test
    public void testPageDataListByInstances() {
        String pageTemplateId = "page1";
        Optional<PageTemplate> pageTemplateOptional = PageTemplateContainer.getPageTemplateById(pageTemplateId);
        assertEquals(true, pageTemplateOptional.isPresent());
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        try {
            Mockito.when(pageDataRepository.findAllByWorkflowInstance(workflowInstance))
                    .thenReturn(PageDataContainer.getPageDataList(workflowInstance));
            List<PageData> pageDataList = pageService.getPageDataByWorkflowInstace(workflowInstance);
            assertEquals(2, pageDataList.size());
            JsonNode jsonNode1 = pageDataList.get(0).getData();
            JsonNode jsonNode2 = pageDataList.get(1).getData();
            assertEquals(1, jsonNode1.get("a").asInt());
            assertEquals(3, jsonNode2.get("c").asInt());
        } catch (Exception ex) {
            fail("failed due to " + ex.getMessage());
        }
    }

    @Test
    public void testPageDataByWorkflowInstanceAndPageTemplate() {
        String pageTemplateId1 = "page1";
        Integer workflowInstanceId1 = 1;
        String pageTemplateId2 = "page2";
        Integer workflowInstanceId2 = 2;
        try {
            // Checking not null pageData
            PageData pageData = PageDataContainer.getPageDataList(WorkflowInstanceContainer.getWorkflowInstance()).get(0);
            Mockito.when(pageDataRepository.findByWorkflowInstanceIdAndPageTemplateId(workflowInstanceId1,
                    pageTemplateId1)).thenReturn(pageData);
            PageDataResponseDTO pageDataDTO1 = pageService.getPageDataByWorkflowInstanceAndPageTemplate(workflowInstanceId1,
                    pageTemplateId1);
            assertEquals(pageData.getData(), pageDataDTO1.getData());
            assertEquals(pageData.getPageTemplate().getId(), pageDataDTO1.getPageTemplateId());
            Mockito.when(pageDataRepository.findByWorkflowInstanceIdAndPageTemplateId(workflowInstanceId2,
                    pageTemplateId2)).thenReturn(null);
            pageService.getPageDataByWorkflowInstanceAndPageTemplate(workflowInstanceId2,
                    pageTemplateId2);

            fail("should throw no such resource exception");

        } catch (IOException ex) {
            fail("failed due to " + ex.getMessage());
        } catch (NoSuchResourceException e) {
            assertNotNull(e.getPropertyErrors());
            assertTrue(e.getPropertyErrors().containsKey("message"));
            assertEquals("Page data not found for the workflow instance", e.getPropertyErrors().get("message"));
            assertTrue(e.getPropertyErrors().containsKey("data"));
            assertEquals(workflowInstanceId2, e.getPropertyErrors().get("data"));
        }
    }

    @Test
    public void testSavePageData() {
        try {
            String pageTemplateId1 = "page1";
            Integer workflowInstanceId1 = 1;
            List<PageData> pageDataList = PageDataContainer.getPageDataList(WorkflowInstanceContainer.getWorkflowInstance());
            PageData pageData1 = pageDataList.get(0);
            PageDataResponseDTO pageDataResponseDTO = new PageDataResponseDTO();
            pageDataResponseDTO.setId(1);
            pageDataResponseDTO.setPageTemplateId(pageTemplateId1);
            pageDataResponseDTO.setWorkflowInstanceId(workflowInstanceId1);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readValue("{\"k1\":\"v1\"}", JsonNode.class);
            pageDataResponseDTO.setData(data);
            
            Optional<WorkflowInstance> optionalWorkflowInstance = Optional.of(WorkflowInstanceContainer.getWorkflowInstance());
            Mockito.when(workflowService.getInstance(Mockito.anyInt())).thenReturn(optionalWorkflowInstance);
            
            Optional<PageTemplate> pageTemplate = Optional.of(pageData1.getPageTemplate());
            Mockito.when(pageTemplateRepository.findById(Mockito.anyString())).thenReturn(pageTemplate);
            
            Mockito.when(pageDataRepository.save(Mockito.any(PageData.class))).thenReturn(pageData1);
            
            List<NameLabel> entities = new ArrayList<NameLabel>();
            PageDataRequestDTO pageDataReq = new PageDataRequestDTO();
            pageDataReq.setData(data);
            pageDataReq.setEntities(entities);
            PageDataResponseDTO pageDataResponseDTOOutput = pageService.savePageData(pageDataReq, workflowInstanceId1, pageTemplateId1);
            
            assertEquals(pageDataResponseDTO.getPageTemplateId(), pageDataResponseDTOOutput.getPageTemplateId());
        } catch (Exception ex) {
            fail("failed due to " + ex.getMessage());
        }
    }
    
    @Test
    public void testSaveUpdatePageData() {
        try {
            String pageTemplateId1 = "page1";
            Integer workflowInstanceId1 = 1;
            List<PageData> pageDataList = PageDataContainer.getPageDataList(WorkflowInstanceContainer.getWorkflowInstance());
            PageData pageData1 = pageDataList.get(0);
            PageDataResponseDTO pageDataResponseDTO = new PageDataResponseDTO();
            pageDataResponseDTO.setId(1);
            pageDataResponseDTO.setPageTemplateId(pageTemplateId1);
            pageDataResponseDTO.setWorkflowInstanceId(workflowInstanceId1);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readValue("{\"k1\":\"v1\"}", JsonNode.class);
            pageDataResponseDTO.setData(data);
            
            Optional<WorkflowInstance> optionalWorkflowInstance = Optional.of(WorkflowInstanceContainer.getWorkflowInstance());
            Mockito.when(workflowService.getInstance(Mockito.anyInt())).thenReturn(optionalWorkflowInstance);
            
            Mockito.when(pageDataRepository.findByWorkflowInstanceIdAndPageTemplateId(Mockito.anyInt(), Mockito.anyString())).thenReturn(pageData1);
            
            Mockito.when(pageDataRepository.save(Mockito.any(PageData.class))).thenReturn(pageData1);
            
            List<NameLabel> entities = new ArrayList<NameLabel>();
            PageDataRequestDTO pageDataReq = new PageDataRequestDTO();
            pageDataReq.setData(data);
            pageDataReq.setEntities(entities);
            PageDataResponseDTO pageDataResponseDTOOutput = pageService.savePageData(pageDataReq, workflowInstanceId1, pageTemplateId1);
            
            assertEquals(pageDataResponseDTO.getPageTemplateId(), pageDataResponseDTOOutput.getPageTemplateId());
            assertEquals(pageDataResponseDTO.getData(), pageDataResponseDTOOutput.getData());
        } catch (Exception ex) {
            fail("failed due to " + ex.getMessage());
        }
    }


}
