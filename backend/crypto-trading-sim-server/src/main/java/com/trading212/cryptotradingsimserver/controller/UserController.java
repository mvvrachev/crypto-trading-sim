package com.trading212.cryptotradingsimserver.controller;

import com.trading212.cryptotradingsimserver.model.Holding;
import com.trading212.cryptotradingsimserver.repository.HoldingRepository;
import com.trading212.cryptotradingsimserver.service.UserService;
import dto.TradeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HoldingRepository holdingRepository;

    public UserController(UserService userService, HoldingRepository holdingRepository) {
        this.userService = userService;
        this.holdingRepository = holdingRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable int userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }


    @PostMapping("/{userId}/reset")
    public ResponseEntity<String> resetBalance(@PathVariable int userId) {
        boolean success = userService.resetBalance(userId);
        if (success) {
            return ResponseEntity.ok("Balance reset to 10,000");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/{userId}/buy")
    public ResponseEntity<String> buyCrypto(@PathVariable int userId, @RequestBody TradeRequest request) {
        try {
            userService.buyCrypto(userId, request);
            return ResponseEntity.ok("Buy successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/sell")
    public ResponseEntity<String> sellCrypto(@PathVariable int userId, @RequestBody TradeRequest request) {
        try {
            userService.sellCrypto(userId, request);
            return ResponseEntity.ok("Sell successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/holdings")
    public List<Holding> getHoldings(@PathVariable int userId) {
        return holdingRepository.findAllByUserId(userId);
    }

    @PostMapping("/users/{id}/reset")
    public ResponseEntity<String> resetAccount(@PathVariable int id) {
        userService.resetAccount(id);
        return ResponseEntity.ok("Account reset successfully.");
    }


}

