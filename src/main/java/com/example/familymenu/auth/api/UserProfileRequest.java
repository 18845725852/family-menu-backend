package com.example.familymenu.auth.api;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
public class UserProfileRequest {
    @NotBlank(message = "称呼不能为空") @Size(max = 50, message = "称呼不能超过50个字")
    private String nickname;
    @Size(max = 500, message = "头像地址过长")
    private String avatarUrl;
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
