//package com.example.service;
//
//import com.example.dto.AddMoneyDto;
//import com.example.entity.Wallet;
//
//public interface WalletService {
//    Wallet registerNewWallet(int userId);
//    Wallet addMoney(AddMoneyDto addMoneyDto);
//
//    // ✅ New: Get wallet balance
//    double getBalance(int userId);
//	Wallet getWalletByUserId(int userId);
//	Wallet addMoneyToWallet(int userId, double amount);
//}
package com.example.service;

import com.example.dto.AddMoneyDto;
import com.example.entity.Wallet;

public interface WalletService {
    Wallet registerNewWallet(int userId);
    Wallet addMoney(AddMoneyDto addMoneyDto);

    // ✅ New: Get wallet balance
    double getBalance(int userId);
	Wallet getWalletByUserId(int userId);
	Wallet addMoneyToWallet(int userId, double amount);
}
