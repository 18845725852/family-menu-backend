package com.example.familymenu.order.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemRequest {

    @NotNull(message = "菜品不能为空")
    private Long dishId;
    @Min(value = 1, message = "数量至少为1")
    private int quantity;
    private String remark;

    public OrderItemRequest() {
    }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
