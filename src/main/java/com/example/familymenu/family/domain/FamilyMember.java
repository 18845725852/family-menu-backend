package com.example.familymenu.family.domain;

public class FamilyMember {
    private final Long id;
    private final Long familyId;
    private final Long userId;
    private final String nickname;
    private final String role;
    private final String avatarUrl;

    public FamilyMember(Long id, Long familyId, Long userId, String nickname, String role) {
        this(id, familyId, userId, nickname, role, null);
    }

    public FamilyMember(Long id, Long familyId, Long userId, String nickname, String role, String avatarUrl) {
        this.id = id;
        this.familyId = familyId;
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() { return id; }
    public Long getFamilyId() { return familyId; }
    public Long getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getRole() { return role; }
    public String getAvatarUrl() { return avatarUrl; }
}
