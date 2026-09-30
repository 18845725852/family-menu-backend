package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!memory")
public class JdbcFamilyWheelRepository implements FamilyWheelRepository {
    private final JdbcTemplate jdbc;

    public JdbcFamilyWheelRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<UserWheelRestaurant> findOptions(Long familyId) {
        return jdbc.query(
                "SELECT id, name, sort FROM family_wheel_restaurants WHERE family_id=? ORDER BY sort ASC, id ASC",
                new Object[]{familyId},
                (rs, rowNum) -> new UserWheelRestaurant(rs.getLong("id"), rs.getString("name"), rs.getInt("sort")));
    }

    public void deleteOptions(Long familyId) {
        jdbc.update("DELETE FROM family_wheel_restaurants WHERE family_id=?", familyId);
    }

    public UserWheelRestaurant insertOption(Long familyId, String name, int sort) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO family_wheel_restaurants(family_id, name, sort) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, familyId);
            statement.setString(2, name);
            statement.setInt(3, sort);
            return statement;
        }, keys);
        return new UserWheelRestaurant(keys.getKey().longValue(), name, sort);
    }

    public FamilyWheelSpin insertSpin(Long familyId, Long userId, String nickname, String avatarUrl,
                                      String restaurantName, LocalDateTime createdAt) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO family_wheel_spins(family_id, user_id, nickname, avatar_url, restaurant_name, created_at) VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, familyId);
            statement.setLong(2, userId);
            statement.setString(3, nickname);
            statement.setString(4, avatarUrl);
            statement.setString(5, restaurantName);
            statement.setTimestamp(6, Timestamp.valueOf(createdAt));
            return statement;
        }, keys);
        return new FamilyWheelSpin(keys.getKey().longValue(), userId, nickname, avatarUrl, restaurantName, createdAt);
    }

    public List<FamilyWheelSpin> findSpins(Long familyId, int limit) {
        String sql = "SELECT s.id, s.user_id, s.nickname, s.avatar_url, u.nickname AS user_nickname, u.avatar_url AS user_avatar, "
                + "s.restaurant_name, s.created_at "
                + "FROM family_wheel_spins s LEFT JOIN users u ON u.id=s.user_id "
                + "WHERE s.family_id=? ORDER BY s.id DESC";
        if (limit > 0) sql = sql + " LIMIT " + limit;
        return jdbc.query(
                sql,
                new Object[]{familyId},
                (rs, rowNum) -> {
                    String liveName = rs.getString("user_nickname");
                    String storedName = rs.getString("nickname");
                    String nickname = liveName != null && !liveName.trim().isEmpty() ? liveName : storedName;
                    String liveAvatar = rs.getString("user_avatar");
                    String avatar = liveAvatar != null && !liveAvatar.trim().isEmpty() ? liveAvatar : rs.getString("avatar_url");
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    return new FamilyWheelSpin(rs.getLong("id"), rs.getLong("user_id"), nickname, avatar,
                            rs.getString("restaurant_name"), createdAt == null ? null : createdAt.toLocalDateTime());
                });
    }
}
