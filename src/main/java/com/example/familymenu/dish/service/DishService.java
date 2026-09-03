package com.example.familymenu.dish.service;

import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.dto.CreateDishRequest;

import java.util.List;

public interface DishService {

    List<Dish> listAvailable(String category);

    Dish create(CreateDishRequest request);

    Dish update(Long id, CreateDishRequest request);

    Dish updateAvailability(Long id, boolean available);
}
