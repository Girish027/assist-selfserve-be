package com.tfsc.ilabs.selfservice.security.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.*;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ApiKeyValidationFilterTest {

    @Mock
    DBConfigService dbConfigService;

    @InjectMocks
    ApiKeyValidationFilter apiKeyValidationFilter = new ApiKeyValidationFilter(dbConfigService,
            Constants.HEADER_AUTHORIZATION_METHOD_BEARER, new String[]{Constants.EXPIRE_API_CACHE_URI, Constants.EXPIRE_ENTITY_CACHE_URI});


    @Test
    public void test_doFilter() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("value", mapper.convertValue("validtoken", JsonNode.class));
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class)))
                .thenReturn(root);
        HttpServletResponse response = getResponse();
        apiKeyValidationFilter.doFilter(getRequest(), response, new MockFilterChain());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void test_doFilter_invalid() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("value", mapper.convertValue("validtoken2", JsonNode.class));
        Mockito.when(dbConfigService.findByCode(Mockito.anyString(), Mockito.any(Class.class)))
                .thenReturn(root);
        HttpServletResponse response = getResponse();
        apiKeyValidationFilter.doFilter(getRequest(), response, new MockFilterChain());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    public HttpServletRequest getRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(Constants.HEADER_AUTHORIZATION, "Bearer validtoken");
        return  request;
    }

    public HttpServletResponse getResponse() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.addHeader(Constants.HEADER_AUTHORIZATION, "Bearer validtoken");
        return  response;
    }
}
