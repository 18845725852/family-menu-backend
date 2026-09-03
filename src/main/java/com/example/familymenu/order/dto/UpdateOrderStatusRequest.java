package com.example.familymenu.order.dto;

import com.example.familymenu.order.domain.OrderStatus;
import javax.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {

    @NotNull(message = "订单状态不能为空")
    private OrderStatus status;

    public UpdateOrderStatusRequest() {
    }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
