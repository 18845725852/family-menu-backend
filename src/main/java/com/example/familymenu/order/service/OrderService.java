package com.example.familymenu.order.service;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderStatus;
import com.example.familymenu.order.dto.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    Order create(CreateOrderRequest request);

    List<Order> list(OrderStatus status);

    Order updateStatus(Long id, OrderStatus status);
}
