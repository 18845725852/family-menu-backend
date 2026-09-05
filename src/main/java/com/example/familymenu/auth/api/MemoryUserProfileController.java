package com.example.familymenu.auth.api;
import com.example.familymenu.common.api.ApiResponse;
import org.springframework.context.annotation.Profile; import org.springframework.web.bind.annotation.*; import java.util.Collections; import java.util.Map;
import javax.validation.Valid;
@RestController @RequestMapping("/api/me") @Profile("memory") @LoginRequired
public class MemoryUserProfileController {
    @PutMapping("/profile") public ApiResponse<Map<String,Object>> update(@Valid @RequestBody UserProfileRequest request) {
        return ApiResponse.success(Collections.<String,Object>singletonMap("nickname", request.getNickname().trim()));
    }
}
