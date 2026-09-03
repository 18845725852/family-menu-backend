package com.example.familymenu.order.repository;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll(OrderStatus status);
}
