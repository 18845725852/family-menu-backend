package com.example.familymenu.dish.dto;

import com.example.familymenu.dish.domain.Dish;

public class DishResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final String description;
    private final String recipe;
    private final String imageUrl;
    private final boolean available;
    private final int sort;

    public DishResponse(Long id, String name, String category, String description, String recipe,
                        String imageUrl, boolean available, int sort) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.recipe = recipe;
        this.imageUrl = imageUrl;
        this.available = available;
        this.sort = sort;
    }

    public static DishResponse from(Dish dish) {
        return new DishResponse(dish.getId(), dish.getName(), dish.getCategory(), dish.getDescription(),
                dish.getRecipe(), dish.getImageUrl(), dish.isAvailable(), dish.getSort());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getRecipe() { return recipe; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return available; }
    public int getSort() { return sort; }
}
