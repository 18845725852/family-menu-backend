package com.example.familymenu.restaurant.domain;

public class Restaurant {

    private final Long id;
    private final String name;
    private final int sort;
    private final boolean enabled;

    public Restaurant(Long id, String name, int sort, boolean enabled) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSort() {
        return sort;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
