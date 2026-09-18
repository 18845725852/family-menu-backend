package com.example.familymenu.restaurant.api;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import com.example.familymenu.restaurant.dto.UserWheelRestaurantItemRequest;
import com.example.familymenu.restaurant.dto.UserWheelRestaurantSaveRequest;
import com.example.familymenu.restaurant.service.UserWheelRestaurantService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me/wheel-restaurants")
@RequiredArgsConstructor
@LoginRequired
public class UserWheelRestaurantController {
    private final UserWheelRestaurantService service;

    @GetMapping
    public ApiResponse<List<UserWheelRestaurant>> list() {
        return ApiResponse.success(service.listMine(CurrentUser.requireId()));
    }

    @PutMapping
    public ApiResponse<List<UserWheelRestaurant>> save(@Valid @RequestBody UserWheelRestaurantSaveRequest request) {
        List<String> names = new ArrayList<String>();
        if (request.getItems() != null) {
            for (UserWheelRestaurantItemRequest item : request.getItems()) {
                names.add(item.getName());
            }
        }
        return ApiResponse.success(service.replaceMine(CurrentUser.requireId(), names));
    }
}
