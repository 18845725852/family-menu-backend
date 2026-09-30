package com.example.familymenu.restaurant.dto;

import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.util.List;

public class FamilyWheelResponse {
    private final boolean customized;
    private final List<UserWheelRestaurant> restaurants;
    private final List<FamilyWheelSpin> spins;

    public FamilyWheelResponse(boolean customized, List<UserWheelRestaurant> restaurants, List<FamilyWheelSpin> spins) {
        this.customized = customized;
        this.restaurants = restaurants;
        this.spins = spins;
    }

    public boolean isCustomized() { return customized; }
    public List<UserWheelRestaurant> getRestaurants() { return restaurants; }
    public List<FamilyWheelSpin> getSpins() { return spins; }
}
