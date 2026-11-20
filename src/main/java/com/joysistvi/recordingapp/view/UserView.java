package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.service.UserService;

import java.util.List;
import java.util.Scanner;

public class UserView {

    private final UserService userService;
    private final Scanner scanner;

    public UserView(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void displayAllUsers() {
        displayAllUsers(false);
    }

    public void displayAllUsers(boolean isArchived) {
        List<User> users = isArchived ? userService.getAllArchivedUsers() : userService.getAllActiveUsers();

        String title = isArchived ? "Archived Users" : "Users";
        printUsers(users, title);
    }

    public void promptCreateUser() {
        System.out.println("\n----- Create New User -----");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        boolean created = userService.createUser(username, password, email);

        if (created) {
            System.out.println("\nUser '" + username.trim() + "' created successfully!");
        } else {
            System.out.println("\nFailed to create user.");
        }
    }

    public void promptUpdateUser() {
        System.out.println("\n----- Update User -----");
        System.out.print("Enter user id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();

        boolean updated = userService.updateUser(id, username, email);

        if (updated) {
            System.out.println("\nUser ID " + id + " updated successfully!");
        } else {
            System.out.println("\nFailed to update user ID " + id + ".");
        }
    }

    public void promptDeleteUser() {
        System.out.println("\n----- Delete User -----");
        System.out.print("Enter user id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            System.out.println("\nUser ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo user found with ID " + id + ".");
        }
    }

    public void promptArchiveUser() {
        System.out.println("\n----- Archive User -----");
        System.out.print("Enter user id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean archived = userService.archiveUser(id);

        if (archived) {
            System.out.println("\nUser ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo user found with ID " + id + ".");
        }
    }

    public void promptRestoreUser() {
        System.out.println("\n----- Restore User -----");
        List<User> users = userService.getAllArchivedUsers();
        printUsers(users, "Archived Users");

        if (users == null || users.isEmpty()) {
            return;
        }

        System.out.print("Enter user id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean restored = userService.restoreUser(id);

        if (restored) {
            System.out.println("\nUser ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo user found with ID " + id + ".");
        }
    }

    public void promptSearchUsers() {
        System.out.println("\n----- Search Users -----");
        System.out.print("Enter keyword (search): ");
        String keyword = scanner.nextLine();

        List<User> users = userService.searchUsers(keyword);
        printUsers(users, "Search Users");
    }

    public void promptLoginUser() {
        System.out.println("\n----- Login User -----");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean loggedIn = userService.loginUser(username, password);

        if (loggedIn) {
            System.out.println("\nLogin successful! Welcome, " + username.trim() + "!");
        } else {
            System.out.println("\nLogin failed: Invalid username or password.");
        }
    }

    private void printUsers(List<User> users, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-20s| %-28s| %-12s%n", "ID", "Username", "Email", "Role");

        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            System.out.printf("%-4d| %-20s| %-28s| %-12s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole());
        }
    }
}
