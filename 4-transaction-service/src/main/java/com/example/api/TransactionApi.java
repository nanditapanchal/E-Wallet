package com.example.api;

import com.example.dto.TransactionDto;
import com.example.dto.TransactionHistoryResponse;
import com.example.entity.Transaction;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions") // Base path for all transaction APIs
public class TransactionApi {

    @Autowired
    private TransactionService service;

    // ✅ Fund Transfer Endpoint
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> fundTransfer(@RequestBody TransactionDto transactionDto) {
        Transaction savedTransaction = service.fundTransfer(transactionDto);

        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", savedTransaction.getId());
        response.put("status", savedTransaction.getStatus());

        if ("SUCCESS".equals(savedTransaction.getStatus())) {
            response.put("message", "Funds transferred successfully");
        } else {
            response.put("message", "Transfer failed: insufficient balance or user not found");
        }

        return ResponseEntity.ok(response);
    }

    // ✅ Transaction History Endpoint
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(
            @PathVariable("userId") int userId) {
        return ResponseEntity.ok(service.getTransactionsWithNames(userId));
    }
}
