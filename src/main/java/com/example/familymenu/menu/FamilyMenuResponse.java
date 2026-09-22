package com.example.familymenu.menu;

import com.example.familymenu.dish.dto.DishResponse;
import java.util.List;

public class FamilyMenuResponse {
    private final boolean customMenu;
    private final List<DishResponse> dishes;

    public FamilyMenuResponse(boolean customMenu, List<DishResponse> dishes) {
        this.customMenu = customMenu;
        this.dishes = dishes;
    }

    public boolean isCustomMenu() { return customMenu; }
    public List<DishResponse> getDishes() { return dishes; }
}
