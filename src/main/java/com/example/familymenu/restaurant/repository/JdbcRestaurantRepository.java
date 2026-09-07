package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.Restaurant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!memory")
public class JdbcRestaurantRepository implements RestaurantRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRestaurantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Restaurant map(ResultSet resultSet) throws SQLException {
        return new Restaurant(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getInt("sort"), resultSet.getBoolean("enabled"));
    }

    @Override
    public List<Restaurant> findAll(boolean all) {
        String sql = "SELECT id,name,sort,enabled FROM restaurants" + (all ? "" : " WHERE enabled=TRUE") + " ORDER BY sort,id";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> map(resultSet));
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        List<Restaurant> restaurants = jdbcTemplate.query(
                "SELECT id,name,sort,enabled FROM restaurants WHERE id=?",
                new Object[]{id},
                (resultSet, rowNum) -> map(resultSet));
        return restaurants.isEmpty() ? Optional.<Restaurant>empty() : Optional.of(restaurants.get(0));
    }

    @Override
    public Optional<Restaurant> findByName(String name) {
        List<Restaurant> restaurants = jdbcTemplate.query(
                "SELECT id,name,sort,enabled FROM restaurants WHERE name=?",
                new Object[]{name},
                (resultSet, rowNum) -> map(resultSet));
        return restaurants.isEmpty() ? Optional.<Restaurant>empty() : Optional.of(restaurants.get(0));
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO restaurants(name,sort,enabled) VALUES(?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, restaurant.getName());
                statement.setInt(2, restaurant.getSort());
                statement.setBoolean(3, restaurant.isEnabled());
                return statement;
            }, keyHolder);
            return new Restaurant(keyHolder.getKey().longValue(), restaurant.getName(), restaurant.getSort(), restaurant.isEnabled());
        }
        jdbcTemplate.update(
                "UPDATE restaurants SET name=?,sort=?,enabled=? WHERE id=?",
                restaurant.getName(), restaurant.getSort(), restaurant.isEnabled(), restaurant.getId());
        return restaurant;
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM restaurants WHERE id=?", id) > 0;
    }
}
