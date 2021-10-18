package com.tfsc.ilabs.selfservice.pagetest.models;

import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getPageDefinition;
import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getPageTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PageTemplateTest {

    @Mock
    PageTemplate pageTemplate;

    @Test
    public void testPageTemplate() throws IOException {
        pageTemplate = getPageTemplate();
        Assert.assertEquals(pageTemplate.getId(),"1");
        Assert.assertEquals(pageTemplate.getDescription(),"test-description");
        Assert.assertEquals(pageTemplate.getTitle(),"test-title");
    }
}