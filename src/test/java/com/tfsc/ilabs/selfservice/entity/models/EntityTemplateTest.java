package com.tfsc.ilabs.selfservice.entity.models;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getEntityTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EntityTemplateTest {

    @Mock
    EntityTemplate entityTemplate;

    @Test
    public void testEntityTemplate(){
        entityTemplate = getEntityTemplate();
        String str =  "EntityTemplate(" +
                "id=" + 1 +
                ", name=" + "test-name"  +
                ", fetchFor=" + "test-data" +
                ')';
        Assert.assertEquals((Integer)1, entityTemplate.getId());
        Assert.assertEquals("test-name", entityTemplate.getName());
        Assert.assertEquals("test-data", entityTemplate.getFetchFor());
        Assert.assertEquals(str, entityTemplate.toString());

    }
}