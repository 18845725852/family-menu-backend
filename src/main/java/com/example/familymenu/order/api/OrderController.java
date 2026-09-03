package com.example.familymenu.order.api;

import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.domain.OrderStatus;
import com.example.familymenu.order.dto.CreateOrderRequest;
import com.example.familymenu.order.dto.OrderResponse;
import com.example.familymenu.order.dto.UpdateOrderStatusRequest;
import com.example.familymenu.order.service.OrderService;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(OrderResponse.from(orderService.create(request)));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> list(@RequestParam(required = false) OrderStatus status) {
        return ApiResponse.success(orderService.list(status).stream().map(OrderResponse::from)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateOrderStatusRequest request) {
        Order updated = orderService.updateStatus(id, request.getStatus());
        return ApiResponse.success(OrderResponse.from(updated));
    }
}
