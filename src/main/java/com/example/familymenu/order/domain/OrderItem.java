package com.example.familymenu.order.domain;

public class OrderItem {

    private final Long dishId;
    private final String dishName;
    private final int quantity;
    private final String remark;

    public OrderItem(Long dishId, String dishName, int quantity, String remark) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.remark = remark;
    }

    public Long getDishId() { return dishId; }
    public String getDishName() { return dishName; }
    public int getQuantity() { return quantity; }
    public String getRemark() { return remark; }
}
