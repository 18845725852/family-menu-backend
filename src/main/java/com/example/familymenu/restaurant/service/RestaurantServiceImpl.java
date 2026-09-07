package com.example.familymenu.restaurant.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.restaurant.domain.Restaurant;
import com.example.familymenu.restaurant.repository.RestaurantRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final Random random = new Random();

    @Override
    public List<Restaurant> list(boolean all) {
        return repository.findAll(all);
    }

    @Override
    public Restaurant create(String name, Integer sort, Boolean enabled) {
        ensureUnique(name, null);
        return repository.save(new Restaurant(null, name, sort == null ? 0 : sort, enabled == null || enabled));
    }

    @Override
    public Restaurant update(Long id, String name, Integer sort, Boolean enabled) {
        Restaurant old = repository.findById(id).orElseThrow(() -> new BusinessException("餐厅不存在: " + id));
        ensureUnique(name, id);
        return repository.save(new Restaurant(id, name, sort == null ? old.getSort() : sort, enabled == null ? old.isEnabled() : enabled));
    }

    @Override
    public void delete(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new BusinessException("餐厅不存在: " + id);
        }
        if (!repository.deleteById(id)) {
            throw new BusinessException("餐厅不存在: " + id);
        }
    }

    @Override
    public Restaurant spin() {
        List<Restaurant> restaurants = repository.findAll(false);
        if (restaurants.isEmpty()) {
            throw new BusinessException("当前没有可抽取的餐厅");
        }
        return restaurants.get(random.nextInt(restaurants.size()));
    }

    private void ensureUnique(String name, Long excludeId) {
        repository.findByName(name).ifPresent(restaurant -> {
            if (!restaurant.getId().equals(excludeId)) {
                throw new BusinessException("餐厅已存在");
            }
        });
    }
}
