package com.example.familymenu.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final String customerName;
    private final List<OrderItem> items;
    private final String remark;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

    public Order(Long id, String customerName, List<OrderItem> items, String remark,
                 OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.remark = remark;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return items; }
    public String getRemark() { return remark; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Order withStatus(OrderStatus nextStatus) {
        return new Order(id, customerName, items, remark, nextStatus, createdAt);
    }
}
