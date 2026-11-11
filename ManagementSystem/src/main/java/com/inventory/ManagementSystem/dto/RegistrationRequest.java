package com.inventory.ManagementSystem.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String phoneNumber;

    // supplier added data

    private String address;
    private  String companyName;
}
