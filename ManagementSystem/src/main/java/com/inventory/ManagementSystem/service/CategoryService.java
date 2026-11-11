package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.CategoryRequest;
import com.inventory.ManagementSystem.dto.CategoryResponse;
import com.inventory.ManagementSystem.model.Category;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.CategoryRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    public CategoryResponse newCategory(CategoryRequest request) {

        boolean isExisting = categoryRepo.existsByCategoryName(request.getCategoryName());

        if (isExisting){

            throw new RuntimeException("Category name already exists");
        }

        //MAP CREATE CATEGORY REQUEST

        Category category = new Category();
        category.setCategoryName(request.getCategoryName());

        Category savedCategory = categoryRepo.save(category);

        //MAP CREATE CATEGORY RESPONSE

        CategoryResponse response = new CategoryResponse();

        response.setId(savedCategory.getId());
        response.setCategoryName(savedCategory.getCategoryName());

        return response;
    }
    public List<Category> allCategories(){

        return categoryRepo.findAll();

    }

    public Category getCategory(int id) {

        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id:" + id));
    }

    public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if(categoryRequest.getCategoryName() != null && !categoryRequest.getCategoryName().isEmpty()){
            existing.setCategoryName(categoryRequest.getCategoryName());
        }


        Category savedCategory = categoryRepo.save(existing);

        //MAP UPDATED CATEGORY RESPONSE
        CategoryResponse response = new CategoryResponse();

        response.setId(savedCategory.getId());
        response.setCategoryName(savedCategory.getCategoryName());

        return response;


    }

    public void deleteCategory(int id) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryRepo.delete(existing);
    }

    public List<Product> getCategoryProduct(int id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));

        return productRepo.findByCategory(category);
    }
}
