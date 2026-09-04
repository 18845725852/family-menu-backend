package com.example.familymenu.family.dto;
import javax.validation.constraints.NotBlank;
public class UpdateMemberRoleRequest { @NotBlank private String role; public String getRole(){return role;} public void setRole(String v){role=v;} }
