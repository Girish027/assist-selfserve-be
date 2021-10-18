package com.tfsc.ilabs.selfservice.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.ThreadLocalContext;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import com.tfsc.ilabs.selfservice.security.service.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import java.io.IOException;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class SessionValidationFilter extends GenericFilterBean {
    private static final Logger sessionLogger =
            LoggerFactory.getLogger(SessionValidationFilter.class);

    @Autowired
    private SessionValidator sessionValidator;

    SessionValidationFilter(SessionValidator sessionValidator) {
        this.sessionValidator = sessionValidator;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            ThreadLocalContext.setThreadLocalContext(sessionValidator.getUUID((HttpServletRequest) servletRequest), Constants.DEFAULT_USER);
            UserSession userSession = sessionValidator.validateSession((HttpServletRequest) servletRequest);
            ThreadLocalContext.setLocaleCode(getLocaleCode(((HttpServletRequest) servletRequest).getHeader("accept-language")).toLowerCase());
            if (userSession == null) {
                sessionLogger.info("failed to authenticate request={}", servletRequest);
                throw new BadCredentialsException("invalid session");
            }
            ThreadLocalContext.setThreadLocalContext(sessionValidator.getUUID((HttpServletRequest) servletRequest), userSession.getUserId());

            SecurityContextHolder.getContext().setAuthentication(BaseUtil.toToken(userSession));
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException e) {
            sendResponse(servletResponse, HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
        } catch (ForbiddenException e) {
            sendResponse(servletResponse, HttpServletResponse.SC_FORBIDDEN, e.getLocalizedMessage());
        } catch (Exception e) {
            sendResponse(servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void sendResponse(ServletResponse servletResponse, Integer httpServletResponse, String msg) throws IOException {
        servletResponse.setContentType("application/json");
        ((HttpServletResponse) servletResponse).setStatus(httpServletResponse);
        servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(msg));
    }

    private String getLocaleCode(String str) {
        try {
            return (str != null && !str.trim().equals("")) ? str.split(",")[0] : "en-us";
        } catch (Exception e) {
            sessionLogger.error("Not able to find accept-language header in request ");
            return "en-us";
        }
    }

    @Override
    public void destroy() {
        ThreadLocalContext.clearThreadLocalContext();
    }
}
