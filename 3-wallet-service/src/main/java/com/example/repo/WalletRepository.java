//package com.example.repo;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import com.example.entity.Wallet;
//
//public interface WalletRepository extends JpaRepository<Wallet, Integer> {
//
//    // ✅ New method to fetch wallet by userId
//    Wallet findByUserId(int userId);
//    
//}
package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    // Fetch wallet by userId (unique or assumed to be one-to-one)
    Wallet findByUserId(int userId);
    
    // Optionally: if you want safe nullable fetching
    // Optional<Wallet> findByUserId(int userId);
}
