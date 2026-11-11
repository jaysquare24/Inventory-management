package com.inventory.ManagementSystem.controller;


import com.inventory.ManagementSystem.dto.CategoryRequest;
import com.inventory.ManagementSystem.dto.CategoryResponse;
import com.inventory.ManagementSystem.dto.CustomApiResponse;
import com.inventory.ManagementSystem.model.Category;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.CategoryRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import com.inventory.ManagementSystem.service.CategoryService;
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
@RequestMapping("api/")


@Tag(name = "Categories", description = "APIs for managing categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @PostMapping("category")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a new category",
            description = "Creates a new category in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource")
    })
    public ResponseEntity<CustomApiResponse<CategoryResponse>> newCategory(@RequestBody CategoryRequest newCategoryRequest){

         CategoryResponse response = categoryService.newCategory(newCategoryRequest);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Category created successfully",
                        response
                )
        );
    }

    @GetMapping("categories")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get all categories",
            description = "Returns all categories in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource")
    })
    public ResponseEntity<CustomApiResponse<List<Category>>> allCategories() {
        List<Category> categories = categoryService.allCategories();
        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Categories retrieved successfully",
                        categories
                )
        );
    }

    @GetMapping("category/{id}/products")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get products by category",
            description = "Returns all products belonging to a specific category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CustomApiResponse<List<Product>>> getProducts(@PathVariable int id){
        List<Product> products = categoryService.getCategoryProduct(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Category's products retrieved successfully",
                        products
                )
        );
    }

    @GetMapping("category/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get category by ID",
            description = "Returns a category using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CustomApiResponse<Category>> getCategory(@PathVariable int id){

        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Category retrieved successfully",
                        category
                )
        );
    }

    @PutMapping("category/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update a category",
            description = "Updates an existing category using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CustomApiResponse<CategoryResponse>> updateCategory(@PathVariable int id, @RequestBody CategoryRequest categoryUpdateRequest) {
        CategoryResponse response = categoryService.updateCategory(id, categoryUpdateRequest);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Category updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("category/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete a category",
            description = "Deletes a category using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CustomApiResponse<Void>> deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                new CustomApiResponse<>(
                        true,
                        "Category Deleted Successfully",
                        null
                )
        );
    }
}