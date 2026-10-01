package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer > {

    Optional<Category> findById(int id);
    boolean existsByCategoryName(String categoryName);
}
