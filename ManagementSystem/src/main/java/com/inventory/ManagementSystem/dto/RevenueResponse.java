package com.inventory.ManagementSystem.dto;

import java.math.BigDecimal;

public class RevenueResponse {
    private BigDecimal totalRevenue;
    private int totalOrders;

    public RevenueResponse(BigDecimal totalRevenue, int totalOrders) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}
