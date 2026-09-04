package com.example.familymenu.order.service;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.dto.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    Order create(CreateOrderRequest request);

    List<Order> list();

    Order detail(Long id);

    void delete(Long id);
}
