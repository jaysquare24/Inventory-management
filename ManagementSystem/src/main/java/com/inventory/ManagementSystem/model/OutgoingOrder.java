package com.inventory.ManagementSystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outgoing_order_table")
public class OutgoingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal totalPriceBeforeDiscount;
    private BigDecimal totalPriceAfterDiscount;
    private BigDecimal discount;

    @CreationTimestamp
    private LocalDate orderDate;

    public OutgoingOrder() {
    }

    public OutgoingOrder(List<OrderItem> orderItems, BigDecimal totalPriceBeforeDiscount, BigDecimal totalPriceAfterDiscount, BigDecimal discount) {
        this.items = orderItems;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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
                ", orderItems=" + items +
                ", totalPriceBeforeDiscount=" + totalPriceBeforeDiscount +
                ", totalPriceAfterDiscount=" + totalPriceAfterDiscount +
                ", discount=" + discount +
                ", orderDate=" + orderDate +
                '}';
    }
}
