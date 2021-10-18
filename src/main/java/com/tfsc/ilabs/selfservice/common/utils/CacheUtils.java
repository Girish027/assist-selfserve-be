package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.dto.ApiConfig;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(CacheLogger.class);
    private static Map<String, Long> lastExecutedTimeMap = new HashMap<>();

    private CacheUtils() {
    }

    public static void evictAllApiCacheTimed(DBConfigService dbConfigService, String code) {
        Long cacheClearMaxtime = dbConfigService.findByCode(Constants.CACHE_CLEAR_MAXTIME, Long.class);
        Long currentTime = System.currentTimeMillis();

        if (cacheClearMaxtime == null) {
            cacheClearMaxtime = 60000L;
        }

        if (!lastExecutedTimeMap.containsKey(code)) {
            evictCache(dbConfigService, code);
        } else {
            Long lastExecutedTime = lastExecutedTimeMap.get(code);
            if (currentTime - lastExecutedTime > cacheClearMaxtime) {
                evictCache(dbConfigService, code);
            } else {
                logger.debug("cache max-time not reached");
            }
        }
    }

    public static Thread evictCache(DBConfigService dbConfigService, String cacheCode) {
        Thread t = new Thread(() -> evictCacheWithApiCall(dbConfigService, cacheCode));
        t.start();
        return t;
    }

    private static void evictCacheWithApiCall(DBConfigService dbConfigService, String cacheCode) {
        JavaType type = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ApiConfig.class);
        List<ApiConfig> urlDefinitions = dbConfigService.findByCode(cacheCode, type);

        if (urlDefinitions != null) {
            Long currentTime = System.currentTimeMillis();
            lastExecutedTimeMap.put(cacheCode, currentTime);

            urlDefinitions.forEach(urlDefinition -> {
                logger.debug("clearing cache: {}", urlDefinition.getUrl());

                HttpUtils.makePostOrPutCall(
                        urlDefinition.getUrl(),
                        urlDefinition.getMethod(),
                        urlDefinition.getHeaders(),
                        urlDefinition.getBody().toString()
                );
            });
        }
    }
}
