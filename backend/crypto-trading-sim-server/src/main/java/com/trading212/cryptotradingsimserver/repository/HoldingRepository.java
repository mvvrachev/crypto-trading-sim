package com.trading212.cryptotradingsimserver.repository;

import com.trading212.cryptotradingsimserver.model.Holding;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HoldingRepository {

    private final JdbcTemplate jdbcTemplate;

    public HoldingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Holding> findByUserIdAndSymbol(int userId, String symbol) {
        String sql = "SELECT * FROM holdings WHERE user_id = ? AND symbol = ?";
        List<Holding> results = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Holding(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("symbol"),
                        rs.getDouble("quantity")
                ), userId, symbol);
        return results.stream().findFirst();
    }

    public void saveOrUpdate(int userId, String symbol, double quantityToAdd) {
        Optional<Holding> existing = findByUserIdAndSymbol(userId, symbol);
        if (existing.isPresent()) {
            double newQty = existing.get().quantity() + quantityToAdd;
            if (newQty <= 0) {
                // Remove holding if quantity reaches 0 or below
                delete(userId, symbol);
            } else {
                jdbcTemplate.update(
                        "UPDATE holdings SET quantity = ? WHERE user_id = ? AND symbol = ?",
                        newQty, userId, symbol
                );
            }
        } else {
            jdbcTemplate.update(
                    "INSERT INTO holdings (user_id, symbol, quantity) VALUES (?, ?, ?)",
                    userId, symbol, quantityToAdd
            );
        }
    }

    public void delete(int userId, String symbol) {
        jdbcTemplate.update("DELETE FROM holdings WHERE user_id = ? AND symbol = ?", userId, symbol);
    }

    public List<Holding> findAllByUserId(int userId) {
        String sql = "SELECT * FROM holdings WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Holding(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("symbol"),
                rs.getDouble("quantity")
        ), userId);
    }
}

