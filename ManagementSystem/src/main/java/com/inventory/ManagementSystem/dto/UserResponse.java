package com.inventory.ManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String phoneNumber;
    private String roles;

    // supplier added data
    private String address;
    private  String companyName;
}
