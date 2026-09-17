package com.example.familymenu.order.dto;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final Long creatorUserId;
    private final String customerName;
    private final String customerAvatarUrl;
    private final List<OrderItemResponse> items;
    private final String remark;
    private final LocalDateTime createdAt;

    public OrderResponse(Long id, Long creatorUserId, String customerName, String customerAvatarUrl,
                         List<OrderItemResponse> items, String remark, LocalDateTime createdAt) {
        this.id = id;
        this.creatorUserId = creatorUserId;
        this.customerName = customerName;
        this.customerAvatarUrl = customerAvatarUrl;
        this.items = items;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getCreatorUserId(), order.getCustomerName(),
                order.getCustomerAvatarUrl(), items, order.getRemark(), order.getCreatedAt());
    }

    public Long getId() { return id; }
    public Long getCreatorUserId() { return creatorUserId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerAvatarUrl() { return customerAvatarUrl; }
    public List<OrderItemResponse> getItems() { return items; }
    public String getRemark() { return remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
