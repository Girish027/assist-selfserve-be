package com.tfsc.ilabs.selfservice.entity.services.api;

import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import java.util.List;
import java.util.Optional;

public interface EntityService {

    List<EntityInstance> getEntityInstances(WorkflowInstance workflowInstance);

    void createEntityInstance(String name,String label, WorkflowInstance workflowInstance, EntityTemplate entityTemplate);

    boolean isLockAvailable(String entityName, WorkflowTemplate workflowTemplate, WorkflowInstance currentWorkflowInstance);

    void markEntityInstanceStatusDiscarded(EntityInstance entityInstance);

    void markEntityInstanceStatusCompleted(EntityInstance entityInstance);

    void markEntityInstanceStatusFailed(EntityInstance entityInstance);

    void save(EntityInstance entityInstance);

    void save(WorkflowInstance workflowInstance, List<NameLabel> entityInstances);

    Optional<EntityInstance> getInstance(Integer entityInstanceId);

    EntityTemplate getTemplate(Integer entityTemplateId);
}
