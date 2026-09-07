package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.Restaurant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final Map<Long, Restaurant> data = new HashMap<Long, Restaurant>();
    private final AtomicLong seq = new AtomicLong();

    public InMemoryRestaurantRepository() {
        save(new Restaurant(null, "鲁园外地锅鸡", 1, true));
        save(new Restaurant(null, "牛new寿喜烧", 2, true));
        save(new Restaurant(null, "西塔老太太烤肉", 3, true));
        save(new Restaurant(null, "烤匠", 4, true));
        save(new Restaurant(null, "马丫东北菜", 5, true));
        save(new Restaurant(null, "海底捞火锅", 6, true));
        save(new Restaurant(null, "鹊拾湘", 7, true));
        save(new Restaurant(null, "一绪寿喜烧", 8, true));
        save(new Restaurant(null, "添添潮牛", 9, true));
        save(new Restaurant(null, "荆九爷爆炒", 10, true));
    }

    @Override
    public List<Restaurant> findAll(boolean all) {
        List<Restaurant> restaurants = new ArrayList<Restaurant>(data.values());
        if (!all) {
            restaurants.removeIf(restaurant -> !restaurant.isEnabled());
        }
        restaurants.sort(Comparator.comparingInt(Restaurant::getSort).thenComparing(Restaurant::getId));
        return restaurants;
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Restaurant> findByName(String name) {
        return data.values().stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        long id = restaurant.getId() == null ? seq.incrementAndGet() : restaurant.getId();
        Restaurant saved = new Restaurant(id, restaurant.getName(), restaurant.getSort(), restaurant.isEnabled());
        data.put(id, saved);
        return saved;
    }

    @Override
    public boolean deleteById(Long id) {
        return data.remove(id) != null;
    }
}
