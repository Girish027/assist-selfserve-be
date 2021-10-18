package com.tfsc.ilabs.selfservice.common.services;

import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthBuilder;
import com.tfsc.ilabs.selfservice.common.services.impl.ExternalServiceAuthFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.tfsc.ilabs.selfservice.common.services.impl.AssistExternalAuthTokenBuilder.SERVICE_ASSIST;

@RunWith(MockitoJUnitRunner.class)
public class ExternalServiceAuthFactoryTest {
    @InjectMocks
    ExternalServiceAuthFactory externalServiceAuthFactory = new ExternalServiceAuthFactory();

    @Mock
    private ExternalAuthBuilder assistExternalAuthTokenBuilder;

    @Test
    public void test_getBuilder(){
        ExternalAuthBuilder result = externalServiceAuthFactory.getBuilder(SERVICE_ASSIST);
        Assert.assertEquals(assistExternalAuthTokenBuilder,result);
        result = externalServiceAuthFactory.getBuilder("something");
        Assert.assertNull(result);
    }
}