package com.tfsc.ilabs.selfservice;

import com.google.gson.Gson;
import com.tfsc.ilabs.selfservice.bulkupload.properties.FileStorageProperties;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SelfServiceApplication implements WebMvcConfigurer {
    private static final String ALLOWED_ORIGINS = "allowed.origins";
    private static Logger logger = LoggerFactory.getLogger(SelfServiceApplication.class);
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SelfServiceApplication.class, args);
    }

    @Bean
    public Gson getDefaultGson() {
        return new Gson();
    }

    @Bean
    public CommandLineRunner parseCommandLine() {
        return (String... args) -> logger.info("Application started please visit the swagger-ui.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedOrigins = env.getProperty(ALLOWED_ORIGINS);

        if (BaseUtil.isNullOrBlank(allowedOrigins)) {
            throw new SelfServeException(ALLOWED_ORIGINS + " is not configured");
        }
        // trim any spaces
        String[] allowedOriginsArray = allowedOrigins.split(",");
        List<String> allowedOriginsTrimmed = new ArrayList<>();
        for (String allowedOrigin : allowedOriginsArray) {
            if (!allowedOrigin.trim().isEmpty()) {
                allowedOriginsTrimmed.add(allowedOrigin.trim());
            }
        }
        registry.addMapping("/**")
                .allowedOrigins(allowedOriginsTrimmed.toArray(new String[0]))
                .allowedMethods("*")
                .allowCredentials(true);
    }


}
