package com.inventory.ManagementSystem.service;


import com.inventory.ManagementSystem.model.Category;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.CategoryRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    public Product newProduct(Product product) {   // Fetch the full category from database
        Category category = categoryRepo.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);
        return productRepo.save(product);
    }

    public List<Product> allProducts(){
        return productRepo.findAll();
    }

    public Product getProduct(int id){
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public Product updateProduct(int id, Product product) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setQuantity(product.getQuantity());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());

        return productRepo.save(existing);
    }


    /*public Product adjustStock(int id, int quantityChange) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newQuantity = existing.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock. Current: " + existing.getQuantity());
        }

        existing.setQuantity(newQuantity);
        return productRepo.save(existing);
    }*/


    public void deleteProduct(int id){
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepo.delete(existing);
    }
}
