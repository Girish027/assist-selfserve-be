package com.tfsc.ilabs.selfservice.common.utils;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger implements CacheEventListener<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(CacheLogger.class);

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        logger.info("Cache Key: {} | EventType: {} ", cacheEvent.getKey(),
                cacheEvent.getType());
        logger.debug("Cache Key: {} | EventType: {} | Old value: {} | New value: {}", cacheEvent.getKey(),
                cacheEvent.getType(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
