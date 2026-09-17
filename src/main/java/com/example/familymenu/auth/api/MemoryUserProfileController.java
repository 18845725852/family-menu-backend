package com.example.familymenu.auth.api;
import com.example.familymenu.common.api.ApiResponse;
import org.springframework.context.annotation.Profile; import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap; import java.util.Map;
import javax.validation.Valid;
@RestController @RequestMapping("/api/me") @Profile("memory") @LoginRequired
public class MemoryUserProfileController {
    static String storedAvatar;
    @PutMapping("/profile") public ApiResponse<Map<String,Object>> update(@Valid @RequestBody UserProfileRequest request) {
        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("nickname", request.getNickname().trim());
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().trim().isEmpty()) {
            storedAvatar = request.getAvatarUrl().trim();
        }
        data.put("avatarUrl", storedAvatar);
        return ApiResponse.success(data);
    }
}
