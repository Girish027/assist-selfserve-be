package com.tfsc.ilabs.selfservice.action.models.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tfsc.ilabs.selfservice.action.models.ActionType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * Created by ravi.b on 05-06-2019.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionRestDefinition.class, name = "REST")
})
public abstract class ActionDefinition {

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private ActionType type;

    public ActionDefinition() {
        type = ActionType.REST;
    }
}
