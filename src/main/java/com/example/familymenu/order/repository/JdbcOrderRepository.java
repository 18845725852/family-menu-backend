package com.example.familymenu.order.repository;

import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memory")
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        Long orderId = order.getId();
        if (orderId == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO orders (family_id, creator_user_id, customer_name, remark, created_at) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, order.getFamilyId());
                statement.setLong(2, order.getCreatorUserId());
                statement.setString(3, order.getCustomerName());
                statement.setString(4, order.getRemark());
                statement.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
                return statement;
            }, keyHolder);
            orderId = keyHolder.getKey().longValue();
            for (OrderItem item : order.getItems()) {
                jdbcTemplate.update("INSERT INTO order_items (order_id, dish_id, dish_name, quantity, remark) "
                                + "VALUES (?, ?, ?, ?, ?)", orderId, item.getDishId(), item.getDishName(),
                        item.getQuantity(), item.getRemark());
            }
        } else {
        }
        return new Order(orderId, order.getCustomerName(), Collections.unmodifiableList(
                new ArrayList<OrderItem>(order.getItems())), order.getRemark(), order.getCreatedAt());
    }

    @Override
    public Optional<Order> findById(Long id) {
        List<Order> orders = jdbcTemplate.query("SELECT id, family_id, creator_user_id, customer_name, remark, created_at "
                        + "FROM orders WHERE id = ?", new Object[]{id}, (rs, rowNum) -> mapOrder(rs));
        return orders.isEmpty() ? Optional.<Order>empty() : Optional.of(withItems(orders.get(0)));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM orders WHERE id = ?", id) > 0;
    }

    @Override
    public long countItemsByDishId(Long dishId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM order_items WHERE dish_id = ?",
                Long.class, dishId);
        return count == null ? 0L : count;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query("SELECT id, family_id, creator_user_id, customer_name, remark, created_at FROM orders "
                + "ORDER BY created_at DESC, id DESC", (rs, rowNum) -> mapOrder(rs));
        List<Order> result = new ArrayList<Order>(orders.size());
        for (Order order : orders) {
            result.add(withItems(order));
        }
        return result;
    }

    private Order mapOrder(java.sql.ResultSet rs) throws java.sql.SQLException {
        long familyId = rs.getLong("family_id");
        long creatorUserId = rs.getLong("creator_user_id");
        return new Order(rs.getLong("id"), rs.wasNull() ? null : familyId, creatorUserId, rs.getString("customer_name"), Collections.<OrderItem>emptyList(),
                rs.getString("remark"), rs.getTimestamp("created_at").toLocalDateTime());
    }

    private Order withItems(Order order) {
        List<OrderItem> items = jdbcTemplate.query("SELECT dish_id, dish_name, quantity, remark FROM order_items "
                        + "WHERE order_id = ? ORDER BY id ASC", new Object[]{order.getId()}, (rs, rowNum) ->
                new OrderItem(rs.getLong("dish_id"), rs.getString("dish_name"), rs.getInt("quantity"),
                        rs.getString("remark")));
        return new Order(order.getId(), order.getFamilyId(), order.getCreatorUserId(), order.getCustomerName(), items, order.getRemark(),
                order.getCreatedAt());
    }

    @Override
    public List<Order> findAllByFamilyId(Long familyId) {
        List<Order> orders = jdbcTemplate.query("SELECT id, family_id, creator_user_id, customer_name, remark, created_at FROM orders WHERE family_id = ? ORDER BY created_at DESC, id DESC", new Object[]{familyId}, (rs, rowNum) -> mapOrder(rs));
        List<Order> result = new ArrayList<Order>(orders.size());
        for (Order order : orders) result.add(withItems(order));
        return result;
    }

    @Override
    public boolean belongsToFamily(Long orderId, Long familyId) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM orders WHERE id=? AND family_id=?", Integer.class, orderId, familyId);
        return count != null && count > 0;
    }
}
