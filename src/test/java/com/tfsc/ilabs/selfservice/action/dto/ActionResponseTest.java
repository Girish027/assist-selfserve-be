package com.tfsc.ilabs.selfservice.action.dto;

import com.tfsc.ilabs.selfservice.action.dto.response.ActionResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActionResponseTest {


    private ActionResponse actionResponse= new ActionResponse();

    private Integer testId = 2;
    @Test
    public  void test_actionResponse(){
        actionResponse.setId(testId);
        Assert.assertEquals(testId,actionResponse.getId());

    }
}