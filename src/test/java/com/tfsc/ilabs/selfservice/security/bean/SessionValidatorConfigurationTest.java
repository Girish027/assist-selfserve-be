package com.tfsc.ilabs.selfservice.security.bean;


import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.security.service.AllowAllSessionValidator;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by ravi.b on 30/07/2019.
 */
@RunWith(MockitoJUnitRunner.class)
@Import(SessionValidatorConfiguration.class)
public class SessionValidatorConfigurationTest {

    @InjectMocks
    SessionValidatorConfiguration sessionValidatorConfiguration;

    @Mock
    Environment environment;

    @Test
    public void createSessionResolverTest_withSSODISABLED()
    {
        SessionValidator sessionValidatorexpected = new AllowAllSessionValidator();
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(Constants.TRUE);
        SessionValidator sessionValidatoractual=sessionValidatorConfiguration.createSessionResolver();
        Assert.assertNotNull(sessionValidatoractual);
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED1()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED2()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED3()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED4()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        when(environment.getProperty(eq(Constants.OKTA_BASEURL))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_AUTHORIZATION_SERVICE_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_SECRET))).thenReturn("");
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED5()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        when(environment.getProperty(eq(Constants.OKTA_BASEURL))).thenReturn("Test-OktaBaseUrl");
        when(environment.getProperty(eq(Constants.OKTA_AUTHORIZATION_SERVICE_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_SECRET))).thenReturn("");
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED6()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        when(environment.getProperty(eq(Constants.OKTA_BASEURL))).thenReturn("http://test.com");
        when(environment.getProperty(eq(Constants.OKTA_AUTHORIZATION_SERVICE_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_SECRET))).thenReturn("");
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test(expected = SelfServeException.class)
    public void createSessionResolverTest_withSSOANABLED7()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        when(environment.getProperty(eq(Constants.OKTA_BASEURL))).thenReturn("http://test.com");
        when(environment.getProperty(eq(Constants.OKTA_AUTHORIZATION_SERVICE_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_ID))).thenReturn("TestClientId");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_SECRET))).thenReturn("");
        sessionValidatorConfiguration.createSessionResolver();
    }

    @Test
    public void createSessionResolverTest_withSSOANABLED8()
    {
        when(environment.getProperty(eq(Constants.SSO_DISABLE))).thenReturn(null);
        when(environment.getProperty(eq(Constants.OKTA_BASEURL))).thenReturn("http://test.com");
        when(environment.getProperty(eq(Constants.OKTA_AUTHORIZATION_SERVICE_ID))).thenReturn("");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_ID))).thenReturn("TestClientId");
        when(environment.getProperty(eq(Constants.OKTA_CLIENT_SECRET))).thenReturn("TestClientSecret");
        when(environment.getProperty(eq(Constants.OKTA_CONNECTION_TIMEOUT))).thenReturn("1000");
        when(environment.getProperty(eq(Constants.OKTA_CONNECTION_TIMEOUT_MULTIPLY))).thenReturn("1000");
        SessionValidator sessionValidator=sessionValidatorConfiguration.createSessionResolver();
        Assert.assertNotNull(sessionValidator);
    }

}
