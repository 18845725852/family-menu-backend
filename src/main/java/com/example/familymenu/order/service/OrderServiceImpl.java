package com.example.familymenu.order.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.repository.DishRepository;
import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;
import com.example.familymenu.order.dto.CreateOrderRequest;
import com.example.familymenu.order.dto.OrderItemRequest;
import com.example.familymenu.order.repository.OrderRepository;
import com.example.familymenu.family.repository.FamilyRepository;
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
    private final FamilyRepository familyRepository;

    @Override
    public Order create(Long familyId, Long userId, CreateOrderRequest request) {
        requireMember(familyId, userId);
        List<OrderItem> items = new ArrayList<OrderItem>(request.getItems().size());
        for (OrderItemRequest itemRequest : request.getItems()) {
            Dish dish = dishRepository.findById(itemRequest.getDishId())
                    .filter(Dish::isAvailable)
                    .orElseThrow(() -> new BusinessException("菜品不可用: " + itemRequest.getDishId()));
            items.add(new OrderItem(dish.getId(), dish.getName(), itemRequest.getQuantity(), itemRequest.getRemark()));
        }
        Order order = new Order(null, familyId, userId, null, items, request.getRemark(),
                LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> list(Long familyId, Long userId) {
        requireMember(familyId, userId);
        return orderRepository.findAllByFamilyId(familyId);
    }

    @Override
    public Order detail(Long familyId, Long id, Long userId) {
        requireMember(familyId, userId);
        return orderRepository.findById(id).filter(order -> familyId.equals(order.getFamilyId()))
                .orElseThrow(() -> new BusinessException("订单不存在: " + id));
    }

    @Override
    public void delete(Long familyId, Long id, Long userId) {
        requireMember(familyId, userId);
        if (!orderRepository.belongsToFamily(id, familyId) || !orderRepository.deleteById(id)) {
            throw new BusinessException("订单不存在: " + id);
        }
    }

    private void requireMember(Long familyId, Long userId) {
        if (familyId == null || !familyRepository.isMember(familyId, userId)) {
            throw new BusinessException("你不是该家庭成员");
        }
    }
}
