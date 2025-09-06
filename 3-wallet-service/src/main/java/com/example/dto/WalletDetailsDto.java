package com.example.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class WalletDetailsDto {
    private int walletId;
    private double walletBalance;
    private LocalDate createdDate;
    private LocalDate lastUpdated;
    private String status;

    private int userId;
    private String userName;
    private String email;
    private long phoneNo;
    private String address;
}
