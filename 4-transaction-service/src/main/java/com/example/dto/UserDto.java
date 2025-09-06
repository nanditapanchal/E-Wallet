package com.example.dto;

import lombok.Data;

@Data
public class UserDto {
    private int userId;
    private String userName;
    private long phoneNo;
    private String address;
    private String email;
    private double Balance; // added for wallet balance tracking
}
