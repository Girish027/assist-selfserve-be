package com.tfsc.ilabs.selfservice.workflow.controllers;

import java.util.*;

import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.StringUtils;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDefinitionDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageTemplateDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.EntityDataResponseDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.NodeResponseDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.SummaryStatus;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkFlowMetaDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowDefinitionDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowTemplateDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;

import javax.inject.Scope;
import javax.swing.text.html.Option;

@RestController
@RequestMapping("/api")
public class WorkflowController {
  
    private static Logger userEventLogger = LoggerFactory.getLogger(Constants.USER_EVENT_LOG);

    @Autowired
    private WorkflowService workflowService;

    @GetMapping(Constants.GET_WORKFLOWS)
    public Map<String, WorkflowTemplateDTO> getWorkflowTemplates(@PathVariable(required = true) ScopeType scopeType,
                                                                 @PathVariable(required = true) String scopeId) {
        return workflowService.getActivities(scopeType, scopeId);
    }

    @GetMapping("/bookmarks")
    public Map<String, WorkflowTemplateDTO> getBookmarks() {
        return workflowService.getBookmarks();
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/instances/refresh")
    public List<WorkflowInstanceDTO> refreshWorkflowInstances(@PathVariable String clientId, @PathVariable String accountId) {

        return workflowService.refreshWorkflowInstances(clientId, accountId);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/instances/poll")
    public List<WorkflowInstanceDTO> pollWorkflowInstances(@PathVariable String clientId, @PathVariable String accountId) {

        return workflowService.pollWorkflowInstances(clientId, accountId);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/instances")
    public List<WorkflowInstanceDTO> getWorkflowInstances(@PathVariable String clientId,
                                                          @PathVariable String accountId) {
        return workflowService.getWorkflowInstances(clientId, accountId);
    }

    @GetMapping("/workflow/instances/{instanceId}")
    public WorkflowInstanceDTO getWorkflowInstanceById(@PathVariable Integer instanceId) {
        return workflowService.getWorkflowInstanceById(instanceId);
    }

    @PostMapping("/userEventLog")
    public void logSelfServeUserMetrics(@RequestBody String userEventData) {
        userEventLogger.info(userEventData);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/instances/summary")
    public Map<SummaryStatus, Long> getWorkflowInstancesCountSummary(
            @PathVariable String clientId, @PathVariable String accountId) {
        return workflowService.getWorkflowInstancesCountSummary(clientId, accountId);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/{workflowId}/instances/summary")
    public Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> getOrderedWorkflowInstances(
            @PathVariable String clientId, @PathVariable String accountId, @PathVariable String workflowId) {
        return workflowService.getWorkflowInstancesOrderByStatus(clientId, accountId, workflowId);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/{workflowId}/instances")
    public List<WorkflowInstanceDTO> getWorkflowInstances(@PathVariable String clientId, @PathVariable String accountId,
                                                          @PathVariable String workflowId) {
        return workflowService.getWorkflowInstances(clientId, accountId, workflowId);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/{workflowId}/instances/{status}")
    public List<WorkflowInstanceDTO> getWorkflowInstances(@PathVariable String clientId, @PathVariable String accountId,
                                                          @PathVariable String workflowId, @PathVariable String status) {
        return workflowService.getWorkflowInstancesForStatus(clientId, accountId, workflowId, status);
    }

    // Delete workflowInstance and related entities
    @PreAuthorize("@authorization.hasInstancePermission(authentication, #workflowInstanceId, 'ACTIVITY_DRAFT_DISCARD')")
    @DeleteMapping("/workflow/instance/{workflowInstanceId}/draft")
    public void deleteWorkflowInstanceDraft(@PathVariable Integer workflowInstanceId) {
        workflowService.deleteWorkflowInstance(workflowInstanceId, Environment.DRAFT);
    }

    @PreAuthorize("@authorization.hasInstancePermission(authentication, #workflowInstanceId, 'ACTIVITY_TEST_DISCARD')")
    @DeleteMapping("/workflow/instance/{workflowInstanceId}/test")
    public void deleteWorkflowInstanceTest(@PathVariable Integer workflowInstanceId) {
        workflowService.deleteWorkflowInstance(workflowInstanceId, Environment.TEST);
    }

    // Acquire lock for workflowInstance
    @PreAuthorize("@authorization.hasInstancePermission(authentication, #workflowInstanceId, 'ACTIVITY_DRAFT_EDIT')")
    @PostMapping("/workflow/instance/{workflowInstanceId}/edit")
    public void acquireEditLock(@PathVariable Integer workflowInstanceId) {
        workflowService.acquireEditLock(workflowInstanceId);
    }

    // Release lock of workflowInstance
    @PreAuthorize("@authorization.hasInstancePermission(authentication, #workflowInstanceId, 'ACTIVITY_DRAFT_EDIT')")
    @PostMapping("/workflow/instance/{workflowInstanceId}/save")
    public void releaseEditLock(@PathVariable Integer workflowInstanceId) {
        workflowService.releaseEditLock(workflowInstanceId);
    }

    @GetMapping("/workflow/{id}/config")
    public WorkFlowMetaDTO getWorkflowMetadata(@PathVariable String id) {
        return workflowService.getWorkflowMetaData(id);
    }

    @GetMapping("/workflow/{id}/page/{type}/config")
    public WorkflowDefinitionDTO getWorkflowDefintion(@PathVariable String id, @PathVariable String type) {
        return convertPageDefinitionToJsonNode(workflowService.getWorkflowDefintion(id, type));

    }

    @PreAuthorize("@authorization.hasPermission(authentication, #clientId, #accountId, 'ACTIVITY_DRAFT_CREATE')")
    @PostMapping("/client/{clientId}/account/{accountId}/workflow/{workflowId}/type/{type}/instance")
    public WorkflowInstanceDTO createWorkflowInstance(
            @RequestBody WorkflowInstanceRequestDTO workflowInstanceRequestDTO, @PathVariable String clientId,
            @PathVariable String accountId, @PathVariable String workflowId, @PathVariable DefinitionType type) {
        return workflowService.createWorkflowInstance(clientId, accountId, workflowId, workflowInstanceRequestDTO, type);
    }

    /**
     * get workflowInstanceId if entity already in draft state. Workflow status
     * considered for draft ('DRAFT_EDIT','DRAFT_SAVE', 'TEST_PROMOTION_FAILED')
     *
     * @param clientId
     * @param accountId
     * @param entityName
     * @param workflowTemplateId
     * @return
     */
    @GetMapping("/client/{clientId}/account/{accountId}/entity/{entityName}/workflow/{workflowTemplateId}")
    public EntityDataResponseDTO getWorkflowInstanceIdForDraftEntity(@PathVariable String clientId,
                                                                     @PathVariable String accountId, @PathVariable String entityName, @PathVariable String workflowTemplateId, @RequestParam(required = false) String entityLabel) {
        return workflowService.getWorkflowInstanceIdForDraftEntity(accountId, clientId, entityName, entityLabel, workflowTemplateId);
    }

    @GetMapping(Constants.GET_ALL_NODES)
    public NodeResponseDTO getAllNodes(@PathVariable(value = "menuGroupName", required = true) String menuGroupName
            , @PathVariable(required = true) ScopeType scopeType, @PathVariable(required = true) String scopeId) {
        return workflowService.getAllNodes(menuGroupName, scopeType, scopeId);
    }

    @GetMapping(Constants.GET_PARENT_NODES)
    public NodeResponseDTO getParentNodes(@PathVariable (required = true) ScopeType scopeType, @PathVariable (required = true) String scopeId) {
        return workflowService.getParentNodes(scopeType, scopeId);
    }

    private WorkflowDefinitionDTO convertPageDefinitionToJsonNode(WorkflowDefinitionDTO workflowDefinitionDTO) {
        if (workflowDefinitionDTO != null && workflowDefinitionDTO.getPages() != null) {
            Iterator<Map.Entry<String, PageTemplateDTO>> itr = workflowDefinitionDTO.getPages().entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, PageTemplateDTO> entry = itr.next();
                PageTemplateDTO pageTemplateDTO = entry.getValue();
                PageDefinitionDTO pageDefinitionDTO = new PageDefinitionDTO();
                pageDefinitionDTO.setUiSchema(StringUtils.valueAsJsonNode(entry.getValue().getDefinition().getUiSchema()));
                pageDefinitionDTO.setFetch(StringUtils.valueAsJsonNode(entry.getValue().getDefinition().getFetch()));
                pageDefinitionDTO.setSchema(StringUtils.valueAsJsonNode(entry.getValue().getDefinition().getSchema().toString()));
                pageDefinitionDTO.setViewUiSchema(StringUtils.valueAsJsonNode(entry.getValue().getDefinition().getViewUiSchema().toString()));
                pageDefinitionDTO.setValidation(StringUtils.valueAsJsonNode(entry.getValue().getDefinition().getValidation()));

                pageTemplateDTO.setDefinition(pageDefinitionDTO);
                entry.setValue(pageTemplateDTO);
            }
        }
        return workflowDefinitionDTO;
    }
}
