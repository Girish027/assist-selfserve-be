package com.tfsc.ilabs.selfservice.security.service;

import com.tfsc.ilabs.selfservice.common.models.AccessTokenDetails;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import org.apache.http.auth.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import com.tfsc.ilabs.selfservice.common.models.ComponentInfo;

import java.util.UUID;

/**
 * Created by ravi.b on 30/07/2019.
 */
public interface SessionValidator {

    UserSession validateSession(HttpServletRequest request);
    
    UUID getUUID();

    UUID getUUID(HttpServletRequest request);

    void logout(String tokenId);

    String getAuthRedirectUrl(HttpServletRequest request) throws URISyntaxException, IOException;

    UserAccessTokenInfo getTokenFromCode(String code, HttpServletRequest request) throws IOException, ServletException, AuthenticationException;

    public UserAccessTokenInfo getTokenDetails(String accessToken, HttpServletRequest request) throws IOException, AuthenticationException;

    UserProfile getUserProfileFromOktaAndUMS(UserAccessTokenInfo userAccessTokenInfo);

    public String getOktaLogoutRedirectUrl(HttpServletRequest request) throws URISyntaxException;

    ComponentInfo getComponentInfoFromTokenMap(String clientId, String componentId);
}