package com.tfsc.ilabs.selfservice.security.config;

import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.security.service.SessionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.ONE_YEAR_MILLISECS;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Order(2)
    public static class SessionSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private SessionValidator sessionValidator;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf().disable()
                    .headers()
                    .httpStrictTransportSecurity() // Strict-Transport-Security is only added on HTTPS requests
                    .includeSubDomains(true).maxAgeInSeconds(ONE_YEAR_MILLISECS)
                    .and()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                    .addHeaderWriter(new XXssProtectionHeaderWriter())
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .addFilterAfter(new SessionValidationFilter(sessionValidator), ExceptionTranslationFilter.class);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/**/health");
            web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
            web.ignoring().antMatchers(HttpMethod.GET, "/v2/api-docs");
            web.ignoring().antMatchers(HttpMethod.GET, "/getClientMetaData");
            web.ignoring().antMatchers(HttpMethod.GET,"/");
            web.ignoring().antMatchers(HttpMethod.GET,"/console/authorization-code/callback*");
            web.ignoring().antMatchers(HttpMethod.GET,"/oauth/token*");
            web.ignoring().antMatchers(HttpMethod.GET,"/__webpack_hmr");

        }
    }

    @Configuration
    @Order(1)
    public static class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private DBConfigService dbConfigService;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            String[] urlsForApiKeyValidation = {Constants.API_KEY_VALIDATION_ENTITY_CACHE_URI, Constants.API_KEY_VALIDATION_API_CACHE_URI};
            httpSecurity
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requestMatchers()
                    .antMatchers(urlsForApiKeyValidation)
                    .and()
                    .addFilterAfter(new ApiKeyValidationFilter(dbConfigService, Constants.HEADER_AUTHORIZATION_METHOD_BEARER, urlsForApiKeyValidation), ExceptionTranslationFilter.class)
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
        }
    }
}