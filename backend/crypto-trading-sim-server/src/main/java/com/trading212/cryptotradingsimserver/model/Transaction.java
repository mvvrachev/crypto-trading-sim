package com.trading212.cryptotradingsimserver.model;

import java.time.LocalDateTime;

public record Transaction(
        int id,
        int userId,
        String type,
        String symbol,
        double quantity,
        double price,
        LocalDateTime timestamp
) {}
