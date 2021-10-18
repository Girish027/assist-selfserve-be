package com.tfsc.ilabs.selfservice.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ActionWorkflow;
import com.tfsc.ilabs.selfservice.action.models.ExecutionStatus;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionExecutionMeta;
import com.tfsc.ilabs.selfservice.action.repositories.ActionExecutionMonitorRepository;
import com.tfsc.ilabs.selfservice.action.repositories.ActionRepository;
import com.tfsc.ilabs.selfservice.action.repositories.ActionWorkflowRepository;
import com.tfsc.ilabs.selfservice.action.services.impl.ActionExecutorServiceImpl;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.ComponentInfo;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.repositories.ServiceUrlRepository;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.utils.ScriptUtil;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.FetchConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityIdLookup;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityIdLookupRepository;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.testcontainer.*;
import com.tfsc.ilabs.selfservice.workflow.models.PublishState;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import com.tfsc.ilabs.selfservice.workflow.services.api.WorkflowService;
import io.swagger.models.HttpMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tfsc.ilabs.selfservice.testutils.TestConstants.ACCOUNT_ID;
import static com.tfsc.ilabs.selfservice.testutils.TestConstants.CLIENT_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ScriptUtil.class, BaseUtil.class, HttpUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ActionExecutorServiceImplTest {
    @InjectMocks
    ActionExecutorServiceImpl actionExecutorServiceImpl;

    @Mock
    EntityIdLookupRepository entityIdLookupRepository;
    @Mock
    FetchConfigService fetchConfigService;
    @Mock
    WorkflowService workflowService;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    PageService pageService;
    @Mock
    ServiceUrlRepository serviceUrlRepository;
    @Mock
    ActionExecutionMonitorRepository actionExecutionMonitorRepository;
    @Mock
    EntityInstanceRepository entityInstanceRepository;
    @Mock
    ActionWorkflowRepository actionWorkflowRepository;
    @Mock
    ActionRepository actionRepository;
    @Mock
    HttpResponse<String> httpResponse;
    @Mock
    SessionValidator sessionValidator;

    @Mock org.springframework.core.env.Environment env;

    @Before
    public void setup() {
        Mockito.when(this.env.getProperty(Constants.STORAGE_TYPE)).thenReturn("local");
        Mockito.when(objectMapper.createObjectNode()).thenReturn(new ObjectMapper().createObjectNode());
        PowerMockito.mockStatic(BaseUtil.class);
        PowerMockito.mockStatic(ScriptUtil.class);
        PowerMockito.mockStatic(HttpUtils.class);
    }

    private ApiResponse setupFor_makeActionCall() throws IOException {
        Mockito.when(entityIdLookupRepository.findByTestEntityId(anyString()))
                .thenReturn(new EntityIdLookup());
        Mockito.when(fetchConfigService.getDataFromService(anyString(), any(JsonNode.class), any(FetchType.class), any(Environment.class)))
                .thenReturn(new ObjectMapper().createObjectNode());
        PowerMockito.when(ScriptUtil.invokeFunction(anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(Environment.class), anyString(), any(ObjectNode.class)))
                .thenReturn("{\"whitelistedDomains\":[\"stagewhitelist1\", \"stagewhitelist2\"]}");

        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), any(Environment.class)))
                .thenReturn(ServiceUrlsContainer.getServiceUrls(Environment.TEST));

        ApiResponse body = new ApiResponse(PublishState.SUCCESS, "SUCCESS");
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body.toString());
        PowerMockito.when(HttpUtils.makePostOrPutCall(anyString(), any(HttpMethod.class), anyMap(), anyString()))
                .thenReturn(httpResponse);
        PowerMockito.when(HttpUtils.makePostOrPutCall(anyString(), any(HttpMethod.class), anyMap(), anyString(), anyBoolean()))
                .thenReturn(httpResponse);
        return body;
    }

    private ApiResponse setupFor_makeActionCallWithoutServiceUrls() throws IOException {
        Mockito.when(entityIdLookupRepository.findByTestEntityId(anyString()))
                .thenReturn(new EntityIdLookup());
        Mockito.when(fetchConfigService.getDataFromService(anyString(), any(JsonNode.class), any(FetchType.class), any(Environment.class)))
                .thenReturn(new ObjectMapper().createObjectNode());
        PowerMockito.when(ScriptUtil.invokeFunction(anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), any(Environment.class), anyString(), any(ObjectNode.class)))
                .thenReturn("{\"whitelistedDomains\":[\"stagewhitelist1\", \"stagewhitelist2\"]}");

        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), any(Environment.class)))
                .thenReturn(null);

        ApiResponse body = new ApiResponse(PublishState.SUCCESS, "SUCCESS");
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(body.toString());
        PowerMockito.when(HttpUtils.makePostOrPutCall(anyString(), any(HttpMethod.class), anyMap(), anyString()))
                .thenReturn(httpResponse);
        return body;
    }

    @Test
    public void test_createActionExecutionMonitorIfNotExist() {
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(null, "entityName", 1, "entityLabel");
        List<EntityInstance> entityInstances = List.of(entityInstance);
        Mockito.when(entityInstanceRepository.findAllByWorkflowInstance(any(WorkflowInstance.class)))
                .thenReturn(entityInstances);

        ActionWorkflow actionWorkFlow = ActionWorkFlowContainer.getActionWorkFlowInstance(ActionContainer.createActionInstance(),
                WorkflowTemplateContainer.getSingletonWorkflow("aa"));
        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplateAndActionDefinitionType(any(WorkflowTemplate.class), any(DefinitionType.class)))
                .thenReturn(List.of(actionWorkFlow));

        ActionExecutionMonitor actionExecutionMonitor = Mockito.mock(ActionExecutionMonitor.class);
        Mockito.when(actionExecutionMonitorRepository.findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(any(Action.class),
                any(WorkflowInstance.class), any(EntityInstance.class), any(Environment.class)))
                .thenReturn(actionExecutionMonitor);

        actionExecutorServiceImpl.createActionExecutionMonitorIfNotExist(WorkflowInstanceContainer.getWorkflowInstance(), Environment.TEST);

        Mockito.verify(actionExecutionMonitor, times(1)).setExecutionMeta(new ActionExecutionMeta());
        Mockito.verify(actionExecutionMonitorRepository, times(1))
                .save(Mockito.any(ActionExecutionMonitor.class));
    }

    @Test
    public void test_execute_forDraftInstance() throws IOException {
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any(WorkflowInstance.class)))
                .thenReturn(Environment.LIVE);

        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE);
        Mockito.when(pageService.getPageDataByWorkflowInstace(any(WorkflowInstance.class)))
                .thenReturn(PageDataContainer.getTiePageDataList(workflowInstance));
        ComponentInfo info = new ComponentInfo();
        info.setComponentAccountId("testComponentAccountId");
        info.setComponentClientId("testComponentClientId");
        info.setComponentId("testComponentId");
        Mockito.when(sessionValidator.getComponentInfoFromTokenMap(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(info);
        Mockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.anyVararg())).thenReturn("body");
        setupFor_makeActionCall();

        ExecutionStatus status = actionExecutorServiceImpl.execute(ActionContainer.createActionInstance(), workflowInstance,
                EntityInstanceContainer.createEntityInstance(null, "entityNane", 1, "entityLabel"),
                CLIENT_ID, ACCOUNT_ID);

        Mockito.verify(actionExecutionMonitorRepository, times(1))
                .save(any(ActionExecutionMonitor.class));
        assertEquals("COMPLETED", status.name());
    }

    @Test
    public void test_execute_forDraftInstance_nullServiceUrl() throws IOException {
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any(WorkflowInstance.class)))
                .thenReturn(Environment.LIVE);

        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE);
        Mockito.when(pageService.getPageDataByWorkflowInstace(any(WorkflowInstance.class)))
                .thenReturn(PageDataContainer.getTiePageDataList(workflowInstance));
        ComponentInfo info = new ComponentInfo();
        info.setComponentAccountId("testComponentAccountId");
        info.setComponentClientId("testComponentClientId");
        info.setComponentId("testComponentId");
        Mockito.when(sessionValidator.getComponentInfoFromTokenMap(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(info);
        Mockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.anyVararg())).thenReturn("body");
        setupFor_makeActionCallWithoutServiceUrls();

        ExecutionStatus status = actionExecutorServiceImpl.execute(ActionContainer.createActionInstance(), workflowInstance,
                EntityInstanceContainer.createEntityInstance(null, "entityName", 1, "entityLabel"),
                CLIENT_ID, ACCOUNT_ID);

        Mockito.verify(actionExecutionMonitorRepository, times(1))
                .save(any(ActionExecutionMonitor.class));
        assertEquals("FAILED", status.name());
    }

    @Test
    @Ignore
    public void test_execute_forDraftInstance_ForFile() throws IOException {
        PowerMockito.when(BaseUtil.getEnvironmentToExecuteForWorkflowInstance(any(WorkflowInstance.class)))
                .thenReturn(Environment.LIVE);
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE);
        Mockito.when(pageService.getPageDataByWorkflowInstace(any(WorkflowInstance.class)))
                .thenReturn(PageDataContainer.getFilePageDataList(workflowInstance));
        ComponentInfo info = new ComponentInfo();
        info.setComponentAccountId("testComponentAccountId");
        info.setComponentClientId("testComponentClientId");
        info.setComponentId("testComponentId");
        Mockito.when(sessionValidator.getComponentInfoFromTokenMap(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(info);
        Mockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.anyVararg())).thenReturn("body");
        setupFor_makeActionCall();
        ExecutionStatus status = actionExecutorServiceImpl.execute(ActionContainer.createActionInstance_File(), workflowInstance,
                EntityInstanceContainer.createEntityInstance(null, "entityNane", 1, "entityLabel"),
                CLIENT_ID, ACCOUNT_ID);

        Mockito.verify(actionExecutionMonitorRepository, times(1))
                .save(any(ActionExecutionMonitor.class));
        assertEquals("COMPLETED", status.name());
    }

    @Test
    public void test_execute_forCompletedInstance() {
        ExecutionStatus status = actionExecutorServiceImpl.execute(ActionContainer.createActionInstance(),
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_LIVE),
                EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"),
                "client", "account");
        assertEquals("COMPLETED", status.name());
    }

    @Test
    public void test_execute_witActionId() throws IOException {
        Integer actionId = 1;
        JsonNode pageData = objectMapper.createObjectNode();
        NameLabel entityInstance = new NameLabel("testEntity", "TestEntity");
        Environment env = Environment.TEST;

        PowerMockito.when(BaseUtil.translateAPIResponse(any(), any(), any()))
                .thenReturn(new ApiResponse(PublishState.SUCCESS, "SUCCESS"));

        when(actionRepository.findById(actionId)).thenReturn(Optional.of(ActionContainer.createActionInstance()));
        ApiResponse expectedResponse = setupFor_makeActionCall();
        ComponentInfo info = new ComponentInfo();
        info.setComponentAccountId("testComponentAccountId");
        info.setComponentClientId("testComponentClientId");
        info.setComponentId("testComponentId");
        Mockito.when(sessionValidator.getComponentInfoFromTokenMap(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(info);
        Mockito.when(ScriptUtil.invokeFunction(Mockito.anyString(), Mockito.anyString(), Mockito.anyVararg())).thenReturn("body");
        ApiResponse actualResponse = actionExecutorServiceImpl.execute(actionId, pageData, entityInstance, CLIENT_ID, ACCOUNT_ID, env);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_execute_withActionId_withException() {
        Integer actionId = 1;
        JsonNode pageData = objectMapper.createObjectNode();
        NameLabel entityInstance = new NameLabel("testEntity", "TestEntity");
        Environment env = Environment.TEST;

        when(actionRepository.findById(actionId)).thenReturn(Optional.empty());

        actionExecutorServiceImpl.execute(actionId, pageData, entityInstance, CLIENT_ID, ACCOUNT_ID, env);
    }

    public void testExecute_LIVE() throws IOException {
        ExecutionStatus status = actionExecutorServiceImpl.execute(ActionContainer.getActionExecutionMonitor().getAction(),
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_LIVE), EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"), "client", "account");
        assertEquals("COMPLETED", status.name());
    }

    @Test
    public void test_createActionExecutionMonitorIfParentPending_LIVE_type_UPDATE() {
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(null, "name", 1, "label");
        List<EntityInstance> entityInstances = new ArrayList<EntityInstance>();
        entityInstances.add(entityInstance);

        Mockito.when(entityInstanceRepository.findAllByWorkflowInstance(Mockito.any(WorkflowInstance.class)))
                .thenReturn(entityInstances);

        List<ActionWorkflow> actionWorkFlowList = new ArrayList<>();
        ActionWorkflow actionWorkFlow = ActionWorkFlowContainer.getActionWorkFlowInstance(ActionContainer.createActionInstance(), WorkflowTemplateContainer.getSingletonWorkflow("aa"));
        actionWorkFlowList.add(actionWorkFlow);

        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplate(WorkflowInstanceContainer.getWorkflowInstance().getWorkflowTemplate()))
                .thenReturn(actionWorkFlowList);
        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplateAndActionDefinitionType(Mockito.any(WorkflowTemplate.class),
                Mockito.any(DefinitionType.class))).thenReturn(actionWorkFlowList);
        Optional<WorkflowInstance> parentInstance = Optional.of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_TEST));
        Mockito.when(workflowService.getInstance(Mockito.anyInt())).thenReturn(parentInstance);
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        workflowInstance.setType(DefinitionType.UPDATE);
        workflowInstance.setPendingParentId("01");
        List<Action> actionsList = actionExecutorServiceImpl.createActionExecutionMonitorIfNotExist(workflowInstance, Environment.LIVE);
        Assert.assertEquals(1, actionsList.size());
    }

    @Test
    public void test_createActionExecutionMonitorIfParentPending_LIVE_type_CREATE() {
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(null, "name", 1, "label");
        List<EntityInstance> entityInstances = new ArrayList<EntityInstance>();
        entityInstances.add(entityInstance);

        Mockito.when(entityInstanceRepository.findAllByWorkflowInstance(Mockito.any(WorkflowInstance.class)))
                .thenReturn(entityInstances);

        List<ActionWorkflow> actionWorkFlowList = new ArrayList<>();
        ActionWorkflow actionWorkFlow = ActionWorkFlowContainer.getActionWorkFlowInstance(ActionContainer.createActionInstance(), WorkflowTemplateContainer.getSingletonWorkflow("aa"));
        actionWorkFlowList.add(actionWorkFlow);

        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplate(WorkflowInstanceContainer.getWorkflowInstance().getWorkflowTemplate()))
                .thenReturn(actionWorkFlowList);
        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplateAndActionDefinitionType(Mockito.any(WorkflowTemplate.class),
                Mockito.any(DefinitionType.class))).thenReturn(actionWorkFlowList);
        Optional<WorkflowInstance> parentInstance = Optional.of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_TEST));
        Mockito.when(workflowService.getInstance(Mockito.anyInt())).thenReturn(parentInstance);
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        workflowInstance.setType(DefinitionType.CREATE);
        workflowInstance.setPendingParentId("01");
        List<Action> actionsList = actionExecutorServiceImpl.createActionExecutionMonitorIfNotExist(workflowInstance, Environment.LIVE);
        Assert.assertEquals(1, actionsList.size());
    }

    @Test
    public void test_createActionExecutionMonitorIfParentPending_TEST_type_UPDATE() {
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(null, "name", 1, "label");
        List<EntityInstance> entityInstances = new ArrayList<EntityInstance>();
        entityInstances.add(entityInstance);

        Mockito.when(entityInstanceRepository.findAllByWorkflowInstance(Mockito.any(WorkflowInstance.class)))
                .thenReturn(entityInstances);

        List<ActionWorkflow> actionWorkFlowList = new ArrayList<>();
        ActionWorkflow actionWorkFlow = ActionWorkFlowContainer.getActionWorkFlowInstance(ActionContainer.createActionInstance(), WorkflowTemplateContainer.getSingletonWorkflow("aa"));
        actionWorkFlowList.add(actionWorkFlow);

        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplate(WorkflowInstanceContainer.getWorkflowInstance().getWorkflowTemplate()))
                .thenReturn(actionWorkFlowList);
        Mockito.when(actionWorkflowRepository.findAllByWorkflowTemplateAndActionDefinitionType(Mockito.any(WorkflowTemplate.class),
                Mockito.any(DefinitionType.class))).thenReturn(actionWorkFlowList);
        Optional<WorkflowInstance> parentInstance = Optional.of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_TEST));
        Mockito.when(workflowService.getInstance(Mockito.anyInt())).thenReturn(parentInstance);
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        workflowInstance.setType(DefinitionType.UPDATE);
        workflowInstance.setPendingParentId("01");
        List<Action> actionsList = actionExecutorServiceImpl.createActionExecutionMonitorIfNotExist(workflowInstance, Environment.TEST);
        Assert.assertEquals(1, actionsList.size());
    }

    @Test
    public void test_getActionExecutionMonitor() {
        ActionExecutionMonitor actionExecutionMonitor = new ActionExecutionMonitor(new Action(), new WorkflowInstance(),
                new EntityInstance(), Environment.TEST);
        Mockito.when(actionExecutionMonitorRepository.findByActionAndWorkflowInstanceAndEntityInstanceAndEnv(any(Action.class),
                any(WorkflowInstance.class), any(EntityInstance.class), any(Environment.class)))
                .thenReturn(actionExecutionMonitor);

        ActionExecutionMonitor actionExecutionMonitorResult = actionExecutorServiceImpl.getActionExecutionMonitor(actionExecutionMonitor);

        assertEquals(actionExecutionMonitor, actionExecutionMonitorResult);
    }

    @Test
    public void test_getBaseServiceUrl() throws Exception {
        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), any(Environment.class)))
                .thenReturn(ServiceUrlsContainer.getServiceUrls(Environment.LIVE));
        String baseUrl = actionExecutorServiceImpl.getBaseServiceUrl("assist", Environment.LIVE);
        assertEquals("http://test", baseUrl);
    }

    @Test(expected = SelfServeException.class)
    public void test_getBaseServiceUrlWithNullURl() {
        Mockito.when(serviceUrlRepository.findByServiceIdAndEnv(Mockito.anyString(), any(Environment.class)))
                .thenReturn(null);
        actionExecutorServiceImpl.getBaseServiceUrl("assist", Environment.LIVE);
    }
}
