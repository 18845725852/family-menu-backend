package com.example.familymenu.family.dto;
import javax.validation.constraints.NotBlank;
public class CreateFamilyRequest { @NotBlank private String name; public String getName(){return name;} public void setName(String v){name=v;} }
