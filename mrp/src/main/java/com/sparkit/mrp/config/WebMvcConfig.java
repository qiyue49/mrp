package com.sparkit.mrp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Handles CORS, static resources, and SPA routing
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handle favicon
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
        
        // Handle all static resources with no-cache headers
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // SPA routing fallback - serve index.html for all non-API paths
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
        registry.addViewController("/customers").setViewName("forward:/index.html");
        registry.addViewController("/orders").setViewName("forward:/index.html");
        registry.addViewController("/materials").setViewName("forward:/index.html");
        registry.addViewController("/bom").setViewName("forward:/index.html");
        registry.addViewController("/suppliers").setViewName("forward:/index.html");
        registry.addViewController("/inventory").setViewName("forward:/index.html");
        registry.addViewController("/purchase-orders").setViewName("forward:/index.html");
        registry.addViewController("/production-orders").setViewName("forward:/index.html");
        registry.addViewController("/mrp").setViewName("forward:/index.html");
        registry.addViewController("/rocketmq").setViewName("forward:/index.html");
    }
}
