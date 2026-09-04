package com.example.familymenu.family.dto;
import javax.validation.constraints.NotBlank;
public class CreateFamilyRequest { @NotBlank private String name; @NotBlank private String ownerName; public String getName(){return name;} public void setName(String v){name=v;} public String getOwnerName(){return ownerName;} public void setOwnerName(String v){ownerName=v;} }
