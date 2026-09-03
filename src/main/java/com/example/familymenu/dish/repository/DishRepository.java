package com.example.familymenu.dish.repository;

import com.example.familymenu.dish.domain.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository {

    List<Dish> findAvailable(String category);

    Optional<Dish> findById(Long id);

    Dish save(Dish dish);
}
