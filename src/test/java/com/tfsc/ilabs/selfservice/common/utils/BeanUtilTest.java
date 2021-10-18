package com.tfsc.ilabs.selfservice.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

/**
 * @author Sushil.Kumar
 */
@RunWith(MockitoJUnitRunner.class)
public class BeanUtilTest {

    @InjectMocks
    BeanUtil beanUtil;
    @Mock
    private ApplicationContext context;

    @Test
    public void getBeanTest(){
        Assert.assertSame(null, beanUtil.getBean(String.class));
    }

    @Test
    public void setApplicationContext(){
        Assert.assertNotNull(context);
        beanUtil.setApplicationContext(context);
    }
}