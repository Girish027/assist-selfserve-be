package com.tfsc.ilabs.selfservice.common.dto;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserAudit {

    private String userId;

    private String clientId;

    private String accountId;

    private String entityName;

    private int entityId;

    private String action;

    private EntityInstanceStatus status;

    private String activityName;

    private DefinitionType entityAction;

    private String lastModifiedBy;
}
