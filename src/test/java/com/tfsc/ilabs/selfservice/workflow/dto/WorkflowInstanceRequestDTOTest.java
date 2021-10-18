package com.tfsc.ilabs.selfservice.workflow.dto;

import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowInstanceRequestDTOTest {

    @Mock
    WorkflowInstanceRequestDTO workflowInstanceRequestDTO;

    @Test
    public void test_requestDTO() {
        
        NameLabel nameLabel = new NameLabel("test-default-account-default-test-aatest","Test-data");
        List<NameLabel> entities = new ArrayList<>();
        entities.add(nameLabel);
        workflowInstanceRequestDTO = new WorkflowInstanceRequestDTO("testId",entities);
        Assert.assertEquals(entities, workflowInstanceRequestDTO.getEntities());
        Assert.assertEquals("testId", workflowInstanceRequestDTO.getPageId());
    }
    
}
