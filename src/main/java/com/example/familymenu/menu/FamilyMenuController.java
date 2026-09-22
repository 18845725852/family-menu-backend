package com.example.familymenu.menu;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.dish.dto.CreateDishRequest;
import com.example.familymenu.dish.dto.DishResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/families/{familyId}/menu")
@LoginRequired
public class FamilyMenuController {
    private final FamilyMenuService service;

    public FamilyMenuController(FamilyMenuService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<FamilyMenuResponse> get(@PathVariable Long familyId) {
        return ApiResponse.success(service.get(familyId, CurrentUser.requireId()));
    }

    @PutMapping
    public ApiResponse<FamilyMenuResponse> setCustomMenu(@PathVariable Long familyId,
                                                         @RequestBody CustomMenuRequest request) {
        return ApiResponse.success(service.setCustomMenu(familyId, CurrentUser.requireId(), request.getCustomMenu()));
    }

    @PostMapping("/dishes")
    public ApiResponse<DishResponse> create(@PathVariable Long familyId, @Valid @RequestBody CreateDishRequest request) {
        return ApiResponse.success(DishResponse.from(service.create(familyId, CurrentUser.requireId(), request)));
    }

    @PutMapping("/dishes/{dishId}")
    public ApiResponse<DishResponse> update(@PathVariable Long familyId, @PathVariable Long dishId,
                                            @Valid @RequestBody CreateDishRequest request) {
        return ApiResponse.success(DishResponse.from(service.update(familyId, CurrentUser.requireId(), dishId, request)));
    }

    @DeleteMapping("/dishes/{dishId}")
    public ApiResponse<Void> delete(@PathVariable Long familyId, @PathVariable Long dishId) {
        service.delete(familyId, CurrentUser.requireId(), dishId);
        return ApiResponse.success(null);
    }
}
