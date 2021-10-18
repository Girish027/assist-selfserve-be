package com.tfsc.ilabs.selfservice.action.models.definition;

import com.tfsc.ilabs.selfservice.action.models.ActionConfigType;
import lombok.Data;

@Data
public class ActionPollingConfig extends ActionConfig {
    private int retryCount;
    private int retryInterval;  // in milliseconds

    private ActionPollingConfig() {
        this.type = ActionConfigType.POLL;
    }

    public ActionPollingConfig(int retryCount, int retryInterval) {
        this();
        this.retryCount = retryCount;
        this.retryInterval = retryInterval;
    }
}
