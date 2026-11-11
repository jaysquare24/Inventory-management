package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.Category;
import com.inventory.ManagementSystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(Category category);

    boolean existsByName(String name);
}
