package com.inventory.ManagementSystem.dto;

import java.math.BigDecimal;

public class SalesReportResponse {
    private int productId;
    private String productName;
    private int quantitySold;
    private BigDecimal totalSales;

    public SalesReportResponse(int id, String productName, int quantitySold, BigDecimal totalSales) {
        this.productId = id;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
    }

    public int getProductId(){
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
}
