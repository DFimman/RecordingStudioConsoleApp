package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final DbConnection dbConnection;

    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Read Active Users
    @Override
    public List<User> findAllActive() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, email, role, is_archived FROM users " +
                "WHERE is_archived = 0 ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getBoolean("is_archived")));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Users Error: " + e.getMessage());
        }

        return users;
    }

    // Read Archived Users
    @Override
    public List<User> findAllArchived() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, email, role, is_archived FROM users " +
                "WHERE is_archived = 1 ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getBoolean("is_archived")));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Archived Users Error: " + e.getMessage());
        }

        return users;
    }

    // Find User By Username (Login)
    @Override
    public User findByUsername(String username) {
        String query = "SELECT id, username, password, email, role, is_archived FROM users WHERE username = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role"),
                            resultSet.getBoolean("is_archived"));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nLogin User Error: " + e.getMessage());
        }

        return null;
    }

    // Create User
    @Override
    public boolean save(User user) {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'USER')";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nCreate User Error: " + e.getMessage());
            return false;
        }
    }

    // Update User
    @Override
    public boolean update(User user) {
        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setInt(3, user.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nUpdate User Error: " + e.getMessage());
            return false;
        }
    }

    // Delete User
    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nDelete User Error: " + e.getMessage());
            return false;
        }
    }

    // Archive User (Soft Delete)
    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE users SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nArchive User Error: " + e.getMessage());
            return false;
        }
    }

    // Restore User
    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE users SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nRestore User Error: " + e.getMessage());
            return false;
        }
    }

    // Search Active Users
    @Override
    public List<User> searchByUsername(String keyword) {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, email, role, is_archived FROM users " +
                "WHERE is_archived = 0 AND username LIKE ? ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role"),
                            resultSet.getBoolean("is_archived")));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nSearch User Error: " + e.getMessage());
        }

        return users;
    }
}
