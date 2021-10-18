package com.tfsc.ilabs.selfservice.security.service;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.security.token.SSAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class SecurityContextUtil {

    private SecurityContextUtil() {
        // hiding implicit public constructor
    }

    public static SSAuthenticationToken getAuthenticationToken() {
        return (SSAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getCurrentUserName() {
        SSAuthenticationToken authenticationToken = getAuthenticationToken();
        // token will be null when we run junit test cases or when we turn off sso using
        // sso.disable property.
        return authenticationToken == null || authenticationToken.getUserName() == null ? Constants.DEFAULT_USER
                : authenticationToken.getUserName();
    }

    public static String getCurrentUserId() {
        SSAuthenticationToken authenticationToken = getAuthenticationToken();
        // token will be null when we run junit test cases or when we turn off sso using
        // sso.disable property.
        return authenticationToken == null || authenticationToken.getUserId() == null ? Constants.DEFAULT_USER
                : authenticationToken.getUserId();
    }
}
