package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.testutils.ModelUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ResponseUtilsTest {

    @Test
    public void test_evaluateContract() throws IOException {
        Assert.assertNotNull(ModelUtils.getTestJsonNode());
        ResponseUtils.evaluateContract(ModelUtils.getTestJsonNode(),ModelUtils.getTestJsonNode(),ModelUtils.getTestJsonNode());
    }
}
