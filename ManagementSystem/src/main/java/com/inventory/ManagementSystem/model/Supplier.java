package com.inventory.ManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name  = "supplier_table")
public class Supplier {



    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  int  id;

    private  String email;
    private  String firstname;
    private String address;
    private  String companyName;
    private  String lastname;



    public Supplier() {
    }

    public Supplier(String companyName, String address, String lastname, String email, String firstname) {
        this.companyName = companyName;
        this.address = address;
        this.firstname = firstname;
        this.email = email;
        this.lastname = lastname;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "SupplierModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + firstname + '\'' +
                ", address='" + address + '\'' +
                ", companyName='" + companyName + '\'' +
                ", supervisor='" + lastname + '\'' +
                '}';
    }
}
