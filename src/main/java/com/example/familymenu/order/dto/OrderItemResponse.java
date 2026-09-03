package com.example.familymenu.order.dto;

import com.example.familymenu.order.domain.OrderItem;

public class OrderItemResponse {

    private final Long dishId;
    private final String dishName;
    private final int quantity;
    private final String remark;

    public OrderItemResponse(Long dishId, String dishName, int quantity, String remark) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.remark = remark;
    }

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(item.getDishId(), item.getDishName(), item.getQuantity(), item.getRemark());
    }

    public Long getDishId() { return dishId; }
    public String getDishName() { return dishName; }
    public int getQuantity() { return quantity; }
    public String getRemark() { return remark; }
}
