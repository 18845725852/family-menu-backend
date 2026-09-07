package com.example.familymenu.restaurant.api;

import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.restaurant.domain.Restaurant;
import com.example.familymenu.restaurant.dto.RestaurantRequest;
import com.example.familymenu.restaurant.service.RestaurantService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @GetMapping
    public ApiResponse<List<Restaurant>> list(@RequestParam(defaultValue = "false") boolean all) {
        return ApiResponse.success(service.list(all));
    }

    @GetMapping("/spin")
    public ApiResponse<Restaurant> spin() {
        return ApiResponse.success(service.spin());
    }

    @PostMapping
    public ApiResponse<Restaurant> create(@Valid @RequestBody RestaurantRequest request) {
        return ApiResponse.success(service.create(request.getName(), request.getSort(), request.getEnabled()));
    }

    @PutMapping("/{id}")
    public ApiResponse<Restaurant> update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        return ApiResponse.success(service.update(id, request.getName(), request.getSort(), request.getEnabled()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success(null);
    }
}
