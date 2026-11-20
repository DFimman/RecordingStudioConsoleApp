package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;
import java.util.List;

// Application Tier: Business Rule / Validation
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }

    @Override
    public List<User> getAllArchivedUsers() {
        return userRepository.findAllArchived();
    }

    @Override
    public boolean createUser(String username, String rawPassword, String email) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            System.out.println("\nValidation Error: Valid email is required.");
            return false;
        }
        if (rawPassword == null || rawPassword.trim().length() < 6) {
            System.out.println("\nValidation Error: Password must be at least 6 characters.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(rawPassword.trim(), BCrypt.gensalt());
        User user = new User(username.trim(), hashedPassword, email.trim());
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(int id, String username, String email) {
        if (id <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }
        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            System.out.println("\nValidation Error: Valid email is required.");
            return false;
        }

        User user = new User(id, username.trim(), email.trim());
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        return userRepository.deleteById(id);
    }

    @Override
    public boolean archiveUser(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        return userRepository.archiveById(id);
    }

    @Override
    public boolean restoreUser(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        return userRepository.restoreById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return Collections.emptyList();
        }

        return userRepository.searchByUsername(keyword.trim());
    }

    @Override
    public boolean loginUser(String username, String candidatePassword) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required to log in.");
            return false;
        }
        if (candidatePassword == null || candidatePassword.isEmpty()) {
            System.out.println("\nValidation Error: Password is required to log in.");
            return false;
        }

        User user = userRepository.findByUsername(username.trim());

        if (user == null || user.isArchived() || user.getPassword() == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(candidatePassword, user.getPassword());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
