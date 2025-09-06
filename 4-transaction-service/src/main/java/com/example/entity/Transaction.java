package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ Foreign key to User (sender)
    @Column(name = "sender_id", nullable = false)
    private int senderId;

    // ✅ Foreign key to User (receiver)
    @Column(name = "receiver_id", nullable = false)
    private int receiverId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(length = 20, nullable = false)
    private String status; // SUCCESS, FAILED, PENDING

    // 🔹 Default constructor
    public Transaction() {}

    // 🔹 Constructor with fields
    public Transaction(int senderId, int receiverId, double amount, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.transactionDate = LocalDateTime.now();
        this.status = status;
    }

    // 🔹 Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
