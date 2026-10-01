package com.inventory.ManagementSystem.controller;


import com.inventory.ManagementSystem.dto.*;
import com.inventory.ManagementSystem.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Tag(
        name = "Reports",
        description = "APIs for viewing inventory, sales, revenue and product reports")
public class ReportController {


    private  final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("inventory")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get inventory report",
            description = "Returns inventory information. Accessible by ADMIN and STAFF."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory report fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "You do not have permission to access this resource"
            )
    })

    public ResponseEntity<CustomApiResponse<List<InventoryReportResponse>>> inventory(){
        List<InventoryReportResponse> inventory = reportService.getInventory();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Inventory fetch successful",
                        inventory
                )
        );
    }

    @GetMapping("sales")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get sales report",
            description = "Returns sales information. Accessible only by ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sales report fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only ADMIN users can access this resource"
            )
    })
    public ResponseEntity<CustomApiResponse<List<SalesReportResponse>>> sales(){
        List<SalesReportResponse> sales = reportService.getSales();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Sales report successful",
                        sales
                )
        );
    }

    @GetMapping("top-products")
    @Operation(
            summary = "Get top products",
            description = "Returns a list of the best-performing products."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Top products fetched successfully"
    )
    public ResponseEntity<CustomApiResponse<List<TopProductReportResponse>>> topProducts(){
        List<TopProductReportResponse> topProducts = reportService.getTopProducts();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Top product fetched successfully",
                        topProducts
                )
        );
    }

    @GetMapping("low-stock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get low-stock products",
            description = "Returns products whose stock level is low. Accessible by ADMIN and STAFF."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Low-stock products fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "You do not have permission to access this resource"
            )
    })
    public ResponseEntity<CustomApiResponse<List<LowStockResponse>>> lowStockProduct (){
        List<LowStockResponse> lowStocks = reportService.lowStocks();

        return  ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Low stocks fetched successfully",
                        lowStocks
                )
        );
    }

    @GetMapping("revenue")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get revenue report",
            description = "Returns the business revenue report. Accessible only by ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Revenue report fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only ADMIN users can access this resource"
            )
    })
    public ResponseEntity<CustomApiResponse<RevenueResponse>> revenue(){

        RevenueResponse response = reportService.getRevenue();
        return  ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Revenue fetched successfully",
                        response
                )
        ) ;
    }


}