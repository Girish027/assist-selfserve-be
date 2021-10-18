package com.tfsc.ilabs.selfservice.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.config.TfsEvnConfig;
import com.tfsc.ilabs.selfservice.common.models.TfsEnvVariables;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.utils.MockUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class HTMLContentCreatorTest {

    @InjectMocks
    HTMLContentCreator htmlContentCreator;
    @Mock
    org.springframework.core.env.Environment environment;

    @Mock
    TfsEvnConfig tfsEvnConfig;

    @Mock
    SessionValidator sessionValidator;

    @Mock
    HttpResponse<String> httpResponse;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void generateIndexHTMLContent_test() throws IOException, InterruptedException {
        PowerMockito.mockStatic(HttpUtils.class);
        String clients = "{}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode clientsJsonNode = objectMapper.readTree(clients);
        Mockito.when(tfsEvnConfig.createTfsEnvConfig()).thenReturn(new TfsEnvVariables("https://stable.developer.sv2.247-inc.net/docportal/Content/Assist/Assist.htm","https://247inc.atlassian.net/secure/RapidBoard.jspa?rapidView=1178&projectKey=SRE","/self-serve","ASSIST","Assist","internal","local","https://login.247ai.com","http://localhost:8090/self-serve/api","https://stable.developer.sv2.247-inc.net/home","true"));
        Mockito.when(sessionValidator.getUserProfileFromOktaAndUMS(Mockito.any())).thenReturn(new UserProfile("Swati.Taneja","Swati.Taneja@247.ai","Swati.Taneja","{}","https://login.247ai.com" ,clientsJsonNode ));

        Mockito.when(httpResponse.body()).thenReturn("<script type=\"text/javascript\">var TFS_ENV_VARS = \"{TFS_ENV_VARS}\" ;var USER_PROFILE = \"{USER_PROFILE}\"; var STORE = \"{STORE}\";</script>");
        Mockito.when(HttpUtils.makeGetCall(Mockito.anyString())).thenReturn(httpResponse);
        String expectedContent = "<script type=\"text/javascript\">var TFS_ENV_VARS = {\"docLink\":\"https://stable.developer.sv2.247-inc.net/docportal/Content/Assist/Assist.htm\",\"supportLink\":\"https://247inc.atlassian.net/secure/RapidBoard.jspa?rapidView=1178&projectKey=SRE\",\"toolRoot\":\"/self-serve\",\"toolName\":\"ASSIST\",\"toolTitle\":\"Assist\",\"toolType\":\"internal\",\"env\":\"local\",\"oktaUrl\":\"https://login.247ai.com\",\"serviceBaseUrl\":\"http://localhost:8090/self-serve/api\",\"parentBaseUrl\":\"https://stable.developer.sv2.247-inc.net/home\",\"cssOktaEnabled\":\"true\"} ;var USER_PROFILE = {\"name\":\"Swati.Taneja\",\"email\":\"Swati.Taneja@247.ai\",\"userName\":\"Swati.Taneja\",\"access\":\"{}\",\"oktaUrl\":\"https://login.247ai.com\",\"clients\":{}}; var STORE = {};</script>";
        Assert.assertEquals(htmlContentCreator.generateIndexHTMLContent(new UserAccessTokenInfo()),expectedContent);
    }
}
