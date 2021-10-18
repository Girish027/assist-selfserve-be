package com.tfsc.ilabs.selfservice.pagetest.dto;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDefinitionDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageTemplateDTO;
import com.tfsc.ilabs.selfservice.testutils.ModelUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getPageDefinition;
import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getPageTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PageTemplateDTOTest {
    @Mock
    PageTemplateDTO pageTemplateDTO;

    @Mock
    PageDefinitionDTO pageDefinitionDTO;

    @Test
    public void test_PageTemplateDTO() throws IOException {
        pageTemplateDTO = new PageTemplateDTO("1", "test-title", "test-description", pageDefinitionDTO);
        Assert.assertEquals("1",pageTemplateDTO.getId());
        Assert.assertEquals("test-title",pageTemplateDTO.getTitle());
        Assert.assertEquals("test-description",pageTemplateDTO.getDescription());
        Assert.assertEquals(pageDefinitionDTO,pageTemplateDTO.getDefinition());
    }
}