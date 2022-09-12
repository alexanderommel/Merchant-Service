package com.tongue.merchantservice.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Cors enabled for all origins");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080/","http://localhost:3000/")
                .allowedMethods("GET","POST","OPTIONS","PUT","PATCH","DELETE").allowCredentials(true);
    }

}