package com.trading212.cryptotradingsimserver.model;

public record Holding(
        int id,
        int userId,
        String symbol,
        double quantity
) {}

