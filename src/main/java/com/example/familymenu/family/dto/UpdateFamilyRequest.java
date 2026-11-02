package com.example.familymenu.family.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateFamilyRequest {
    @NotBlank(message = "请填写家庭组名称")
    @Size(max = 30, message = "家庭组名称不能超过30个字")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
