package com.example.familymenu.order.repository;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("memory")
public class InMemoryOrderRepository implements OrderRepository {

    private final ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Order save(Order order) {
        Long id = order.getId() == null ? idGenerator.incrementAndGet() : order.getId();
        Order saved = new Order(id, order.getCustomerName(),
                java.util.Collections.unmodifiableList(new java.util.ArrayList<OrderItem>(order.getItems())),
                order.getRemark(), order.getCreatedAt());
        orders.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return orders.remove(id) != null;
    }

    @Override
    public List<Order> findAll() {
        return orders.values().stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
