package com.example.familymenu.restaurant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserWheelRestaurantItemRequest {
    @NotBlank(message = "餐厅名称不能为空")
    @Size(max = 20, message = "餐厅名称不能超过20个字")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
