package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.util.List;

public interface UserWheelRestaurantRepository {
    List<UserWheelRestaurant> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    UserWheelRestaurant insert(Long userId, String name, int sort);
}
