package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.IncomingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomingOrderRepo extends JpaRepository<IncomingOrder, Integer> {

    Optional<IncomingOrder> findById(int id);
    boolean existsById(int id);
    List<IncomingOrder> findByProductId(int productId);
    List<IncomingOrder> findBySupplierId(int supplierId);
}
