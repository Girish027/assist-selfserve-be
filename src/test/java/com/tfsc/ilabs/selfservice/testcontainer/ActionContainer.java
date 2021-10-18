package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.action.models.Action;
import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import com.tfsc.ilabs.selfservice.action.models.ActionExecutionMonitor;
import com.tfsc.ilabs.selfservice.action.models.ActionType;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionPathParamsConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionRestDefinition;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import io.swagger.models.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionContainer {

    public static String javascriptCode = "function translate( pageData, entityId,clientId , accountId, env){ var uiData= JSON.parse(pageData).tags_p0; var whiteList = env == \"TEST\" ? uiData.domainWhitelisting.stageWhitelist : uiData.domainWhitelisting.productionWhitelist; var tagData = {}; tagData.whitelistedDomains = whiteList; return JSON.stringify(tagData); }";

    public static Action createActionInstance() {

        ActionRestDefinition actionRestDefinition = new ActionRestDefinition();
        //actionRestDefinition.setServiceId("1");
        actionRestDefinition.setServiceId("tieAction");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        actionRestDefinition.setHeaders(headers);
        actionRestDefinition.setGetPathParamFromPageData(true);
        actionRestDefinition.setMethod(HttpMethod.PUT);
        actionRestDefinition.setRelativePath("/test/UI");
        actionRestDefinition.setUploadFile(false);
        Map<String, String> bodyDefinition = new HashMap<>();
        actionRestDefinition.setBodyDefinition(bodyDefinition);
        actionRestDefinition.setObjectTranslator("function translate( pageData, entityId,clientId , accountId, env){ var uiData= JSON.parse(pageData).tags_p0; var whiteList = env == \"TEST\" ? uiData.domainWhitelisting.stageWhitelist : uiData.domainWhitelisting.productionWhitelist; var tagData = {}; tagData.whitelistedDomains = whiteList; return JSON.stringify(tagData); }");

        actionRestDefinition.setIdLookupKeys("stageWhitelist");
        actionRestDefinition.setIdLookupRequired(true);
        JsonNode prefetchDefinition = null;
        try {
            prefetchDefinition = new ObjectMapper().readTree("{\"listPasswordPolicy\": {\"fetchFor\": \"listPasswordPolicy\", \"fetchType\": \"PAGE_DATA_VALUE\"}}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionRestDefinition.setPreFetchDefinition(prefetchDefinition);

        Action action = new Action();
        action.setId(1);
        action.setDefinition(actionRestDefinition);
        action.setDescription("new action");
        action.setType(ActionType.REST);
        Map<ActionConfigType, ActionConfig> configs = new HashMap<>();
        configs.put(ActionConfigType.PATH_PARAMS, new ActionPathParamsConfig(List.of(Map.of("key", "uploadOption"))));
        action.setConfigs(configs);
        return action;
    }

    public static Action createActionInstance_File() {

        ActionRestDefinition actionRestDefinition = new ActionRestDefinition();
        //actionRestDefinition.setServiceId("1");
        actionRestDefinition.setServiceId("tieAction");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        actionRestDefinition.setHeaders(headers);
        actionRestDefinition.setGetPathParamFromPageData(true);
        actionRestDefinition.setMethod(HttpMethod.PUT);
        actionRestDefinition.setRelativePath("/test/UI");
        actionRestDefinition.setUploadFile(true);
        Map<String, String> bodyDefinition = new HashMap<>();
        actionRestDefinition.setBodyDefinition(bodyDefinition);
        actionRestDefinition.setObjectTranslator("function translate( pageData, entityId,clientId , accountId, env){ return uiData }");
        actionRestDefinition.setIdLookupRequired(false);
        JsonNode prefetchDefinition = null;
        try {
            prefetchDefinition = new ObjectMapper().readTree("{\"listPasswordPolicy\": {\"fetchFor\": \"listPasswordPolicy\", \"fetchType\": \"PAGE_DATA_VALUE\"}}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionRestDefinition.setPreFetchDefinition(prefetchDefinition);

        Action action = new Action();
        action.setId(1);
        action.setDefinition(actionRestDefinition);
        action.setDescription("new action");
        action.setType(ActionType.REST);
        Map<ActionConfigType, ActionConfig> configs = new HashMap<>();
        configs.put(ActionConfigType.PATH_PARAMS, new ActionPathParamsConfig(List.of(Map.of("key", "uploadOption"))));
        action.setConfigs(configs);
        return action;
    }

    public static ActionExecutionMonitor getActionExecutionMonitor() {
        EntityTemplate entityTemplate = EntityTemplateContainer.createEntityTemplate("queue", 1);
        EntityInstance entityInstance = EntityInstanceContainer.createEntityInstance(entityTemplate, "name", 1, "label");
        return new ActionExecutionMonitor(createActionInstance(),
                WorkflowInstanceContainer.getWorkflowInstance(), entityInstance, Environment.DRAFT);
    }
}
