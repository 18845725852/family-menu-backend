package com.example.familymenu.dish.repository;

import com.example.familymenu.dish.domain.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository {

    List<Dish> findAvailable(String category);

    List<Dish> findAvailableByFamily(Long familyId);

    int countByFamily(Long familyId);

    Optional<Dish> findById(Long id);

    Dish save(Dish dish);

    boolean deleteById(Long id);

    boolean existsByNameAndCategory(String name, String category, Long excludeId);

    boolean existsFamilyDish(Long familyId, String name, String category, Long excludeId);

    long countByCategory(String category);

    void renameCategory(String oldName, String newName);
}
