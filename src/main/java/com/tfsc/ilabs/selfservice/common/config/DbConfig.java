package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.common.services.impl.CommonServiceImpl;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.model.Config;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;

@Component("dbConfig")
public class DbConfig {
    private static ArrayList<String> excludeListForCaching;

    @Autowired
    private DBConfigService dbConfigService;
    @Autowired
    private CommonServiceImpl commonService;

    public static ArrayList<String> getExcludeListForCaching() {
        return excludeListForCaching;
    }

    public static void setExcludeListForCaching(ArrayList<String> excludeListForCaching) {
        DbConfig.excludeListForCaching = excludeListForCaching;
    }

    @PostConstruct
    public void init() {
        populateDbConfig();
    }

    private void populateDbConfig() {
        ArrayList<String> tempExcludeListForCaching = new ArrayList<>();
        String excludeForCaching = dbConfigService.findByCode(Constants.EXCLUDE_CACHE_FETCHFOR, String.class);
        if (BaseUtil.isNotNullOrBlank(excludeForCaching)) {
            String[] excludeCache = excludeForCaching.split(",");
            Collections.addAll(tempExcludeListForCaching, excludeCache);
        }
        excludeListForCaching = tempExcludeListForCaching;
    }

    public void evictConfigCacheAndPopulate() {
        commonService.evictCache(Config.class.getSimpleName());
        populateDbConfig();
    }
}
