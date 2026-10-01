package com.inventory.ManagementSystem.dto;

public class TopProductReportResponse {

    private int productId;
    private String productName;
    private int quantitySold;

    public TopProductReportResponse(int productId, String productName, int quantitySold) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}
