package com.example.familymenu.auth.api;

public class WechatLoginResponse {
    private final Long userId;
    private final String nickname;
    private final String token;
    private final String avatarUrl;

    public WechatLoginResponse(Long userId, String nickname, String token) {
        this(userId, nickname, token, null);
    }

    public WechatLoginResponse(Long userId, String nickname, String token, String avatarUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.token = token;
        this.avatarUrl = avatarUrl;
    }

    public Long getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getToken() { return token; }
    public String getAvatarUrl() { return avatarUrl; }
}
