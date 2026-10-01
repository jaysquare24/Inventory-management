package com.inventory.ManagementSystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "outgoing_order_table")
public class OutgoingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int orderQuantity;
    private BigDecimal totalPriceBeforeDiscount;
    private BigDecimal totalPriceAfterDiscount;
    private BigDecimal discount;

    @CreationTimestamp
    private LocalDate orderDate;

    public OutgoingOrder() {
    }

    public OutgoingOrder(Product product, int orderQuantity, BigDecimal totalPriceBeforeDiscount, BigDecimal totalPriceAfterDiscount, BigDecimal discount) {
        this.product = product;
        this.orderQuantity = orderQuantity;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.discount = discount;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

    public void setTotalPriceBeforeDiscount(BigDecimal totalPriceBeforeDiscount) {
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
    }

    public BigDecimal getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public void setTotalPriceAfterDiscount(BigDecimal totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OutgoingOrder{" +
                "id=" + id +
                ", product=" + product +
                ", orderQuantity=" + orderQuantity +
                ", totalPriceBeforeDiscount=" + totalPriceBeforeDiscount +
                ", totalPriceAfterDiscount=" + totalPriceAfterDiscount +
                ", discount=" + discount +
                ", orderDate=" + orderDate +
                '}';
    }
}
