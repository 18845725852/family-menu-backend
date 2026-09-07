package com.example.familymenu.order.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;

public class CreateOrderRequest {

    @NotEmpty(message = "至少选择一道菜")
    @Valid
    private List<OrderItemRequest> items;
    @Size(max = 200, message = "订单备注不能超过200个字")
    private String remark;

    public CreateOrderRequest() {
    }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
