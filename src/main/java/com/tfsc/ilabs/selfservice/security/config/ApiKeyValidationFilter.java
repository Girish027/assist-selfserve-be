package com.tfsc.ilabs.selfservice.security.config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ApiKeyValidationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyValidationFilter.class);

    private DBConfigService dbConfigService;
    private String authMethod; // can be Basic, Bearer, etc
    private List<String> urls;

    ApiKeyValidationFilter(DBConfigService dbConfigService, String authMethod, String[] urls) {
        this.dbConfigService = dbConfigService;
        this.authMethod = authMethod;
        this.urls = Arrays.asList(urls);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String apiKey = getApiKey((HttpServletRequest) servletRequest);
        if (apiKey == null || !isValid(apiKey)) {
            logger.info("failed to authenticate request={}", servletRequest);
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.reset();
            String msg = "Invalid API KEY";
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setHeader("Strict-Transport-Security", "max-age=31536000");
            servletResponse.setContentLength(msg.length());
            servletResponse.getWriter().write(msg);
            return;
        }

        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (isAllowed(path)) {
            servletRequest.getRequestDispatcher(path).forward(servletRequest, servletResponse);
        }
    }

    private boolean isAllowed(String path) {
        boolean allowed = false;
        for (String url : urls) {
            allowed = allowed || path.toLowerCase().startsWith(url);
        }
        return allowed;
    }

    private boolean isValid(String apiKey) {
        boolean valid = false;
        if (apiKey != null) {
            ObjectNode cacheApikeyObj = dbConfigService.findByCode(Constants.CACHE_APIKEY, ObjectNode.class);
            String cacheApikey = cacheApikeyObj.get("value").asText();
            if (cacheApikey != null && cacheApikey.equals(apiKey)) {
                valid = true;
            }
        }
        return valid;
    }

    private String getApiKey(HttpServletRequest httpRequest) {
        String apiKey = null;

        String authHeader = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION);
        if (authHeader != null) {
            authHeader = authHeader.trim();
            if (authHeader.startsWith(authMethod + " ")) {
                apiKey = authHeader.substring(authMethod.length()).trim();
            }
        }

        return apiKey;
    }
}
