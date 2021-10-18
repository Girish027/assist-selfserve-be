package com.tfsc.ilabs.selfservice.security.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.AccessTokenDetails;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import com.tfsc.ilabs.selfservice.common.exception.InternalException;
import com.tfsc.ilabs.selfservice.common.utils.Constants;

import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.utils.MockUtils;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.mock.web.MockHttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ravi.b on 30/07/2019.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class SessionValidatorWithOKTATest {

    @InjectMocks
    SessionValidatorWithOKTA sessionValidatorWithOKTA = new SessionValidatorWithOKTA("http://test.com", "Test-AuthorizationServerId",
            "Test-oidcClientId", "Test-oidcClientSecret", "1000", "1000", null,
            null, null, null, "http://umsurl.com", true, true);

    @Mock
    CloseableHttpClient httpclient;

    @Mock
    HttpResponse httpResponse;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    org.springframework.core.env.Environment environment;

    private static class MockContext {
        CloseableHttpResponse httpResponse;
        HttpEntity httpEntity;
        public MockContext(CloseableHttpResponse httpResponse, HttpEntity httpEntity) {
            this.httpResponse = httpResponse;
            this.httpEntity = httpEntity;
        }

        public CloseableHttpResponse getHttpResponse() {
            return httpResponse;
        }

        public HttpEntity getHttpEntity() {
            return httpEntity;
        }
    }
    @Mock
    DBConfigService dbConfigService;

    MockContext setupMocks(String json) {
        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = new HttpEntity() {
            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public boolean isChunked() {
                return false;
            }

            @Override
            public long getContentLength() {
                return 0;
            }

            @Override
            public Header getContentType() {
                return null;
            }

            @Override
            public Header getContentEncoding() {
                return null;
            }

            @Override
            public InputStream getContent() throws UnsupportedOperationException {

                return new ByteArrayInputStream(json.getBytes());
            }

            @Override
            public void writeTo(OutputStream outputStream)  {

            }

            @Override
            public boolean isStreaming() {
                return false;
            }

            @Override
            @Deprecated
            public void consumeContent(){

            }
        };

        return new MockContext(httpResponse, httpEntity);
    }

    @Before
    public void setup() {
        this.request = new MockHttpServletRequest();
        this.request.setScheme("http");
        this.request.setServerName("localhost");
        this.request.setServerPort(8090);
        this.request.setContextPath("/self-serve");
        String appRedirectURL="http://localhost:8090/self-serve";
        Mockito.when(environment.getProperty(Constants.SERVICE_BASE_URL)).thenReturn(appRedirectURL);
    }

    @Test(expected = BadCredentialsException.class)
    public void validateSessionTest_CookieNull() {
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        sessionValidatorWithOKTA.validateSession(httpServletRequest);
    }

    @Test(expected = CredentialsExpiredException.class)
    public void validateSessionTest_CookieNotNull_accessToken_is_expired() throws Exception {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.makeGetCall(Mockito.any(String.class))).thenReturn(httpResponse);
        PowerMockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn("");
        Mockito.when(httpResponse.body()).thenReturn(MockUtils.getUMSRoles());
        String json = "{\"active\":\"false\", \"sub\":\"subValue\"}";
        SessionValidatorWithOKTATest.MockContext mockContext = setupMocks(json);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        when(httpclient.execute(any(HttpUriRequest.class))).thenReturn(mockContext.getHttpResponse());
        when(mockContext.getHttpResponse().getEntity()).thenReturn(mockContext.getHttpEntity());
        sessionValidatorWithOKTA.validateSession(httpServletRequest);
    }

    @Test(expected = InternalException.class)
    public void validateSessionTest_CookieNotNull2() throws Exception {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.makeGetCall(Mockito.any(String.class))).thenReturn(httpResponse);
        PowerMockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn("");
        Mockito.when(httpResponse.body()).thenReturn(MockUtils.getUMSRoles());
        String json = "{\"active\":\"active\", \"sub\":\"\"}";
        SessionValidatorWithOKTATest.MockContext mockContext = setupMocks(json);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        ((MockHttpServletRequest) httpServletRequest).addHeader(Constants.ACCESS_TOKEN, "testToken");
        when(mockContext.getHttpResponse().getEntity()).thenReturn(mockContext.getHttpEntity());
        sessionValidatorWithOKTA.validateSession(httpServletRequest);
    }

    @Test
    public void validateSessionTest_CookieNotNull3() throws Exception {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.makeGetCall(Mockito.any(String.class))).thenReturn(httpResponse);
        PowerMockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn("");
        Mockito.when(httpResponse.body()).thenReturn(MockUtils.getUMSRoles());
        String json = "{\"active\":\"true\", \"sub\":\"testEmail\" , \"username\" : \"testUserName\"}";
        SessionValidatorWithOKTATest.MockContext mockContext = setupMocks(json);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        when(httpclient.execute(any(HttpUriRequest.class))).thenReturn(mockContext.getHttpResponse());
        when(mockContext.getHttpResponse().getEntity()).thenReturn(mockContext.getHttpEntity());
        UserSession userSession = sessionValidatorWithOKTA.validateSession(httpServletRequest);
        Assert.assertNotNull(userSession);
    }

    @Test
    public void validateSessionTest_CookieNotNull4() throws Exception {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.makeGetCall(Mockito.any(String.class))).thenReturn(httpResponse);
        PowerMockito.when(HttpUtils.getSessionCookies(Mockito.any())).thenReturn("randomAccessToken");
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class))).thenReturn("");
        Mockito.when(httpResponse.body()).thenReturn(MockUtils.getUMSRoles());
        String json = "{\"active\":\"true\", \"sub\":\"testEmail\" , \"username\" : \"testUserName\"}";
        SessionValidatorWithOKTATest.MockContext mockContext = setupMocks(json);
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        when(httpclient.execute(any(HttpUriRequest.class))).thenReturn(mockContext.getHttpResponse());
        when(mockContext.getHttpResponse().getEntity()).thenReturn(mockContext.getHttpEntity());
        UserSession userSession = sessionValidatorWithOKTA.validateSession(httpServletRequest);
        Assert.assertNotNull(userSession);
    }

    @Test
    public void test_getTokenFromCode() throws IOException, ServletException, AuthenticationException {
        String json = "{\"access_token\":\"eyJraWQiOiIzcmZFTm45alM0QzZSUmtr\", \"refresh_token\": \"false\", \"id_token\":\"eyJraWQiOg\", \"token_type\":\"Bearer\", \"expires_in\":\"86400\"}";
        SessionValidatorWithOKTATest.MockContext mockContext = setupMocks(json);
        when(httpclient.execute(any(HttpUriRequest.class))).thenReturn(mockContext.getHttpResponse());
        when(mockContext.getHttpResponse().getEntity()).thenReturn(mockContext.getHttpEntity());
        UserAccessTokenInfo accessTokenInfo = sessionValidatorWithOKTA.getTokenFromCode("someRandomCode",request);
        Assert.assertNotNull(accessTokenInfo);
    }

    @Test
    public void getUserProfileFromOktaAndUMS_test() throws IOException {
        PowerMockito.mockStatic(HttpUtils.class);
        Mockito.when(HttpUtils.makeGetCall(Mockito.any(String.class))).thenReturn(httpResponse);
        Mockito.when(httpResponse.body()).thenReturn(MockUtils.getUMSRoles());
        UserProfile  userProfile = sessionValidatorWithOKTA.getUserProfileFromOktaAndUMS(new UserAccessTokenInfo());

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(MockUtils.getUMSRoles());

        Assert.assertEquals(userProfile.getClients() , jsonNode.get("clients"));
    }

    @Test
    public void getOktaLogoutRedirectUrl_test() throws URISyntaxException{
        Map<String, UserAccessTokenInfo> tokenMapMock = new HashMap<>();
        AccessTokenDetails oktaAccessTokenInfoMock = new AccessTokenDetails();
        oktaAccessTokenInfoMock.setIdToken("randomIdToken");
        UserAccessTokenInfo userAccessTokenInfo = new UserAccessTokenInfo();
        userAccessTokenInfo.setAccessTokenDetails(oktaAccessTokenInfoMock);
        tokenMapMock.put("randomAccessToken",userAccessTokenInfo);
        Whitebox.setInternalState( SessionValidatorWithOKTA.class, "tokenMap", tokenMapMock );
        PowerMockito.mockStatic(HttpUtils.class);
        when(HttpUtils.getSessionCookies(request)).thenReturn("randomAccessToken");
        String uri= sessionValidatorWithOKTA.getOktaLogoutRedirectUrl(request);
        Assert.assertEquals(uri, "http://test.com/oauth2/Test-AuthorizationServerId/v1/logout?id_token_hint=randomIdToken&post_logout_redirect_uri=http%3A%2F%2Flocalhost%3A8090%2Fself-serve");
    }
}
