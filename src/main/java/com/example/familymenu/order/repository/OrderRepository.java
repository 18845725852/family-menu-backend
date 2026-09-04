package com.example.familymenu.order.repository;

import com.example.familymenu.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    boolean deleteById(Long id);

    long countItemsByDishId(Long dishId);

    List<Order> findAll();
}
