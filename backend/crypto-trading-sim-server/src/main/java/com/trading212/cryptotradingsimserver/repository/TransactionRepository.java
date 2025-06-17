package com.trading212.cryptotradingsimserver.repository;

import com.trading212.cryptotradingsimserver.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, type, symbol, quantity, price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transaction.userId(), transaction.type(), transaction.symbol(), transaction.quantity(), transaction.price());
    }

    public List<Transaction> findByUserId(int userId) {
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Transaction(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("type"),
                rs.getString("symbol"),
                rs.getDouble("quantity"),
                rs.getDouble("price"),
                rs.getTimestamp("timestamp").toLocalDateTime()
        ), userId);
    }
}

