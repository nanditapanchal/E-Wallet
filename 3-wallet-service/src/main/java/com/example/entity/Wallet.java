package com.example.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wallet")   // ✅ make sure table name matches your DB
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")   // ✅ DB column
    private int walletId;

    @Column(name = "wallet_balance")  // ✅ DB column
    private double walletBalance;

    @Column(name = "created_date")   // ✅ DB column
    private LocalDate createdDate;

    @Column(name = "last_updated")   // ✅ DB column
    private LocalDate lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")   // ✅ DB column
    private WalletStatus status;

    @Column(name = "user_id")   // ✅ DB column
    private int userId;
}
