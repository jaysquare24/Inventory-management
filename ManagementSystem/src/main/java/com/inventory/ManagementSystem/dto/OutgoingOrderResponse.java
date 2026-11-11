package com.inventory.ManagementSystem.dto;

import com.inventory.ManagementSystem.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OutgoingOrderResponse {

    private Long id;
    private List<OrderItem> items;
    private BigDecimal totalPriceBeforeDiscount;
    private BigDecimal totalPriceAfterDiscount;
    private BigDecimal discount;
    private LocalDate orderDate;

    public OutgoingOrderResponse(Long id, List<OrderItem> items, BigDecimal totalPriceBeforeDiscount, BigDecimal totalPriceAfterDiscount, BigDecimal discount, LocalDate orderDate) {
        this.id = id;
        this.items = items;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.discount = discount;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

    public BigDecimal getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }


}
