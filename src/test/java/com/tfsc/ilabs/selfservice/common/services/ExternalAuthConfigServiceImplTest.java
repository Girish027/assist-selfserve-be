package com.tfsc.ilabs.selfservice.common.services;

import com.tfsc.ilabs.selfservice.common.models.ExternalServiceAuthConfig;
import com.tfsc.ilabs.selfservice.common.repositories.ExternalAuthConfigRepository;
import com.tfsc.ilabs.selfservice.common.services.impl.ExternalAuthConfigServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;

@RunWith(MockitoJUnitRunner.class)
public class ExternalAuthConfigServiceImplTest {

    @InjectMocks
    ExternalAuthConfigServiceImpl externalAuthConfigService = new ExternalAuthConfigServiceImpl();

    @Mock
    ExternalAuthConfigRepository externalAuthConfigRepository;

    @Mock
    ExternalServiceAuthConfig externalServiceAuthConfig = new ExternalServiceAuthConfig();

    @Before
    public void init(){
        Timestamp timestamp = new Timestamp(10000);
        externalServiceAuthConfig.setCreatedBy("test-user");
        externalServiceAuthConfig.setCreatedOn(timestamp);
        externalServiceAuthConfig.setLastUpdatedBy("test-user");
        externalServiceAuthConfig.setLastUpdatedOn(timestamp);
        Mockito.when(externalAuthConfigRepository.findByNameAndServiceId("test-name","test-service-id")).thenReturn(java.util.Optional.of(externalServiceAuthConfig));
    }

    @Test
    public void test_getValue() {
        String result = externalAuthConfigService.getValue("test-name", "test-service-id");
        Assert.assertNull(result);

    }
}