package com.example.familymenu.auth.api;

import javax.validation.constraints.NotBlank;

public class WechatLoginRequest {
    @NotBlank(message = "微信登录凭证不能为空")
    private String code;
    private String nickname;
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
