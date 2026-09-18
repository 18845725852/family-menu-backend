package com.example.familymenu.dish.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateDishRequest {

    @NotBlank(message = "菜名不能为空")
    @Size(max = 50, message = "菜名不能超过50个字")
    private String name;
    @NotBlank(message = "分类不能为空")
    private String category;
    private String imageUrl;
    private String description;
    private String recipe;
    private Integer sort;

    public CreateDishRequest() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRecipe() { return recipe; }
    public void setRecipe(String recipe) { this.recipe = recipe; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
}
