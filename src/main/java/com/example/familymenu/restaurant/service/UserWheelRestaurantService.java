package com.example.familymenu.restaurant.service;

import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.util.List;

public interface UserWheelRestaurantService {
    List<UserWheelRestaurant> listMine(Long userId);
    List<UserWheelRestaurant> replaceMine(Long userId, List<String> names);
}
