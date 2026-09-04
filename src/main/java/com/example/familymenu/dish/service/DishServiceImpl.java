package com.example.familymenu.dish.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.dto.CreateDishRequest;
import com.example.familymenu.dish.repository.DishRepository;
import com.example.familymenu.order.repository.OrderRepository;
import com.example.familymenu.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Dish> listAvailable(String category) {
        return dishRepository.findAvailable(category);
    }

    @Override
    public Dish create(CreateDishRequest request) {
        ensureCategory(request.getCategory());
        ensureUnique(request.getName(), request.getCategory(), null);
        int sort = request.getSort() == null ? 0 : request.getSort();
        return dishRepository.save(new Dish(null, request.getName(), request.getCategory(),
                request.getImageUrl(), true, sort));
    }

    @Override
    public Dish update(Long id, CreateDishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new BusinessException("菜品不存在: " + id));
        ensureCategory(request.getCategory());
        ensureUnique(request.getName(), request.getCategory(), id);
        int sort = request.getSort() == null ? dish.getSort() : request.getSort();
        return dishRepository.save(new Dish(dish.getId(), request.getName(), request.getCategory(),
                request.getImageUrl(), dish.isAvailable(), sort));
    }

    @Override
    public Dish updateAvailability(Long id, boolean available) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new BusinessException("菜品不存在: " + id));
        return dishRepository.save(new Dish(dish.getId(), dish.getName(), dish.getCategory(),
                dish.getImageUrl(), available, dish.getSort()));
    }

    @Override
    public void delete(Long id) {
        if (!dishRepository.findById(id).isPresent()) {
            throw new BusinessException("菜品不存在: " + id);
        }
        if (orderRepository.countItemsByDishId(id) > 0) {
            throw new BusinessException("菜品已存在历史订单，只能下架，不能删除");
        }
        if (!dishRepository.deleteById(id)) {
            throw new BusinessException("菜品不存在: " + id);
        }
    }

    private void ensureUnique(String name, String category, Long excludeId) {
        if (dishRepository.existsByNameAndCategory(name, category, excludeId)) {
            throw new BusinessException("同分类下已存在相同菜名");
        }
    }

    private void ensureCategory(String category) {
        if (!categoryRepository.findByName(category).filter(c -> c.isEnabled()).isPresent()) {
            throw new BusinessException("菜品类型不存在或已停用: " + category);
        }
    }
}
