package com.example.familymenu.restaurant.service;

import com.example.familymenu.restaurant.domain.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> list(boolean all);

    Restaurant create(String name, Integer sort, Boolean enabled);

    Restaurant update(Long id, String name, Integer sort, Boolean enabled);

    void delete(Long id);

    Restaurant spin();
}
