package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.dto.CustomApiResponse;
import com.inventory.ManagementSystem.dto.RegistrationRequest;
import com.inventory.ManagementSystem.dto.UserResponse;
import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("suppliers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse<List<UserResponse>>> getAllSuppliers() {
        List<UserResponse> supplierList = supplierService.getAllSuppliers();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "All suppliers fetched successfully",
                        supplierList
                )
        );
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
    public ResponseEntity<CustomApiResponse<UserResponse>> getSupplier(
            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable Integer id) {

        UserResponse response = supplierService.getSupplier(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Supplier fetched successfully",
                        response
                )
        );
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
    public ResponseEntity<CustomApiResponse<UserResponse>> createSupplier(
            @RequestBody RegistrationRequest request) {

        UserResponse response = supplierService.createSupplier(request);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Supplier account registered successfully",
                        response
                )
        );
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
    public ResponseEntity<CustomApiResponse<UserResponse>> updateSupplier(

            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable int id,

            @RequestBody RegistrationRequest supplier) {

        UserResponse response = supplierService.updateSupplier(id, supplier);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Supply profile updated Successfully",
                        response
                )
        );
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
    public ResponseEntity<CustomApiResponse<Void>> deleteSupplier(
            @Parameter(
                    description = "Unique ID of the supplier",
                    example = "1"
            )
            @PathVariable int id) {

        supplierService.deleteSupplier(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Supply Account Deleted Successfully",
                        null
                )
        );

    }
}