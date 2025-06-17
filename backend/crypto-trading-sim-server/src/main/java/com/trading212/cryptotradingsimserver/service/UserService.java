package com.trading212.cryptotradingsimserver.service;

import com.trading212.cryptotradingsimserver.model.Holding;
import com.trading212.cryptotradingsimserver.model.Transaction;
import com.trading212.cryptotradingsimserver.model.User;
import com.trading212.cryptotradingsimserver.repository.HoldingRepository;
import com.trading212.cryptotradingsimserver.repository.TransactionRepository;
import com.trading212.cryptotradingsimserver.repository.UserRepository;
import dto.TradeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final HoldingRepository holdingRepository;

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository, HoldingRepository holdingRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.holdingRepository = holdingRepository;
    }

    public int registerUser(String username, String password) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User(0, username, password, null);

        userRepository.save(newUser);
    }

    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        if (!user.password().equals(password)) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return user;
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public boolean resetBalance(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        userRepository.updateBalance(userId, 10000.0);
        return true;
    }

    public void buyCrypto(int userId, TradeRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        double totalCost = request.price() * request.quantity();

        if (user.balance().equals(totalCost)) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        userRepository.updateBalance(userId, user.balance() - totalCost);
        transactionRepository.save(new Transaction(0, userId, "BUY", request.symbol(), request.quantity(), request.price(), null));

        holdingRepository.saveOrUpdate(userId, request.symbol(), request.quantity());
    }

    public void sellCrypto(int userId, TradeRequest request) {

        Holding holding = holdingRepository.findByUserIdAndSymbol(userId, request.symbol())
                .orElseThrow(() -> new IllegalArgumentException("You don't own any " + request.symbol()));

        if (holding.quantity() < request.quantity()) {
            throw new IllegalArgumentException("Not enough " + request.symbol() + " to sell");
        }


        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        double totalGain = request.price() * request.quantity();

        userRepository.updateBalance(userId, user.balance() + totalGain);
        transactionRepository.save(new Transaction(0, userId, "SELL", request.symbol(), request.quantity(), request.price(), null));
    }


    public void resetAccount(int id) {
        userRepository.updateBalance(id, 10000.0);
    }
}

