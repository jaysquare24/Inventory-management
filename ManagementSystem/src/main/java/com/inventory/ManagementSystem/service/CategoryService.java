package com.inventory.ManagementSystem.service;

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

    public Category newCategory(Category category) {

        boolean isExisting = categoryRepo.existsByCategoryName(category.getCategoryName());

        if (isExisting){

            throw new RuntimeException("Category name already exists");
        }
        return categoryRepo.save(category);
    }
    public List<Category> allCategories(){

        return categoryRepo.findAll();

    }

    public Category getCategory(int id) {

        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id:" + id));
    }

    public Category updateCategory(int id, Category category) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setCategoryName(category.getCategoryName());

        return categoryRepo.save(existing);
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
