package com.tfsc.ilabs.selfservice.action.models.definition;

import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ActionPathParamsConfig extends ActionConfig {
    private List<Map<String, String>> pathParams;

    private ActionPathParamsConfig() {
        this.type = ActionConfigType.PATH_PARAMS;
    }

    public ActionPathParamsConfig(List<Map<String, String>> pathParams) {
        this();
        this.pathParams = pathParams;
    }
}