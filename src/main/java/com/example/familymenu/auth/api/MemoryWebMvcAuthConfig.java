package com.example.familymenu.auth.api;
import org.springframework.context.annotation.Configuration; import org.springframework.context.annotation.Profile; import org.springframework.web.servlet.config.annotation.*;
@Configuration @Profile("memory") public class MemoryWebMvcAuthConfig implements WebMvcConfigurer { private final MemoryLoginInterceptor interceptor; public MemoryWebMvcAuthConfig(MemoryLoginInterceptor i){interceptor=i;} public void addInterceptors(InterceptorRegistry registry){registry.addInterceptor(interceptor);} }
