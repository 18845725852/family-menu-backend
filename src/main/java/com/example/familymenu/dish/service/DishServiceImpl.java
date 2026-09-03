package com.example.familymenu.dish.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.dto.CreateDishRequest;
import com.example.familymenu.dish.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    public List<Dish> listAvailable(String category) {
        return dishRepository.findAvailable(category);
    }

    @Override
    public Dish create(CreateDishRequest request) {
        int sort = request.getSort() == null ? 0 : request.getSort();
        return dishRepository.save(new Dish(null, request.getName(), request.getCategory(), request.getDescription(),
                request.getImageUrl(), true, sort));
    }

    @Override
    public Dish updateAvailability(Long id, boolean available) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new BusinessException("菜品不存在: " + id));
        return dishRepository.save(new Dish(dish.getId(), dish.getName(), dish.getCategory(), dish.getDescription(),
                dish.getImageUrl(), available, dish.getSort()));
    }
}
