package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.dto.CustomApiResponse;
import com.inventory.ManagementSystem.dto.OutgoingOrderRequest;
import com.inventory.ManagementSystem.dto.OutgoingOrderResponse;
import com.inventory.ManagementSystem.service.OutgoingOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Outgoing Orders", description = "APIs for managing outgoing orders")
public class OutgoingOrderController {


    private final OutgoingOrderService outgoingOrderService;

    public OutgoingOrderController(OutgoingOrderService outgoingOrderService) {
        this.outgoingOrderService = outgoingOrderService;
    }

    @PostMapping("outgoing-orders")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Create an outgoing order",
            description = "Creates a new outgoing order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource")
    })
    public ResponseEntity<CustomApiResponse<Void>> createOrder(@Valid @RequestBody OutgoingOrderRequest request){
        outgoingOrderService.createOrder(request);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Order Successfully Created",
                        null
                )
        );
    }

    @GetMapping("outgoing-orders/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get all outgoing orders",
            description = "Returns all outgoing orders in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource")
    })
    public ResponseEntity<CustomApiResponse<List<OutgoingOrderResponse>>> getAllOutgoingOrders(){

        List<OutgoingOrderResponse> outgoingOrders = outgoingOrderService.getAllOutgoingOrder();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Orders fetched successfully",
                        outgoingOrders
                )
        );
    }

    @GetMapping("outgoing-orders/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get outgoing order by ID",
            description = "Returns an outgoing order using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order fetched successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<OutgoingOrderResponse>> getOutgoingOrder(@PathVariable int id){

        OutgoingOrderResponse orderResponse =  outgoingOrderService.getOutgoingOrder(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Order fetched successful",
                        orderResponse
                )
        );
    }

    @PutMapping("outgoing-orders/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Edit an outgoing order",
            description = "Updates an existing outgoing order using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order edited successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<Void>> editOutgoingOrder (@PathVariable int id, @Valid @RequestBody OutgoingOrderRequest request){
        outgoingOrderService.editOutgoingOder(id, request);
        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Order edited successfully",
                        null
                )
        );
    }

    @GetMapping("outgoing-orders/product/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get outgoing orders for a product",
            description = "Returns all outgoing orders associated with a specific product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product orders fetched successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<CustomApiResponse<List<OutgoingOrderResponse>>> getAllProductOutgoingOrder(@PathVariable int id){
        List<OutgoingOrderResponse> productOrders =  outgoingOrderService.getAllProductOutgoingOrder(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product Order fetch successful",
                        productOrders
                )
        ) ;
    }

    @DeleteMapping("outgoing-orders/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Delete an outgoing order",
            description = "Deletes an outgoing order using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<CustomApiResponse<Void>> deleteOutgoingOrder(@PathVariable int id){
        outgoingOrderService.deleteOutgoingOrder(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Order successfully Deleted",
                        null
                )
        ) ;
    }

}