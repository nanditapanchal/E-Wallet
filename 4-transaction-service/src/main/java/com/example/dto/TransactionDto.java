package com.example.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private int fromUserId;
    private int toUserId;
    private double amount; // changed from float amtToTransfer to double amount
}
