package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.joysistvi.recordingapp.view.ConsoleOutput.clearScreen;
import static com.joysistvi.recordingapp.view.ConsoleOutput.truncate;

public class SongView {

    private final SongService songService;
    private final Scanner scanner;

    public SongView(SongService songService, Scanner scanner) {
        this.songService = songService;
        this.scanner = scanner;
    }

    public void handleMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n=== MANAGE SONGS ===");
            System.out.println("1. View All Songs");
            System.out.println("2. Create Song");
            System.out.println("3. Update Song");
            System.out.println("4. Search Song");
            System.out.println("5. Archive Song");
            System.out.println("6. Restore Song");
            System.out.println("7. Delete Song");
            System.out.println("0. Back");

            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) {
                return;
            }

            switch (choice) {
                case 1 -> displayAllSongs();
                case 2 -> promptCreateSong();
                case 3 -> promptUpdateSong();
                case 4 -> promptSearchSongs();
                case 5 -> promptArchiveSong();
                case 6 -> promptRestoreSong();
                case 7 -> promptDeleteSong();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    public void displayAllSongs() {
        displayAllSongs(false);
    }

    public void displayAllSongs(boolean isArchived) {
        clearScreen();
        List<Song> songs = isArchived ? songService.getAllArchivedSongs() : songService.getAllActiveSongs();
        printSongs(songs, isArchived ? "Archived Songs" : "Songs");
    }

    public void promptCreateSong() {
        clearScreen();
        System.out.println("\n----- Create New Song -----");
        String title = getStringInput("Enter song title: ");
        if (title == null) {
            return;
        }

        String genre = getStringInput("Enter genre: ");
        if (genre == null) {
            return;
        }

        String length = getStringInput("Enter song length (e.g. 3:45): ");
        if (length == null) {
            return;
        }

        Integer albumId = getIntInput("Enter album id: ");
        if (albumId == null) {
            return;
        }

        boolean created = songService.createSong(title, genre, length, albumId);

        if (created) {
            System.out.println("\nSong " + title.trim() + " created successfully!");
        } else {
            System.out.println("\nFailed to create song.");
        }
    }

    public void promptUpdateSong() {
        clearScreen();
        System.out.println("\n----- Update Song -----");
        Integer id = getIntInput("Enter song id: ");
        if (id == null) return;

        Song existing = songService.findById(id);
        if (existing == null) {
            System.out.println("\nNo active song found with ID " + id + ".");
            return;
        }
        System.out.println("Current: " + existing.getTitle() + " | " + existing.getGenre() +
                " | " + existing.getLength() + " | Album " + existing.getAlbumId());
        System.out.println("1. Title");
        System.out.println("2. Genre");
        System.out.println("3. Length");
        System.out.println("4. Album ID");
        System.out.println("0. Cancel");
        Integer choice = promptForInt("Which detail to change? ");
        if (choice == null || choice == 0) return;

        String title = existing.getTitle();
        String genre = existing.getGenre();
        String length = existing.getLength();
        int albumId = existing.getAlbumId();
        switch (choice) {
            case 1 -> { title = getStringInput("Enter new title: "); if (title == null) return; }
            case 2 -> { genre = getStringInput("Enter new genre: "); if (genre == null) return; }
            case 3 -> { length = getStringInput("Enter new length (e.g. 3:45): "); if (length == null) return; }
            case 4 -> { Integer value = getIntInput("Enter new album ID: ");
                        if (value == null) return; albumId = value; }
            default -> { System.out.println("Invalid option."); return; }
        }
        boolean updated = songService.updateSong(id, title, genre, length, albumId);
        System.out.println(updated ? "\nSong updated successfully!" : "\nFailed to update song.");
    }

    public void promptDeleteSong() {
        clearScreen();
        System.out.println("\n----- Delete Song -----");
        Integer id = getIntInput("Enter song id: ");
        if (id == null) {
            return;
        }

        boolean deleted = songService.deleteSong(id);

        if (deleted) {
            System.out.println("\nSong ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo song found with ID " + id + ".");
        }
    }

    public void promptArchiveSong() {
        clearScreen();
        System.out.println("\n----- Archive Song -----");
        Integer id = getIntInput("Enter song id: ");
        if (id == null) {
            return;
        }

        boolean archived = songService.archiveSong(id);

        if (archived) {
            System.out.println("\nSong ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo song found with ID " + id + ".");
        }
    }

    public void promptRestoreSong() {
        clearScreen();
        System.out.println("\n----- Restore Song -----");
        List<Song> archivedSongs = songService.getAllArchivedSongs();
        printSongs(archivedSongs, "Archived Songs");

        if (archivedSongs == null || archivedSongs.isEmpty()) {
            return;
        }

        Integer id = getIntInput("Enter song id to restore: ");
        if (id == null) {
            return;
        }

        boolean restored = songService.restoreSong(id);

        if (restored) {
            System.out.println("\nSong ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo archived song found with ID " + id + ".");
        }
    }

    public void promptSearchSongs() {
        clearScreen();
        System.out.println("\n----- Search Songs -----");
        String keyword = getStringInput("Enter keyword (search): ");
        if (keyword == null) {
            return;
        }

        List<Song> songs = songService.searchSongs(keyword);
        printSongs(songs, "Search Songs");
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

    // Get a valid menu option (0 = Back)
    private Integer promptForInt(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            if (input == null) {
                return null;
            }

            try {
                int number = Integer.parseInt(input.trim());
                if (number < 0) {
                    System.out.println("\nValidation Error: Please enter a non-negative number.");
                    continue;
                }

                return number;
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Error: Please enter a valid whole number.");
            }
        }
    }

    private void printSongs(List<Song> songs, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-25s| %-15s| %-8s| %-22s%n", "ID", "Song Title", "Genre", "Length", "Album");

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        for (Song song : songs) {
            System.out.printf("%-4d| %-25s| %-15s| %-8s| %-22s%n",
                    song.getId(),
                    truncate(song.getTitle(), 25),
                    truncate(song.getGenre(), 15),
                    song.getLength(),
                    truncate(song.getAlbumName(), 22));
        }
    }
}
