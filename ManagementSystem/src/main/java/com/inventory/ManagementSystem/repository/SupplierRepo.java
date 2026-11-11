package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepo  extends JpaRepository<Supplier,Integer> {

    boolean existsByEmail(String email);


}
