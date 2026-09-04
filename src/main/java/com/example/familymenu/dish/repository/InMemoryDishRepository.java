package com.example.familymenu.dish.repository;

import com.example.familymenu.dish.domain.Dish;
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
public class InMemoryDishRepository implements DishRepository {

    private final ConcurrentMap<Long, Dish> dishes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(2);

    public InMemoryDishRepository() {
        save(new Dish(1L, "番茄炒蛋", "家常菜", null, true, 1));
        save(new Dish(2L, "紫菜蛋花汤", "汤", null, true, 2));
    }

    @Override
    public List<Dish> findAvailable(String category) {
        return dishes.values().stream()
                .filter(Dish::isAvailable)
                .filter(dish -> category == null || category.trim().isEmpty() || dish.getCategory().equals(category))
                .sorted(Comparator.comparingInt(Dish::getSort).thenComparing(Dish::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return Optional.ofNullable(dishes.get(id));
    }

    @Override
    public Dish save(Dish dish) {
        Long id = dish.getId() == null ? idGenerator.incrementAndGet() : dish.getId();
        Dish saved = new Dish(id, dish.getName(), dish.getCategory(), dish.getImageUrl(),
                dish.isAvailable(), dish.getSort());
        dishes.put(id, saved);
        return saved;
    }

    @Override
    public boolean deleteById(Long id) {
        return dishes.remove(id) != null;
    }

    @Override
    public boolean existsByNameAndCategory(String name, String category, Long excludeId) {
        return dishes.values().stream().anyMatch(dish -> !dish.getId().equals(excludeId)
                && dish.getName().equals(name) && dish.getCategory().equals(category));
    }
}
