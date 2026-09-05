package com.example.familymenu.auth.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!memory")
public class WebMvcAuthConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    public WebMvcAuthConfig(LoginInterceptor loginInterceptor) { this.loginInterceptor = loginInterceptor; }
    @Override
    public void addInterceptors(InterceptorRegistry registry) { registry.addInterceptor(loginInterceptor); }
}
