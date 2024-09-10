package com.revature.eeecommerce.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Frontend URL
                        .allowedMethods("*") // Allow all methods (GET, POST, etc.)
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true) // Allow credentials
                        .exposedHeaders("userId", "userType");
            }
        };
    }
}
