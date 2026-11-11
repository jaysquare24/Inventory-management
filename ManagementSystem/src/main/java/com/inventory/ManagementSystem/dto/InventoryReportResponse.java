package com.inventory.ManagementSystem.dto;

import java.math.BigDecimal;

public class InventoryReportResponse {
    private String productName;
    private String category;
    private int availableQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalValue;


    public InventoryReportResponse(String productName, String category, int availableQuantity, BigDecimal unitPrice, BigDecimal totalValue) {
        this.productName = productName;
        this.category = category;
        this.availableQuantity = availableQuantity;
        this.unitPrice = unitPrice;
        this.totalValue = totalValue;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
}
