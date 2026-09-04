package com.example.familymenu.common.web;
import org.springframework.context.annotation.Configuration; import org.springframework.web.servlet.config.annotation.*;
@Configuration public class UploadResourceConfig implements WebMvcConfigurer { @Override public void addResourceHandlers(ResourceHandlerRegistry registry){registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");} }
