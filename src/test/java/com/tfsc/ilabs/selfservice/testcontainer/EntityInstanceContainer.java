package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;


public class EntityInstanceContainer {

    public static EntityInstance createEntityInstance(EntityTemplate entityTemplate, String name, Integer id, String label) {
        EntityInstance entityInstance = new EntityInstance();
        entityInstance.setName(name);
        entityInstance.setEntityTemplate(entityTemplate);
        entityInstance.setId(id);
        entityInstance.setStatus(EntityInstanceStatus.DRAFT);
        entityInstance.setLabel(label);
        entityInstance.setPollingId("testId");
        return entityInstance;
    }

    public static EntityInstance createLockedEntityInstance(EntityTemplate entityTemplate, String name, Integer id,WorkflowInstance workflowInstance) {
        EntityInstance entityInstance = createEntityInstance(entityTemplate, name, id, null);
        entityInstance.setStatus(EntityInstanceStatus.PROMOTED_TO_LIVE);
        entityInstance.setWorkflowInstance(workflowInstance);
        return entityInstance;
    }
    public static EntityInstance createAvailableEntityInstance(EntityTemplate entityTemplate, String name, Integer id,WorkflowInstance workflowInstance) {
        EntityInstance entityInstance = createEntityInstance(entityTemplate, name, id, null);
        entityInstance.setStatus(EntityInstanceStatus.PROMOTED_TO_TEST);
        entityInstance.setWorkflowInstance(workflowInstance);
        return entityInstance;
    }
}
