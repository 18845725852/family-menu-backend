package com.example.familymenu.order.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.repository.DishRepository;
import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;
import com.example.familymenu.order.domain.OrderStatus;
import com.example.familymenu.order.dto.CreateOrderRequest;
import com.example.familymenu.order.dto.OrderItemRequest;
import com.example.familymenu.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    @Override
    public Order create(CreateOrderRequest request) {
        List<OrderItem> items = new ArrayList<OrderItem>(request.getItems().size());
        for (OrderItemRequest itemRequest : request.getItems()) {
            Dish dish = dishRepository.findById(itemRequest.getDishId())
                    .filter(Dish::isAvailable)
                    .orElseThrow(() -> new BusinessException("菜品不可用: " + itemRequest.getDishId()));
            items.add(new OrderItem(dish.getId(), dish.getName(), itemRequest.getQuantity(), itemRequest.getRemark()));
        }
        Order order = new Order(null, request.getCustomerName(), items, request.getRemark(), OrderStatus.PENDING,
                LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> list(OrderStatus status) {
        return orderRepository.findAll(status);
    }

    @Override
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("订单不存在: " + id));
        validateStatusTransition(order.getStatus(), status);
        return orderRepository.save(order.withStatus(status));
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.COMPLETED || current == OrderStatus.CANCELLED) {
            throw new BusinessException("订单已结束，不能修改状态");
        }
        if (current == OrderStatus.PENDING && next == OrderStatus.COMPLETED) {
            throw new BusinessException("订单需要先进入制作中");
        }
    }
}
