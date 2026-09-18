package com.example.familymenu.restaurant.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserWheelRestaurantSaveRequest {
    @NotNull(message = "餐厅列表不能为空")
    @Size(min = 3, max = 16, message = "餐厅数量需要在3到16家之间")
    @Valid
    private List<UserWheelRestaurantItemRequest> items;

    public List<UserWheelRestaurantItemRequest> getItems() { return items; }
    public void setItems(List<UserWheelRestaurantItemRequest> items) { this.items = items; }
}
