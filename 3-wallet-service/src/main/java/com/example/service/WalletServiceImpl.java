package com.example.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.dto.AddMoneyDto;
import com.example.dto.UserDto;
import com.example.entity.Wallet;
import com.example.entity.WalletStatus;
import com.example.repo.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepo;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Wallet registerNewWallet(int userId) {
		// First, check if a wallet already exists for this user
		Wallet existingWallet = walletRepo.findByUserId(userId);
		if (existingWallet != null) {
			throw new RuntimeException("Wallet already exists for user ID: " + userId);
		}

		// Then verify the user
		boolean userChk = verifyUser(userId);
		if (userChk) {
			Wallet w = new Wallet();
			w.setStatus(WalletStatus.ACTIVE);
			w.setUserId(userId);
			w.setWalletBalance(500);
			w.setCreatedDate(LocalDate.now());
			w.setLastUpdated(LocalDate.now());
			return walletRepo.save(w);
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");

	}

	@Override
	public Wallet addMoney(AddMoneyDto addMoneyDto) {
		return addMoneyToWallet(addMoneyDto.getUserId(), addMoneyDto.getAmount());
	}

	@Override
	public Wallet getWalletByUserId(int userId) {
		Wallet wallet = walletRepo.findByUserId(userId);
		if (wallet != null) {
			return wallet;
		}
		throw new RuntimeException("Wallet not found for user: " + userId);
	}

	// ✅ New method to get wallet balance
	@Override
	public double getBalance(int userId) {
		Wallet wallet = walletRepo.findByUserId(userId);
		if (wallet != null) {
			return wallet.getWalletBalance();
		}
		throw new RuntimeException("Wallet not found for user: " + userId);
	}

	@Autowired
	private RestTemplate restTemplate1;
	boolean verifyUser(int userId) {
		RestTemplate template=new RestTemplate();
		//currently no load balancing is used ////
		String url="http://localhost:9000/users/"+userId;
		UserDto user=template.getForObject(url, UserDto.class);
		if(user!=null) {
			return true;
		}
		else return false;
	}



	

	

//    private boolean verifyUser(int userId) {
//        RestTemplate template = new RestTemplate();
//        // Currently no load balancing is used
//        String url = "http://user-service/users/" + userId;
//        UserDto user = template.getForObject(url, UserDto.class);
//        return user != null;
//    }
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private boolean verifyUser(int userId) {
//        String url = "http://user-service/users/" + userId;  // 👈 service name registered with Eureka
//        UserDto user = restTemplate.getForObject(url, UserDto.class);
//        return user != null;
//    }

	@Override
	public Wallet addMoneyToWallet(int userId, double amount) {
		Wallet wallet = walletRepo.findByUserId(userId);
		if (wallet == null) {
			throw new RuntimeException("Wallet not found for user: " + userId);
		}

		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}

		double newBalance = wallet.getWalletBalance() + amount;
		wallet.setWalletBalance(newBalance);
		return walletRepo.save(wallet);
	}

}
//package com.example.service;
//
//import java.time.LocalDate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.client.RestTemplate;
//
//import com.example.dto.AddMoneyDto;
//import com.example.dto.UserDto;
//import com.example.entity.Wallet;
//import com.example.entity.WalletStatus;
//import com.example.repo.WalletRepository;
//
//@Service
//public class WalletServiceImpl implements WalletService {
//
//	@Autowired
//	private WalletRepository walletRepo;
//	@Autowired
//	private RestTemplate restTemplate;
//
//	@Override
//	public Wallet registerNewWallet(int userId) {
//		// First, check if a wallet already exists for this user
//		Wallet existingWallet = walletRepo.findByUserId(userId);
//		if (existingWallet != null) {
//			throw new RuntimeException("Wallet already exists for user ID: " + userId);
//		}
//
//		// Then verify the user
//		boolean userChk = verifyUser(userId);
//		if (userChk) {
//			Wallet w = new Wallet();
//			w.setStatus(WalletStatus.ACTIVE);
//			w.setUserId(userId);
//			w.setWalletBalance(500);
//			w.setCreatedDate(LocalDate.now());
//			w.setLastUpdated(LocalDate.now());
//			return walletRepo.save(w);
//		}
//
//		throw new RuntimeException("User does not exist");
//	}
//
//	@Override
//	public Wallet addMoney(AddMoneyDto addMoneyDto) {
//		return addMoneyToWallet(addMoneyDto.getUserId(), addMoneyDto.getAmt());
//	}
//
//	@Override
//	public Wallet getWalletByUserId(int userId) {
//		Wallet wallet = walletRepo.findByUserId(userId);
//		if (wallet != null) {
//			return wallet;
//		}
//		throw new RuntimeException("Wallet not found for user: " + userId);
//	}
//
//	// ✅ New method to get wallet balance
//	@Override
//	public double getBalance(int userId) {
//		Wallet wallet = walletRepo.findByUserId(userId);
//		if (wallet != null) {
//			return wallet.getWalletBalance();
//		}
//		throw new RuntimeException("Wallet not found for user: " + userId);
//	}
//
//	@Autowired
//	private RestTemplate restTemplate1;
//
//	private boolean verifyUser(int userId) {
//		String url = "http://2-user-service/users/" + userId;
//	
//
//	    try {
//	        System.out.println("Calling URL: " + url);
//	        UserDto user = restTemplate.getForObject(url, UserDto.class);
//	        return !ObjectUtils.isEmpty(user);
//	    } catch (Exception e) {
//	        System.out.println("User verification failed: " + e.getMessage());
//	        return false;
//	    }
//	}
//
//	
//
//	
//
////    private boolean verifyUser(int userId) {
////        RestTemplate template = new RestTemplate();
////        // Currently no load balancing is used
////        String url = "http://user-service/users/" + userId;
////        UserDto user = template.getForObject(url, UserDto.class);
////        return user != null;
////    }
////    @Autowired
////    private RestTemplate restTemplate;
////
////    private boolean verifyUser(int userId) {
////        String url = "http://user-service/users/" + userId;  // 👈 service name registered with Eureka
////        UserDto user = restTemplate.getForObject(url, UserDto.class);
////        return user != null;
////    }
//
//	@Override
//	public Wallet addMoneyToWallet(int userId, double amount) {
//		Wallet wallet = walletRepo.findByUserId(userId);
//		if (wallet == null) {
//			throw new RuntimeException("Wallet not found for user: " + userId);
//		}
//
//		if (amount <= 0) {
//			throw new IllegalArgumentException("Amount must be greater than 0");
//		}
//
//		double newBalance = wallet.getWalletBalance() + amount;
//		wallet.setWalletBalance(newBalance);
//		return walletRepo.save(wallet);
//	}
//
//}
