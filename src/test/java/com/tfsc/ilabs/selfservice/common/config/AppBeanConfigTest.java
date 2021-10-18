package com.tfsc.ilabs.selfservice.common.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;

@RunWith(MockitoJUnitRunner.class)
public class AppBeanConfigTest {

    @InjectMocks
    AppBeanConfig appBeanConfig = new AppBeanConfig();

    @Mock
    Environment env;

    @Mock
    ResourceBundleMessageSource resourceBundleMessageSource;



    private String key = "thread.concurrency";
    @Before
    public void init(){
        resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames("locale/messages");
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Mockito.when(env.getProperty(key)).thenReturn("10");
    }

    @Test
    public void messageSource(){
        MessageSource messageSource = appBeanConfig.messageSource();
        Assert.assertNotNull(messageSource);
    }

    @Test
    public void test_auditorAware(){
        AuditorAware<String> result = appBeanConfig.auditorAware();
        Assert.assertNotNull(result);
    }

    @Test
    public void test_getScheduledExecutorService(){
        ScheduledExecutorService result = appBeanConfig.getScheduledExecutorService();
        Assert.assertNotNull(result);
    }
}