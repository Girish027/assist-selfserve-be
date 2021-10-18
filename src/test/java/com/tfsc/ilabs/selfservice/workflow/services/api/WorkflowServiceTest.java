package com.tfsc.ilabs.selfservice.workflow.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.tfsc.ilabs.selfservice.common.dto.ApiConfig;
import com.tfsc.ilabs.selfservice.common.exception.InvalidResourceException;
import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.exception.ResourceException;
import com.tfsc.ilabs.selfservice.common.models.*;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.ConfigDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.repositories.EntityInstanceRepository;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import com.tfsc.ilabs.selfservice.testcontainer.*;
import com.tfsc.ilabs.selfservice.testutils.TestConstants;
import com.tfsc.ilabs.selfservice.workflow.dto.request.WorkflowInstanceRequestDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.*;
import com.tfsc.ilabs.selfservice.workflow.models.*;
import com.tfsc.ilabs.selfservice.workflow.repositories.*;
import com.tfsc.ilabs.selfservice.workflow.services.impl.WorkflowServiceImpl;
import io.swagger.models.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author subhasish.c
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityContextUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class WorkflowServiceTest {
    @InjectMocks
    WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();

    @Mock
    WorkflowTemplateRepository workflowTemplateRepository;
    @Mock
    BookmarkRepository bookmarkRepository;
    @Mock
    WorkflowPageRepository workflowPageRepository;
    @Mock
    WorkflowInstanceRepository workflowInstanceRepository;
    @Mock
    EntityInstanceRepository entityInstanceRepository;
    @Mock
    PageService pageService;
    @Mock
    EntityService entityService;
    @Mock
    DBConfigService dbConfigService;
    @Mock
    NodeRepository nodeRepository;
    @Mock
    ClientsConfigRepository clientsConfigRepository;
    @Mock
    NodeGroupRepository nodeGroupRepository;
    @Mock
    MenuRepository menuRepository;
    @Mock
    ClientsConfigService clientsConfigService;

    @Before
    public void setup() {
        PowerMockito.mockStatic(SecurityContextUtil.class);
        PowerMockito.when(SecurityContextUtil.getAuthenticationToken()).thenReturn(new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList("")));
        Mockito.when(dbConfigService.findByCode(Constants.CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT, Integer.class)).thenReturn(90);
    }

    @Test
    public void testGetWorkflowTemplates() {

        List<WorkflowTemplate> workflowTemplates = new ArrayList<>();

        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setId("adf");
        workflowTemplate.setTitle("ADF");
        workflowTemplate.setDescription("Agent Disposition Form");
        workflowTemplate.setEntityLocation(WorkflowTemplateContainer.getEntityLocation("adf_p0.queues"));
        workflowTemplate.setEntityTemplate(EntityTemplateContainer.createEntityTemplate("queue", 1));
        workflowTemplate.setModel(WorkflowModel.MENU);
        workflowTemplate.setPromotionApproval(false);
        workflowTemplate.setType(WorkflowType.NON_SINGLETON);
        workflowTemplates.add(workflowTemplate);

        Service service = new Service("ohs", "ohs", "ohs");
        ServiceUrls serviceUrl = new ServiceUrls(service, Environment.TEST, "http://ohs.com/", new TextNode(""));

        List<Bookmark> bookmarks = new ArrayList<>();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1);
        bookmark.setDescription("bookmark description");
        bookmark.setDisplayLabel("bookmark label");
        bookmark.setRelativeUrl("/relativeURL");
        bookmark.setServiceUrl(serviceUrl);
        bookmarks.add(bookmark);

        WorkflowPage workflowPage = WorkflowPageContainer.createWorkflowPage();
        List<WorkflowPage> workflowPages = new ArrayList<>();
        workflowPages.add(workflowPage);

        Mockito.when(bookmarkRepository.findAll()).thenReturn(bookmarks);
        Mockito.when(workflowTemplateRepository.findAll()).thenReturn(workflowTemplates);
        Mockito.when(workflowPageRepository.findAllByWorkflowTemplate(workflowTemplate)).thenReturn(workflowPages);
        Mockito.when(clientsConfigService.filterWorkflowTemplatesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(workflowTemplates);

        // Calling the getWorkflowTemplates method
        Map<String, WorkflowTemplateDTO> workflowTemplateDTOs = workflowServiceImpl.getActivities(ScopeType.client, "test_client");
        assertEquals(1, workflowTemplateDTOs.size());
        Map<String, WorkflowTemplateDTO> bookmarkDTOs = workflowServiceImpl.getBookmarks();
        assertEquals(1, bookmarkDTOs.size());

        WorkflowTemplateDTO activityDTO = workflowTemplateDTOs.values().stream()
                .filter(workflowTemplateDTO -> workflowTemplateDTO.getType().equals(WorkflowTileType.ACTIVITY))
                .findFirst().get();
        WorkflowTemplateDTO bookMarkDTO = bookmarkDTOs.values().stream()
                .filter(bookmarkDTO -> bookmarkDTO.getType().equals(WorkflowTileType.BOOKMARK))
                .findFirst().get();

        // Asserting the output values for activity
        assertEquals(activityDTO.getId(), workflowTemplate.getId());
        assertEquals(activityDTO.getTitle(), workflowTemplate.getTitle());
        assertEquals(activityDTO.getDescription(), workflowTemplate.getDescription());

        // Asserting the output values for bookmark
        assertEquals(bookMarkDTO.getId(), bookmark.getId().toString());
        assertEquals(bookMarkDTO.getTitle(), bookmark.getDisplayLabel());
        assertEquals(bookMarkDTO.getDescription(), bookmark.getDescription());
    }

    @Test
    public void testGetWorkflowInstances() {
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        workflowInstances.add(WorkflowInstanceContainer.getWorkflowInstance());
        Mockito.when(workflowInstanceRepository.findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn(workflowInstances);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString())).thenReturn(new ConfigDTO(Constants.CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT, "90", ConfigType.UI_APP, true));
        Mockito.when(nodeRepository.findByNodeIdAndNodeType(Mockito.anyString(), Mockito.any())).thenReturn(new Node("id", NodeType.BOOKMARK, -1));
        // Calling getWorkflowInstances method
        List<WorkflowInstanceDTO> workflowInstanceDTOs = workflowServiceImpl.getWorkflowInstances("client",
                "accountId");

        // Asserting the output values
        assertEquals(workflowInstances.get(0).getStatus(), workflowInstanceDTOs.get(0).getStatus());
    }

    @Test
    public void testGetWorkflowInstancesOrderByStatus() {
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        workflowInstances.add(WorkflowInstanceContainer.getWorkflowInstance());
        Mockito.when(workflowInstanceRepository.findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any())).thenReturn(workflowInstances);
        Mockito.when(dbConfigService.findByCode(Mockito.anyString())).thenReturn(new ConfigDTO(Constants.CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT, "90", ConfigType.UI_APP, true));
        // Calling method getWorkflowInstancesOrderByStatus
        Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> workFlowStatusDTOMap = workflowServiceImpl
                .getWorkflowInstancesOrderByStatus("client", "accountId");

        // Asserting the output
        assertEquals("Optional[DRAFT_SAVE]", workFlowStatusDTOMap.keySet().stream().findFirst().toString());
    }

    @Test
    public void testGetWorkflowInstancesOrderByStatusWithWorkFlowId() {
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        workflowInstances.add(WorkflowInstanceContainer.getWorkflowInstance());
        Optional<WorkflowTemplate> optional = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance().getWorkflowTemplate());
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString())).thenReturn(optional);
        Mockito.when(workflowInstanceRepository.findByWorkflowTemplateAndClientIdAndAccountId(Mockito.any(),
                Mockito.anyString(), Mockito.anyString())).thenReturn(workflowInstances);

        // Calling method getWorkflowInstancesOrderByStatus
        Map<WorkflowInstanceStatus, List<WorkflowInstanceDTO>> workFlowStatusDTOMap = workflowServiceImpl
                .getWorkflowInstancesOrderByStatus("client", "accountId", "1");

        // Asserting the output
        assertEquals("Optional[DRAFT_SAVE]", workFlowStatusDTOMap.keySet().stream().findFirst().toString());
    }

    @Test
    public void testGetWorkflowInstancesWithWorkFlow() {
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        workflowInstances.add(WorkflowInstanceContainer.getWorkflowInstance());
        Optional<WorkflowTemplate> optional = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance().getWorkflowTemplate());
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString())).thenReturn(optional);
        Mockito.when(workflowInstanceRepository.findByWorkflowTemplateAndClientIdAndAccountId(Mockito.any(),
                Mockito.anyString(), Mockito.anyString())).thenReturn(workflowInstances);

        // calling the getWorkflowInstances method
        List<WorkflowInstanceDTO> workflowInstanceDTOs = workflowServiceImpl.getWorkflowInstances("client", "accountId",
                "1");

        // Asserting the output
        assertEquals(workflowInstances.get(0).getStatus(), workflowInstanceDTOs.get(0).getStatus());
    }

    @Test
    public void testGetInstance() {
        Optional<WorkflowInstance> input = Optional.of(WorkflowInstanceContainer.getWorkflowInstance());
        Mockito.when(workflowInstanceRepository.findById(Mockito.any())).thenReturn(input);

        // calling the getInstance method
        Optional<WorkflowInstance> woOptional = workflowServiceImpl
                .getInstance(WorkflowInstanceContainer.getWorkflowInstance().getId());

        // Asserting the output
        assertEquals(woOptional.get().getId(), input.get().getId());
    }

    @Test
    public void testMarkWorkflowInstanceStatus() {
        assertNotNull(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE));
        // calling the markWorkflowInstanceStatusRunning method
        workflowServiceImpl.markWorkflowInstanceStatusRunning(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE));

        // calling the markWorkflowInstanceStatusFailed method
        workflowServiceImpl.markWorkflowInstanceStatusFailed(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE));

        // calling the markWorkflowInstanceStatusDiscarded method
        workflowServiceImpl.markWorkflowInstanceStatusDiscarded(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE));

        // calling the markWorkflowInstanceStatusCompleted method
        workflowServiceImpl.markWorkflowInstanceStatusCompleted(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_TEST));
    }

    @Test
    public void testDeleteWorkflowInstance() {
        Optional<WorkflowInstance> optionalWorkflowInstance = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE));
        //Mockito.when(ssAuthenticationToken.getUserId()).thenReturn("dummy");
        Mockito.when(workflowInstanceRepository.findById(Mockito.anyInt())).thenReturn(optionalWorkflowInstance);
        List<EntityInstance> entityInstances = new ArrayList<>();

        entityInstances.add(EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"));
        Mockito.when(entityService.getEntityInstances(Mockito.any(WorkflowInstance.class))).thenReturn(entityInstances);
        // calling the deleteWorkflowInstance method
        workflowServiceImpl.deleteWorkflowInstance(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE).getId(), Environment.DRAFT);
        assertNotNull(entityInstances);
    }

    @Test
    public void testCreateWorkflowInstance() {
        List<NameLabel> nameLabelList = new ArrayList<>();
        NameLabel nameLabel = new NameLabel();
        nameLabel.setLabel("entity12");
        nameLabel.setName("entity12");
        nameLabelList.add(nameLabel);
        Optional<WorkflowTemplate> optionalWOptional = Optional
                .of(WorkflowTemplateContainer.getSingletonWorkflow("entity"));
        Mockito.when(workflowTemplateRepository.findById(Mockito.any())).thenReturn(optionalWOptional);

        Optional<PageTemplate> optionalPageTemplate = Optional.of(PageTemplateContainer.getPageTemplateList().get(0));
        Mockito.when(pageService.findById(Mockito.any())).thenReturn(optionalPageTemplate);

        Mockito.when(entityService.isLockAvailable(Mockito.anyString(), Mockito.any(WorkflowTemplate.class),
                Mockito.any(WorkflowInstance.class))).thenReturn(true);

        Mockito.when(workflowInstanceRepository.save(Mockito.any(WorkflowInstance.class)))
                .thenReturn(WorkflowInstanceContainer.getWorkflowInstance());

        Mockito.when(nodeRepository.findByNodeIdAndNodeType(Mockito.anyString(), Mockito.any())).thenReturn(new Node("id", NodeType.WORKFLOW, -1));

        // Calling createWorkflowInstance method
        WorkflowInstanceDTO workflowInstanceDTO = workflowServiceImpl.createWorkflowInstance("client", "account", "1",
                new WorkflowInstanceRequestDTO(WorkflowInstanceContainer.getWorkflowInstance().getCurrentPageTemplate().getId(), nameLabelList), DefinitionType.CREATE);

        // Asserting the output
        assertEquals(workflowInstanceDTO.getEntityInstances().get(0).getName(), nameLabel.getName());
        assertEquals(workflowInstanceDTO.getEntityInstances().get(0).getLabel(), nameLabel.getLabel());
    }

    @Test
    public void testReleaseEditLock() {
        Optional<WorkflowInstance> input = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT));
        PowerMockito.when(SecurityContextUtil.getCurrentUserId()).thenReturn(Constants.DEFAULT_USER);
        Mockito.when(workflowInstanceRepository.findById(Mockito.any())).thenReturn(input);
        assertNotNull(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT).getId());
        // Calling createWorkflowInstance method
        workflowServiceImpl.releaseEditLock(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT).getId());
    }

    @Test
    public void testAcquireEditLock() {
        Optional<WorkflowInstance> input = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT));
        PowerMockito.when(SecurityContextUtil.getCurrentUserId()).thenReturn(Constants.DEFAULT_USER);
        Mockito.when(workflowInstanceRepository.findById(Mockito.any())).thenReturn(input);
        assertNotNull(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT).getId());
        // Calling acquireEditLock method
        workflowServiceImpl.acquireEditLock(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT).getId());
    }

    @Test
    public void testGetWorkflowDefintion() {
        WorkflowTemplate inputWorkFlowTemplate = WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues");
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(inputWorkFlowTemplate));
        Mockito.when(workflowPageRepository.findAllByWorkflowTemplateAndPageTemplateType(Mockito.any(WorkflowTemplate.class),
                Mockito.any(DefinitionType.class))).thenReturn(WorkflowPageContainer.createWorkflowPageList());
        // Calling getWorkflowDefintion method
        WorkflowDefinitionDTO workflowDefinitionDTO = workflowServiceImpl
                .getWorkflowDefintion(TestConstants.WORKFLOW_ID, TestConstants.TYPE);
        // Asserting the output
        assertNotNull(workflowDefinitionDTO.getPages());
        assertNotNull(workflowDefinitionDTO.getSchemaEntities());
    }

    @Test
    public void testGetWorkflowDefintionReturnsNoResourceException() {
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        // Calling getWorkflowDefintion method

        try {
            workflowServiceImpl.getWorkflowDefintion(TestConstants.WORKFLOW_ID, TestConstants.TYPE);
            fail();
        } catch (Exception e) {
            assertEquals(NoSuchResourceException.class, e.getClass());
        }
    }

    @Test
    public void testGetWorkflowDefintionThrowsEInvalidTyperror() {
        try {
            workflowServiceImpl.getWorkflowDefintion(TestConstants.WORKFLOW_ID, TestConstants.WORKFLOW_ID);
            fail();
        } catch (Exception e) {
            assertEquals(InvalidResourceException.class, e.getClass());
        }
    }

    @Test
    public void testGetWorkflowMetaData() {
        WorkflowTemplate inputWorkFlowTemplate = WorkflowTemplateContainer.getSingletonWorkflow("adf_p0.queues");
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(inputWorkFlowTemplate));
        Mockito.when(workflowPageRepository.findAllByWorkflowTemplate(Mockito.any(WorkflowTemplate.class)))
                .thenReturn(WorkflowPageContainer.createWorkflowPageList());
        // Calling getWorkflowMetaData method
        WorkFlowMetaDTO workflowMetaDTO = workflowServiceImpl.getWorkflowMetaData(TestConstants.WORKFLOW_ID);
        // Asserting the output
        assertNotNull(workflowMetaDTO.getPageTypes());
        assertNotNull(workflowMetaDTO.getWorkflow());
    }

    @Test
    public void testGetWorkflowMetaDataReuturnsNoSuchResourceException() {
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        // Calling getWorkflowMetaData method
        try {
            workflowServiceImpl.getWorkflowMetaData(TestConstants.WORKFLOW_ID);
            fail();
        } catch (Exception e) {
            assertEquals(NoSuchResourceException.class, e.getClass());
        }
    }

    @Test
    public void testCreateWorkflowInstance_has_PendingInstance() {

        Optional<WorkflowTemplate> optionalWOptional = Optional
                .of(WorkflowTemplateContainer.getSingletonWorkflow("entity"));
        Mockito.when(workflowTemplateRepository.findById(Mockito.any())).thenReturn(optionalWOptional);

        Optional<PageTemplate> optionalPageTemplate = Optional.of(PageTemplateContainer.getPageTemplateList().get(0));
        Mockito.when(pageService.findById(Mockito.any())).thenReturn(optionalPageTemplate);

        Mockito.when(entityService.isLockAvailable(Mockito.anyString(), Mockito.any(WorkflowTemplate.class),
                Mockito.any(WorkflowInstance.class))).thenReturn(true);
        WorkflowInstance workflowInstance = WorkflowInstanceContainer.getWorkflowInstance();
        Mockito.when(workflowInstanceRepository.save(Mockito.any(WorkflowInstance.class)))
                .thenReturn(workflowInstance);
        Mockito.when(entityInstanceRepository.findAllByNameAndEntityTemplateOrderByIdDesc(Mockito.anyString(), Mockito.any(EntityTemplate.class)))
                .thenReturn(List.of(EntityInstanceContainer.createAvailableEntityInstance(null, "1", 1, workflowInstance)));

        Mockito.when(nodeRepository.findByNodeIdAndNodeType(Mockito.anyString(), Mockito.any())).thenReturn(new Node("id", NodeType.WORKFLOW, -1));

        WorkflowInstanceRequestDTO workflowInstanceRequestDTO = new WorkflowInstanceRequestDTO(workflowInstance.getCurrentPageTemplate().getId(), WorkflowTemplateContainer.getNameLabelList());
        // Calling createWorkflowInstance method
        WorkflowInstanceDTO workflowInstanceDTO = workflowServiceImpl.createWorkflowInstance("client", "account", "1",
                workflowInstanceRequestDTO, DefinitionType.CREATE);

        // Asserting the output
        assertEquals("entity12", workflowInstanceDTO.getEntityInstances().get(0).getName());
        assertEquals("entity12", workflowInstanceDTO.getEntityInstances().get(0).getLabel());
    }

    @Test
    public void test_getParentNodes_ClientAndDefaultPresent() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1)
        );
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response =  workflowServiceImpl.getParentNodes(ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(1, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(1).getTitle());
    }

    @Test
    public void test_getParentNodes_ClientPresent() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1)
        );
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response =  workflowServiceImpl.getParentNodes(ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(1, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(1).getTitle());
    }

    @Test
    public void test_getParentNodes_DefaultPresent() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("2", "Queues"))
        );
        List<Node> nodes = List.of(
                NodeContainer.createNode("2", NodeType.GROUP, -1)
        );
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response =  workflowServiceImpl.getParentNodes(ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(1, response.getNodes().size());
        assertEquals("Queues", response.getNodes().get(2).getTitle());
    }

    @Test
    public void test_getParentNodes_NoConfig() throws Exception {
        List<Node> nodes = List.of(
                NodeContainer.createNode("1", NodeType.GROUP, -1),
                NodeContainer.createNode("2", NodeType.GROUP, -1),
                NodeContainer.createNode("3", NodeType.GROUP, -1)
        );
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(nodes);
        Mockito.when(nodeGroupRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response =  workflowServiceImpl.getParentNodes(ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(3, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(3).getTitle());
    }

    @Test
    public void test_getParentNodes() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById(Mockito.anyString())).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        List<Node> nodes = List.of(
                NodeContainer.createNode("3", NodeType.GROUP, -1)
        );
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response = workflowServiceImpl.getParentNodes(ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(1, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(3).getTitle());
    }

    @Test
    public void test_getParentNodes_nullScopeType() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById("1")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        Mockito.when(nodeGroupRepository.findById("2")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("2", "Advanced Admin"))
        );
        Mockito.when(nodeGroupRepository.findById("3")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("3", "Queues"))
        );
        NodeResponseDTO response = workflowServiceImpl.getParentNodes(null, "test-default");
        assertNotNull(response);
        assertEquals(3, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(1).getTitle());
    }

    @Test
    public void test_getParentNodes_emptyScopeId() throws Exception {
        Mockito.when(nodeRepository.findAllByParentId(Constants.PARENT_ID)).thenReturn(
                List.of(
                        NodeContainer.createNode("1", NodeType.GROUP, -1),
                        NodeContainer.createNode("2", NodeType.GROUP, -1),
                        NodeContainer.createNode("3", NodeType.GROUP, -1)
                )
        );
        Mockito.when(nodeGroupRepository.findById("1")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        Mockito.when(nodeGroupRepository.findById("2")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("2", "Advanced Admin"))
        );
        Mockito.when(nodeGroupRepository.findById("3")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("3", "Queues"))
        );
        NodeResponseDTO response = workflowServiceImpl.getParentNodes(ScopeType.client, "");
        assertNotNull(response);
        assertEquals(3, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(1).getTitle());
    }

    @Test
    public void test_refreshWorkflowInstances() {
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_CLEAR_MAXTIME), Mockito.any(Class.class))).thenReturn(null);
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_EXPIRE_CONFIG), Mockito.any(Class.class))).thenReturn(getAPIConfigList());
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_EVICT_ON_INSTANCE_API), Mockito.any(Class.class))).thenReturn(true);
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT), Mockito.any(Class.class))).thenReturn(90);
        Mockito.when(workflowInstanceRepository.findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(Date.class))).thenReturn(getWorkflowList());
        Mockito.when(nodeRepository.findByNodeIdAndNodeType(Mockito.anyString(), Mockito.any(NodeType.class))).thenReturn(NodeContainer.createNode("2", NodeType.GROUP, 2));
        List<WorkflowInstanceDTO> list = workflowServiceImpl.refreshWorkflowInstances("test-default", "test-default");
        assertNotNull(list);
        assertEquals(5, list.size());
    }

    @Test
    public void testDeleteWorkflowInstance_UpdateParentOnDiscard() {
        Optional<WorkflowInstance> optionalWorkflowInstance = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE));
        Optional<WorkflowInstance> optionalParentInstance = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(PublishType.LIVE_ONLY, "parent"));
        PowerMockito.when(SecurityContextUtil.getAuthenticationToken()).thenReturn(new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList("")));
        Mockito.when(workflowInstanceRepository.findById(Mockito.eq(1))).thenReturn(optionalWorkflowInstance);
        Mockito.when(workflowInstanceRepository.findById(Mockito.eq(2))).thenReturn(optionalParentInstance);
        List<EntityInstance> entityInstances = new ArrayList<>();

        entityInstances.add(EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"));
        Mockito.when(entityService.getEntityInstances(Mockito.any(WorkflowInstance.class))).thenReturn(entityInstances);
        // calling the deleteWorkflowInstance method
        workflowServiceImpl.deleteWorkflowInstance(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_SAVE).getId(), Environment.DRAFT);
        assertNotNull(entityInstances);
    }

    @Test(expected = ResourceException.class)
    public void testDeleteWorkflowInstance_throwException_1() {
        Optional<WorkflowInstance> optionalWorkflowInstance = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT));
        PowerMockito.when(SecurityContextUtil.getAuthenticationToken()).thenReturn(new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList("")));
        Mockito.when(workflowInstanceRepository.findById(Mockito.eq(1))).thenReturn(optionalWorkflowInstance);
        List<EntityInstance> entityInstances = new ArrayList<>();

        entityInstances.add(EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"));
        Mockito.when(entityService.getEntityInstances(Mockito.any(WorkflowInstance.class))).thenReturn(entityInstances);
        // calling the deleteWorkflowInstance method
        workflowServiceImpl.deleteWorkflowInstance(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.DRAFT_EDIT).getId(), Environment.DRAFT);
    }

    @Test(expected = ResourceException.class)
    public void testDeleteWorkflowInstance_throwException_2() {
        Optional<WorkflowInstance> optionalWorkflowInstance = Optional
                .of(WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE));
        PowerMockito.when(SecurityContextUtil.getAuthenticationToken()).thenReturn(new SSAuthenticationToken(AuthorityUtils.commaSeparatedStringToAuthorityList("")));
        Mockito.when(workflowInstanceRepository.findById(Mockito.eq(1))).thenReturn(optionalWorkflowInstance);
        List<EntityInstance> entityInstances = new ArrayList<>();
        entityInstances.add(EntityInstanceContainer.createEntityInstance(null, "name", 1, "label"));
        Mockito.when(entityService.getEntityInstances(Mockito.any(WorkflowInstance.class))).thenReturn(entityInstances);
        // calling the deleteWorkflowInstance method
        workflowServiceImpl.deleteWorkflowInstance(
                WorkflowInstanceContainer.getWorkflowInstance(WorkflowInstanceStatus.PROMOTING_TO_LIVE).getId(), Environment.TEST);
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_getWorkflowInstanceIdForDraftEntity_Exception_NoInstance(){
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(WorkflowTemplateContainer.getWorkflow("testlocation", PublishType.LIVE_ONLY)));
        Mockito.when(workflowInstanceRepository
                .findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyList(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new ArrayList<>());
        workflowServiceImpl.getWorkflowInstanceIdForDraftEntity("test-default", "test", "queues", "Queues", "temp_id");
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_getWorkflowInstanceIdForDraftEntity_Exception_InstanceInTerminalState(){
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(WorkflowTemplateContainer.getWorkflow("testlocation", PublishType.LIVE_ONLY)));
        WorkflowInstance instance = WorkflowInstanceContainer.getWorkflowInstance(PublishType.LIVE_ONLY, "instance");
        instance.setStatus(WorkflowInstanceStatus.PROMOTED_TO_LIVE);
        Mockito.when(workflowInstanceRepository
                .findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyList(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(List.of(instance));
        workflowServiceImpl.getWorkflowInstanceIdForDraftEntity("test-default", "test", "queues", "Queues", "temp_id");
    }

    @Test
    public void test_getWorkflowInstanceIdForDraftEntity(){
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(WorkflowTemplateContainer.getWorkflow("testlocation", PublishType.LIVE_ONLY)));
        WorkflowInstance instance = WorkflowInstanceContainer.getWorkflowInstance(PublishType.LIVE_ONLY, "instance");
        Mockito.when(workflowInstanceRepository
                .findByClientIdAndAccountIdAndStatusInAndEntityInstancesNameAndWorkflowTemplateIdOrderByIdDesc(Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyList(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(List.of(instance));
        EntityDataResponseDTO response = workflowServiceImpl.getWorkflowInstanceIdForDraftEntity("test-default", "test", "queues", "Queues", "temp_id");
        assertEquals(Optional.of(1), Optional.of(response.getWorkflowInstanceId()));
    }

    @Test
    public void test_getAllNodes() {
        Mockito.when(menuRepository.findAllByMenuGroupName(Mockito.anyString())).thenReturn(
            List.of(MenuContainer.getMenu(1,NodeContainer.createNode("1", NodeType.GROUP, -1)),
                    MenuContainer.getMenu(2,NodeContainer.createNode("2", NodeType.GROUP, -1)),
                    MenuContainer.getMenu(3,NodeContainer.createNode("3", NodeType.GROUP, -1)))
        );
        Mockito.when(nodeGroupRepository.findById("1")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("1", "Tags"))
        );
        Mockito.when(nodeGroupRepository.findById("2")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("2", "Advanced Admin"))
        );
        Mockito.when(nodeGroupRepository.findById("3")).thenReturn(
                Optional.of(NodeGroupContainer.createNodeGroup("3", "Queues"))
        );
        List<Node> nodes = List.of(NodeContainer.createNode("1", NodeType.GROUP, -1));
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        Mockito.when(clientsConfigService.filterNodesByConfig(Mockito.any(ScopeType.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(ConfigNames.class)))
                .thenReturn(nodes);
        NodeResponseDTO response =  workflowServiceImpl.getAllNodes("nav", ScopeType.client, "test-default");
        assertNotNull(response);
        assertEquals(1, response.getNodes().size());
        assertEquals("Tags", response.getNodes().get(1).getTitle());
    }

    @Test
    public void test_getWorkflowTemplate() {
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(WorkflowTemplateContainer.getWorkflow("test", PublishType.LIVE_ONLY)));
        WorkflowTemplate template = workflowServiceImpl.getWorkflowTemplate("1");
        assertNotNull(template);
        assertEquals(WorkflowType.SINGLETON, template.getType());
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_getWorkflowTemplate_Exception() {
        Mockito.when(workflowTemplateRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
        workflowServiceImpl.getWorkflowTemplate("1");
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_getWorkflowInstanceById_Exception() {
        Mockito.when(workflowInstanceRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        workflowServiceImpl.getWorkflowInstanceById(1);
    }

    @Test
    public void test_getWorkflowInstanceById() {
        Mockito.when(workflowInstanceRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getWorkflowInstance(1)));
        workflowServiceImpl.getWorkflowInstanceById(1);
    }

    @Test
    public void test_getWorkflowInstancesForClient(){
        Mockito.when(dbConfigService.findByCode(Mockito.eq("public void test_max_instances_days_limit"), Mockito.any(Class.class)))
                .thenReturn(90);
        Mockito.when(workflowInstanceRepository.findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(Date.class))).thenReturn(getWorkflowList());
        Map<SummaryStatus, Long> result = workflowServiceImpl.getWorkflowInstancesCountSummary("test-default", "account-default");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void test_save(){
        Mockito.when(workflowInstanceRepository.save(Mockito.any(WorkflowInstance.class)))
                .thenReturn(WorkflowInstanceContainer.getWorkflowInstance(1));
        Mockito.verify(workflowInstanceRepository, Mockito.atMost(1)).save(Mockito.any(WorkflowInstance.class));
        workflowServiceImpl.save(WorkflowInstanceContainer.getWorkflowInstance(1));
    }

    @Test
    public void test_validateAndGetWorkflowInstance(){
        Mockito.when(workflowInstanceRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(WorkflowInstanceContainer.getWorkflowInstance(1)));
        WorkflowInstance response = workflowServiceImpl.validateAndGetWorkflowInstance("clientId", "accountId" ,1);
        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test(expected = NoSuchResourceException.class)
    public void test_validateAndGetWorkflowInstance_Exception(){
        Mockito.when(workflowInstanceRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        workflowServiceImpl.validateAndGetWorkflowInstance("test-default", "test-default" ,1);
    }

    @Test
    public void test_pollWorkflowInstances(){
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_EVICT_ON_INSTANCE_API), Mockito.any(Class.class)))
                .thenReturn(true);
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_CLEAR_MAXTIME), Mockito.any(Class.class))).thenReturn(null);
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_EXPIRE_CONFIG), Mockito.any(Class.class))).thenReturn(getAPIConfigList());
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CACHE_EVICT_ON_INSTANCE_API), Mockito.any(Class.class))).thenReturn(true);
        Mockito.when(dbConfigService.findByCode(Mockito.eq(Constants.CONFIG_KEY_MAX_INSTANCES_FOR_REFRESH_MINUTES_LIMIT), Mockito.any(Class.class))).thenReturn(90);
        Mockito.when(workflowInstanceRepository
                .findByClientIdAndAccountIdAndHiddenAndLastUpdatedOnAfterOrderByLastUpdatedOnDesc(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(Date.class)))
                .thenReturn(List.of(
                        WorkflowInstanceContainer.getWorkflowInstance(1),
                        WorkflowInstanceContainer.getWorkflowInstance(2),
                        WorkflowInstanceContainer.getWorkflowInstance(3),
                        WorkflowInstanceContainer.getWorkflowInstance(4),
                        WorkflowInstanceContainer.getWorkflowInstance(5)
                ));
        Mockito.when(nodeRepository.findByNodeIdAndNodeType(Mockito.anyString(), Mockito.any(NodeType.class)))
                .thenReturn(NodeContainer.createNode("1", NodeType.GROUP, 1));
        List<WorkflowInstanceDTO> results = workflowServiceImpl.pollWorkflowInstances("clientId", "accountId");
        assertNotNull(results);
        assertEquals(5, results.size());
    }

    private List<ApiConfig> getAPIConfigList() {
        List<ApiConfig> urlConfigList = new ArrayList<>();
        ApiConfig config1 = new ApiConfig();
        config1.setUrl("/self-serve/api/api-cache/expire");
        config1.setMethod(HttpMethod.POST);
        config1.setHeaders(Map.of("Authorization", "123456789"));
        config1.setBody(new ObjectMapper().createObjectNode());
        ApiConfig config2 = new ApiConfig();
        config2.setUrl("/self-serve/api/api-cache/expire");
        config2.setMethod(HttpMethod.POST);
        config2.setHeaders(Map.of("Authorization", "965421"));
        config2.setBody(new ObjectMapper().createObjectNode());
        urlConfigList.add(config1);
        urlConfigList.add(config2);
        return urlConfigList;
    }

    private List<WorkflowInstance> getWorkflowList() {
        List<WorkflowInstance> workflowList = new ArrayList<>();
        workflowList.add(WorkflowInstanceContainer.getWorkflowInstance(1));
        workflowList.add((WorkflowInstanceContainer.getWorkflowInstance(3)));
        workflowList.add((WorkflowInstanceContainer.getWorkflowInstance(2)));
        workflowList.add((WorkflowInstanceContainer.getWorkflowInstance(4)));
        workflowList.add((WorkflowInstanceContainer.getWorkflowInstance(5)));
        return workflowList;
    }


}