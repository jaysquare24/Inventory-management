package com.inventory.ManagementSystem.controller;

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
    public IncomingOrder newOrder(@RequestBody IncomingOrder incomingOrder){
        return incomingOrderService.newOrder(incomingOrder);
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
    public List<IncomingOrder> allOrders(){
        return incomingOrderService.allOrders();
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
    public IncomingOrder getOrder(@PathVariable int id){
        return incomingOrderService.getOrder(id);
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
    public IncomingOrder updateOrder(@PathVariable int id,@RequestBody IncomingOrder incomingOrder){
        return incomingOrderService.updateOrder(id, incomingOrder);
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
    public ResponseEntity <String> deleteOrder(@PathVariable int id){
        incomingOrderService.deleteOrder(id);

        return ResponseEntity.ok("Order has been deleted");
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
    public List<IncomingOrder> getOrdersByProduct(@PathVariable int productId) {
        return incomingOrderService.getOrdersByProduct(productId);
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
    public List<IncomingOrder> getOrdersBySupplier(@PathVariable int supplierId){
        return incomingOrderService.getOrdersBySupplier(supplierId);
    }
}