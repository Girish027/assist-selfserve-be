package com.tfsc.ilabs.selfservice.entity.models;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.tfsc.ilabs.selfservice.testutils.ModelUtils.getEntityTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EntityInstanceTest {

    @Mock
    EntityInstance entityInstance;

    @Mock
    WorkflowInstance workflowInstance;

    @Test
    public void test_entityInstanceModel(){
        String str = "EntityInstance(" +
                "id=" + 1 +
                ", name=" + "test-name"  +
                ", label=" + "test-label"  +
                ", workflowInstance=" + workflowInstance +
                ", entityTemplate=" + getEntityTemplate() +
                ", status=" + EntityInstanceStatus.DRAFT +
                ", pollingId=" + null +
                ')';
        entityInstance = new EntityInstance("test-name", "test-label", workflowInstance, getEntityTemplate(), EntityInstanceStatus.DRAFT, null);
        entityInstance.setId(1);
        Assert.assertEquals("test-name", entityInstance.getName());
        Assert.assertEquals("test-label", entityInstance.getLabel());
        Assert.assertEquals(workflowInstance, entityInstance.getWorkflowInstance());
        Assert.assertEquals(EntityInstanceStatus.DRAFT, entityInstance.getStatus());
        Assert.assertEquals(str, entityInstance.toString());
        Assert.assertEquals(true, entityInstance.isLocked());
    }

}