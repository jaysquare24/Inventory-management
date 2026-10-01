package com.inventory.ManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OutgoingOrderResponse {

    private Long id;
    private String productName;
    private int orderQuantity;
    private BigDecimal totalPriceBeforeDiscount;
    private BigDecimal totalPriceAfterDiscount;
    private BigDecimal discount;
    private LocalDate orderDate;

    public OutgoingOrderResponse(Long id, String productName, int orderQuantity, BigDecimal totalPriceBeforeDiscount, BigDecimal totalPriceAfterDiscount, BigDecimal discount, LocalDate orderDate) {
        this.id = id;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.discount = discount;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }


    public String getProductName() {
        return productName;
    }


    public int getOrderQuantity() {
        return orderQuantity;
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
