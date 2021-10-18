package com.tfsc.ilabs.selfservice.security.service;

import com.tfsc.ilabs.selfservice.common.models.AccessTokenDetails;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import com.tfsc.ilabs.selfservice.common.models.ComponentInfo;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.apache.http.auth.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class AllowAllSessionValidator implements SessionValidator {

    @Override
    public UserSession validateSession(HttpServletRequest request) {
        UserSession userSession = new UserSession();
        userSession.setUserName(Constants.DEFAULT_USER);
        return userSession;
    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getUUID(HttpServletRequest request) {
        return UUID.randomUUID();
    }

    @Override
    public void logout(String tokenId) {
        // do nothing.
    }

    @Override
    public String getAuthRedirectUrl(HttpServletRequest request) throws URISyntaxException, IOException {
        return "";
    }

    @Override
    public UserAccessTokenInfo getTokenFromCode(String code, HttpServletRequest request) throws IOException {
        return null;
    }

    @Override
    public UserProfile getUserProfileFromOktaAndUMS(UserAccessTokenInfo userAccessToken) {
        return null;
    }

    @Override
    public String getOktaLogoutRedirectUrl(HttpServletRequest request) throws URISyntaxException {
        return null;
    }

    @Override
    public UserAccessTokenInfo getTokenDetails(String accessToken, HttpServletRequest request) throws IOException, AuthenticationException {
        return null;
    }
  
    @Override
    public ComponentInfo getComponentInfoFromTokenMap(String clientId, String componentId) {
        return null;
    }

}