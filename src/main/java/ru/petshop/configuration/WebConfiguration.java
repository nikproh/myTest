package ru.petshop.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@Log4j2
public class WebConfiguration implements WebMvcConfigurer {
    private static final String LOG_TAG = "[WEB_CONFIGURATION] ::";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    @PostConstruct
    public void init() {
        log.info("{} has been initialized.", LOG_TAG);
    }
}
