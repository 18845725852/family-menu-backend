package com.example.familymenu.order.api;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.order.dto.CreateOrderRequest;
import com.example.familymenu.order.dto.OrderResponse;
import com.example.familymenu.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/families/{familyId}/orders")
@RequiredArgsConstructor
@LoginRequired
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> create(@PathVariable Long familyId, @Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(OrderResponse.from(orderService.create(familyId, CurrentUser.requireId(), request)));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> list(@PathVariable Long familyId) {
        return ApiResponse.success(orderService.list(familyId, CurrentUser.requireId()).stream().map(OrderResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> detail(@PathVariable Long familyId, @PathVariable Long id) {
        return ApiResponse.success(OrderResponse.from(orderService.detail(familyId, id, CurrentUser.requireId())));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long familyId, @PathVariable Long id) {
        orderService.delete(familyId, id, CurrentUser.requireId());
        return ApiResponse.success(null);
    }
}
