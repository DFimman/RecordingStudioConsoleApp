package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.joysistvi.recordingapp.view.ConsoleOutput.clearScreen;
import static com.joysistvi.recordingapp.view.ConsoleOutput.truncate;

public class ArtistView {

    private final ArtistService artistService;
    private final Scanner scanner;

    public ArtistView(ArtistService artistService, Scanner scanner) {
        this.artistService = artistService;
        this.scanner = scanner;
    }

    // Artist Management Menu
    public void handleMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n=== MANAGE ARTISTS ===");
            System.out.println("1. View All Artists");
            System.out.println("2. Create Artist");
            System.out.println("3. Update Artist");
            System.out.println("4. Search Artist");
            System.out.println("5. Archive Artist");
            System.out.println("6. Restore Artist");
            System.out.println("7. Delete Artist");
            System.out.println("0. Back");

            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> displayAllArtist();
                case 2 -> promptCreateArtist();
                case 3 -> promptUpdateArtist();
                case 4 -> promptSearchArtist();
                case 5 -> promptArchiveArtist();
                case 6 -> promptRestoreArtist();
                case 7 -> promptDeleteArtist();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    public void displayAllArtist() {
        displayAllArtist(false);
    }

    public void displayAllArtist(boolean isArchived) {
        clearScreen();
        List<Artist> artists = isArchived ? artistService.getAllArchivedArtists() : artistService.getAllActiveArtists();

        String title = isArchived ? "Archived Artists" : "Artists";
        printArtists(artists, title);
    }

    public void promptCreateArtist() {
        clearScreen();
        System.out.println("\n----- Create New Artist -----");
        String name = getStringInput("Enter artist name: ");
        if (name == null) {
            return;
        }

        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("Artist " + name.trim() + " created successfully");
        } else {
            System.out.println("Failed to create artist.");
        }
    }

    public void promptUpdateArtist() {
        clearScreen();
        System.out.println("\n----- Update Artist -----");
        Integer id = getIntInput("Enter artist id: ");
        if (id == null) return;

        Artist existing = artistService.findById(id);
        if (existing == null) {
            System.out.println("\nNo active artist found with ID " + id + ".");
            return;
        }
        System.out.println("Current artist: " + existing.getName());
        System.out.println("1. Change Name");
        System.out.println("0. Cancel");
        Integer choice = promptForInt("Which detail to change? ");
        if (choice == null || choice == 0) return;
        if (choice != 1) {
            System.out.println("Invalid option.");
            return;
        }
        String name = getStringInput("Enter new artist name: ");
        if (name == null) return;
        boolean updated = artistService.updateArtist(id, name);
        System.out.println(updated ? "\nArtist updated successfully!" : "\nFailed to update artist.");
    }

    public void promptDeleteArtist(){
        clearScreen();
        System.out.println("\n----- Delete Artist -----");
        Integer id = getIntInput("Enter artist id: ");
        if (id == null) {
            return;
        }

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("\nArtist ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptArchiveArtist(){
        clearScreen();
        System.out.println("\n----- Archive Artist -----");
        Integer id = getIntInput("Enter artist id: ");
        if (id == null) {
            return;
        }

        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("\nArtist ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptRestoreArtist(){
        clearScreen();
        System.out.println("\n----- Restore Artist -----");

        List<Artist> archivedArtists = artistService.getAllArchivedArtists();
        printArtists(archivedArtists, "Archived Artists");

        if (archivedArtists == null || archivedArtists.isEmpty()) {
            return;
        }

        Integer id = getIntInput("Enter artist id: ");
        if (id == null) {
            return;
        }

        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("\nArtist ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptSearchArtist(){
        clearScreen();
        System.out.println("\n----- Search Artists -----");

        String keyword = getStringInput("Enter Keyword (search): ");
        if (keyword == null) {
            return;
        }

        List<Artist> artists = artistService.searchArtists(keyword);
        printArtists(artists, "Search Artists");
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

    private void printArtists(List<Artist> artists, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-20s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No Artist found.");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-4d| %-20s%n",
                    artist.getId(),
                    truncate(artist.getName(), 20));
        }
    }
}
