package com.tfsc.ilabs.selfservice.pagetest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.repositories.PageDataRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PageDataRequestDTOTest {

    @Mock
    PageDataRequestDTO pageDataRequestDTO;
    @Mock
    ObjectMapper objectMapper;

    @Test
    public void testRequestDTO() throws IOException {

        String testNode = "{\"testData\":\"data\"}";
        NameLabel nameLabel = new NameLabel("test-default-account-default-queue-aatest","API-Queue");
        JsonNode jsonNode = objectMapper.readTree(testNode);
        List<NameLabel> entities = new ArrayList<>();
        entities.add(nameLabel);
        String testString = "PageDataRequestDTO(" +
                "entities=" + entities +

                ')';
        pageDataRequestDTO = new PageDataRequestDTO(jsonNode,entities);
        Assert.assertEquals(pageDataRequestDTO.getEntities(),entities);
        Assert.assertEquals(testString,pageDataRequestDTO.toString());

    }

}