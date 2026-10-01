package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(
        name = "Suppliers",
        description = "APIs for managing suppliers"
)
public class SupplierController {


    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    @Operation(
            summary = "Get all suppliers",
            description = "Retrieves a list of all suppliers in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Suppliers retrieved successfully"
    )
    @GetMapping("suppliers/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }


    @Operation(
            summary = "Get supplier by ID",
            description = "Retrieves a specific supplier using their unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found"
            )
    })
    @GetMapping("supplier/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPLIER')")
    public Supplier getSupplier(
            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable Integer id) {

        return supplierService.getSupplier(id);
    }


    @Operation(
            summary = "Create a supplier",
            description = "Creates a new supplier in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid supplier information"
            )
    })
    @PostMapping("supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Supplier createSupplier(
            @RequestBody Supplier supplier) {

        return supplierService.createSupplier(supplier);
    }


    @Operation(
            summary = "Update a supplier",
            description = "Updates the information of an existing supplier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid supplier information"
            )
    })
    @PutMapping("supplier/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPLIER')")
    public Supplier updateSupplier(

            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable int id,

            @RequestBody Supplier supplier) {

        return supplierService.updateSupplier(id, supplier);
    }


    @Operation(
            summary = "Delete a supplier",
            description = "Deletes an existing supplier from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found"
            )
    })
    @DeleteMapping("supplier/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSupplier(
            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable int id) {

        supplierService.deleteSupplier(id);
    }
}