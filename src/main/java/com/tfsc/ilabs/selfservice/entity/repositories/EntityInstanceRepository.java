package com.tfsc.ilabs.selfservice.entity.repositories;

import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EntityInstanceRepository extends JpaRepository<EntityInstance, Integer> {

    List<EntityInstance> findAllByWorkflowInstance(WorkflowInstance workflowInstance);

    List<EntityInstance> findAllByEntityTemplate(EntityTemplate entityTemplate);

    List<EntityInstance> findAllByName(String name);
    
    List<EntityInstance> findAllByNameAndEntityTemplateOrderByIdDesc(String name, EntityTemplate entityTemplate);

    List<EntityInstance> findAllByWorkflowInstanceAndEntityTemplate(WorkflowInstance workflowInstance,
            EntityTemplate entityTemplate);

    Optional<EntityInstance> findByWorkflowInstanceAndEntityTemplateAndName(WorkflowInstance workflowInstance,
            EntityTemplate entityTemplate, String name);
}
