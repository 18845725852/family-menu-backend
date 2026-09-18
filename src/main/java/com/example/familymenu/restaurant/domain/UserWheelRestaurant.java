package com.example.familymenu.restaurant.domain;

public class UserWheelRestaurant {
    private final Long id;
    private final String name;
    private final int sort;

    public UserWheelRestaurant(Long id, String name, int sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getSort() { return sort; }
}
