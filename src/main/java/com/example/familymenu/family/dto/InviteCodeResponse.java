package com.example.familymenu.family.dto;

public class InviteCodeResponse {
    private final Long familyId;
    private final String inviteCode;
    private final String expiresAt;
    public InviteCodeResponse(Long familyId, String inviteCode, String expiresAt) { this.familyId = familyId; this.inviteCode = inviteCode; this.expiresAt = expiresAt; }
    public Long getFamilyId() { return familyId; }
    public String getInviteCode() { return inviteCode; }
    public String getExpiresAt() { return expiresAt; }
}
