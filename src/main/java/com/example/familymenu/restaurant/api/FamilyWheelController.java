package com.example.familymenu.restaurant.api;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.dto.FamilyWheelResponse;
import com.example.familymenu.restaurant.dto.FamilyWheelSpinRequest;
import com.example.familymenu.restaurant.dto.UserWheelRestaurantItemRequest;
import com.example.familymenu.restaurant.dto.UserWheelRestaurantSaveRequest;
import com.example.familymenu.restaurant.service.FamilyWheelService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/families/{familyId}/wheel")
@LoginRequired
public class FamilyWheelController {
    private final FamilyWheelService service;

    public FamilyWheelController(FamilyWheelService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<FamilyWheelResponse> get(@PathVariable Long familyId) {
        return ApiResponse.success(service.get(familyId, CurrentUser.requireId()));
    }

    @PutMapping("/restaurants")
    public ApiResponse<FamilyWheelResponse> replace(@PathVariable Long familyId,
                                                    @Valid @RequestBody UserWheelRestaurantSaveRequest request) {
        List<String> names = new ArrayList<String>();
        if (request.getItems() != null) {
            for (UserWheelRestaurantItemRequest item : request.getItems()) {
                names.add(item.getName());
            }
        }
        return ApiResponse.success(service.replace(familyId, CurrentUser.requireId(), names));
    }

    @GetMapping("/spins")
    public ApiResponse<List<FamilyWheelSpin>> spins(@PathVariable Long familyId) {
        return ApiResponse.success(service.listSpins(familyId, CurrentUser.requireId()));
    }

    @PostMapping("/spins")
    public ApiResponse<FamilyWheelSpin> spin(@PathVariable Long familyId,
                                             @Valid @RequestBody FamilyWheelSpinRequest request) {
        return ApiResponse.success(service.spin(familyId, CurrentUser.requireId(), request.getName()));
    }
}
