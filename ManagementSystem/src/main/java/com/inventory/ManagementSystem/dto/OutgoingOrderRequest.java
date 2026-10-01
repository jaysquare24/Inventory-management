package com.inventory.ManagementSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OutgoingOrderRequest {
    @NotNull
    private Long productId;
    @Min(1)
    private int orderQuantity;


    public OutgoingOrderRequest(Long productId, int orderQuantity) {
        this.productId = productId;
        this.orderQuantity = orderQuantity;

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

}
