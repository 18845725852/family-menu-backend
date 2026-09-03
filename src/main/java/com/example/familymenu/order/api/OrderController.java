package com.example.familymenu.order.api;

import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.dto.CreateOrderRequest;
import com.example.familymenu.order.dto.OrderResponse;
import com.example.familymenu.order.service.OrderService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(OrderResponse.from(orderService.create(request)));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> list() {
        return ApiResponse.success(orderService.list().stream().map(OrderResponse::from)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ApiResponse.success(null);
    }
}
