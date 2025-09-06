package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Wallet;
import com.example.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletApi {

    @Autowired
    private WalletService walletService;

    // ✅ Create/register a new wallet using path variable
    // POST http://localhost:8087/wallet/1
    @PostMapping("/{userId}")
    public ResponseEntity<Wallet> registerNewWallet(@PathVariable("userId") int userId) {
        Wallet wallet = walletService.registerNewWallet(userId);
        return ResponseEntity.ok(wallet);
    }

    // ✅ Add money to wallet
    // POST http://localhost:8087/wallet/1/add-money?amount=500
    @PostMapping("/{userId}/add-money")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @PathVariable int userId,
            @RequestParam double amount) {
        Wallet wallet = walletService.addMoneyToWallet(userId, amount);
        return ResponseEntity.ok(wallet);
    }

    // ✅ Get wallet balance
    // GET http://localhost:8087/wallet/1/balance
    @GetMapping("/{userId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable("userId") int userId) {
        double balance = walletService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }

    // ✅ Get full wallet details
    // GET http://localhost:8087/wallet/1
    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWalletDetails(@PathVariable("userId") int userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }
}
