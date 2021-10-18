package com.tfsc.ilabs.selfservice.workflow.services.api;

import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.testcontainer.NodeContainer;
import com.tfsc.ilabs.selfservice.testcontainer.ClientsConfigContainer;
import com.tfsc.ilabs.selfservice.testcontainer.WorkflowTemplateContainer;
import com.tfsc.ilabs.selfservice.workflow.repositories.ClientsConfigRepository;
import com.tfsc.ilabs.selfservice.workflow.services.impl.ClientsConfigServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import com.tfsc.ilabs.selfservice.workflow.models.*;

import javax.xml.transform.Templates;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ClientsConfigServiceTest {
    @InjectMocks
    ClientsConfigServiceImpl service = new ClientsConfigServiceImpl();

    @Mock
    ClientsConfigRepository clientsConfigRepository;

    @Test
    public void testFilterNodesByBlacklistedGroups_WithNullScopeType() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(null);
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1),
                NodeContainer.createNode("2", NodeType.GROUP, -1),
                NodeContainer.createNode("3", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(null, "test-client", nodes, ConfigNames.BLACKLISTED_GROUPS);
        assertNotNull(resultantNodes);
        assertEquals(3, resultantNodes.size());
    }

    @Test
    public void testFilterNodesByBlacklistedGroups_WithEmptyClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_GROUPS, List.of("1", "3"))
                ));
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1),
                NodeContainer.createNode("2", NodeType.GROUP, -1),
                NodeContainer.createNode("3", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(ScopeType.client, "test-client", nodes, ConfigNames.BLACKLISTED_GROUPS);
        assertNotNull(resultantNodes);
        assertEquals(1, resultantNodes.size());
        assertEquals(2, resultantNodes.get(0).getId());
    }

    @Test
    public void testFilterNodesByBlacklistedGroups_WithClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, "test-client", ScopeType.client, ConfigNames.BLACKLISTED_GROUPS, List.of("2", "3")),
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_GROUPS, List.of("1", "3"))
                ));
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1),
                NodeContainer.createNode("2", NodeType.GROUP, -1),
                NodeContainer.createNode("3", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(ScopeType.client, "test-client", nodes, ConfigNames.BLACKLISTED_GROUPS);
        assertNotNull(resultantNodes);
        assertEquals(1, resultantNodes.size());
        assertEquals(1, resultantNodes.get(0).getId());
    }

    @Test
    public void testFilterNodesByBlacklistedActivities_WithNullScopeType() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(null);
        List<Node> nodes = List.of(
                NodeContainer.createNode(1, "testAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(2, "liveAgents ", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(3, "bulkAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(4, "1", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(null, "test-client", nodes, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(4, resultantNodes.size());
    }

    @Test
    public void testFilterNodesByBlacklistedActivities_WithEmptyClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("testAgents", "bulkAgents"))
                ));
        List<Node> nodes = List.of(
                NodeContainer.createNode(1, "testAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(2, "liveAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(3, "bulkAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(4, "1", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(ScopeType.client, "test-client", nodes, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(2, resultantNodes.size());
        assertEquals("liveAgents", resultantNodes.get(0).getNodeId());
    }

    @Test
    public void testFilterNodesByBlacklistedActivities_WithClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, "test-client", ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("liveAgents", "bulkAgents")),
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("testAgents", "bulkAgents"))
                ));
        List<Node> nodes = List.of(
                NodeContainer.createNode(1, "testAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(2, "liveAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(3, "bulkAgents", NodeType.WORKFLOW, -1),
                NodeContainer.createNode(4, "1", NodeType.GROUP, -1)
        );
        List<Node> resultantNodes = service.filterNodesByConfig(ScopeType.client, "test-client", nodes, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(2, resultantNodes.size());
        assertEquals("testAgents", resultantNodes.get(0).getNodeId());
    }

    @Test
    public void testFilterWorkflowTemplatesByBlacklistedActivities_WithNullScopeType() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(null);
        List<WorkflowTemplate> templates = List.of(
                WorkflowTemplateContainer.getWorkflowTemplate("testAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("liveAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("bulkAgents", "test location", PublishType.TEST_ONLY)
        );
        List<WorkflowTemplate> resultantNodes = service.filterWorkflowTemplatesByConfig(null, "test-client", templates, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(3, resultantNodes.size());
    }

    @Test
    public void testFilterWorkflowTemplatesByBlacklistedActivities_WithEmptyClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("testAgents", "bulkAgents"))
                ));
        List<WorkflowTemplate> templates = List.of(
                WorkflowTemplateContainer.getWorkflowTemplate("testAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("liveAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("bulkAgents", "test location", PublishType.TEST_ONLY)
        );
        List<WorkflowTemplate> resultantNodes = service.filterWorkflowTemplatesByConfig(ScopeType.client, "test-client", templates, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(1, resultantNodes.size());
        assertEquals("liveAgents", resultantNodes.get(0).getId());
    }

    @Test
    public void testFilterWorkflowTemplatesByBlacklistedActivities_WithClientId() {
        Mockito.when(clientsConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(Mockito.any(ScopeType.class), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(List.of(
                        ClientsConfigContainer.createClientsConfig(1, "test-client", ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("liveAgents", "bulkAgents")),
                        ClientsConfigContainer.createClientsConfig(1, Constants.DEFAULT_CLIENT_ID, ScopeType.client, ConfigNames.BLACKLISTED_ACTIVITIES, List.of("testAgents", "bulkAgents"))
                ));
        List<WorkflowTemplate> templates = List.of(
                WorkflowTemplateContainer.getWorkflowTemplate("testAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("liveAgents", "test location", PublishType.TEST_ONLY),
                WorkflowTemplateContainer.getWorkflowTemplate("bulkAgents", "test location", PublishType.TEST_ONLY)
        );
        List<WorkflowTemplate> resultantNodes = service.filterWorkflowTemplatesByConfig(ScopeType.client, "test-client", templates, ConfigNames.BLACKLISTED_ACTIVITIES);
        assertNotNull(resultantNodes);
        assertEquals(1, resultantNodes.size());
        assertEquals("testAgents", resultantNodes.get(0).getId());
    }
}
