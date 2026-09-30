package com.example.familymenu.restaurant.domain;

import java.time.LocalDateTime;

public class FamilyWheelSpin {
    private final Long id;
    private final Long userId;
    private final String nickname;
    private final String avatarUrl;
    private final String restaurantName;
    private final LocalDateTime createdAt;

    public FamilyWheelSpin(Long id, Long userId, String nickname, String avatarUrl,
                           String restaurantName, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.restaurantName = restaurantName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getRestaurantName() { return restaurantName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
