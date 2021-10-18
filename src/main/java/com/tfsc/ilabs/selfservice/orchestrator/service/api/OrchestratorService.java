package com.tfsc.ilabs.selfservice.orchestrator.service.api;

import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;

/**
 * Created by ravi.b on 01-08-2019.
 */
public interface OrchestratorService {
    void orchestrate(String clientId , String accountId , WorkflowInstance workflowInstanceId, Environment publishToEnv);

    WorkflowInstanceDTO getStatus(String clientId, String accountId, Integer workflowInstanceId);
}
