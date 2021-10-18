package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.models.TfsEnvVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class TfsEvnConfig {

    @Autowired
    private Environment env;

    public TfsEnvVariables createTfsEnvConfig(){
        String docLink = env.getProperty(Constants.DOC_LINK);
        String supportLink = env.getProperty(Constants.SUPPORT_LINK);
        String toolRoot = env.getProperty(Constants.TOOLROOT);
        String toolName = env.getProperty(Constants.TOOLNAME);
        String toolTitle = env.getProperty(Constants.TOOLTITLE);
        String toolType = env.getProperty(Constants.TOOLTYPE);
        String environment = env.getProperty(Constants.ENV);
        String oktaUrl = env.getProperty(Constants.OKTA_BASEURL);
        String serviceBaseUrl = env.getProperty(Constants.SERVICE_BASE_URL)+Constants.BASE_URI;
        String parentBaseUrl = env.getProperty(Constants.PARENT_BASE_URL);
        String cssOktaEnabled = env.getProperty(Constants.CSS_OKTA_ENABLED);

        return new TfsEnvVariables(docLink,supportLink,toolRoot,toolName,toolTitle,toolType,environment,oktaUrl,serviceBaseUrl,parentBaseUrl,cssOktaEnabled);
    }
}
