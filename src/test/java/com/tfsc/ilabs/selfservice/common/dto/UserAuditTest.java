package com.tfsc.ilabs.selfservice.common.dto;

import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstanceStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserAuditTest {

    private UserAudit userAudit = new UserAudit();

    @Test
    public void test_userAudit(){
        userAudit = new UserAudit("test-id","test-id","test-id","test-entity",1,"test-action", EntityInstanceStatus.DISCARDED,"test-activity-name", DefinitionType.CREATE,"test-last-modifiedBy");
        Assert.assertEquals("test-id",userAudit.getUserId());
        Assert.assertEquals("test-id",userAudit.getAccountId());
        Assert.assertEquals("test-id",userAudit.getClientId());
        Assert.assertEquals("test-entity",userAudit.getEntityName());
        Assert.assertEquals(1,userAudit.getEntityId());
        Assert.assertEquals("test-action",userAudit.getAction());
        Assert.assertEquals(EntityInstanceStatus.DISCARDED,userAudit.getStatus());
        Assert.assertEquals("test-activity-name",userAudit.getActivityName());
        Assert.assertEquals("test-last-modifiedBy",userAudit.getLastModifiedBy());
        Assert.assertEquals(DefinitionType.CREATE,userAudit.getEntityAction());
    }
}
