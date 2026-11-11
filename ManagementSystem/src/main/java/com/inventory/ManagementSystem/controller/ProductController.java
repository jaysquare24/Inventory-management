package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.dto.CustomApiResponse;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(
            summary = "new product",
            description = "Create/Add a new product to the management inventory"

    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid Product data")

    })
    @PostMapping("product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse<Product>> newProduct(@RequestBody Product product){
        Product productCreated = productService.newProduct(product);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product created Successfully",
                        productCreated
                )
        );
    }


    @Operation(
            summary = "Get all product",
            description = "Returns a list of all product"
    )
    @GetMapping("products")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<CustomApiResponse<List<Product>>> allProducts(){
       List<Product> products = productService.allProducts();

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Products fetched Successfully",
                        products
                )
        );
    }


    @Operation(
            summary = "Get one product",
            description = "Returns a product by its Id"
    )
    @GetMapping("getproduct/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<CustomApiResponse<Product>> getProduct(@PathVariable int id){
        Product product = productService.getProduct(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product fetched Successfully",
                        product
                )
        );
    }

    @Operation(
            summary = "Edit one product",
            description = "Edit an existing product"
    )
    @PutMapping("product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse<Product>> updateProduct(@PathVariable int id,@RequestBody Product product){
        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product updated Successfully",
                        updatedProduct
                )
        );


    }
    /*@PostMapping("product/{id}/adjust-stock")
    public Product adjustStock(@PathVariable int id, @RequestBody StockAdjustmentRequest request) {
        return productService.adjustStock(id, request.getQuantityChange());
    }*/


    @Operation(
            summary = "Delete product",
            description = "Delete a product from management inventory"
    )
    @DeleteMapping("product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse<String>> deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Product Deleted Successfully",
                        null
                )
        );


    }
}