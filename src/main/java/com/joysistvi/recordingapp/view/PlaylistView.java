package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.service.PlaylistService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PlaylistView {

    private final PlaylistService playlistService;
    private final Scanner scanner;

    public PlaylistView(PlaylistService playlistService, Scanner scanner) {
        this.playlistService = playlistService;
        this.scanner = scanner;
    }

    // Playlist Management Menu
    public void handleMenu() {
        while (true) {
            System.out.println("\n=== MANAGE PLAYLISTS ===");
            System.out.println("1. View All Playlists");
            System.out.println("2. Create Playlist");
            System.out.println("3. Update Playlist");
            System.out.println("4. Search Playlist");
            System.out.println("5. Archive Playlist");
            System.out.println("6. Restore Playlist");
            System.out.println("7. Delete Playlist");
            System.out.println("0. Back");

            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> displayAllPlaylists();
                case 2 -> promptCreatePlaylist();
                case 3 -> promptUpdatePlaylist();
                case 4 -> promptSearchPlaylists();
                case 5 -> promptArchivePlaylist();
                case 6 -> promptRestorePlaylist();
                case 7 -> promptDeletePlaylist();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllPlaylists() {
        displayAllPlaylists(false);
    }

    public void displayAllPlaylists(boolean isArchived) {
        List<Playlist> playlists = isArchived ? playlistService.getAllArchivedPlaylists() : playlistService.getAllActivePlaylists();
        printPlaylists(playlists, isArchived ? "Archived Playlists" : "Playlists");
    }

    public void promptCreatePlaylist() {
        System.out.println("\n----- Create New Playlist -----");
        String name = getStringInput("Enter playlist name: ");
        if (name == null) {
            return;
        }

        Integer userId = getIntInput("Enter user id: ");
        if (userId == null) {
            return;
        }

        boolean created = playlistService.createPlaylist(name, userId);

        if (created) {
            System.out.println("\nPlaylist " + name.trim() + " created successfully!");
        } else {
            System.out.println("\nFailed to create playlist.");
        }
    }

    public void promptUpdatePlaylist() {
        System.out.println("\n----- Update Playlist -----");
        Integer id = getIntInput("Enter playlist id: ");
        if (id == null) {
            return;
        }

        String name = getStringInput("Enter new playlist name: ");
        if (name == null) {
            return;
        }

        Integer userId = getIntInput("Enter new user id: ");
        if (userId == null) {
            return;
        }

        boolean updated = playlistService.updatePlaylist(id, name, userId);

        if (updated) {
            System.out.println("\nPlaylist ID " + id + " updated successfully!");
        } else {
            System.out.println("\nFailed to update playlist ID " + id + ".");
        }
    }

    public void promptDeletePlaylist() {
        System.out.println("\n----- Delete Playlist -----");
        Integer id = getIntInput("Enter playlist id: ");
        if (id == null) {
            return;
        }

        boolean deleted = playlistService.deletePlaylist(id);

        if (deleted) {
            System.out.println("\nPlaylist ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo playlist deleted for ID " + id + ".");
        }
    }

    public void promptArchivePlaylist() {
        System.out.println("\n----- Archive Playlist -----");
        Integer id = getIntInput("Enter playlist id: ");
        if (id == null) {
            return;
        }

        boolean archived = playlistService.archivePlaylist(id);

        if (archived) {
            System.out.println("\nPlaylist ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo playlist found with ID " + id + ".");
        }
    }

    public void promptRestorePlaylist() {
        System.out.println("\n----- Restore Playlist -----");
        List<Playlist> archivedPlaylists = playlistService.getAllArchivedPlaylists();
        printPlaylists(archivedPlaylists, "Archived Playlists");

        if (archivedPlaylists == null || archivedPlaylists.isEmpty()) {
            return;
        }

        Integer id = getIntInput("Enter playlist id to restore: ");
        if (id == null) {
            return;
        }

        boolean restored = playlistService.restorePlaylist(id);

        if (restored) {
            System.out.println("\nPlaylist ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo archived playlist found with ID " + id + ".");
        }
    }

    public void promptSearchPlaylists() {
        System.out.println("\n----- Search Playlists -----");
        String keyword = getStringInput("Enter keyword (search): ");
        if (keyword == null) {
            return;
        }

        List<Playlist> playlists = playlistService.searchPlaylists(keyword);
        printPlaylists(playlists, "Search Playlists");
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

    // Get non-blank string input
    private String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                String input = scanner.nextLine();

                if (input.trim().isEmpty()) {
                    System.out.println("\nValidation Error: Input cannot be blank.");
                    continue;
                }

                return input;
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nInput Error: Unable to read input. Operation cancelled.");
                return null;
            }
        }
    }

    // Get a valid positive integer
    private Integer getIntInput(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            if (input == null) {
                return null;
            }

            try {
                int number = Integer.parseInt(input.trim());
                if (number <= 0) {
                    System.out.println("\nValidation Error: Please enter a positive number.");
                    continue;
                }

                return number;
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Error: Please enter a valid whole number.");
            }
        }
    }

    private void printPlaylists(List<Playlist> playlists, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-30s| %-20s| %-20s%n", "ID", "Playlist Name", "Username", "Date Created");

        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No playlists found.");
            return;
        }

        for (Playlist playlist : playlists) {
            System.out.printf("%-4d| %-30s| %-20s| %-20s%n",
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getUsername(),
                    playlist.getDateCreated());
        }
    }
}
