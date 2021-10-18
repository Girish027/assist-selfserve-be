package com.tfsc.ilabs.selfservice.common.controller;

import com.tfsc.ilabs.selfservice.common.config.SwaggerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SwaggerConfigTest {
    @InjectMocks
    SwaggerConfig swaggerConfig;

    @Before
    public void setup() {
    }

    @Test
    public void responseBodyAdvice() {
        Docket expected = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        Docket actual = swaggerConfig.apis();

        assertEquals(expected.getGroupName(), actual.getGroupName());
        assertEquals(expected.getDocumentationType(), actual.getDocumentationType());
    }
}