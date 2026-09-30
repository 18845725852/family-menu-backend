package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.time.LocalDateTime;
import java.util.List;

public interface FamilyWheelRepository {
    List<UserWheelRestaurant> findOptions(Long familyId);

    void deleteOptions(Long familyId);

    UserWheelRestaurant insertOption(Long familyId, String name, int sort);

    FamilyWheelSpin insertSpin(Long familyId, Long userId, String nickname, String avatarUrl,
                               String restaurantName, LocalDateTime createdAt);

    List<FamilyWheelSpin> findSpins(Long familyId, int limit);
}
