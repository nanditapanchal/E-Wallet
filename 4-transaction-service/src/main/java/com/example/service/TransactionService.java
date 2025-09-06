//package com.example.service;
//
//import com.example.dto.TransactionDto;
//import com.example.dto.TransactionHistoryResponse;
//import com.example.dto.UserDto;
//import com.example.entity.Transaction;
//import com.example.repo.TransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TransactionService {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    private static final String TOPIC = "transaction-topic";
//
//    // Fund Transfer
//    public Transaction fundTransfer(TransactionDto transactionDto) {
//        UserDto sender = restTemplate.getForObject(
//                "http://user-service/users/" + transactionDto.getFromUserId(), UserDto.class);
//        UserDto receiver = restTemplate.getForObject(
//                "http://user-service/users/" + transactionDto.getToUserId(), UserDto.class);
//
//        Transaction transaction = new Transaction();
//        transaction.setSenderId(transactionDto.getFromUserId());
//        transaction.setReceiverId(transactionDto.getToUserId());
//        transaction.setAmount(transactionDto.getAmount());
//        transaction.setTransactionDate(LocalDateTime.now());
//
//        if (sender == null || receiver == null) {
//            transaction.setStatus("FAILED");
//            return transactionRepository.save(transaction);
//        }
//
//        if (sender.getBalance() < transactionDto.getAmount()) {
//            transaction.setStatus("FAILED");
//            return transactionRepository.save(transaction);
//        }
//
//        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
//        receiver.setBalance(receiver.getBalance() + transactionDto.getAmount());
//
//        restTemplate.put("http://user-service/users/" + sender.getUserId(), sender);
//        restTemplate.put("http://user-service/users/" + receiver.getUserId(), receiver);
//
//        transaction.setStatus("SUCCESS");
//        Transaction savedTx = transactionRepository.save(transaction);
//
//        // Send Kafka message
//        kafkaTemplate.send(TOPIC, "Transaction " + savedTx.getId() + " completed successfully");
//
//        return savedTx;
//    }
//
//    // Listen to Kafka messages
//    @KafkaListener(topics = TOPIC, groupId = "transaction-service-group")
//    public void listenKafkaMessages(String message) {
//        System.out.println("📩 Kafka received: " + message);
//    }
//
//    // Get Transaction History
//    public List<TransactionHistoryResponse> getTransactionsWithNames(int userId) {
//        List<Transaction> transactions = transactionRepository
//                .findBySenderIdOrReceiverIdOrderByTransactionDateDesc(userId, userId);
//
//        return transactions.stream().map(tx -> {
//            UserDto sender = restTemplate.getForObject(
//                    "http://user-service/users/" + tx.getSenderId(), UserDto.class);
//            UserDto receiver = restTemplate.getForObject(
//                    "http://user-service/users/" + tx.getReceiverId(), UserDto.class);
//
//            return new TransactionHistoryResponse(
//                    tx.getId(),
//                    tx.getSenderId(),
//                    sender != null ? sender.getUserName() : "Unknown",
//                    tx.getReceiverId(),
//                    receiver != null ? receiver.getUserName() : "Unknown",
//                    tx.getAmount(),
//                    tx.getTransactionDate(),
//                    tx.getStatus()
//            );
//        }).collect(Collectors.toList());
//    }
//}
package com.example.service;

import com.example.dto.TransactionDto;
import com.example.dto.TransactionHistoryResponse;
import com.example.dto.UserDto;
import com.example.entity.Transaction;
import com.example.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "transaction-topic";
    private static final String USER_SERVICE_URL = "http://2-user-service/users/";

    // Fund Transfer
    public Transaction fundTransfer(TransactionDto transactionDto) {
        // Fetch sender and receiver details from user-service
        UserDto sender = restTemplate.getForObject(USER_SERVICE_URL + transactionDto.getFromUserId(), UserDto.class);
        UserDto receiver = restTemplate.getForObject(USER_SERVICE_URL + transactionDto.getToUserId(), UserDto.class);

        Transaction transaction = new Transaction();
        transaction.setSenderId(transactionDto.getFromUserId());
        transaction.setReceiverId(transactionDto.getToUserId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());

        // Validate users
        if (sender == null || receiver == null) {
            transaction.setStatus("FAILED");
            return transactionRepository.save(transaction);
        }

        // Check sender balance
        if (sender.getBalance() < transactionDto.getAmount()) {
            transaction.setStatus("FAILED");
            return transactionRepository.save(transaction);
        }

        // Perform balance updates
        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionDto.getAmount());

        // Update balances in user-service
        restTemplate.put(USER_SERVICE_URL + sender.getUserId(), sender);
        restTemplate.put(USER_SERVICE_URL + receiver.getUserId(), receiver);

        // Save transaction
        transaction.setStatus("SUCCESS");
        Transaction savedTx = transactionRepository.save(transaction);

        // Send Kafka message
        kafkaTemplate.send(TOPIC, "Transaction " + savedTx.getId() + " completed successfully");

        return savedTx;
    }

    // Kafka Listener
    @KafkaListener(topics = TOPIC, groupId = "transaction-service-group")
    public void listenKafkaMessages(String message) {
        System.out.println("📩 Kafka received: " + message);
    }

    // Transaction History
    public List<TransactionHistoryResponse> getTransactionsWithNames(int userId) {
        List<Transaction> transactions =
                transactionRepository.findBySenderIdOrReceiverIdOrderByTransactionDateDesc(userId, userId);

        return transactions.stream().map(tx -> {
            UserDto sender = restTemplate.getForObject(USER_SERVICE_URL + tx.getSenderId(), UserDto.class);
            UserDto receiver = restTemplate.getForObject(USER_SERVICE_URL + tx.getReceiverId(), UserDto.class);

            return new TransactionHistoryResponse(
                    tx.getId(),
                    tx.getSenderId(),
                    sender != null ? sender.getUserName() : "Unknown",
                    tx.getReceiverId(),
                    receiver != null ? receiver.getUserName() : "Unknown",
                    tx.getAmount(),
                    tx.getTransactionDate(),
                    tx.getStatus()
            );
        }).collect(Collectors.toList());
    }
}
