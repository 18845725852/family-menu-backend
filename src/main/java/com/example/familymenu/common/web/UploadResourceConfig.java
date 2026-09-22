package com.example.familymenu.common.web;

import com.example.familymenu.files.FileStorageProperties;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadResourceConfig implements WebMvcConfigurer {
    private final FileStorageProperties properties;

    public UploadResourceConfig(FileStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location(properties.legacyDir()), location(Paths.get("uploads")))
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic())
                .resourceChain(true);
    }

    private String location(Path path) {
        String uri = path.toAbsolutePath().normalize().toUri().toString();
        return uri.endsWith("/") ? uri : uri + "/";
    }
}
