package com.example.familymenu.restaurant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FamilyWheelSpinRequest {
    @NotBlank(message = "餐厅名称不能为空")
    @Size(max = 50, message = "餐厅名称不能超过50个字")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
