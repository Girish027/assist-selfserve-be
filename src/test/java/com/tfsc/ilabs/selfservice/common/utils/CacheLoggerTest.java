package com.tfsc.ilabs.selfservice.common.utils;

import org.ehcache.event.CacheEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Sushil.Kumar
 */
@RunWith(MockitoJUnitRunner.class)
public class CacheLoggerTest {

    @InjectMocks
    CacheLogger cacheLogger;
    @Mock
    CacheEvent cacheEvent;

    @Test
    public void testLog(){
        Assert.assertNotNull(cacheEvent);
        cacheLogger.onEvent(cacheEvent);
    }
}
