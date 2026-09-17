package com.example.familymenu.auth.api;

import com.example.familymenu.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController @RequestMapping("/api/me") @RequiredArgsConstructor @LoginRequired @Profile("!memory")
public class UserProfileController {
    private final JdbcTemplate jdbc;
    @PutMapping("/profile")
    public ApiResponse<Map<String, Object>> update(@Valid @RequestBody UserProfileRequest request) {
        String nickname = request.getNickname().trim();
        Long userId = CurrentUser.requireId();
        String avatarUrl = request.getAvatarUrl() == null ? null : request.getAvatarUrl().trim();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            jdbc.update("UPDATE users SET nickname=?, avatar_url=? WHERE id=?", nickname, avatarUrl, userId);
        } else {
            jdbc.update("UPDATE users SET nickname=? WHERE id=?", nickname, userId);
        }
        String savedAvatar = jdbc.queryForObject("SELECT avatar_url FROM users WHERE id=?", String.class, userId);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("nickname", nickname);
        data.put("avatarUrl", savedAvatar);
        return ApiResponse.success(data);
    }
}
