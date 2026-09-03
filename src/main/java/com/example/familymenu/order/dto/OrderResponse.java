package com.example.familymenu.order.dto;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;
import com.example.familymenu.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final String customerName;
    private final List<OrderItemResponse> items;
    private final String remark;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

    public OrderResponse(Long id, String customerName, List<OrderItemResponse> items, String remark,
                         OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.remark = remark;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getCustomerName(), items, order.getRemark(), order.getStatus(),
                order.getCreatedAt());
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItemResponse> getItems() { return items; }
    public String getRemark() { return remark; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
