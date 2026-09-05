package com.example.familymenu.family.dto;

import javax.validation.constraints.NotBlank;

public class JoinFamilyRequest {
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;
    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
}
