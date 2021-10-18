package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.common.services.impl.CommonServiceImpl;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.EXCLUDE_CACHE_FETCHFOR;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DbConfigTest {
    @InjectMocks
    DbConfig dbConfig;
    @Mock
    private DBConfigService dbConfigService;
    @Mock
    private CommonServiceImpl commonService;

    @Before
    public void init() {
        Mockito.when(dbConfigService.findByCode(EXCLUDE_CACHE_FETCHFOR, String.class))
                .thenReturn("a,b");
    }

    @Test
    public void test_init() {
        dbConfig.init();

        Mockito.verify(dbConfigService, Mockito.times(1))
                .findByCode(EXCLUDE_CACHE_FETCHFOR, String.class);
    }

    @Test
    public void test_evictConfigCacheAndPopulate() {
        dbConfig.evictConfigCacheAndPopulate();

        Mockito.verify(commonService, Mockito.times(1))
                .evictCache(anyString());
        Mockito.verify(dbConfigService, Mockito.times(1))
                .findByCode(EXCLUDE_CACHE_FETCHFOR, String.class);
    }
}
