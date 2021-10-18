package com.tfsc.ilabs.selfservice.workflow.services.impl;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.workflow.models.*;
import com.tfsc.ilabs.selfservice.workflow.repositories.ClientsConfigRepository;
import com.tfsc.ilabs.selfservice.workflow.services.api.ClientsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientsConfigServiceImpl implements ClientsConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ClientsConfigServiceImpl.class);

    @Autowired
    private ClientsConfigRepository clientConfigRepository;

    @Override
    public List<Node> filterNodesByConfig(ScopeType scopeType, String scopeId, List<Node> nodes, ConfigNames configName) {
        ClientsConfig blacklistedGroupsClientConfig = getBlacklistedConfig(scopeType, scopeId, configName);
        List<Node> filteredNodes = nodes;
        if (blacklistedGroupsClientConfig != null) {
            List<String> blacklistedGroupIds = blacklistedGroupsClientConfig.getValues();
            logger.info("ClientsConfigServiceImpl::filterNodesByBlacklistedGroups:: Blacklisted Group Ids " + blacklistedGroupIds);
            filteredNodes = nodes.stream().filter(node -> !blacklistedGroupIds.contains(node.getNodeId())).collect(Collectors.toList());
        } else {
            logger.info("ClientsConfigServiceImpl::filterNodesByBlacklistedGroups:: No config present for given client: " + scopeId);
        }
        return filteredNodes;
    }

    @Override
    public List<WorkflowTemplate> filterWorkflowTemplatesByConfig(ScopeType scopeType, String scopeId, List<WorkflowTemplate> workflowTemplates, ConfigNames configName) {
        ClientsConfig blacklistedClientConfig = getBlacklistedConfig(scopeType, scopeId, configName);
        List<WorkflowTemplate> filteredWorkflowTemplates = workflowTemplates;
        if (blacklistedClientConfig != null) {
            List<String> blacklistedIds = blacklistedClientConfig.getValues();
            logger.info("ClientsConfigServiceImpl::filterWorkflowTemplatesByConfig:: " + configName + " Ids " + blacklistedIds);
            filteredWorkflowTemplates = workflowTemplates.stream().filter(workflowTemplate -> !blacklistedIds.contains(workflowTemplate.getId())).collect(Collectors.toList());
        } else {
            logger.info("ClientsConfigServiceImpl::filterWorkflowTemplatesByBlacklistedActivities:: No config present for given client: " + scopeId);
        }
        return filteredWorkflowTemplates;
    }

    private ClientsConfig getBlacklistedConfig(ScopeType scopeType, String scopeId, ConfigNames configName) {
        List<String> scopeIds = new ArrayList<>();
        scopeIds.add(scopeId);
        scopeIds.add(Constants.DEFAULT_CLIENT_ID);
        List<ClientsConfig> blacklistedGroupsClientsConfig = clientConfigRepository.findByScopeTypeAndScopeIdInAndConfigName(scopeType, scopeIds, configName);
        ClientsConfig config = blacklistedGroupsClientsConfig.stream()
                .filter(con -> con.getScopeId().equals(scopeId))
                .findFirst()
                .orElse(CollectionUtils.isEmpty(blacklistedGroupsClientsConfig) ? null : blacklistedGroupsClientsConfig.get(0));
        return config;
    }
}
