package com.tfsc.ilabs.selfservice.workflow.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode
public class UiSchema implements Serializable {

    @NotNull
    private String icon;

    @NotNull
    private String toolTip;

    @NotNull
    private boolean dashboard;

    @NotNull
    private String menuGroupName;

    @NotNull
    private int displayOrder;

    @NotNull
    private boolean isActive;

    @NotNull
    private Object uiOptions;

    public UiSchema() {
        // For jackson
    }
    public UiSchema(String icon, String toolTip, boolean dashboard, String menuGroupName, int displayOrder, boolean isActive, Object uiOptions) {
        this.icon = icon;
        this.toolTip = toolTip;
        this.dashboard = dashboard;
        this.menuGroupName = menuGroupName;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.uiOptions = uiOptions;
    }
}
