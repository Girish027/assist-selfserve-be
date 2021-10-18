package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.common.dto.UserAudit;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAuditLogUtils {
  private static Logger userAuditLogger = LoggerFactory.getLogger(Constants.USER_AUDIT_LOG);
  
  private UserAuditLogUtils() {
    // hide implicit public constructor
  }

  public static void logUserAudit(UserAudit userAudit) {
    if (userAudit != null) {
      ThreadLocalContext.setUserAndClientContext(userAudit.getUserId(), userAudit.getClientId(),
          userAudit.getAccountId());
      String userAuditString = BaseUtil.getJsonString(userAudit);
      if (userAuditString != null) {
        userAuditLogger.info(userAuditString);
      }
    }
  }

  public static UserAudit constructPublishUserAudit(EntityInstance entityInstance, String userId, String clientId,
      String accountId, String activityName, DefinitionType entityAction) {
   
    UserAudit userAudit = new UserAudit();
    if (EntityInstanceStatus.PROMOTED_TO_TEST == entityInstance.getStatus()) {
      userAudit.setAction(Constants.ACTION_TEST);
    } else if (EntityInstanceStatus.PROMOTED_TO_LIVE == entityInstance.getStatus()) {
      userAudit.setAction(Constants.ACTION_LIVE);
    } else if (EntityInstanceStatus.LIVE_PROMOTION_FAILED == entityInstance.getStatus()) {
      userAudit.setAction(Constants.STATUS_FAILED);
    } else if (EntityInstanceStatus.TEST_PROMOTION_FAILED == entityInstance.getStatus()) {
      userAudit.setAction(Constants.STATUS_FAILED);
    }
    userAudit.setUserId(userId);
    userAudit.setClientId(clientId);
    userAudit.setAccountId(accountId);
    userAudit.setEntityName(entityInstance.getName());
    userAudit.setEntityId(entityInstance.getId());
    userAudit.setStatus(entityInstance.getStatus());
    userAudit.setActivityName(activityName);
    userAudit.setEntityAction(entityAction);
    userAudit.setLastModifiedBy(entityInstance.getLastUpdatedBy());
    
    return userAudit;
  }
}
