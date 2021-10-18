package com.tfsc.ilabs.selfservice.common.config;

import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@PropertySource("application.properties")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AppBeanConfig {
    @Autowired
    private Environment env;

    AppBeanConfig() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    private String getPropertyValue(String key) {
        return Objects.requireNonNull(env.getProperty(key)).trim();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("locale/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Bean
    public ScheduledExecutorService getScheduledExecutorService() {
        String threadCount = getPropertyValue(Constants.THREAD_COUNT);

        return Executors.newScheduledThreadPool(BaseUtil.isNullOrBlank(threadCount) ? 10 : Integer.parseInt(threadCount));
    }
}
