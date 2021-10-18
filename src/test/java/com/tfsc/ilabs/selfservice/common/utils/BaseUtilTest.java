package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.security.service.UserSession;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.testcontainer.BaseUtilContainer;
import com.tfsc.ilabs.selfservice.testcontainer.EntityInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowInstanceContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowTemplateContainer;
import com.tfsc.ilabs.selfservice.workflow.dto.response.SummaryStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowConfig;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import org.apache.http.impl.client.CloseableHttpClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author prasada
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class BaseUtilTest {
    @Mock
    DBConfigService dbConfigService;

    @Test
    public void testGetEnvironmentToExecuteForWorkflowInstance() {
        assertEquals(Environment.LIVE, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.PROMOTED_TO_TEST)));
        assertEquals(Environment.LIVE, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.LIVE_PROMOTION_FAILED)));
        assertEquals(Environment.LIVE, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE)));
        assertEquals(Environment.TEST, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE)));
        assertEquals(Environment.TEST, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.TEST_PROMOTION_FAILED)));
        assertEquals(Environment.TEST, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(WorkflowInstanceContainer.
                getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_TEST)));
    }

    @Test
    public void merge_shouldMergeTwoNestedObjects() {
        try {
            JsonNode objFirst = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonObject1);
            JsonNode objSecond = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonObject2);
            assertEquals(BaseUtilContainer.mergedJsonObject, BaseUtil.merge(objFirst, objSecond).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void merge_shouldMergeTwoNestedArrays() {
        try {
            JsonNode objFirst = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonArray1);
            JsonNode objSecond = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonArray2);
            assertEquals(BaseUtilContainer.mergedJsonArray, BaseUtil.merge(objFirst, objSecond).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void merge_shouldMergeOneArrayWithNull() {
        try {
            JsonNode objFirst = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonArray1);
            assertEquals(BaseUtilContainer.nestedJsonArray1, BaseUtil.merge(null, objFirst).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void merge_shouldMergeOneObjectWithNull() {
        try {
            JsonNode objFirst = new ObjectMapper().readTree(BaseUtilContainer.nestedJsonObject1);
            assertEquals(BaseUtilContainer.nestedJsonObject1, BaseUtil.merge(objFirst, null).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_isEntityInstanceInTerminalState_forNonTerminalStateOfDefaultPublishType() {
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        EntityInstance entityInstance = EntityInstanceContainer.createAvailableEntityInstance(new EntityTemplate(), "entity1", 1, workflowInstance);

        boolean retval = BaseUtil.isEntityInstanceInTerminalState(entityInstance);

        assertFalse(retval);
    }

    @Test
    public void test_isEntityInstanceInTerminalState_forNonTerminalStateOfTestOnlyPublishType() {
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        WorkflowTemplate workflowTemplate = WorkflowTemplateContainer.getSingletonWorkflow("field");
        workflowTemplate.setConfigs(new WorkflowConfig(PublishType.TEST_ONLY, false, new HashMap<>(), null, null, null, null, false, false, true, null));
        workflowInstance.setWorkflowTemplate(workflowTemplate);
        EntityInstance entityInstance = EntityInstanceContainer.createAvailableEntityInstance(new EntityTemplate(), "entity1", 1, workflowInstance);
        entityInstance.setStatus(EntityInstanceStatus.DRAFT);

        boolean retval = BaseUtil.isEntityInstanceInTerminalState(entityInstance);

        assertFalse(retval);
    }

    @Test
    public void test_isEntityInstanceInTerminalState_forTerminalStateOfTestOnlyPublishType() {
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        WorkflowTemplate workflowTemplate = WorkflowTemplateContainer.getSingletonWorkflow("field");
        workflowTemplate.setConfigs(new WorkflowConfig(PublishType.TEST_ONLY, false, new HashMap<>(), null, null, null, null, false, false, true, null));
        workflowInstance.setWorkflowTemplate(workflowTemplate);
        EntityInstance entityInstance = EntityInstanceContainer.createAvailableEntityInstance(new EntityTemplate(), "entity1", 1, workflowInstance);

        boolean retval = BaseUtil.isEntityInstanceInTerminalState(entityInstance);

        assertTrue(retval);
    }

    @Test
    public void test_isEntityInstanceInTerminalState_forTerminalStateOfDefaultPublishType() {
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        WorkflowTemplate workflowTemplate = WorkflowTemplateContainer.getSingletonWorkflow("field");
        workflowInstance.setWorkflowTemplate(workflowTemplate);
        EntityInstance entityInstance = EntityInstanceContainer.createAvailableEntityInstance(new EntityTemplate(), "entity1", 1, workflowInstance);
        entityInstance.setStatus(EntityInstanceStatus.DISCARDED);

        boolean retval = BaseUtil.isEntityInstanceInTerminalState(entityInstance);

        assertTrue(retval);
    }

    @Test
    public void test_isNullOrBlank_Null() {
        String input = null;
        assertTrue(BaseUtil.isNullOrBlank(input));
    }

    @Test
    public void test_isNullOrBlank_Blank() {
        String input = "";
        assertTrue(BaseUtil.isNullOrBlank(input));
    }

    @Test
    public void test_isNullOrBlank() {
        String input = "some val";
        assertFalse(BaseUtil.isNullOrBlank(input));
    }

    @Test
    public void test_isNotNullOrBlank_Null() {
        String input = null;
        assertFalse(BaseUtil.isNotNullOrBlank(input));
    }

    @Test
    public void test_isNotNullOrBlank_Blank() {
        String input = "";
        assertFalse(BaseUtil.isNotNullOrBlank(input));
    }

    @Test
    public void test_isNotNullOrBlank() {
        String input = "some val";
        assertTrue(BaseUtil.isNotNullOrBlank(input));
    }

    @Test
    public void test_isNullOrBlankCollection_Null() {
        List<String> input = null;
        assertTrue(BaseUtil.isNullOrBlankCollection(input));
    }

    @Test
    public void test_isNullOrBlankCollection_Empty() {
        List<String> input = new ArrayList<>();
        assertTrue(BaseUtil.isNullOrBlankCollection(input));
    }

    @Test
    public void test_isNullOrBlankCollection() {
        List<Object> input = new ArrayList<>();
        input.add("Time");
        assertFalse(BaseUtil.isNullOrBlankCollection(input));
    }

    @Test
    public void test_joinUrlParts() {
        String prefix = "abc";
        String suffix = "xyz";
        assertEquals("abc/xyz", BaseUtil.joinUrlParts(prefix, suffix));
    }

    @Test
    public void test_getOktaHttpClient(){
        CloseableHttpClient client = BaseUtil.getOktaHttpClient( "1000", "1000",
                "true", "jost", "8080", "https");
        assertNotNull(client);
    }

    @Test
    public void test_convertSetToString_Null() {
        Set<String> input = null;
        String output = BaseUtil.convertSetToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_convertSetToString_Empty(){
        Set<String> input = new HashSet<>();
        String output = BaseUtil.convertSetToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_convertSetToString(){
        Set<String> input = new HashSet<>();
        input.add("test");
        input.add("subject");
        String output = BaseUtil.convertSetToString(input);
        assertEquals("[test,subject]", output);
    }

    @Test
    public void test_convertListToString_Null() {
        List<String> input = null;
        String output = BaseUtil.convertListToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_convertListToString_Empty(){
        List<String> input = new ArrayList<>();
        String output = BaseUtil.convertListToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_convertListToString(){
        List<String> input = new ArrayList<>();
        input.add("test");
        input.add("subject");
        String output = BaseUtil.convertListToString(input);
        assertEquals("[test,subject]", output);
    }

    @Test
    public void test_convertMapToString(){
        Map<String, Object> input = new HashMap<>();
        input.put("hey", "subject");
        input.put("test", "subject");
        String output = BaseUtil.convertMapToString(input);
        assertEquals("[test=subject,hey=subject]", output);
    }

    @Test
    public void test_convertMapToString_Null(){
        Map<String, Object> input = null;
        String output = BaseUtil.convertMapToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_convertMapToString_Empty(){
        Map<String, Object> input = new HashMap<>();
        String output = BaseUtil.convertMapToString(input);
        assertEquals("[]", output);
    }

    @Test
    public void test_getEnvironmentToFetchFrom() {
        assertEquals(Environment.LIVE, BaseUtil.getEnvironmentToFetchFrom(PublishType.LIVE_ONLY));
    }

    @Test
    public void test_getEnvironmentToExecuteForWorkflowInstance_Test() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DRAFT_SAVE);
        instance.getWorkflowTemplate().getConfigs().setPublishType(PublishType.TEST_ONLY);
        assertEquals(Environment.TEST, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(instance));
    }

    @Test(expected = SelfServeException.class)
    public void test_getEnvironmentToExecuteForWorkflowInstance_Exception() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DRAFT_EDIT);
        instance.getWorkflowTemplate().getConfigs().setPublishType(PublishType.TEST_ONLY);
        BaseUtil.getEnvironmentToExecuteForWorkflowInstance(instance);
    }

    @Test
    public void test_getEnvironmentToExecuteForWorkflowInstance_Live() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DRAFT_SAVE);
        instance.getWorkflowTemplate().getConfigs().setPublishType(PublishType.LIVE_ONLY);
        assertEquals(Environment.LIVE, BaseUtil.getEnvironmentToExecuteForWorkflowInstance(instance));
    }

    @Test(expected = SelfServeException.class)
    public void test_getEnvironmentToExecuteForWorkflowInstance_Exceptions() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DRAFT_EDIT);
        instance.getWorkflowTemplate().getConfigs().setPublishType(PublishType.TEST_ONLY);
        BaseUtil.getEnvironmentToExecuteForWorkflowInstance(instance);
    }

    @Test
    public void test_mergeResponse_Arrays() throws Exception {
        JsonNode response = BaseUtil.mergeResponse(getResponse_Ok(), getResponse_Error());
        assertEquals(2, response.size());
    }

    @Test
    public void test_mergeResponse_Maps() throws Exception {
        JsonNode response = BaseUtil.mergeResponse(getResponse_OK_Instances(), getResponse_Error_Message());
        assertEquals(3, response.size());
    }

    @Test
    public void test_mergeResponse_Array_Null() throws Exception {
        JsonNode response = BaseUtil.mergeResponse(null, getResponse_Ok());
        assertEquals(1, response.size());
    }

    @Test
    public void test_mergeResponse_Object_Null() throws Exception {
        JsonNode response = BaseUtil.mergeResponse(null, getResponse_OK_Instances());
        assertEquals(2, response.size());
    }

    @Test
    public void test_toToken() {
        UserSession session = new UserSession();
        session.setUserId("test");
        session.setUserName("test user");
        session.setAccessToken("zxcvbnmkhd124332");
        SSAuthenticationToken token = BaseUtil.toToken(session);
        assertEquals("zxcvbnmkhd124332", token.getAccessToken());
    }

    @Test
    public void test_extractSummaryStatus_Live() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_LIVE);
        SummaryStatus status = BaseUtil.extractSummaryStatus(instance);
        assertEquals("LIVE", status.name());
    }

    @Test
    public void test_extractSummaryStatus_Draft() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DRAFT_EDIT);
        SummaryStatus status = BaseUtil.extractSummaryStatus(instance);
        assertEquals("DRAFT", status.name());
    }

    @Test
    public void test_extractSummaryStatus_Test() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_TEST);
        SummaryStatus status = BaseUtil.extractSummaryStatus(instance);
        assertEquals("TEST", status.name());
    }

    @Test
    public void test_extractSummaryStatus_Discarded() {
        WorkflowInstance instance = getWorkflowInstance();
        instance.setStatus(WorkflowInstanceStatus.DISCARDED);
        SummaryStatus status = BaseUtil.extractSummaryStatus(instance);
        assertEquals("DISCARDED", status.name());
    }

    @Test
    public void test_shouldStubLivePublish_1() {
        WorkflowInstance instance = WorkflowInstanceContainer.getWorkflowInstance(PublishType.LIVE_ONLY, "instance");
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(true);
        assertFalse(BaseUtil.shouldStubLivePublish(instance, dbConfigService));
    }

    @Test
    public void test_shouldStubLivePublish_2() {
        WorkflowInstance instance = WorkflowInstanceContainer.getWorkflowInstance(PublishType.LIVE_ONLY, "instance");
        instance.setStatus(WorkflowInstanceStatus.PROMOTING_TO_LIVE);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(true);
        assertFalse(BaseUtil.shouldStubLivePublish(instance, dbConfigService));
    }

    @Test
    public void test_getSuccessRequestResponseDefinitionStub() throws Exception {
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(getMockSuccessResponse());
        ActionExecutorService.RequestResponseDefinition responseDefinition = BaseUtil.getSuccessRequestResponseDefinitionStub(dbConfigService);
        assertEquals(Optional.of(200), Optional.of(responseDefinition.getResponseDefinition().getResponseCode()));
    }

    public WorkflowInstance getWorkflowInstance() {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        WorkflowTemplate template = new WorkflowTemplate();
        template.setConfigs(new WorkflowConfig());
        workflowInstance.setWorkflowTemplate(template);
        return workflowInstance;
    }

    public JsonNode getResponse_OK_Instances() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "" +
                "{\n" +
                "    \"status\" : \"OK\", \n" +
                "    \"instances\" : [\"xyz\", \"abc\"]\n" +
                "}";
        return mapper.readValue(jsonString, JsonNode.class);
    }

    public JsonNode getResponse_Error_Message() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "" +
                "{\n" +
                "    \"errMessage\" : \"Please Check the status again\", \n" +
                "    \"instances\" : [\"klm\", \"pqr\"]\n" +
                "}";
        return mapper.readValue(jsonString, JsonNode.class);
    }

    public JsonNode getResponse_Ok() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "" +
                "[\n" +
                "    {\n" +
                "        \"status\": \"OK\"\n" +
                "    }\n" +
                "]";
        return mapper.readValue(jsonString, JsonNode.class);
    }

    public JsonNode getResponse_Error() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "" +
                "\n" +
                "[\n" +
                "    {\n" +
                "        \"status\": \"ERROR\"\n" +
                "    }\n" +
                "]";
        return mapper.readValue(jsonString, JsonNode.class);
    }

    public JsonNode getMockSuccessResponse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = "{\"response\": \"success\"}";
        return mapper.readValue(response, JsonNode.class);
    }
}
