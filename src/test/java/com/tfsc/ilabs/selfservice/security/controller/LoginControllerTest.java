package com.tfsc.ilabs.selfservice.security.controller;

import com.tfsc.ilabs.selfservice.common.models.AccessTokenDetails;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import com.tfsc.ilabs.selfservice.security.service.HTMLContentCreator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import com.tfsc.ilabs.selfservice.security.service.UserSession;
import org.apache.http.auth.AuthenticationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class, SessionValidatorWithOKTA.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class LoginControllerTest {

    @InjectMocks
    LoginController loginController = new LoginController();

    @Mock
    SessionValidator sessionValidator;

    @Mock
    HTMLContentCreator htmlContentCreator;

    @Mock
    org.springframework.core.env.Environment environment;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void redirectToOktaLogin_testWithAccessToken() throws URISyntaxException, AuthenticationException, InterruptedException, IOException {
        PowerMockito.mockStatic(HttpUtils.class);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        ((MockHttpServletRequest) httpServletRequest).addHeader(Constants.ACCESS_TOKEN,"testToken");
        Mockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("testToken");
        Mockito.when(sessionValidator.validateSession(Mockito.any())).thenReturn(new UserSession());
        Mockito.when(htmlContentCreator.generateIndexHTMLContent(Mockito.any())).thenReturn("<HTML></HTML>");

        Object HTMLContent = loginController.redirectToOktaLogin(httpServletRequest);
        Assert.assertEquals(HTMLContent,"<HTML></HTML>" );

    }

    @Test
    public void redirectToOktaLogin_testWithoutAccessToken() throws URISyntaxException, AuthenticationException, InterruptedException, IOException {
        PowerMockito.mockStatic(HttpUtils.class);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        Mockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn(null);
        Mockito.when(sessionValidator.validateSession(Mockito.any())).thenReturn(new UserSession());
        Mockito.when(sessionValidator.getAuthRedirectUrl(Mockito.any())).thenReturn("/authorize");

        RedirectView oktaRedirectView = (RedirectView) loginController.redirectToOktaLogin(httpServletRequest);
        Assert.assertEquals(oktaRedirectView.getUrl(),"/authorize");

    }

    @Test
    public void exchangeTokensForCode_test() throws IOException, AuthenticationException, ServletException {
        PowerMockito.mockStatic(HttpUtils.class);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Mockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Map<String, List<String>> roles = null;
        Map<String, UserAccessTokenInfo> externalApiTokenMap = null;
        List<String> clientIds = null;
        AccessTokenDetails accessTokenDetails = new AccessTokenDetails("randomAccessToken",false, "randomIdToken","type",240000);
        Mockito.when(sessionValidator.getTokenFromCode(Mockito.anyString(),Mockito.any())).thenReturn(new UserAccessTokenInfo("randomAccesssToken",true,"scope","randomUserName",24000,24000,"sub","iss","uid",24000, roles,externalApiTokenMap,clientIds,accessTokenDetails));
        Mockito.when(environment.getProperty(Constants.COOKIE_SECURE_FLAG)).thenReturn("false");
        Mockito.when(environment.getProperty(Constants.COOKIE_HTTPONLY_FLAG)).thenReturn("false");
        String appRedirectURL="http://localhost:8090/self-serve";
        Mockito.when(environment.getProperty(Constants.SERVICE_BASE_URL)).thenReturn(appRedirectURL);
        Assert.assertEquals(loginController.exchangeTokensForCode("randomCode", httpServletResponse, httpServletRequest).getUrl(), appRedirectURL+"/");
    }

    @Test
    public void getOktaLogoutUrl_test() throws URISyntaxException {
        PowerMockito.mockStatic(HttpUtils.class);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        Mockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Mockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Mockito.when(sessionValidator.getOktaLogoutRedirectUrl(httpServletRequest)).thenReturn("/logout");
        Assert.assertEquals(loginController.getOktaLogoutUrl("/self-serve",httpServletRequest,httpServletResponse).getUrl(),"/logout");
    }
}
