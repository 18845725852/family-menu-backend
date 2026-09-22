package com.example.familymenu.dish.repository;

import com.example.familymenu.dish.domain.Dish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memory")
public class JdbcDishRepository implements DishRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcDishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Dish> findAvailable(String category) {
        String sql = "SELECT id, name, category, description, recipe, image_url, available, sort, family_id "
                + "FROM dishes WHERE available = TRUE AND family_id IS NULL";
        if (category == null || category.trim().isEmpty()) {
            sql += " ORDER BY sort ASC, id ASC";
            return jdbcTemplate.query(sql, (rs, rowNum) -> map(rs));
        }
        sql += " AND category = ? ORDER BY sort ASC, id ASC";
        return jdbcTemplate.query(sql, new Object[]{category}, (rs, rowNum) -> map(rs));
    }

    @Override
    public Optional<Dish> findById(Long id) {
        List<Dish> dishes = jdbcTemplate.query(
                "SELECT id, name, category, description, recipe, image_url, available, sort, family_id FROM dishes WHERE id = ?",
                new Object[]{id}, (rs, rowNum) -> map(rs));
        return dishes.isEmpty() ? Optional.<Dish>empty() : Optional.of(dishes.get(0));
    }

    @Override
    public List<Dish> findAvailableByFamily(Long familyId) {
        return jdbcTemplate.query(
                "SELECT id, name, category, description, recipe, image_url, available, sort, family_id "
                        + "FROM dishes WHERE available = TRUE AND family_id = ? ORDER BY sort ASC, id ASC",
                new Object[]{familyId}, (rs, rowNum) -> map(rs));
    }

    @Override
    public int countByFamily(Long familyId) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM dishes WHERE family_id = ?", Integer.class, familyId);
        return count == null ? 0 : count.intValue();
    }

    @Override
    public Dish save(Dish dish) {
        if (dish.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO dishes (name, category, description, recipe, image_url, available, sort, family_id) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, dish.getName());
                statement.setString(2, dish.getCategory());
                statement.setString(3, dish.getDescription());
                statement.setString(4, dish.getRecipe());
                statement.setString(5, dish.getImageUrl());
                statement.setBoolean(6, dish.isAvailable());
                statement.setInt(7, dish.getSort());
                if (dish.getFamilyId() == null) statement.setNull(8, java.sql.Types.BIGINT);
                else statement.setLong(8, dish.getFamilyId());
                return statement;
            }, keyHolder);
            return new Dish(keyHolder.getKey().longValue(), dish.getName(), dish.getCategory(),
                    dish.getDescription(), dish.getRecipe(), dish.getImageUrl(), dish.isAvailable(), dish.getSort(), dish.getFamilyId());
        }
        jdbcTemplate.update("UPDATE dishes SET name = ?, category = ?, description = ?, recipe = ?, image_url = ?, available = ?, sort = ? "
                        + "WHERE id = ?", dish.getName(), dish.getCategory(), dish.getDescription(), dish.getRecipe(), dish.getImageUrl(),
                dish.isAvailable(), dish.getSort(), dish.getId());
        return dish;
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM dishes WHERE id = ?", id) > 0;
    }

    @Override
    public boolean existsByNameAndCategory(String name, String category, Long excludeId) {
        String sql = "SELECT COUNT(1) FROM dishes WHERE name = ? AND category = ? AND family_id IS NULL";
        if (excludeId == null) {
            return jdbcTemplate.queryForObject(sql, Integer.class, name, category) > 0;
        }
        return jdbcTemplate.queryForObject(sql + " AND id <> ?", Integer.class, name, category, excludeId) > 0;
    }

    @Override
    public boolean existsFamilyDish(Long familyId, String name, String category, Long excludeId) {
        String sql = "SELECT COUNT(1) FROM dishes WHERE family_id = ? AND name = ? AND category = ?";
        if (excludeId == null) {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, familyId, name, category);
            return count != null && count.intValue() > 0;
        }
        Integer count = jdbcTemplate.queryForObject(sql + " AND id <> ?", Integer.class, familyId, name, category, excludeId);
        return count != null && count.intValue() > 0;
    }

    @Override
    public long countByCategory(String category) { return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM dishes WHERE category = ?", Long.class, category); }

    @Override
    public void renameCategory(String oldName, String newName) { jdbcTemplate.update("UPDATE dishes SET category = ? WHERE category = ?", newName, oldName); }

    private Dish map(java.sql.ResultSet rs) throws java.sql.SQLException {
        long familyIdValue = rs.getLong("family_id");
        Long familyId = rs.wasNull() ? null : Long.valueOf(familyIdValue);
        return new Dish(rs.getLong("id"), rs.getString("name"), rs.getString("category"), rs.getString("description"),
                rs.getString("recipe"), rs.getString("image_url"), rs.getBoolean("available"),
                rs.getInt("sort"), familyId);
    }
}
