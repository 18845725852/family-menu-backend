package com.example.familymenu.order.service;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.dto.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    Order create(Long familyId, Long userId, CreateOrderRequest request);

    List<Order> list(Long familyId, Long userId);

    Order detail(Long familyId, Long id, Long userId);

    void delete(Long familyId, Long id, Long userId);
}
