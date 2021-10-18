package com.tfsc.ilabs.selfservice.common.services;

import com.tfsc.ilabs.selfservice.common.services.impl.CommonServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class CommonServiceImplTest {

    @InjectMocks
    CommonServiceImpl commonService = new CommonServiceImpl();

    @Mock
    EntityManager entityManager;

    @Test(expected = NullPointerException.class)
    public void test_evictCache(){
        commonService.evictCache("test-entity");
    }

}