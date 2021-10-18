package com.tfsc.ilabs.selfservice.pagetest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PageDataResponseDTOTest {
    @Mock
    PageDataResponseDTO pageDataResponseDTO;
    @Mock
    ObjectMapper objectMapper;


    @Test
    public void testResponseDTO() throws IOException {
        String testPageTemplateId = "test-id";
        Integer testWorkflowInstanceId = 5;
        int testId = 1;
        String testNode = "{\"testData\":\"data\"}";
        JsonNode jsonNode = objectMapper.readTree(testNode);
        String testString = "PageDataResponseDTO(" +
                "id=" + testId +
                ", workflowInstanceId=" + testWorkflowInstanceId  +
                ", pageTemplateId=" + testPageTemplateId  +

                ')';
        pageDataResponseDTO = new PageDataResponseDTO(testId,testWorkflowInstanceId,testPageTemplateId,jsonNode);

        Assert.assertEquals(pageDataResponseDTO.getId(),testId);
        Assert.assertEquals(pageDataResponseDTO.getPageTemplateId(),testPageTemplateId);
        Assert.assertEquals(pageDataResponseDTO.getWorkflowInstanceId(),testWorkflowInstanceId);
        Assert.assertEquals(testString,pageDataResponseDTO.toString());
    }
}