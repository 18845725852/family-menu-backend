package com.example.familymenu.family.domain;

public class FamilyMember {
    private final Long id; private final Long familyId; private final Long userId; private final String nickname; private final String role;
    public FamilyMember(Long id, Long familyId, Long userId, String nickname, String role) { this.id=id; this.familyId=familyId; this.userId=userId; this.nickname=nickname; this.role=role; }
    public Long getId(){return id;} public Long getFamilyId(){return familyId;} public Long getUserId(){return userId;} public String getNickname(){return nickname;} public String getRole(){return role;}
}
