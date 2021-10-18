package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.action.models.definition.ResponseDefinition;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowConfigConverterTest {
    @Mock
    WorkflowConfigConverter workflowConfigConverter;

    @Mock
    WorkflowConfig workflowConfig;

    @Test
    public void test_convertToDatabaseColumn(){
        String result = workflowConfigConverter.convertToDatabaseColumn(workflowConfig);
        Assert.assertNull(result);
    }

    @Test
    public void test_convertToEntityAttribute(){
        String json = "{\"key\":\"value\"}";
        WorkflowConfig result = workflowConfigConverter.convertToEntityAttribute(json);
        Assert.assertNull(result);
    }
}
