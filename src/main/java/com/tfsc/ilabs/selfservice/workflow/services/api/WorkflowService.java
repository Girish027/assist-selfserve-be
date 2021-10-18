package com.tfsc.ilabs.selfservice.workflow.services.api;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.*;
import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkflowService {

    Map<String, WorkflowTemplateDTO> getActivities(ScopeType scopeType, String scopeId);

    Map<String, WorkflowTemplateDTO> getBookmarks();

    List<WorkflowInstanceDTO> getWorkflowInstances(String clientId, String accountId);

    List<WorkflowInstanceDTO> refreshWorkflowInstances(String clientId, String accountId);

    List<WorkflowInstanceDTO> pollWorkflowInstances(String clientId, String accountId);

    List<WorkflowInstanceDTO> getWorkflowInstances(String clientId, String accountId, String workflowId);

    WorkFlowMetaDTO getWorkflowMetaData(String workflowId);

    List<PageTemplate> getAllPagesOfWorkflowByType(WorkflowTemplate workflowTemplate, String type);

    WorkflowDefinitionDTO getWorkflowDefintion(String workflowId, String type);

    Optional<WorkflowInstance> getInstance(Integer workflowInstanceId);

    List<PageTemplate> getAllPagesOfWorkflow(WorkflowTemplate workflowTemplate);

    void markWorkflowInstanceStatusCompleted(WorkflowInstance workflowInstance);

    boolean markWorkflowInstanceStatusDiscarded(WorkflowInstance workflowInstance);

    void markWorkflowInstanceStatusRunning(WorkflowInstance workflowInstance);

    void markWorkflowInstanceStatusFailed(WorkflowInstance workflowInstance);

    Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> getWorkflowInstancesOrderByStatus(String clientId,
                                                                                             String accountId, String workflowId);

    Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> getWorkflowInstancesOrderByStatus(String clientId,
                                                                                             String accountId);

    List<WorkflowInstanceDTO> getWorkflowInstancesForStatus(String clientId, String accountId, String workflowId,
                                                            String status);

    void deleteWorkflowInstance(Integer workflowInstanceId, Environment currEnv);

    void acquireEditLock(Integer workflowInstanceId);

    void releaseEditLock(Integer workflowInstanceId);

    List<String> getEntitiesInUseByOtherWorkflows(WorkflowTemplate workflowTemplate, List<String> entities,
                                                  WorkflowInstance currentWorkflowInstance);

    WorkflowInstanceDTO createWorkflowInstance(String clientId, String accountId, String workflowId,
                                               WorkflowInstanceRequestDTO workflowInstanceRequestDTO, DefinitionType type);

    EntityDataResponseDTO getWorkflowInstanceIdForDraftEntity(String accountId, String clientId, String entityName, String entityLabel,
                                                              String wTemplateId);

    NodeResponseDTO getAllNodes(String menuGroupName, ScopeType scopeType, String scopeId);

    NodeResponseDTO getParentNodes(ScopeType scopeType, String scopeId);

    WorkflowTemplate getWorkflowTemplate(String workflowTemplateId);

    WorkflowInstanceDTO getWorkflowInstanceById(Integer instanceId);

    Map<SummaryStatus, Long> getWorkflowInstancesCountSummary(String clientId, String accountId);

    void save(WorkflowInstance workflowInstance);

    WorkflowInstance validateAndGetWorkflowInstance(String clientId, String accountId, Integer workflowInstanceId);
}
