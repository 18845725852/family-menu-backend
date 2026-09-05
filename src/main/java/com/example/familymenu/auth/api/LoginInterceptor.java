package com.example.familymenu.auth.api;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Profile("!memory")
public class LoginInterceptor implements HandlerInterceptor {
    private final JdbcTemplate jdbc;
    public LoginInterceptor(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod method = (HandlerMethod) handler;
        if (method.getMethodAnnotation(LoginRequired.class) == null
                && method.getBeanType().getAnnotation(LoginRequired.class) == null) return true;
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return unauthorized(response);
        String token = header.substring("Bearer ".length()).trim();
        List<Long> ids = jdbc.query("SELECT user_id FROM user_sessions WHERE token=? AND expires_at > CURRENT_TIMESTAMP",
                new Object[]{token}, (rs, rowNum) -> rs.getLong(1));
        if (ids.isEmpty()) return unauthorized(response);
        CurrentUser.set(ids.get(0));
        return true;
    }

    private boolean unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentUser.clear();
    }
}
