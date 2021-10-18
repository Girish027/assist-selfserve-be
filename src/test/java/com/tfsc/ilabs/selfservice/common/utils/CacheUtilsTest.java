package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.tfsc.ilabs.selfservice.configpuller.services.impl.DBConfigServiceImpl;
import com.tfsc.ilabs.selfservice.testcontainer.ConfigContainer;
import io.swagger.models.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class CacheUtilsTest {
    @Mock
    DBConfigServiceImpl dbConfigService;
    @Mock
    HttpResponse<String> httpResponse;

    @Test
    public void test_evictCache() throws IOException {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito
                .when(HttpUtils.makePostOrPutCall(
                        Mockito.any(String.class),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(Map.class),
                        Mockito.any(String.class)
                ))
                .thenReturn(httpResponse);
        Mockito.when(dbConfigService.findByCode(Mockito.any(String.class), Mockito.any(JavaType.class)))
                .thenReturn(ConfigContainer.getCacheConfig(Constants.CACHE_EXPIRE_CONFIG));

        Thread expectedThread = CacheUtils.evictCache(dbConfigService, Constants.CACHE_EXPIRE_CONFIG);
        assertTrue(expectedThread.isAlive());
    }
}
