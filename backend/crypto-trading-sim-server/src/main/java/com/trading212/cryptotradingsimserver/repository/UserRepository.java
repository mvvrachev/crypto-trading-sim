package com.trading212.cryptotradingsimserver.repository;

import com.trading212.cryptotradingsimserver.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final double userAccountStartingBalance = 10000.00;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO Users (username, password, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.username(), user.password(), userAccountStartingBalance);
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            } else {
                return Optional.empty();
            }
        }, username);
    }

    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            } else {
                return Optional.empty();
            }
        }, id);
    }

    public void updateBalance(int userId, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, userId);
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBigDecimal("balance")
        );
    }

    public void resetAccount(int userId) {
        String resetBalanceSql = "UPDATE users SET balance = 10000 WHERE id = ?";
        jdbcTemplate.update(resetBalanceSql, userId);
    }
}
