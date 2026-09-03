package com.example.familymenu.dish.api;

import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.dto.CreateDishRequest;
import com.example.familymenu.dish.dto.DishResponse;
import com.example.familymenu.dish.dto.UpdateDishAvailabilityRequest;
import com.example.familymenu.dish.service.DishService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public ApiResponse<List<DishResponse>> list(@RequestParam(required = false) String category) {
        return ApiResponse.success(dishService.listAvailable(category).stream().map(DishResponse::from)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ApiResponse<DishResponse> create(@Valid @RequestBody CreateDishRequest request) {
        return ApiResponse.success(DishResponse.from(dishService.create(request)));
    }

    @PatchMapping("/{id}/availability")
    public ApiResponse<DishResponse> updateAvailability(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateDishAvailabilityRequest request) {
        return ApiResponse.success(DishResponse.from(dishService.updateAvailability(id, request.getAvailable())));
    }
}
