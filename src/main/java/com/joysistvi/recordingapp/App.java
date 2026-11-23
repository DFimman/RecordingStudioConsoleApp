package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.repository.AlbumRepository;
import com.joysistvi.recordingapp.repository.AlbumRepositoryImpl;
import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.repository.SongRepositoryImpl;
import com.joysistvi.recordingapp.repository.PlaylistRepository;
import com.joysistvi.recordingapp.repository.PlaylistRepositoryImpl;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.repository.UserRepositoryImpl;
import com.joysistvi.recordingapp.service.ArtistService;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.service.AlbumService;
import com.joysistvi.recordingapp.service.AlbumServiceImpl;
import com.joysistvi.recordingapp.service.SongService;
import com.joysistvi.recordingapp.service.SongServiceImpl;
import com.joysistvi.recordingapp.service.PlaylistService;
import com.joysistvi.recordingapp.service.PlaylistServiceImpl;
import com.joysistvi.recordingapp.service.UserService;
import com.joysistvi.recordingapp.service.UserServiceImpl;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.SongView;
import com.joysistvi.recordingapp.view.UserView;
import com.joysistvi.recordingapp.view.PlaylistView;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {

    private final Scanner scanner;
    private final UserService userService;
    private final SongView songView;
    private final ArtistView artistView;
    private final AlbumView albumView;
    private final UserView userView;
    private final PlaylistView playlistView;
    private String currentUser;

    // Constructor for the original four management modules
    public App(Scanner scanner, UserService userService, SongView songView,
               ArtistView artistView, AlbumView albumView, UserView userView) {
        this(scanner, userService, songView, artistView, albumView, userView, null);
    }

    public App(Scanner scanner, UserService userService, SongView songView,
               ArtistView artistView, AlbumView albumView, UserView userView, PlaylistView playlistView) {
        this.scanner = scanner;
        this.userService = userService;
        this.songView = songView;
        this.artistView = artistView;
        this.albumView = albumView;
        this.userView = userView;
        this.playlistView = playlistView;
    }

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        Scanner scanner = new Scanner(System.in);

        // SONG
        SongRepository songRepository = new SongRepositoryImpl(dbConnection);
        SongService songService = new SongServiceImpl(songRepository);
        SongView songView = new SongView(songService, scanner);

        // ARTIST
        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistView artistView = new ArtistView(artistService, scanner);

        // ALBUM
        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        AlbumView albumView = new AlbumView(albumService, scanner);

        // USER
        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserView userView = new UserView(userService, scanner);

        // PLAYLIST
        PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
        PlaylistView playlistView = new PlaylistView(playlistService, scanner);

        App app = new App(scanner, userService, songView, artistView, albumView, userView, playlistView);
        app.run();
    }

    public void run() {
        while (true) {
            showAuthMenuAndAuthenticate();

            if (currentUser == null) {
                return;
            }

            dashboard();
            currentUser = null;
            System.out.println("\nLogged out successfully!");
        }
    }

    // Authentication Menu
    private void showAuthMenuAndAuthenticate() {
        while (true) {
            System.out.println("\n=== RECORDING STUDIO APP ===");
            System.out.println("1. Login");
            System.out.println("2. Register User");
            System.out.println("0. Exit");

            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> {
                    String username = getStringInput("Enter username: ");
                    if (username == null) {
                        return;
                    }

                    String password = getStringInput("Enter password: ");
                    if (password == null) {
                        return;
                    }

                    boolean loggedIn = userService.loginUser(username, password);

                    if (loggedIn) {
                        currentUser = username.trim();
                        System.out.println("\nLogin successful! Welcome, " + currentUser + "!");
                        return;
                    } else {
                        System.out.println("\nLogin failed: Invalid username or password.");
                    }
                }
                case 2 -> userView.promptCreateUser();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Main Dashboard
    private void dashboard() {
        while (true) {
            System.out.println("\n1. Song Management");
            System.out.println("2. Artist Management");
            System.out.println("3. Album Management");
            System.out.println("4. Users Management");
            System.out.println("5. Playlist Management");
            System.out.println("0. Back");

            Integer choice = promptForInt("Choice: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> songView.handleMenu();
                case 2 -> artistView.handleMenu();
                case 3 -> albumView.handleMenu();
                case 4 -> userView.handleMenu();
                case 5 -> {
                    if (playlistView == null) {
                        System.out.println("Playlist Management is unavailable.");
                    } else {
                        playlistView.handleMenu();
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Get console input
    private String getStringInput(String prompt) {
        System.out.print(prompt);

        try {
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\nInput Error: Unable to read input. Operation cancelled.");
            return null;
        }
    }

    // Get a valid menu option (0 = Back)
    private Integer promptForInt(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            if (input == null) {
                return null;
            }

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Error: Please enter a valid whole number.");
            }
        }
    }
}
