package com.tfsc.ilabs.selfservice.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.TfsEnvVariables;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.config.TfsEvnConfig;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class HTMLContentCreator {
    @Autowired
    private Environment env;

    @Autowired
    private TfsEvnConfig tfsEvnConfig;

    @Autowired
    SessionValidator sessionValidator;

    public String generateIndexHTMLContent(UserAccessTokenInfo userAccessTokenInfo) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        TfsEnvVariables tfsEnvVariables = tfsEvnConfig.createTfsEnvConfig();
        String tfsEnvVar = objectMapper.writeValueAsString(tfsEnvVariables);

        UserProfile userProfile = sessionValidator.getUserProfileFromOktaAndUMS(userAccessTokenInfo);
        String userProfileObject = objectMapper.writeValueAsString(userProfile);
        HttpResponse<String> response = HttpUtils.makeGetCall(env.getProperty(Constants.CONTENT_SERVER_BASEPATH)+ env.getProperty(Constants.CONTENT_SERVER_CSS_CONTENT_PATH)+ "index.html");
        String htmlContent = response.body().replace("/css/","/self-serve/");
        htmlContent = htmlContent.replace("\"{TFS_ENV_VARS}\"", tfsEnvVar);
        htmlContent = htmlContent.replace("\"{USER_PROFILE}\"", userProfileObject);
        htmlContent = htmlContent.replace("\"{STORE}\"", "{}");
        return htmlContent;
    }
}
