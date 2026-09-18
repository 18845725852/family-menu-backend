package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!memory")
public class JdbcUserWheelRestaurantRepository implements UserWheelRestaurantRepository {
    private final JdbcTemplate jdbc;

    public JdbcUserWheelRestaurantRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<UserWheelRestaurant> findByUserId(Long userId) {
        return jdbc.query(
                "SELECT id, name, sort FROM user_wheel_restaurants WHERE user_id=? ORDER BY sort ASC, id ASC",
                new Object[]{userId},
                (rs, rowNum) -> new UserWheelRestaurant(rs.getLong("id"), rs.getString("name"), rs.getInt("sort")));
    }

    public void deleteByUserId(Long userId) {
        jdbc.update("DELETE FROM user_wheel_restaurants WHERE user_id=?", userId);
    }

    public UserWheelRestaurant insert(Long userId, String name, int sort) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO user_wheel_restaurants(user_id, name, sort) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, userId);
            statement.setString(2, name);
            statement.setInt(3, sort);
            return statement;
        }, keys);
        return new UserWheelRestaurant(keys.getKey().longValue(), name, sort);
    }
}
