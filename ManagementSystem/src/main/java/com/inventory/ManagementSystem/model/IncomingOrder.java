package com.inventory.ManagementSystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class IncomingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantitySupply;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @CreationTimestamp
    private LocalDateTime supplyDate;

    public IncomingOrder() {

    }

    public IncomingOrder(int quantitySupply, BigDecimal totalPrice, Product product,
                         Supplier supplier, LocalDateTime supplyDate) {
        this.quantitySupply = quantitySupply;
        this.totalPrice = totalPrice;
        this.product = product;
        this.supplier = supplier;
        this.supplyDate = supplyDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantitySupply() {
        return quantitySupply;
    }

    public void setQuantitySupply(int quantitySupply) {
        this.quantitySupply = quantitySupply;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(LocalDateTime supplyDate) {
        this.supplyDate = supplyDate;
    }
}
