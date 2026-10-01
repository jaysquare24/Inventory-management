package com.inventory.ManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name  = "supplier_table")
public class Supplier {



    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  int  id;

    private  String email;
    private  String username;
    private String address;
    private  String companyName;


    public Supplier() {
    }

    public Supplier(String companyName, String address, String userName, String email) {
        this.companyName = companyName;
        this.address = address;
        this.username = userName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SupplierModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + username + '\'' +
                ", address='" + address + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
