package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.service.ProductService;
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

    @PostMapping("product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product newProduct(@RequestBody Product product){
        return productService.newProduct(product);
    }

    @GetMapping("products")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public List<Product> allProducts(){
        return productService.allProducts();
    }

    @GetMapping("getproduct/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Product getProduct(@PathVariable int id){
        return productService.getProduct(id);
    }

    @PutMapping("product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(@PathVariable int id,@RequestBody Product product){
        return productService.updateProduct(id, product);
    }
    /*@PostMapping("product/{id}/adjust-stock")
    public Product adjustStock(@PathVariable int id, @RequestBody StockAdjustmentRequest request) {
        return productService.adjustStock(id, request.getQuantityChange());
    }*/

    @DeleteMapping("product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);

        return ResponseEntity.ok("Product Deleted Successfully");

    }
}