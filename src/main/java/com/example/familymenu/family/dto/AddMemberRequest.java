package com.example.familymenu.family.dto;
import javax.validation.constraints.NotBlank;
public class AddMemberRequest { @NotBlank private String nickname; private String role; public String getNickname(){return nickname;} public void setNickname(String v){nickname=v;} public String getRole(){return role;} public void setRole(String v){role=v;} }
