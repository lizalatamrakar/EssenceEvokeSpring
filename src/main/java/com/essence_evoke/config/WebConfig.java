package com.essence_evoke.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Define the external folder path where profile pictures will be stored
    private final String uploadDir = "uploads/profile-pictures/"; // relative to project root or absolute path

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the filesystem
        registry.addResourceHandler("/uploads/profile-pictures/**")
                .addResourceLocations("file:" + uploadDir);

        // Product images
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:uploads/products/");
    }
}
