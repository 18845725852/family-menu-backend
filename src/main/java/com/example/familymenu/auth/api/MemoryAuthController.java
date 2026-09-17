package com.example.familymenu.auth.api;

import com.example.familymenu.common.api.ApiResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** 开发内存模式使用固定测试用户，生产环境不会加载此 Controller。 */
@RestController
@RequestMapping("/api/auth")
@Profile("memory")
public class MemoryAuthController {
    @PostMapping("/wechat-login")
    public ApiResponse<WechatLoginResponse> login(@RequestBody WechatLoginRequest request) {
        String nickname = request.getNickname() == null || request.getNickname().trim().isEmpty()
                ? DefaultNicknameGenerator.generate() : request.getNickname().trim();
        return ApiResponse.success(new WechatLoginResponse(1L, nickname,
                "memory-" + UUID.randomUUID().toString().replace("-", ""), MemoryUserProfileController.storedAvatar));
    }

    @PostMapping("/logout")
    @LoginRequired
    public ApiResponse<Void> logout() {
        return ApiResponse.success(null);
    }
}
