package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.*;
import com.joysistvi.recordingapp.service.*;
import com.joysistvi.recordingapp.view.*;

import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.joysistvi.recordingapp.view.ConsoleOutput.clearScreen;

public class App {
    private final DbConnection dbConnection = new DbConnection();
    private final Scanner scanner = new Scanner(System.in);

    // Dependencies initialized as fields, bottom-up
    private final SongRepository songRepository = new SongRepositoryImpl(dbConnection);
    private final SongService songService = new SongServiceImpl(songRepository);
    private final SongView songView = new SongView(songService, scanner);

    private final ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
    private final ArtistService artistService = new ArtistServiceImpl(artistRepository);
    private final ArtistView artistView = new ArtistView(artistService, scanner);

    private final AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
    private final AlbumService albumService = new AlbumServiceImpl(albumRepository);
    private final AlbumView albumView = new AlbumView(albumService, scanner);

    private final UserRepository userRepository = new UserRepositoryImpl(dbConnection);
    private final UserService userService = new UserServiceImpl(userRepository);
    private final UserView userView = new UserView(userService, scanner);

    private final PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
    private final PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
    private final PlaylistView playlistView = new PlaylistView(playlistService, songService, scanner);

    private User currentUser;

    public static void main(String[] args) {
        App app = new App();
        try {
            app.run();
        } finally {
            app.scanner.close();
        }
    }

    public void run() {
        while (true) {
            currentUser = showAuthMenuAndAuthenticate();
            if (currentUser == null) {
                System.out.println("\nExiting Recording Studio App.");
                return;
            }
            String role = currentUser.getRole();
            if ("ADMIN".equalsIgnoreCase(role)) {
                adminDashboard();
            } else if ("USER".equalsIgnoreCase(role)) {
                userDashboard();
            } else {
                System.out.println("\nAccess denied: Unknown user role.");
            }
            System.out.println("\nLogged out " + currentUser.getUsername() + ".");
            currentUser = null;
        }
    }

    // Shared authentication for Admin and User
    private User showAuthMenuAndAuthenticate() {
        while (true) {
            clearScreen();
            System.out.println("\n=== RECORDING STUDIO APP ===");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("0. Exit");
            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) return null;

            switch (choice) {
                case 1 -> {
                    String username = getStringInput("Enter username: ");
                    if (username == null) return null;
                    String password = getStringInput("Enter password: ");
                    if (password == null) return null;
                    User authenticated = userService.authenticateUser(username, password);
                    if (authenticated != null) {
                        System.out.println("\nLogin successful! Welcome, " + authenticated.getUsername() +
                                " (" + authenticated.getRole() + ")!");
                        return authenticated;
                    }
                    System.out.println("\nLogin failed: Invalid username or password.");
                }
                case 2 -> { userView.promptCreateUser(); if (!ConsoleOutput.pause(scanner)) return null; }
                case 0 -> { return null; }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ADMIN can manage the catalog and users.
    private void adminDashboard() {
        while (true) {
            clearScreen();
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            System.out.println("1. Manage Artists");
            System.out.println("2. Manage Albums");
            System.out.println("3. Manage Songs");
            System.out.println("4. Manage Users");
            System.out.println("0. Logout");
            Integer choice = promptForInt("Choice: ");
            if (choice == null) return;
            switch (choice) {
                case 1 -> artistView.handleMenu();
                case 2 -> albumView.handleMenu();
                case 3 -> songView.handleMenu();
                case 4 -> userView.handleMenu();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // USER can browse and search the catalog and manage only their own playlists.
    private void userDashboard() {
        while (true) {
            clearScreen();
            System.out.println("\n=== USER DASHBOARD ===");
            System.out.println("Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            System.out.println("1. Browse Artists, Albums, Songs");
            System.out.println("2. Search Songs");
            System.out.println("3. Manage My Playlists");
            System.out.println("4. Add / Remove Songs in Playlist");
            System.out.println("0. Logout");
            Integer choice = promptForInt("Choice: ");
            if (choice == null) return;
            switch (choice) {
                case 1 -> browseCatalog();
                case 2 -> { songView.promptSearchSongs(); if (!ConsoleOutput.pause(scanner)) return; }
                case 3 -> playlistView.handleMenu(currentUser.getId());
                case 4 -> playlistView.handleManageSongsInPlaylist(currentUser.getId());
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void browseCatalog() {
        while (true) {
            clearScreen();
            System.out.println("\n=== BROWSE CATALOG ===");
            System.out.println("1. View Artists");
            System.out.println("2. View Albums");
            System.out.println("3. View Songs");
            System.out.println("0. Back");
            Integer choice = promptForInt("Choice: ");
            if (choice == null) return;
            switch (choice) {
                case 1 -> artistView.displayAllArtist();
                case 2 -> albumView.displayAllAlbums();
                case 3 -> songView.displayAllSongs();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\nInput Error: Unable to read input. Operation cancelled.");
            return null;
        }
    }

    private Integer promptForInt(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            if (input == null) return null;
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Error: Please enter a valid whole number.");
            }
        }
    }
}
