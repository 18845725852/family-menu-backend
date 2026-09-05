package com.example.familymenu.auth.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Profile("memory")
public class MemoryLoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod method = (HandlerMethod) handler;
        if (method.getMethodAnnotation(LoginRequired.class) == null && method.getBeanType().getAnnotation(LoginRequired.class) == null) return true;
        if (request.getHeader("Authorization") == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return false; }
        CurrentUser.set(1L);
        return true;
    }
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { CurrentUser.clear(); }
}
