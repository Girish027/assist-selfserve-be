package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.common.dto.UserAudit;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserAuditLogUtilsTest {


    @Mock
    UserAudit userAudit;

    @Mock
    EntityInstance entityInstance;

    @Test
    public void test_constructPublishUserAudit(){
        userAudit = new UserAudit("test-id","test-id","test-id",null,0,null,null,"test-activity-name",DefinitionType.CREATE,null);
        UserAudit result = UserAuditLogUtils.constructPublishUserAudit(entityInstance,"test-id","test-id","test-id","test-activity-name", DefinitionType.CREATE);
        Assert.assertEquals(userAudit,result);
    }
}
