package com.example.familymenu.auth.api;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
public class UserProfileRequest {
    @NotBlank(message = "称呼不能为空") @Size(max = 50, message = "称呼不能超过50个字")
    private String nickname;
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
