package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.OutgoingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OutgoingOrderRepo extends JpaRepository<OutgoingOrder, Integer> {

    List<OutgoingOrder> findByProductId(int id);
}
