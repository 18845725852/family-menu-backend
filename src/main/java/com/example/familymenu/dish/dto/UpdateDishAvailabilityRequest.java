package com.example.familymenu.dish.dto;

import javax.validation.constraints.NotNull;

public class UpdateDishAvailabilityRequest {

    @NotNull(message = "供应状态不能为空")
    private Boolean available;

    public UpdateDishAvailabilityRequest() {
    }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
