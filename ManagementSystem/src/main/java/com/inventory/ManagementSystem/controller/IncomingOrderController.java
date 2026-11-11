package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.dto.CustomApiResponse;
import com.inventory.ManagementSystem.dto.IncomingOrderResponse;
import com.inventory.ManagementSystem.model.IncomingOrder;
import com.inventory.ManagementSystem.service.IncomingOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Incoming Orders", description = "APIs for managing incoming orders")
public class IncomingOrderController {


    private final IncomingOrderService incomingOrderService;

    public IncomingOrderController(IncomingOrderService incomingOrderService) {
        this.incomingOrderService = incomingOrderService;
    }

    @PostMapping("order")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create incoming order",
            description = "Creates a new incoming order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order details"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<CustomApiResponse<IncomingOrder>> newOrder(@RequestBody IncomingOrder incomingOrder){
        return ResponseEntity.ok(
                new CustomApiResponse<>(
                true,
                "Incoming order successfully saved",
                incomingOrderService.newOrder(incomingOrder)
             )
        );

    }

    @GetMapping("orders")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get all incoming orders",
            description = "Retrieves all incoming orders"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<CustomApiResponse<List<IncomingOrder>>> allOrders(){


        List<IncomingOrder> response = incomingOrderService.allOrders();

       return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Orders fetched successfully",
                        response
                )
        );
    }

    @GetMapping("order/{id}")
    @Operation(
            summary = "Get incoming order",
            description = "Retrieves an incoming order by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<IncomingOrder>> getOrder(@PathVariable int id){
        IncomingOrder response = incomingOrderService.getOrder(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Incoming order fetched successfully",
                        response

                )
        );
    }

    @PutMapping("order/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update incoming order",
            description = "Updates an existing incoming order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order details"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<IncomingOrder>> updateOrder(@PathVariable int id,@RequestBody IncomingOrder incomingOrder){
         IncomingOrder response = incomingOrderService.updateOrder(id, incomingOrder);

        return ResponseEntity.ok(
                 new CustomApiResponse<>(
                         true,
                         "Order successfully updated",
                         response

                 )
         );
    }

    @DeleteMapping("order/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete incoming order",
            description = "Deletes an incoming order by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<String>> deleteOrder(@PathVariable int id){
        incomingOrderService.deleteOrder(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Order has been deleted",
                        null
                )
        );

    }

    @GetMapping("orders/product/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get orders by product",
            description = "Retrieves incoming orders associated with a product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product orders fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<CustomApiResponse<List<IncomingOrder>>> getOrdersByProduct(@PathVariable("id") int productId) {
        List<IncomingOrder> response = incomingOrderService.getOrdersByProduct(productId);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product orders fetched successfully",
                        response
                )
        );
    }

    @GetMapping("orders/supplier/{id}")
    @Operation(
            summary = "Get orders by supplier",
            description = "Retrieves incoming orders associated with a supplier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Supplier orders fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<CustomApiResponse<List<IncomingOrderResponse>>> getOrdersBySupplier(@PathVariable("id") int supplierId){
        List<IncomingOrderResponse> response = incomingOrderService.getOrdersBySupplier(supplierId);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Supplier supplies fetched successfully",
                        response
                )
        );
    }
}