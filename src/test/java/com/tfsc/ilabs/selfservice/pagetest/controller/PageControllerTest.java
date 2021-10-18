package com.tfsc.ilabs.selfservice.pagetest.controller;

import com.tfsc.ilabs.selfservice.page.controllers.PageController;
import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PageControllerTest {

    @InjectMocks
    PageController pageController = new PageController();

    @Mock
    PageService pageService;

    @Mock
    PageDataRequestDTO pageDataRequestDTO;

    @Mock
    PageDataResponseDTO pageDataResponseDTO;

    private int workflowInstanceId = 5;
    private String pageTemplateId = "updateTeams_p0";

    @Before
    public void init() {
        Mockito.when(pageService.savePageData(pageDataRequestDTO, workflowInstanceId, pageTemplateId)).thenReturn(pageDataResponseDTO);
        Mockito.when(pageService.getPageDataByWorkflowInstanceAndPageTemplate(workflowInstanceId, pageTemplateId)).thenReturn(pageDataResponseDTO);
    }

    @Test
    public void test_savePageData() {
        PageDataResponseDTO result = pageController.savePageData(pageDataRequestDTO, workflowInstanceId, pageTemplateId);
        Assert.assertEquals(result, pageDataResponseDTO);
    }

    @Test
    public void test_getPageData() {
        PageDataResponseDTO result = pageController.getPageData(workflowInstanceId, pageTemplateId);
        Assert.assertEquals(result, pageDataResponseDTO);
    }
}