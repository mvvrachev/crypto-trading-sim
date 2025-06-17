package com.trading212.cryptotradingsimserver.model;

import java.math.BigDecimal;

public record User(int id, String username, String password, BigDecimal balance) {
}
