package com.tfsc.ilabs.selfservice.workflow.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
public class WorkflowConfig {

    private PublishType publishType;
    private boolean fetchPageTemplateFromBE;
    private Map<String, List<String>> allowedActions;
    private String preFetchInputValidation;
    private JsonNode subHeader;
    private Boolean isPaginationEnabled;
    private Integer pageSize;
    private Boolean showSummaryFooter;
    private Boolean isServerSearchEnabled;
    private Boolean canDisabledEntitiesPerformActions;
    private String objectTranslator;

    public WorkflowConfig() {
        // for jackson
    }

    public WorkflowConfig(PublishType publishType, boolean fetchPageTemplateFromBE, Map<String, List<String>> allowedActions,
                          String preFetchInputValidation, JsonNode subHeader, Boolean isPaginationEnabled, Integer pageSize,
                          Boolean showSummaryFooter, Boolean isServerSearchEnabled,
                          Boolean canDisabledEntitiesPerformActions, String objectTranslator) {
        this.publishType = publishType;
        this.fetchPageTemplateFromBE = fetchPageTemplateFromBE;
        this.allowedActions = allowedActions;
        this.preFetchInputValidation = preFetchInputValidation;
        this.subHeader = subHeader;
        this.isPaginationEnabled = isPaginationEnabled;
        this.pageSize = pageSize;
        this.showSummaryFooter = showSummaryFooter;
        this.isServerSearchEnabled = isServerSearchEnabled;
        this.canDisabledEntitiesPerformActions = canDisabledEntitiesPerformActions;
        this.objectTranslator = objectTranslator;
    }
}
