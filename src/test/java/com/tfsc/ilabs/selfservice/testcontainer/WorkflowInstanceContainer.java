package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class WorkflowInstanceContainer {

    public static WorkflowInstance getParentWorkflowInstance() {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("testLocation"));
        workflowInstance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_TEST);
        workflowInstance.setCurrentPageTemplate(PageTemplateContainer.getPageTemplateById("page1").get());
        Set<EntityInstance> entityInstances = new HashSet<>();
        workflowInstance.setEntityInstances(entityInstances);
        workflowInstance.setId(2);
        workflowInstance.setClientId("clientId");
        workflowInstance.setAccountId("accountId");
        workflowInstance.setLastUpdatedBy("default.user");
        workflowInstance.setLastUpdatedOn(new Timestamp(System.currentTimeMillis()));
        workflowInstance.setType(DefinitionType.CREATE);
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues"));
        workflowInstance.setPendingParentId("-1");
        return workflowInstance;
    }

    public static WorkflowInstance getWorkflowInstance() {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("testLocation"));
        workflowInstance.setStatus(WorkflowInstanceStatus.DRAFT_SAVE);
        workflowInstance.setCurrentPageTemplate(PageTemplateContainer.getPageTemplateById("page1").get());
        Set<EntityInstance> entityInstances = new HashSet<>();
        workflowInstance.setEntityInstances(entityInstances);
        workflowInstance.setId(1);
        workflowInstance.setClientId("clientId");
        workflowInstance.setAccountId("accountId");
        workflowInstance.setLastUpdatedBy("default.user");
        workflowInstance.setLastUpdatedOn(new Timestamp(System.currentTimeMillis()));
        workflowInstance.setType(DefinitionType.UPDATE);
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues"));
        workflowInstance.setPendingParentId("2");
        return workflowInstance;
    }

    public static WorkflowInstance getWorkflowInstance(EntityInstance entityInstance) {
        WorkflowInstance workflowInstance = getWorkflowInstance();
        Set<EntityInstance> entityInstances = new HashSet<EntityInstance>();
        entityInstances.add(entityInstance);
        workflowInstance.setEntityInstances(entityInstances);
        return workflowInstance;
    }

    public static WorkflowInstance getWorkflowInstance(WorkflowInstanceStatus env) {
        WorkflowInstance workflowInstance = getWorkflowInstance();
        workflowInstance.setStatus(env);
        return workflowInstance;
    }

    public static WorkflowInstance getWorkflowInstance(int id) {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("testLocation"));
        workflowInstance.setStatus(WorkflowInstanceStatus.DRAFT_SAVE);
        workflowInstance.setCurrentPageTemplate(PageTemplateContainer.getPageTemplateById("page1").get());
        Set<EntityInstance> entityInstances = new HashSet<>();
        workflowInstance.setEntityInstances(entityInstances);
        workflowInstance.setId(id);
        workflowInstance.setClientId("clientId");
        workflowInstance.setAccountId("accountId");
        workflowInstance.setLastUpdatedBy(Constants.DEFAULT_USER);
        workflowInstance.setLastUpdatedOn(new Timestamp(System.currentTimeMillis()));
        workflowInstance.setType(DefinitionType.UPDATE);
        workflowInstance.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues"));
        workflowInstance.setPendingParentId("2");
        return workflowInstance;
    }

    public static WorkflowInstance getWorkflowInstance(PublishType publishType, String type) {
        WorkflowInstance workflowInstance = getWorkflowInstance();
        if(type.equals("parent"))
            workflowInstance = getParentWorkflowInstance();
        WorkflowTemplate template = new WorkflowTemplate();
        WorkflowConfig config = new WorkflowConfig();
        config.setPublishType(publishType);
        template.setConfigs(config);
        workflowInstance.setWorkflowTemplate(template);
        return workflowInstance;
    }
}
