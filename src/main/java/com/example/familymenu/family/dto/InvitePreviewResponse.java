package com.example.familymenu.family.dto;

public class InvitePreviewResponse {
    private final Long familyId;
    private final String familyName;
    private final String inviteCode;

    public InvitePreviewResponse(Long familyId, String familyName, String inviteCode) {
        this.familyId = familyId;
        this.familyName = familyName;
        this.inviteCode = inviteCode;
    }

    public Long getFamilyId() { return familyId; }
    public String getFamilyName() { return familyName; }
    public String getInviteCode() { return inviteCode; }
}
