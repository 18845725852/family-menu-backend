package com.example.familymenu.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final String customerName;
    private final List<OrderItem> items;
    private final String remark;
    private final LocalDateTime createdAt;

    public Order(Long id, String customerName, List<OrderItem> items, String remark,
                 LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return items; }
    public String getRemark() { return remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
