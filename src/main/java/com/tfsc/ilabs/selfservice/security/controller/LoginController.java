package com.tfsc.ilabs.selfservice.security.controller;

import com.tfsc.ilabs.selfservice.action.services.impl.ActionExecutorServiceImpl;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.security.service.HTMLContentCreator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.security.service.UserSession;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SessionValidator sessionValidator;

    @Autowired
    HTMLContentCreator htmlContentCreator;

    @Autowired
    Environment environment;

    @GetMapping("/**")
    @ResponseBody
    public Object redirectToOktaLogin(HttpServletRequest request) throws IOException, URISyntaxException, InterruptedException, AuthenticationException {
        String accessToken = HttpUtils.getSessionCookies(request);
        if (accessToken != null) {
            UserSession userSession = sessionValidator.validateSession(request);
            logger.info("Validated OKTA session for user: {}", userSession.toString());
            UserAccessTokenInfo userAccessTokenInfo = sessionValidator.getTokenDetails(accessToken, request);
            return htmlContentCreator.generateIndexHTMLContent(userAccessTokenInfo);
        } else {
            return new RedirectView(sessionValidator.getAuthRedirectUrl(request));
        }
    }

    @GetMapping(Constants.OKTA_AUTHORIZATION_CODE_CALLBACK_URI)
    public RedirectView exchangeTokensForCode(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException, AuthenticationException {
        //to do- callback url also receives state in the response , validate if the same state is being returned by okta which was sent in authorize call
        UserAccessTokenInfo userAccessTokenInfo = sessionValidator.getTokenFromCode(code, request);
        Cookie cookie = new Cookie(Constants.SESSION_COOKIE_NAME, userAccessTokenInfo.getAccessTokenDetails().getAccessToken());
        cookie.setMaxAge(userAccessTokenInfo.getAccessTokenDetails().getExpiresIn());
        cookie.setHttpOnly(Boolean.parseBoolean(environment.getProperty(Constants.COOKIE_SECURE_FLAG)));
        cookie.setSecure(Boolean.parseBoolean(environment.getProperty(Constants.COOKIE_HTTPONLY_FLAG)));
        cookie.setPath("/");
        response.addCookie(cookie);
        return new RedirectView(environment.getProperty(Constants.SERVICE_BASE_URL)+"/",false);
    }

    @GetMapping(Constants.FETCH_JS_FILES_FROM_CONTENT_SERVER_URI)
    public RedirectView getStaticFilesFromContentServer(HttpServletRequest request) {
        String url = request.getRequestURI().replace("/self-serve/","/css/");
        return new RedirectView(environment.getProperty(Constants.CONTENT_SERVER_BASEPATH) + environment.getProperty(Constants.CONTENT_SERVER_CSS_CONTENT_PATH) + url);
    }

    @GetMapping(Constants.OKTA_LOGOUT_URI)
    public RedirectView getOktaLogoutUrl(@RequestParam String redirect_url, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        Cookie cookie = new Cookie(Constants.SESSION_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(Boolean.parseBoolean(environment.getProperty(Constants.COOKIE_SECURE_FLAG)));
        cookie.setSecure(Boolean.parseBoolean(environment.getProperty(Constants.COOKIE_HTTPONLY_FLAG)));
        cookie.setPath("/");
        response.addCookie(cookie);
        return new RedirectView(sessionValidator.getOktaLogoutRedirectUrl(request));
    }

}
