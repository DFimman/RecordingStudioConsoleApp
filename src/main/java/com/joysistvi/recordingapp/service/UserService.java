package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;
import java.util.List;

public interface UserService {

    public List<User> getAllActiveUsers();
    public List<User> getAllArchivedUsers();
    public boolean createUser(String username, String rawPassword, String email);
    public boolean updateUser(int id, String username, String email);
    public boolean deleteUser(int id);
    public boolean archiveUser(int id);
    public boolean restoreUser(int id);
    public List<User> searchUsers(String keyword);
    public boolean loginUser(String username, String candidatePassword);
    public User authenticateUser(String username, String candidatePassword);

    // Look up the current values before updating a selected field
    default User findById(int id) {
        if (id <= 0) return null;
        List<User> records = getAllActiveUsers();
        if (records == null) return null;
        for (User record : records) {
            if (record.getId() == id) return record;
        }
        return null;
    }
}
