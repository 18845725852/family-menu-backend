package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
public class InMemoryUserWheelRestaurantRepository implements UserWheelRestaurantRepository {
    private final ConcurrentMap<Long, List<UserWheelRestaurant>> data = new ConcurrentHashMap<Long, List<UserWheelRestaurant>>();
    private final AtomicLong ids = new AtomicLong();

    public synchronized List<UserWheelRestaurant> findByUserId(Long userId) {
        return new ArrayList<UserWheelRestaurant>(data.getOrDefault(userId, Collections.<UserWheelRestaurant>emptyList()));
    }

    public synchronized void deleteByUserId(Long userId) {
        data.remove(userId);
    }

    public synchronized UserWheelRestaurant insert(Long userId, String name, int sort) {
        UserWheelRestaurant saved = new UserWheelRestaurant(ids.incrementAndGet(), name, sort);
        List<UserWheelRestaurant> list = data.get(userId);
        if (list == null) {
            list = new ArrayList<UserWheelRestaurant>();
            data.put(userId, list);
        }
        list.add(saved);
        return saved;
    }
}
