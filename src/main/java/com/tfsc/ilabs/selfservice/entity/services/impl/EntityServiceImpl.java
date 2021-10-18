package com.tfsc.ilabs.selfservice.entity.services.impl;

import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityTemplateRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.repositories.WorkflowInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntityServiceImpl implements EntityService {

    private static final Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityTemplateRepository entityTemplateRepository;

    @Autowired
    private EntityInstanceRepository entityInstanceRepository;

    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Override
    public List<EntityInstance> getEntityInstances(WorkflowInstance workflowInstance) {
        return entityInstanceRepository.findAllByWorkflowInstance(workflowInstance);
    }

    @Override
    public void createEntityInstance(String name, String label, WorkflowInstance workflowInstance, EntityTemplate entityTemplate) {
        EntityInstance entityInstance = new EntityInstance();
        entityInstance.setName(name);
        entityInstance.setLabel(label);
        entityInstance.setWorkflowInstance(workflowInstance);
        entityInstance.setEntityTemplate(entityTemplate);
        entityInstance.setStatus(EntityInstanceStatus.DRAFT);
        entityInstanceRepository.save(entityInstance);
    }

    /**
     * @param entityName
     * @param workflowTemplate
     * @param currentWorkflowInstance if current workflow instance is provided this instance need to be ignored while calculating lock check
     * @return
     */
    @Override
    public boolean isLockAvailable(String entityName, WorkflowTemplate workflowTemplate, WorkflowInstance currentWorkflowInstance) {
        List<EntityInstance> entityInstances = entityInstanceRepository.findAllByName(entityName);
        Optional<EntityInstance> entity = entityInstances.stream()
                .filter(entityInstance -> entityInstance.getWorkflowInstance().getWorkflowTemplate().equals(workflowTemplate))
                .filter(entityInstance -> !entityInstance.getWorkflowInstance().equals(currentWorkflowInstance))
                .filter(EntityInstance::isLocked)
                .filter(entityInstance -> !BaseUtil.isWorkflowInstanceInTerminalState(entityInstance.getWorkflowInstance()))
                .findAny();
        return entity.isEmpty();
    }

    private void updateEntityInstanceStatus(EntityInstance entityInstance, EntityInstanceStatus entityInstanceStatus) {
        if (entityInstance != null) {
            entityInstance.setStatus(entityInstanceStatus);
            entityInstanceRepository.save(entityInstance);
            logger.info("updated entity instance {} status to {}", entityInstance.getId(), entityInstanceStatus);
        }
    }

    @Override
    public void markEntityInstanceStatusDiscarded(EntityInstance entityInstance) {
        updateEntityInstanceStatus(entityInstance, EntityInstanceStatus.DISCARDED);
    }

    @Override
    public void markEntityInstanceStatusCompleted(EntityInstance entityInstance) {
        Environment environment = BaseUtil
                .getEnvironmentToExecuteForWorkflowInstance(entityInstance.getWorkflowInstance());
        EntityInstanceStatus status = Environment.TEST.equals(environment) ? EntityInstanceStatus.PROMOTED_TO_TEST
                : EntityInstanceStatus.PROMOTED_TO_LIVE;
        updateEntityInstanceStatus(entityInstance, status);
    }

    @Override
    public void markEntityInstanceStatusFailed(EntityInstance entityInstance) {
        Environment environment = BaseUtil
                .getEnvironmentToExecuteForWorkflowInstance(entityInstance.getWorkflowInstance());
        EntityInstanceStatus status = Environment.TEST.equals(environment) ? EntityInstanceStatus.TEST_PROMOTION_FAILED
                : EntityInstanceStatus.LIVE_PROMOTION_FAILED;
        updateEntityInstanceStatus(entityInstance, status);
    }

    @Override
    public void save(EntityInstance entityInstance) {
        entityInstanceRepository.save(entityInstance);
    }

    @Override
    public void save(WorkflowInstance workflowInstance, List<NameLabel> entities) {
        WorkflowTemplate workflowTemplate = workflowInstance.getWorkflowTemplate();
        EntityTemplate entityTemplate = workflowTemplate.getEntityTemplate();

        // Delete existing entities for this workflowInstance

        // mark all records of this workflowInstance not in incoming instances as
        // discarded, and those already present as DRAFT
        List<String> entityNames = entities.stream().map(NameLabel::getName).collect(Collectors.toList());
        entityInstanceRepository.findAllByWorkflowInstanceAndEntityTemplate(workflowInstance, entityTemplate)
                .forEach(entityInstance -> {
                    if (entityNames.contains(entityInstance.getName())) {
                        // Then set the status to draft
                        logger.info("In found row {}", entityInstance.getId());
                        entityInstance.setStatus(EntityInstanceStatus.DRAFT);
                    } else {
                        // set the status to discarded
                        logger.info("Not found row {}", entityInstance.getId());
                        entityInstance.setStatus(EntityInstanceStatus.DISCARDED);
                    }
                    logger.info("Updating changes {}", entityInstance.getId());
                    entityInstanceRepository.save(entityInstance);
                });

        // insert all new instances into table
        entities.forEach(entity -> {
            Optional<EntityInstance> optionalEntityInstance = entityInstanceRepository
                    .findByWorkflowInstanceAndEntityTemplateAndName(workflowInstance, entityTemplate, entity.getName());
            if (optionalEntityInstance.isEmpty()) {
                createEntityInstance(entity.getName(), entity.getLabel(), workflowInstance, entityTemplate);
            }
        });
    }

    @Override
    public Optional<EntityInstance> getInstance(Integer entityInstanceId) {
        return entityInstanceRepository.findById(entityInstanceId);
    }

    @Override
    public EntityTemplate getTemplate(Integer entityTemplateId) {
        Optional<EntityTemplate> entityTemplate = entityTemplateRepository.findById(entityTemplateId);
        if (entityTemplate.isPresent()) {
            return entityTemplate.get();
        } else {
            throw new NoSuchResourceException(EntityTemplate.class, entityTemplateId);
        }
    }
}
