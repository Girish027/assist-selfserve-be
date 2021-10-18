package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.common.models.TfsEnvVariables;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TfsEnvConfigTest {

    @InjectMocks
    TfsEvnConfig tfsEvnConfig;

    @Mock
    org.springframework.core.env.Environment env;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTfsEnvConfig_test(){
        Mockito.when(env.getProperty(Constants.DOC_LINK)).thenReturn("https://stable.developer.sv2.247-inc.net/docportal/Content/Assist/Assist.htm");
        Mockito.when(env.getProperty(Constants.SUPPORT_LINK)).thenReturn("https://247inc.atlassian.net/secure/RapidBoard.jspa?rapidView=1178&projectKey=SRE");
        Mockito.when(env.getProperty(Constants.TOOLROOT)).thenReturn("/self-serve");
        Mockito.when(env.getProperty(Constants.TOOLNAME)).thenReturn("ASSIST");
        Mockito.when(env.getProperty(Constants.TOOLTITLE)).thenReturn("Assist");
        Mockito.when(env.getProperty(Constants.TOOLTYPE)).thenReturn("internal");
        Mockito.when(env.getProperty(Constants.ENV)).thenReturn("local");
        Mockito.when(env.getProperty(Constants.OKTA_BASEURL)).thenReturn("https://login.247ai.com");
        Mockito.when(env.getProperty(Constants.SERVICE_BASE_URL)).thenReturn("http://localhost:8090/self-serve/api");
        Mockito.when(env.getProperty(Constants.PARENT_BASE_URL)).thenReturn("https://stable.developer.sv2.247-inc.net/home");
        Mockito.when(env.getProperty(Constants.CSS_OKTA_ENABLED)).thenReturn("true");


        Assert.assertEquals(tfsEvnConfig.createTfsEnvConfig().getDocLink(),new TfsEnvVariables("https://stable.developer.sv2.247-inc.net/docportal/Content/Assist/Assist.htm","https://247inc.atlassian.net/secure/RapidBoard.jspa?rapidView=1178&projectKey=SRE","/self-serve","ASSIST","Assist","internal","local","https://login.247ai.com","http://localhost:8090/self-serve/api","https://stable.developer.sv2.247-inc.net/home","true").getDocLink());

    }


}
