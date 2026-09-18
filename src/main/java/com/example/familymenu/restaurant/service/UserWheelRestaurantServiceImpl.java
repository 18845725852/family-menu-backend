package com.example.familymenu.restaurant.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import com.example.familymenu.restaurant.repository.UserWheelRestaurantRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWheelRestaurantServiceImpl implements UserWheelRestaurantService {
    public static final int MIN_COUNT = 3;
    public static final int MAX_COUNT = 16;
    public static final int MAX_NAME_LENGTH = 20;

    private final UserWheelRestaurantRepository repository;

    public List<UserWheelRestaurant> listMine(Long userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public List<UserWheelRestaurant> replaceMine(Long userId, List<String> names) {
        List<String> cleaned = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();
        if (names != null) {
            for (String raw : names) {
                String name = raw == null ? "" : raw.trim();
                if (name.isEmpty()) throw new BusinessException("餐厅名称不能为空");
                if (name.length() > MAX_NAME_LENGTH) throw new BusinessException("餐厅名称不能超过20个字");
                if (!seen.add(name)) throw new BusinessException("餐厅名称不能重复: " + name);
                cleaned.add(name);
            }
        }
        if (cleaned.size() < MIN_COUNT || cleaned.size() > MAX_COUNT) {
            throw new BusinessException("餐厅数量需要在3到16家之间");
        }
        repository.deleteByUserId(userId);
        List<UserWheelRestaurant> saved = new ArrayList<UserWheelRestaurant>();
        for (int i = 0; i < cleaned.size(); i++) {
            saved.add(repository.insert(userId, cleaned.get(i), i));
        }
        return saved;
    }
}
