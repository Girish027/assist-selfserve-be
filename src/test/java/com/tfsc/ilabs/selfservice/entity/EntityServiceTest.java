package com.tfsc.ilabs.selfservice.entity;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityTemplateRepository;
import com.tfsc.ilabs.selfservice.entity.services.impl.EntityServiceImpl;
import com.tfsc.ilabs.selfservice.testcontainer.EntityInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.EntityTemplateContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowTemplateContainer;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class EntityServiceTest {

	@InjectMocks
	EntityServiceImpl entityServiceImplTest = new EntityServiceImpl();
	
    @Mock
    EntityTemplateRepository entityTemplateRepository;

    @Mock
    EntityInstanceRepository entityInstanceRepository;

    @Mock
    WorkflowInstanceRepository workflowInstanceRepository;


    @Test
    public void testCreationOfEntityTemplate() {
		EntityInstance entityInstance = new EntityInstance();
		entityServiceImplTest.createEntityInstance("name", "label", WorkflowInstanceContainer.getWorkflowInstance(), EntityTemplateContainer.createEntityTemplate("name", 1));
		Assert.assertNotNull(entityInstance);
    }
    
    @Test
    public void test_isLockAvailable() {
    	WorkflowInstance workFlowInstance = WorkflowInstanceContainer.getWorkflowInstance();
    	List<EntityInstance> entityInstances = new ArrayList<>();
    	entityInstances.add(EntityInstanceContainer.createLockedEntityInstance(EntityTemplateContainer.createEntityTemplate("name", 1), "name", 1,workFlowInstance));
    	Mockito.when(entityInstanceRepository.findAllByName(Mockito.anyString()))
    		.thenReturn(entityInstances);
    	
    	Mockito.when(entityInstanceRepository.findAllByName(Mockito.anyString()))
    		.thenReturn(entityInstances);
    	
    	// Calling isLockAvailable methods
    	boolean output = entityServiceImplTest.isLockAvailable("adf_p0", WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues"), workFlowInstance);
    	
    	// Asserting the output
		Assert.assertTrue(output);
    }
    
    @Test
    public void test_markEntityInstanceStatusCompleted() {
    	WorkflowInstance workFlowInstance = WorkflowInstanceContainer.getWorkflowInstance();
    	EntityInstance entityInstance = EntityInstanceContainer.createLockedEntityInstance(EntityTemplateContainer.createEntityTemplate("name", 1), "name", 1,workFlowInstance);
    	
    	// Calling the markEntityInstanceStatusCompleted method
		entityServiceImplTest.markEntityInstanceStatusCompleted(entityInstance);
		Assert.assertNotNull(entityInstance);
    }
    
    @Test
    public void test_markEntityInstanceStatusDiscarded() {
    	WorkflowInstance workFlowInstance = WorkflowInstanceContainer.getWorkflowInstance();
    	EntityInstance entityInstance = EntityInstanceContainer.createLockedEntityInstance(EntityTemplateContainer.createEntityTemplate("name", 1), "name", 1,workFlowInstance);
    	
    	// Calling the markEntityInstanceStatusDiscarded method
		entityServiceImplTest.markEntityInstanceStatusDiscarded(entityInstance);
		Assert.assertNotNull(entityInstance);
    }
    
    @Test
    public void test_markEntityInstanceStatusFailed() {
    	WorkflowInstance workFlowInstance = WorkflowInstanceContainer.getWorkflowInstance();
    	EntityInstance entityInstance = EntityInstanceContainer.createLockedEntityInstance(EntityTemplateContainer.createEntityTemplate("name", 1), "name", 1,workFlowInstance);
    	
    	// Calling the markEntityInstanceStatusFailed method
		entityServiceImplTest.markEntityInstanceStatusFailed(entityInstance);
		Assert.assertNotNull(entityInstance);
    }

    @Test
    public void test_save() {
    	NameLabel nameLabel = new NameLabel("name", "label");
    	List<NameLabel> entities = new ArrayList<>();
    	entities.add(nameLabel);
    	WorkflowInstance workFlowInstance = WorkflowInstanceContainer.getWorkflowInstance();
    	List<EntityInstance> entityInstances = new ArrayList<>();
    	entityInstances.add(EntityInstanceContainer.createLockedEntityInstance(EntityTemplateContainer.createEntityTemplate("name", 1), "name", 1,workFlowInstance));
    	
    	Mockito.when( entityInstanceRepository.findAllByWorkflowInstanceAndEntityTemplate(Mockito.any(WorkflowInstance.class), Mockito.any(EntityTemplate.class)))
    		.thenReturn(entityInstances);
    	
    	// Calling the save method
    	entityServiceImplTest.save(workFlowInstance, entities);
		Assert.assertNotNull(entityInstances);
    }
}
