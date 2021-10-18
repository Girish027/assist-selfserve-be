package com.tfsc.ilabs.selfservice.workflow.services.impl;

import com.tfsc.ilabs.selfservice.action.repositories.ActionExecutionMonitorRepository;
import com.tfsc.ilabs.selfservice.common.dto.UserAudit;
import com.tfsc.ilabs.selfservice.common.exception.*;
import com.tfsc.ilabs.selfservice.common.models.*;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.CacheUtils;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.UserAuditLogUtils;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.dto.response.PageTemplateDTO;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.*;
import com.tfsc.ilabs.selfservice.workflow.models.*;
import com.tfsc.ilabs.selfservice.workflow.repositories.*;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import com.tfsc.ilabs.selfservice.workflow.services.api.ClientsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ravi.b on 07-06-2019.
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);
    @Autowired
    EntityInstanceRepository entityInstanceRepository;
    @Value("${workflow_instance.timeout}")
    private Integer configuredTimeout;
    @Autowired
    private WorkflowTemplateRepository workflowTemplateRepository;
    @Autowired
    private WorkflowPageRepository workflowPageRepository;
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private NodeGroupRepository nodeGroupRepository;
    @Autowired
    private ActionExecutionMonitorRepository actionExecutionMonitorRepository;
    @Autowired
    private DBConfigService dbConfigService;
    @Autowired
    private ClientsConfigService clientConfigService;

    private Integer getNodeIdFromWorkflowId(String workflowId){
        Node node = nodeRepository.findByNodeIdAndNodeType(workflowId, NodeType.WORKFLOW);
        return node.getId();
    }

    @Override
    public Map<String, WorkflowTemplateDTO> getActivities(ScopeType scopeType, String scopeId) {
        //adding all activities
        List<WorkflowTemplate> workflowTemplates = workflowTemplateRepository.findAll();
        if(!StringUtils.isEmpty(scopeId) && scopeType != null)
            workflowTemplates = clientConfigService.filterWorkflowTemplatesByConfig(scopeType, scopeId, workflowTemplates, ConfigNames.BLACKLISTED_ACTIVITIES);

        Map<String, WorkflowTemplateDTO> wTemplateDTOMap = new HashMap<>();
        workflowTemplates.forEach(workflowTemplate -> {
            List<PageTemplate> pageTemplates = getAllPagesOfWorkflow(workflowTemplate);
            Set<DefinitionType> pageTypes = pageTemplates.stream()
                    .map(PageTemplate::getType)
                    .collect(Collectors.toSet());
            WorkflowTemplateDTO workflowTemplateDTO = workflowTemplate.toWorkflowTemlateDTO(pageTypes);
            wTemplateDTOMap.put(workflowTemplateDTO.getId(), workflowTemplateDTO);
        });
        return wTemplateDTOMap;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName}")
    public Map<String, WorkflowTemplateDTO> getBookmarks() {
        //adding all bookmarks
        return bookmarkRepository.findAll().stream()
                .map(Bookmark::toDTO)
                .collect(Collectors.toMap(BookmarkDTO::getId, Function.identity()));
    }

    @Override
    public WorkFlowMetaDTO getWorkflowMetaData(String workflowId) {
        WorkFlowMetaDTO workflowMetaDTO = new WorkFlowMetaDTO();
        Optional<WorkflowTemplate> optionalWorkflowTemplate = workflowTemplateRepository.findById(workflowId);
        if (optionalWorkflowTemplate.isPresent()) {
            WorkflowTemplate workflowTemplate = optionalWorkflowTemplate.get();
            logger.info("Successfully got workflow template for id: {}", workflowId);
            ActivityDTO activityDTO = new ActivityDTO(workflowTemplate);
            workflowMetaDTO.setWorkflow(activityDTO);
            Set<DefinitionType> pageTypes = getAllPagesOfWorkflow(workflowTemplate).stream().map(PageTemplate::getType)
                    .collect(Collectors.toSet());
            workflowMetaDTO.setPageTypes(pageTypes);
        } else {
            logger.error("Workflow template not found with id: {}", workflowId);
            throw new NoSuchResourceException(
                    new ErrorObject(ErrorCodes.WORK_FLOW_TEMPLATE_NOT_FOUND.getDescription(), workflowId));
        }

        return workflowMetaDTO;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName, #workflowId, #type}")
    public WorkflowDefinitionDTO getWorkflowDefintion(String workflowId, String type) {
        if (!type.isEmpty() && DefinitionType.contains(type)) {
            type = type.toUpperCase();

            Optional<WorkflowTemplate> optionalWorkflowTemplate = workflowTemplateRepository.findById(workflowId);

            if (optionalWorkflowTemplate.isPresent()) {
                WorkflowTemplate workflowTemplate = optionalWorkflowTemplate.get();

                WorkflowDefinitionDTO workflowDefinitionDTO = new WorkflowDefinitionDTO();
                Map<String, WorkflowEntity> schemaEntities = new HashMap<>();

                List<String> workflowSchema = new ArrayList<>();

                workflowPageRepository.findAllByWorkflowTemplateAndPageTemplateType(workflowTemplate, DefinitionType.valueOf(type))
                        .stream().sorted((wfp1, wfp2) -> wfp1.getPageOrder() - wfp2.getPageOrder())
                        .forEach(wfp -> {
                            String sectionName = wfp.getSectionName();
                            PageTemplate pageTemplate = wfp.getPageTemplate();
                            if (StringUtils.isEmpty(sectionName)) {
                                workflowSchema.add(pageTemplate.getId());
                                schemaEntities.put(pageTemplate.getId(), new WorkflowEntity(wfp.getPageOrder() == 0
                                        ? WorkflowEntityType.HOME_PAGE : WorkflowEntityType.PAGE,
                                        pageTemplate.getTitle()));
                            } else {
                                WorkflowEntity workflowSectionEntity = schemaEntities.get(sectionName);
                                if (workflowSectionEntity == null) {
                                    workflowSectionEntity = new WorkflowEntity(WorkflowEntityType.SECTION, sectionName);

                                    List<String> pages = new ArrayList<>();
                                    pages.add(pageTemplate.getId());
                                    workflowSectionEntity.setPages(pages);

                                    schemaEntities.put(sectionName, workflowSectionEntity);
                                    workflowSchema.add(sectionName);
                                } else {
                                    workflowSectionEntity.getPages().add(pageTemplate.getId());
                                }
                            }
                        });

                workflowDefinitionDTO.setWorkflowSchema(workflowSchema);

                workflowDefinitionDTO.setSchemaEntities(schemaEntities);

                Map<String, PageTemplateDTO> pages = getAllPagesOfWorkflowByType(workflowTemplate, type).stream()
                        .collect(Collectors.toMap(PageTemplate::getId, PageTemplateDTO::new));
                workflowDefinitionDTO.setPages(pages);

                return workflowDefinitionDTO;
            } else {
                logger.error("Workflow template not found with id: {}", workflowId);
                throw new NoSuchResourceException(
                        new ErrorObject(ErrorCodes.WORK_FLOW_TEMPLATE_NOT_FOUND.getDescription(), workflowId));
            }
        } else {
            logger.error("Invalid page type provided: {}", type);
            throw new InvalidResourceException(new ErrorObject(ErrorCodes.INVALID_PAGE_TYPE.getDescription(), type));

        }
    }

    @Override
    public List<WorkflowInstanceDTO> refreshWorkflowInstances(String clientId, String accountId) {
        CacheUtils.evictAllApiCacheTimed(dbConfigService, Constants.CACHE_EXPIRE_CONFIG);
        return getWorkflowInstances(clientId, accountId);
    }

    @Override
    public List<WorkflowInstanceDTO> pollWorkflowInstances(String clientId, String accountId) {
        CacheUtils.evictAllApiCacheTimed(dbConfigService, Constants.CACHE_EXPIRE_CONFIG);
        return getWorkflowInstancesForPolling(clientId, accountId).stream()
                .map(WorkflowInstance::toDTO)
                .map(workflowInstanceDTO -> {
                    String workflowId = workflowInstanceDTO.getWorkflowId();
                    workflowInstanceDTO.setNodeId(getNodeIdFromWorkflowId(workflowId));
                    return workflowInstanceDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceDTO> getWorkflowInstances(String clientId, String accountId) {
        Boolean isEvictCacheRequiredOnInstanceApi = dbConfigService.findByCode(Constants.CACHE_EVICT_ON_INSTANCE_API, Boolean.class);
        if (isEvictCacheRequiredOnInstanceApi != null && isEvictCacheRequiredOnInstanceApi) {
            CacheUtils.evictAllApiCacheTimed(dbConfigService, Constants.CACHE_EXPIRE_CONFIG);
        }
        return getWorkflowInstancesForClient(clientId, accountId).stream()
                .map(WorkflowInstance::toDTO)
                .map(workflowInstanceDTO -> {
                    String workflowId = workflowInstanceDTO.getWorkflowId();
                    workflowInstanceDTO.setNodeId(getNodeIdFromWorkflowId(workflowId));
                    return workflowInstanceDTO;
                })
                .collect(Collectors.toList());
    }

    private Stream<WorkflowInstanceDTO> getWorkflowInstanceDTOStream(List<WorkflowInstance> workflowInstances) {
        return workflowInstances.stream().map(WorkflowInstance::toDTO);
    }

    @Override
    public Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> getWorkflowInstancesOrderByStatus(String clientId,
                                                                                                    String accountId, String workflowId) {
        return getWorkflowInstanceDTOStream(getWorkflowInstancesForClient(clientId, accountId, workflowId))
                .collect(Collectors.groupingBy(WorkflowInstanceDTO::getStatus));
    }

    @Override
    public Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> getWorkflowInstancesOrderByStatus(String clientId,
                                                                                                    String accountId) {
        return getWorkflowInstanceDTOStream(getWorkflowInstancesForClient(clientId, accountId))
                .collect(Collectors.groupingBy(WorkflowInstanceDTO::getStatus));
    }

    private List<WorkflowInstance> getWorkflowInstancesForClient(String clientId, String accountId, String workflowId) {
        return workflowInstanceRepository.findByWorkflowTemplateAndClientIdAndAccountId(
                workflowTemplateRepository.findById(workflowId), clientId, accountId);
    }

    private List<WorkflowInstance> getWorkflowInstancesForClient(String clientId, String accountId) {
        Integer maxTime = dbConfigService.findByCode(Constants.CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT, Integer.class);
        Date lastUpdatedOn = Date.from(Instant.now().minus(maxTime, ChronoUnit.DAYS));

        return workflowInstanceRepository
                .findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(clientId, accountId, false, lastUpdatedOn);
    }

    private List<WorkflowInstance> getWorkflowInstancesForPolling(String clientId, String accountId) {
        int maxTime = dbConfigService.findByCode(Constants.CONFIG_KEY_MAX_INSTANCES_FOR_REFRESH_MINUTES_LIMIT, Integer.class);
        Date lastUpdatedOn = Date.from(Instant.now().minus(maxTime, ChronoUnit.MINUTES));
        return workflowInstanceRepository
                .findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(clientId, accountId, false, lastUpdatedOn);
    }

    @Override
    public List<WorkflowInstanceDTO> getWorkflowInstances(String clientId, String accountId, String workflowId) {
        return getWorkflowInstanceDTOStream(getWorkflowInstancesForClient(clientId, accountId, workflowId))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceDTO> getWorkflowInstancesForStatus(String clientId, String accountId, String workflowId,
                                                                   String status) {
        return getWorkflowInstances(clientId, accountId, workflowId).stream()
                .filter(workflowInstance -> workflowInstance.getStatus().name().equals(status)).collect(Collectors.toList());
    }

    @Override
    public Optional<WorkflowInstance> getInstance(Integer workflowInstanceId) {
        return workflowInstanceRepository.findById(workflowInstanceId);
    }

    @Override
    public List<PageTemplate> getAllPagesOfWorkflow(WorkflowTemplate workflowTemplate) {
        return workflowPageRepository.findAllByWorkflowTemplate(workflowTemplate).stream()
                .map(WorkflowPage::getPageTemplate).collect(Collectors.toList());
    }

    @Override
    public List<PageTemplate> getAllPagesOfWorkflowByType(WorkflowTemplate workflowTemplate, String type) {
        return workflowPageRepository.findAllByWorkflowTemplateAndPageTemplateType(workflowTemplate, DefinitionType.valueOf(type))
                .stream().map(WorkflowPage::getPageTemplate).collect(Collectors.toList());
    }

    private void updateWorkflowInstanceStatus(WorkflowInstance workflowInstance,
                                              WorkflowInstanceStatus workflowInstanceStatus) {
        if (workflowInstance != null) {
            workflowInstance.setStatus(workflowInstanceStatus);
            workflowInstanceRepository.save(workflowInstance);
            logger.info("updated workflow instance {} status to {}", workflowInstance.getId(), workflowInstanceStatus);
        }
    }

    @Override
    @Transactional
    public void markWorkflowInstanceStatusRunning(WorkflowInstance workflowInstance) {
        Environment environment = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
        WorkflowInstanceStatus status = Environment.TEST.equals(environment) ? WorkflowInstanceStatus.PROMOTING_TO_TEST
                : WorkflowInstanceStatus.PROMOTING_TO_LIVE;
        updateWorkflowInstanceStatus(workflowInstance, status);
    }

    @Override
    @Transactional
    public boolean markWorkflowInstanceStatusDiscarded(WorkflowInstance workflowInstance) {
        if (workflowInstance.getStatus() == WorkflowInstanceStatus.PROMOTING_TO_LIVE
                || workflowInstance.getStatus() == WorkflowInstanceStatus.PROMOTING_TO_TEST) {
            return false;
        }
        updateWorkflowInstanceStatus(workflowInstance, WorkflowInstanceStatus.DISCARDED);
        return true;
    }

    @Override
    @Transactional
    public void markWorkflowInstanceStatusCompleted(WorkflowInstance workflowInstance) {
        Environment environment = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
        WorkflowInstanceStatus status = Environment.TEST.equals(environment) ? WorkflowInstanceStatus.PROMOTED_TO_TEST
                : WorkflowInstanceStatus.PROMOTED_TO_LIVE;
        updateWorkflowInstanceStatus(workflowInstance, status);
    }

    @Override
    @Transactional
    public void markWorkflowInstanceStatusFailed(WorkflowInstance workflowInstance) {
        Environment environment = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
        WorkflowInstanceStatus status = Environment.TEST.equals(environment)
                ? WorkflowInstanceStatus.TEST_PROMOTION_FAILED : WorkflowInstanceStatus.LIVE_PROMOTION_FAILED;
        updateWorkflowInstanceStatus(workflowInstance, status);
    }

    @Transactional
    @Override
    public void deleteWorkflowInstance(Integer workflowInstanceId, Environment currEnv) {

        // Check workflowInstance status
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowInstanceRepository.findById(workflowInstanceId);

        optionalWorkflowInstance.ifPresentOrElse(workflowInstance -> {
            WorkflowInstanceStatus workflowInstanceStatus = workflowInstance.getStatus();
            if ((Environment.DRAFT.equals(currEnv) && Constants.DRAFT_STATUS.contains(workflowInstanceStatus))
                    || (Environment.TEST.equals(currEnv) && Constants.TEST_STATUS.contains(workflowInstanceStatus))) {
                switch (workflowInstanceStatus) {
                    case DRAFT_EDIT:
                        throwResourceException("Locked! Someone is editing");
                        break;
                    case PROMOTING_TO_LIVE:
                    case PROMOTING_TO_TEST:
                        throwResourceException("Promotion in progress!");
                        break;
                    case PROMOTED_TO_LIVE:
                    case LIVE_PROMOTION_FAILED:
                    case DRAFT_SAVE:
                    case PROMOTED_TO_TEST:
                    case TEST_PROMOTION_FAILED:
                        // If it is promoted partially/completely or in DRAFT, always soft-delete
                        markAsDiscarded(workflowInstance);
                        updateParentOnDiscard(workflowInstance);
                        break;
                    case DISCARDED:
                    default:
                        break;
                }
            }
        }, () -> {
            // workflowInstance is not present
            throw new NoSuchResourceException(WorkflowInstance.class, workflowInstanceId);
        });
    }

    private void updateParentOnDiscard(WorkflowInstance workflowInstance) {
        String parentInstanceId = workflowInstance.getPendingParentId();
        if (parentInstanceId == null) return;

        Optional<WorkflowInstance> optionalParentInstance = workflowInstanceRepository.findById(Integer.parseInt(parentInstanceId));
        optionalParentInstance.ifPresent(parentInstance -> {
            if (!BaseUtil.isWorkflowInstanceInTerminalState(parentInstance)) {
                parentInstance.setHidden(false);
                workflowInstanceRepository.save(parentInstance);
            }
        });
    }

    private void markAsDiscarded(WorkflowInstance workflowInstance) {
        List<EntityInstance> entities = entityService.getEntityInstances(workflowInstance);
        DefinitionType entityAction = workflowInstance.getType();

        // Filter out entities promoted to test and mark as discarded
        entities.forEach(entity -> entityService.markEntityInstanceStatusDiscarded(entity));
        markWorkflowInstanceStatusDiscarded(workflowInstance);

        //Logs for discard
        UserAudit userAudit = UserAuditLogUtils.constructPublishUserAudit(entities.get(0),
                SecurityContextUtil.getAuthenticationToken().getUserId(), workflowInstance.getClientId(),
                workflowInstance.getClientId(), workflowInstance.getWorkflowTemplate().getTitle(), entityAction);
        userAudit.setAction(Constants.ACTION_DISCARD);
        UserAuditLogUtils.logUserAudit(userAudit);
    }

    private void throwResourceException(String s) {
        ResourceException resourceLockedException = new ResourceException(
                new ErrorObject(s));
        resourceLockedException.setHttpStatus(HttpStatus.LOCKED);
        throw resourceLockedException;
    }

    private void setWorkflowInstanceLock(WorkflowInstance workflowInstance, String userId) {
        // Set the last_modified_time and set the last_modified_by to this user
        updateWorkflowInstanceLock(workflowInstance, userId, WorkflowInstanceStatus.DRAFT_EDIT);
    }

    private void releaseWorkflowInstanceLock(WorkflowInstance workflowInstance, String userId) {
        updateWorkflowInstanceLock(workflowInstance, userId, WorkflowInstanceStatus.DRAFT_SAVE);
    }

    private void updateWorkflowInstanceLock(WorkflowInstance workflowInstance, String userId,
                                            WorkflowInstanceStatus workflowInstanceStatus) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        workflowInstance.setStatus(workflowInstanceStatus);
        workflowInstance.setLastUpdatedBy(userId);
        workflowInstance.setLastUpdatedOn(currentTimestamp);
        workflowInstanceRepository.save(workflowInstance);
    }

    /**
     * This service tries to edit an existing workflowInstance If the instance
     * is already locked then return error Else if free then give lock to the
     * requesting user
     */
    @Transactional
    @Override
    public void acquireEditLock(Integer workflowInstanceId) {
        String userId = SecurityContextUtil.getCurrentUserId();
        // Get instance details
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowInstanceRepository.findById(workflowInstanceId);
        if (optionalWorkflowInstance.isPresent()) {
            WorkflowInstance workflowInstance = optionalWorkflowInstance.get();

            // Check whether this instance is in DRAFT_SAVE/DRAFT_EDIT MODE
            if (workflowInstance.isLockAvailable(userId, configuredTimeout)) {
                setWorkflowInstanceLock(workflowInstance, userId);
            } else {
                // Lock acquired by someone and not timed out yet
                throwResourceException(Constants.RESOURCE_LOCKED);
            }
        }
    }

    @Transactional
    @Override
    public void releaseEditLock(Integer workflowInstanceId) {
        String userId = SecurityContextUtil.getCurrentUserId();
        // Check if this instance is in edit mode
        // Check if the current lock has been acquired by this user
        // And this user has not timed out
        // Then set the status of the workflowInstance to DRAFT_SAVE
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowInstanceRepository.findById(workflowInstanceId);
        if (optionalWorkflowInstance.isPresent()) {
            WorkflowInstance workflowInstance = optionalWorkflowInstance.get();

            if (workflowInstance.getStatus() == WorkflowInstanceStatus.DRAFT_EDIT) {
                logger.info("Status: DRAFT_EDIT");
                String oldUserId = workflowInstance.getLastUpdatedBy();
                if (oldUserId.equalsIgnoreCase(userId)) {
                    logger.info("Same user trying to release lock: {}", userId);
                    // User same as the one who lastUpdated
                    if (workflowInstance.isTimedOut(configuredTimeout)) {
                        logger.info("Timeout reached. Throw exception and prevent save");
                        // Then this user cannot save anymore
                        // Lock acquired by someone and not timed out yet
                        throwResourceException(Constants.RESOURCE_LOCKED);
                    } else {
                        logger.info("Not timed out. User can release lock");
                        // Not timed out
                        // So user can save and release lock
                        releaseWorkflowInstanceLock(workflowInstance, userId);
                    }
                } else {
                    logger.info("Some other user is editing. oldUser: {}; currentUser: {}", oldUserId, userId);
                    // Some other user has the lock
                    // This user cannot save
                    // Lock acquired by someone and not timed out yet
                    throwResourceException(Constants.RESOURCE_LOCKED);
                }
            } else {
                logger.info("Not in DRAFT_EDIT. Lock not acquired, thus release is invalid");
                // Cannot save if not in edit mode
                // Lock acquired by someone and not timed out yet
                throwResourceException(Constants.RESOURCE_LOCKED);
            }
        }
    }

    /**
     * This checks and returns the list of entities which are being used in some
     * workflow instance for a given workflow templates
     *
     * @param workflowTemplate : workflow template for which in use entities need to be
     *                         checked
     * @param entities         : list of entities to check from
     * @return list of entities which are in use in some workflow instance of
     * given workflow.
     */
    public List<String> getEntitiesInUseByOtherWorkflows(WorkflowTemplate workflowTemplate, List<String> entities,
                                                         WorkflowInstance currentWorkflowInstance) {
        return entities.stream().filter(
                entityName -> !entityService.isLockAvailable(entityName, workflowTemplate, currentWorkflowInstance))
                .collect(Collectors.toList());
    }

    @Override
    public WorkflowInstanceDTO createWorkflowInstance(String clientId, String accountId, String workflowId,
                                                      WorkflowInstanceRequestDTO workflowInstanceRequest, DefinitionType type) {
        List<NameLabel> entities = workflowInstanceRequest.getEntities();
        Optional<WorkflowTemplate> optionalWorkflowTemplate = workflowTemplateRepository.findById(workflowId);
        Optional<PageTemplate> optionalPageTemplate = pageService.findById(workflowInstanceRequest.getPageId());
        PageTemplate pageTemplate = null;
        WorkflowTemplate workflowTemplate = null;
        if (optionalWorkflowTemplate.isEmpty()) {
            throw new InvalidResourceException(new ErrorObject("WorkflowTemplate not found", workflowId));
        }
        if (optionalPageTemplate.isEmpty()) {
            throw new InvalidResourceException(new ErrorObject("PageTemplate not found", workflowInstanceRequest.getPageId()));
        }
        if (optionalPageTemplate.isPresent()) {
            pageTemplate = optionalPageTemplate.get();
        }
        if (optionalWorkflowTemplate.isPresent()) {
            workflowTemplate = optionalWorkflowTemplate.get();
        }

        // Check none of these entities are already present with the above workflowinstances in entity instances
        // Passing new workflowIntance as current workflow instance as dummy , since no new instance has been created yet
        List<String> entityNames = entities.stream().map(NameLabel::getName).collect(Collectors.toList());
        List<String> duplicateEntities = getEntitiesInUseByOtherWorkflows(workflowTemplate, entityNames, new WorkflowInstance());

        if (!duplicateEntities.isEmpty() && DefinitionType.CREATE.equals(type)) {
            throw new InvalidResourceException(new ErrorObject("Found duplicate entities", duplicateEntities));
        }
        WorkflowInstance workflowInstance = new WorkflowInstance(workflowTemplate, pageTemplate, clientId, accountId, WorkflowInstanceStatus.DRAFT_SAVE, type);

        if(pageTemplate!=null) {
            findAndUpdateParentInstance(pageTemplate, entityNames, workflowInstance);
        }

        // To make an entry in the db
        WorkflowInstance createdWorkflowInstance = workflowInstanceRepository.save(workflowInstance);

        Set<EntityInstance> entityInstances = new HashSet<>();
        WorkflowTemplate finalWorkflowTemplate = workflowTemplate;
        entities.forEach(entity -> {
            EntityInstance entityInstance = new EntityInstance(entity.getName(), entity.getLabel(),
                    createdWorkflowInstance, finalWorkflowTemplate.getEntityTemplate(), EntityInstanceStatus.DRAFT, null);
            entityInstances.add(entityInstance);
            entityService.save(entityInstance);
        });
        createdWorkflowInstance.setEntityInstances(entityInstances);

        WorkflowInstanceDTO workflowInstanceDTO=  createdWorkflowInstance.toDTO();
        workflowInstanceDTO.setNodeId(getNodeIdFromWorkflowId(workflowId));
        return workflowInstanceDTO;
    }

    private void findAndUpdateParentInstance(PageTemplate pageTemplate, List<String> entityNames, WorkflowInstance workflowInstance) {
        if (DefinitionType.UPDATE.equals(pageTemplate.getType())) {
            EntityTemplate entityTemplate = workflowInstance.getWorkflowTemplate().getEntityTemplate();
            String workflowTemplateId = workflowInstance.getWorkflowTemplate().getId();

            // get(0) - assuming that first entity is the one which is pending for PROMOTED_TO_LIVE
            // entityName is entityId
            Optional<WorkflowInstance> optionalParentInstance = entityInstanceRepository
                    .findAllByNameAndEntityTemplateOrderByIdDesc(entityNames.get(0), entityTemplate).stream()
                    .filter(instance -> !BaseUtil.isEntityInstanceInTerminalState(instance)
                            && workflowTemplateId.equals(instance.getWorkflowInstance().getWorkflowTemplate().getId()))
                    .map(EntityInstance::getWorkflowInstance)
                    .findFirst();
            optionalParentInstance.ifPresent(instance -> {
                workflowInstance.setPendingParentId(String.valueOf(instance.getId()));
                instance.setHidden(true);
                workflowInstanceRepository.save(instance);
            });
        }
    }

    @Override
    public EntityDataResponseDTO getWorkflowInstanceIdForDraftEntity(String accountId, String clientId,
                                                                     String entityName, String entityLabel, String wTemplateId) {
        PublishType publishType = PublishType.DEFAULT;
        Optional<WorkflowTemplate> optionalWorkflowTemplate = workflowTemplateRepository.findById(wTemplateId);
        if (optionalWorkflowTemplate.isPresent()) {
            publishType = optionalWorkflowTemplate.get().getConfigs().getPublishType();
        }
        Optional<WorkflowInstance> optionalWorkflowInstance = workflowInstanceRepository
                .findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(clientId, accountId,
                        Arrays.asList(Constants.NON_TERMINAL_STATUS.get(publishType)), entityName, wTemplateId).stream().findFirst();

        if (optionalWorkflowInstance.isEmpty()) {
            optionalWorkflowInstance = workflowInstanceRepository
                    .findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(clientId, accountId,
                            Arrays.asList(Constants.NON_TERMINAL_STATUS.get(publishType)), entityLabel, wTemplateId).stream().findFirst();
            if (optionalWorkflowInstance.isEmpty()) {
                throw new NoSuchResourceException(new ErrorObject("Draft not available for Entity {0}", entityName));
            }
        } else {
            if (optionalWorkflowInstance.isPresent() && BaseUtil.isWorkflowInstanceInTerminalState(optionalWorkflowInstance.get())) {
                    throw new NoSuchResourceException(new ErrorObject("Draft not available for Entity {0}", entityName));
            }
        }

        if ((optionalWorkflowInstance.isEmpty() || !optionalWorkflowInstance.isPresent())
                || (BaseUtil.isWorkflowInstanceInTerminalState(optionalWorkflowInstance.get()))) {
            logger.info("Draft not available for Entity {}", entityName);
            return new EntityDataResponseDTO();
        } else {
            WorkflowInstance workFlowInstance = optionalWorkflowInstance.get();
            return new EntityDataResponseDTO(workFlowInstance.getId(), workFlowInstance.getType());
        }
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName, #menuGroupName , #scopeType, #scopeId}")
    public NodeResponseDTO getAllNodes(String menuGroupName, ScopeType scopeType, String scopeId) {
        NodeResponseDTO response = new NodeResponseDTO();

        List<Menu> menus = menuRepository.findAllByMenuGroupName(menuGroupName);

        List<Integer> list = new ArrayList<>();
        Map<Integer, NodeDTO> nodeMap = new HashMap<>();
        List<Node> nodes = menus.stream().map(Menu::getNodeParentChild).collect(Collectors.toList());
        List<Node> filteredNodes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(nodes) && !StringUtils.isEmpty(scopeId)
                && scopeType != null) {
            List<Node> filteredByGroupNodes = clientConfigService.filterNodesByConfig(scopeType, scopeId, nodes, ConfigNames.BLACKLISTED_GROUPS);
            filteredNodes = clientConfigService.filterNodesByConfig(scopeType, scopeId, filteredByGroupNodes, ConfigNames.BLACKLISTED_ACTIVITIES);
        }
        filteredNodes.forEach(node -> {
            nodeMap.put(node.getId(), getNodeDTO(node));
            list.add(node.getId());
            addAllNodeChildren(nodeMap, list, node.getId());
        });
        response.setNodes(nodeMap);
        response.setList(list);
        return response;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME, key = "{#root.methodName, #scopeType, #scopeId }")
    public NodeResponseDTO getParentNodes(ScopeType scopeType, String scopeId) {
        NodeResponseDTO response = new NodeResponseDTO();
        Map<Integer, NodeDTO> map = new HashMap<>();
        List<Node> nodes = nodeRepository.findAllByParentId(Constants.PARENT_ID);
        if(!CollectionUtils.isEmpty(nodes) && !StringUtils.isEmpty(scopeId)
                && scopeType != null)
            nodes = clientConfigService.filterNodesByConfig(scopeType, scopeId, nodes, ConfigNames.BLACKLISTED_GROUPS);
        for (Node nodeObj : nodes) {
            map.put(nodeObj.getId(), getNodeDTO(nodeObj));
        }
        response.setNodes(map);
        return response;
    }

    private NodeDTO getNodeDTO(Node node) {
        NodeDTO nodeDTO = new NodeDTO();

        nodeDTO.setId(node.getId());
        nodeDTO.setNodeId(node.getNodeId());
        nodeDTO.setParentId(node.getParentId());
        switch (node.getNodeType()) {
            case GROUP:
                setGroupNodeDTO(node, nodeDTO);
                break;
            case WORKFLOW:
                setWorkflowNodeDTO(node, nodeDTO);
                break;
            case BOOKMARK:
                setBookmarkNodeDTO(node, nodeDTO);
                break;
            default: // no-op
        }
        return nodeDTO;
    }

    private void setNodeDTO(NodeDTO nodeDTO, NodeType nodeType, String displayLabel, UiSchema uiSchema) {
        nodeDTO.setNodeType(nodeType);
        nodeDTO.setTitle(displayLabel);
        nodeDTO.setUiSchema(uiSchema);
    }

    private void setBookmarkNodeDTO(Node node, NodeDTO nodeDTO) {
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(Integer.parseInt(node.getNodeId()));
        if (optionalBookmark.isPresent()) {
            Bookmark bookmark = optionalBookmark.get();
            setNodeDTO(nodeDTO, NodeType.BOOKMARK, bookmark.getDisplayLabel(), bookmark.getUiSchema());
        }
    }

    private void setWorkflowNodeDTO(Node node, NodeDTO nodeDTO) {
        Optional<WorkflowTemplate> optionalWorkflowTemplate = workflowTemplateRepository.findById(node.getNodeId());
        if (optionalWorkflowTemplate.isPresent()) {
            WorkflowTemplate workflowTemplate = optionalWorkflowTemplate.get();
            setNodeDTO(nodeDTO, NodeType.WORKFLOW, workflowTemplate.getTitle(), workflowTemplate.getUiSchema());
        }
    }

    private void setGroupNodeDTO(Node node, NodeDTO nodeDTO) {
        Optional<NodeGroup> optionalGroup = nodeGroupRepository.findById(node.getNodeId());
        if (optionalGroup.isPresent()) {
            NodeGroup group = optionalGroup.get();
            setNodeDTO(nodeDTO, NodeType.GROUP, group.getTitle(), group.getUiSchema());
        }
    }

    private void addAllNodeChildren(Map<Integer, NodeDTO> nodes, List<Integer> list, Integer parentNodeId) {
        List<Node> nodeList = nodeRepository.findAllByParentId(parentNodeId);
        nodeList.forEach(node -> {
            list.add(node.getId());
            nodes.put(node.getId(), getNodeDTO(node));
            addAllNodeChildren(nodes, list, node.getId());
        });
    }

    @Override
    public WorkflowTemplate getWorkflowTemplate(String workflowTemplateId) {
        Optional<WorkflowTemplate> workflowTemplate = workflowTemplateRepository.findById(workflowTemplateId);
        if (workflowTemplate.isPresent()) {
            return workflowTemplate.get();
        } else {
            throw new NoSuchResourceException(WorkflowTemplate.class, workflowTemplateId);
        }
    }

    @Override
    public WorkflowInstanceDTO getWorkflowInstanceById(Integer instanceId) {
        Optional<WorkflowInstance> workflowInstance = workflowInstanceRepository.findById(instanceId);
        if (workflowInstance.isPresent()) {
            return workflowInstance.get().toDTO();
        } else {
            throw new NoSuchResourceException(new ErrorObject("Workflow Instance not found {0}", instanceId));
        }
    }

    @Override
    public Map<SummaryStatus, Long> getWorkflowInstancesCountSummary(String clientId, String accountId) {
        Map<SummaryStatus, Long> summaryMap = getWorkflowInstancesForClient(clientId, accountId).stream()
                .filter(instance -> !WorkflowInstanceStatus.DISCARDED.equals(instance.getStatus()))
                .collect(Collectors.groupingBy(BaseUtil::extractSummaryStatus,
                        Collectors.counting()));
        summaryMap.put(SummaryStatus.TOTAL, summaryMap.values().stream().mapToLong(l -> l).sum());
        return summaryMap;
    }

    @Override
    public void save(WorkflowInstance workflowInstance) {
        workflowInstanceRepository.save(workflowInstance);
    }

    @Override
    public WorkflowInstance validateAndGetWorkflowInstance(String clientId, String accountId,
                                                           Integer workflowInstanceId) {
        Optional<WorkflowInstance> optionalWorkflowInstance = this.getInstance(workflowInstanceId);
        WorkflowInstance workflowInstance;

        if (optionalWorkflowInstance.isPresent()) {
            workflowInstance = optionalWorkflowInstance.get();
        } else {
            throw new NoSuchResourceException(WorkflowInstance.class, workflowInstanceId);
        }

        if (!workflowInstance.getClientId().equals(clientId) || !workflowInstance.getAccountId().equals(accountId)) {
            throw new InvalidArgumentsException(new ErrorObject("Client or account provided is invalid"));
        }

        return workflowInstance;
    }
}