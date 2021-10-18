package com.tfsc.ilabs.selfservice.common.services.impl;

import com.google.common.collect.ImmutableMap;
import com.tfsc.ilabs.selfservice.common.models.ExternalServiceAuthConfig;
import com.tfsc.ilabs.selfservice.common.services.api.CommonService;
import com.tfsc.ilabs.selfservice.configpuller.model.Config;
import com.tfsc.ilabs.selfservice.workflow.models.Bookmark;
import com.tfsc.ilabs.selfservice.workflow.models.Menu;
import com.tfsc.ilabs.selfservice.workflow.models.Node;
import com.tfsc.ilabs.selfservice.workflow.models.NodeGroup;
import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
    private static Map<String, Class> ENTITY_CLASS_MAP;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void evictCache(String entityName) {
        initializeMap();
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        if (sessionFactory != null) {
            Cache cache = sessionFactory.getCache();
            Class cName = ENTITY_CLASS_MAP.get(entityName);
            if (cName != null) {
                cache.evict(cName);
                cache.evictRegion(cName.getName());
                cache.evictQueryRegions();
                logger.info("Evicted Cached Entity for {}", entityName);
            } else {
                cache.evictAllRegions();
                cache.evictAll();
                logger.info("Evicted all Cached Entities");
            }
        }
    }

    /**
     * This method creates map for Entities that require cache refresh on demand.
     * For any entity which is cached using second level cache and need refresh like config
     * then entry should be added here.
     */
    private static void initializeMap() {
        if (ENTITY_CLASS_MAP == null || ENTITY_CLASS_MAP.isEmpty()) {
            ENTITY_CLASS_MAP = ImmutableMap.<String, Class>builder()
                    .putAll(ImmutableMap.of(Config.class.getSimpleName(), Config.class, Node.class.getSimpleName(), Node.class, Menu.class.getSimpleName(), Menu.class, NodeGroup.class.getSimpleName(), NodeGroup.class, Bookmark.class.getSimpleName(), Bookmark.class))
                    .put("ExternalServiceAuthConfig", ExternalServiceAuthConfig.class)
                    .build();
        }
    }
}
