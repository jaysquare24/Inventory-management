package com.inventory.ManagementSystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class IncomingOrderResponse {
    private int supplyId;
    private int productId;
    private int supplierId;
    private int supervisorId;
    private String productName;
    private String productDesc;
    private int suppliedQuantity;
    private BigDecimal productUnitPrice;
    private BigDecimal suppliedTotalPrice;
    private LocalDateTime dateSupplied;
    private String productCategoryName;


}
