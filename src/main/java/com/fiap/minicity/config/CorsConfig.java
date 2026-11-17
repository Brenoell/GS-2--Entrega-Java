
package com.fiap.minicity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String origens = System.getenv("CORS_ORIGINS");

        if (origens == null || origens.isBlank()) {
            origens = "http://localhost:5173";
        } else {
            origens += ",http://localhost:5173";
        }

        registry.addMapping("/**")
                .allowedOrigins(origens.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
