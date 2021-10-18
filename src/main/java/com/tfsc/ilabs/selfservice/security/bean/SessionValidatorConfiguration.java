package com.tfsc.ilabs.selfservice.security.bean;

import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.security.service.AllowAllSessionValidator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by ravi.b on 30/07/2019.
 */
@Configuration
public class SessionValidatorConfiguration {

    private static final Logger logger =
            LoggerFactory.getLogger(SessionValidatorConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public SessionValidator createSessionResolver() {
        if (Constants.TRUE.equalsIgnoreCase(env.getProperty(Constants.SSO_DISABLE))) {
            logger.warn("Session Validation is disabled");
            return new AllowAllSessionValidator();
        }
            return getOktaSessionValidator();

    }

    private SessionValidator getOktaSessionValidator() {
        String umsBaseUrl = env.getProperty(Constants.UMS_BASE_URL);
        String oktaBaseUrl = env.getProperty(Constants.OKTA_BASEURL);
        String authorizationServiceId = env.getProperty(Constants.OKTA_AUTHORIZATION_SERVICE_ID);
        String clientId = env.getProperty(Constants.OKTA_CLIENT_ID);
        String clientSecret = env.getProperty(Constants.OKTA_CLIENT_SECRET);
        String oktaConnectionTimeout = env.getProperty(Constants.OKTA_CONNECTION_TIMEOUT);
        String oktaConnectionTimeoutMultiply = env.getProperty(Constants.OKTA_CONNECTION_TIMEOUT_MULTIPLY);
        String oktaConnectionProxy = env.getProperty(Constants.PROXY);
        String oktaConnectionProxyHost = env.getProperty(Constants.PROXY_HOST);
        String oktaConnectionProxyPort = env.getProperty(Constants.PROXY_PORT);
        String oktaConnectionProxyProtocol = env.getProperty(Constants.PROXY_PROTOCOL);
        boolean isAuthorizationEnabled = Constants.TRUE.equalsIgnoreCase(env.getProperty(Constants.AUTHORIZATION_ENABLE));
        boolean isUMSEnabled = Constants.TRUE.equalsIgnoreCase(env.getProperty(Constants.UMS_ENABLE));

        logger.info("umsBaseUrl={}", umsBaseUrl);
        logger.info("oktaBaseUrl={}", oktaBaseUrl);
        logger.info("authorizationServiceId={}", authorizationServiceId);
        logger.info("clientId={}", clientId);
        logger.info("oktaConnectionTimeout={}", oktaConnectionTimeout);
        logger.info("oktaConnectionTimeoutMultiply={}", oktaConnectionTimeoutMultiply);

        if (BaseUtil.isNullOrBlank(oktaBaseUrl)) {
            throw new SelfServeException("oktaBaseUrl not specified");
        }
        if (!BaseUtil.isValidUrl(oktaBaseUrl)) {
            throw new SelfServeException("not a valid oktaBaseUrl: {0}", oktaBaseUrl);
        }
        if (BaseUtil.isNullOrBlank(clientId)) {
            throw new SelfServeException("clientId not specified");
        }
        if (BaseUtil.isNullOrBlank(clientSecret)) {
            throw new SelfServeException("clientSecret not specified");
        }
        if (BaseUtil.isNullOrBlank(oktaConnectionTimeout)) {
            throw new SelfServeException("oktaConnectionTimeout not specified");
        }
        if (BaseUtil.isNullOrBlank(oktaConnectionTimeoutMultiply)) {
            throw new SelfServeException("oktaConnectionTimeoutMultiply not specified");
        }
        if (!BaseUtil.isNullOrBlank(oktaConnectionProxy) && Boolean.valueOf(oktaConnectionProxy)) {
            if (BaseUtil.isNullOrBlank(oktaConnectionProxyHost)) {
                throw new SelfServeException("oktaConnectionProxyHost not specified");
            }
            if (BaseUtil.isNullOrBlank(oktaConnectionProxyPort)) {
                throw new SelfServeException("oktaConnectionProxyPort not specified");
            }
            if (BaseUtil.isNullOrBlank(oktaConnectionProxyProtocol)) {
                throw new SelfServeException("oktaConnectionProxyProtocol not specified");
            }
        }
        return new SessionValidatorWithOKTA(oktaBaseUrl, authorizationServiceId, clientId, clientSecret, oktaConnectionTimeout, oktaConnectionTimeoutMultiply, oktaConnectionProxy,
                oktaConnectionProxyHost, oktaConnectionProxyPort, oktaConnectionProxyProtocol, umsBaseUrl, isAuthorizationEnabled, isUMSEnabled);
    }
}
