package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    List<Restaurant> findAll(boolean all);

    Optional<Restaurant> findById(Long id);

    Optional<Restaurant> findByName(String name);

    Restaurant save(Restaurant restaurant);

    boolean deleteById(Long id);
}
