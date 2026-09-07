package com.example.familymenu.restaurant.dto;

import javax.validation.constraints.NotBlank;

public class RestaurantRequest {

    @NotBlank
    private String name;
    private Integer sort;
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
