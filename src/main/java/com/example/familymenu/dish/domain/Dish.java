package com.example.familymenu.dish.domain;

public class Dish {

    private final Long id;
    private final String name;
    private final String category;
    private final String description;
    private final String imageUrl;
    private final boolean available;
    private final int sort;

    public Dish(Long id, String name, String category,
                String imageUrl, boolean available, int sort) {
        this(id, name, category, null, imageUrl, available, sort);
    }
    public Dish(Long id, String name, String category, String description,
                String imageUrl, boolean available, int sort) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.available = available;
        this.sort = sort;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return available; }
    public int getSort() { return sort; }
}
