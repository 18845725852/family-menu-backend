package com.example.familymenu.auth.api;

import com.example.familymenu.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Profile("!memory")
public class AuthController {
    private final WechatAuthService service;
    @PostMapping("/wechat-login")
    public ApiResponse<WechatLoginResponse> login(@Valid @RequestBody WechatLoginRequest request) {
        return ApiResponse.success(service.login(request.getCode(), request.getNickname()));
    }

    @PostMapping("/logout")
    @LoginRequired
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer ".length()).trim();
        service.logout(CurrentUser.requireId(), token);
        return ApiResponse.success(null);
    }
}
