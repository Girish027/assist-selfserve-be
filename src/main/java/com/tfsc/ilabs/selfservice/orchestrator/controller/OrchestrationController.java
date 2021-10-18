package com.tfsc.ilabs.selfservice.orchestrator.controller;

import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.orchestrator.service.api.OrchestratorService;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrchestrationController {
    @Autowired
    private OrchestratorService orchestratorService;
    @Autowired
    private WorkflowService workflowService;

    private void publishWorkflow(String clientId, String accountId, Integer workflowInstanceId, Environment env) {
        WorkflowInstance workflowInstance = workflowService.validateAndGetWorkflowInstance(clientId, accountId, workflowInstanceId);

        if (workflowInstance == null) {
            throw new NoSuchResourceException(WorkflowInstance.class, workflowInstanceId);
        }

        Environment publishToEnv = BaseUtil.getEnvironmentToExecuteForWorkflowInstance(workflowInstance);
        if (env.equals(publishToEnv)) {
            orchestratorService.orchestrate(clientId, accountId, workflowInstance, env);
        }
    }

    @PreAuthorize("@authorization.hasPermission(authentication, #clientId, #accountId, 'ACTIVITY_TEST_PUBLISH')")
    @PostMapping("/client/{clientId}/account/{accountId}/workflow/instance/{workflowInstanceId}/publish/test")
    @CacheEvict(value = Constants.CACHE_NAME, allEntries = true)
    public void publishWorkflowTest(@PathVariable String clientId, @PathVariable String accountId, @PathVariable Integer workflowInstanceId) {
        publishWorkflow(clientId, accountId, workflowInstanceId, Environment.TEST);
    }

    @PreAuthorize("@authorization.hasPermission(authentication, #clientId, #accountId, 'ACTIVITY_LIVE_PUBLISH')")
    @PostMapping("/client/{clientId}/account/{accountId}/workflow/instance/{workflowInstanceId}/publish/live")
    @CacheEvict(value = Constants.CACHE_NAME, allEntries = true)
    public void publishWorkflowLive(@PathVariable String clientId, @PathVariable String accountId, @PathVariable Integer workflowInstanceId) {
        publishWorkflow(clientId, accountId, workflowInstanceId, Environment.LIVE);
    }

    @GetMapping("/client/{clientId}/account/{accountId}/workflow/instance/{workflowInstanceId}/publish")
    public WorkflowInstanceDTO getPublishedWorkflowStatus(@PathVariable String clientId, @PathVariable String accountId, @PathVariable Integer workflowInstanceId) {
        return orchestratorService.getStatus(clientId, accountId, workflowInstanceId);
    }
}