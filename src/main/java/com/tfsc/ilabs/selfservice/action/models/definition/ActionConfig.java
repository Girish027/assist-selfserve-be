package com.tfsc.ilabs.selfservice.action.models.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionPollingConfig.class, name = "POLL"),
        @JsonSubTypes.Type(value = ActionPathParamsConfig.class, name = "PATH_PARAMS")
})
public abstract class ActionConfig {
    protected ActionConfigType type;
}
