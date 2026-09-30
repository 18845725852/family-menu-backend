package com.example.familymenu.restaurant.service;

import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.dto.FamilyWheelResponse;
import java.util.List;

public interface FamilyWheelService {
    FamilyWheelResponse get(Long familyId, Long userId);

    List<FamilyWheelSpin> listSpins(Long familyId, Long userId);

    FamilyWheelResponse replace(Long familyId, Long userId, List<String> names);

    FamilyWheelSpin spin(Long familyId, Long userId, String name);
}
