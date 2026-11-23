package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.joysistvi.recordingapp.view.ConsoleOutput.clearScreen;
import static com.joysistvi.recordingapp.view.ConsoleOutput.truncate;

public class AlbumView {

    private final AlbumService albumService;
    private final Scanner scanner;

    public AlbumView(AlbumService albumService, Scanner scanner) {
        this.albumService = albumService;
        this.scanner = scanner;
    }

    // Album Management Menu
    public void handleMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n=== MANAGE ALBUMS ===");
            System.out.println("1. View All Albums");
            System.out.println("2. Create Album");
            System.out.println("3. Update Album");
            System.out.println("4. Search Album");
            System.out.println("5. Archive Album");
            System.out.println("6. Restore Album");
            System.out.println("7. Delete Album");
            System.out.println("0. Back");

            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> displayAllAlbums();
                case 2 -> promptCreateAlbum();
                case 3 -> promptUpdateAlbum();
                case 4 -> promptSearchAlbums();
                case 5 -> promptArchiveAlbum();
                case 6 -> promptRestoreAlbum();
                case 7 -> promptDeleteAlbum();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    public void displayAllAlbums() {
        displayAllAlbums(false);
    }

    public void displayAllAlbums(boolean isArchived) {
        clearScreen();
        List<Album> albums = isArchived ? albumService.getAllArchivedAlbums() : albumService.getAllActiveAlbums();
        printAlbums(albums, isArchived ? "Archived Albums" : "Albums");
    }

    public void promptCreateAlbum() {
        clearScreen();
        System.out.println("\n----- Create New Album -----");
        String name = getStringInput("Enter album name: ");
        if (name == null) {
            return;
        }

        Integer year = getIntInput("Enter release year: ");
        if (year == null) {
            return;
        }
        Integer artistId = getIntInput("Enter artist id: ");
        if (artistId == null) {
            return;
        }

        boolean created = albumService.createAlbum(name, year, artistId);

        if (created) {
            System.out.println("\nAlbum " + name.trim() + " created successfully!");
        } else {
            System.out.println("\nFailed to create album.");
        }
    }

    public void promptUpdateAlbum() {
        clearScreen();
        System.out.println("\n----- Update Album -----");
        Integer id = getIntInput("Enter album id: ");
        if (id == null) return;

        Album existing = albumService.findById(id);
        if (existing == null) {
            System.out.println("\nNo active album found with ID " + id + ".");
            return;
        }
        System.out.println("Current: " + existing.getName() + " | " + existing.getYear() +
                " | Artist " + existing.getArtistId());
        System.out.println("1. Name");
        System.out.println("2. Release Year");
        System.out.println("3. Artist ID");
        System.out.println("0. Cancel");
        Integer choice = promptForInt("Which detail to change? ");
        if (choice == null || choice == 0) return;
        String name = existing.getName();
        int year = existing.getYear();
        int artistId = existing.getArtistId();
        switch (choice) {
            case 1 -> { name = getStringInput("Enter new album name: "); if (name == null) return; }
            case 2 -> { Integer value = getIntInput("Enter new release year: ");
                        if (value == null) return; year = value; }
            case 3 -> { Integer value = getIntInput("Enter new artist ID: ");
                        if (value == null) return; artistId = value; }
            default -> { System.out.println("Invalid option."); return; }
        }
        boolean updated = albumService.updateAlbum(id, name, year, artistId);
        System.out.println(updated ? "\nAlbum updated successfully!" : "\nFailed to update album.");
    }

    public void promptDeleteAlbum() {
        clearScreen();
        System.out.println("\n----- Delete Album -----");
        Integer id = getIntInput("Enter album id: ");
        if (id == null) {
            return;
        }

        boolean deleted = albumService.deleteAlbum(id);

        if (deleted) {
            System.out.println("\nAlbum ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo album found with ID " + id + ".");
        }
    }

    public void promptArchiveAlbum() {
        clearScreen();
        System.out.println("\n----- Archive Album -----");
        Integer id = getIntInput("Enter album id: ");
        if (id == null) {
            return;
        }

        boolean archived = albumService.archiveAlbum(id);

        if (archived) {
            System.out.println("\nAlbum ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo active album found with ID " + id + ".");
        }
    }

    public void promptRestoreAlbum() {
        clearScreen();
        System.out.println("\n----- Restore Album -----");
        List<Album> archivedAlbums = albumService.getAllArchivedAlbums();
        printAlbums(archivedAlbums, "Archived Albums");

        if (archivedAlbums == null || archivedAlbums.isEmpty()) {
            return;
        }

        Integer id = getIntInput("Enter album id to restore: ");
        if (id == null) {
            return;
        }

        boolean restored = albumService.restoreAlbum(id);

        if (restored) {
            System.out.println("\nAlbum ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo archived album found with ID " + id + ".");
        }
    }

    public void promptSearchAlbums() {
        clearScreen();
        System.out.println("\n----- Search Albums -----");
        String keyword = getStringInput("Enter keyword (search): ");
        if (keyword == null) {
            return;
        }

        List<Album> albums = albumService.searchAlbums(keyword);
        printAlbums(albums, "Search Albums");
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

    private void printAlbums(List<Album> albums, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-22s| %-8s| %-20s%n", "ID", "Album Name", "Year", "Artist");

        if (albums == null || albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        for (Album album : albums) {
            System.out.printf("%-4d| %-22s| %-8d| %-20s%n",
                    album.getId(),
                    truncate(album.getName(), 22),
                    album.getYear(),
                    truncate(album.getArtistName(), 20));
        }
    }
}
