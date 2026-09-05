package com.example.familymenu.auth.api;

import com.example.familymenu.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController @RequestMapping("/api/me") @RequiredArgsConstructor @LoginRequired @Profile("!memory")
public class UserProfileController {
    private final JdbcTemplate jdbc;
    @PutMapping("/profile")
    public ApiResponse<Map<String, Object>> update(@Valid @RequestBody UserProfileRequest request) {
        String nickname = request.getNickname().trim();
        jdbc.update("UPDATE users SET nickname=? WHERE id=?", nickname, CurrentUser.requireId());
        return ApiResponse.success(Collections.<String, Object>singletonMap("nickname", nickname));
    }
}
