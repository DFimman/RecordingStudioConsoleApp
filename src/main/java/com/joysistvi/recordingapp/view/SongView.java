package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SongView {

    private final SongService songService;
    private final Scanner scanner;

    public SongView(SongService songService, Scanner scanner) {
        this.songService = songService;
        this.scanner = scanner;
    }

    public void handleMenu() {
        while (true) {
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
        }
    }

    public void displayAllSongs() {
        displayAllSongs(false);
    }

    public void displayAllSongs(boolean isArchived) {
        List<Song> songs = isArchived ? songService.getAllArchivedSongs() : songService.getAllActiveSongs();
        printSongs(songs, isArchived ? "Archived Songs" : "Songs");
    }

    public void promptCreateSong() {
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
        System.out.println("\n----- Update Song -----");
        Integer id = getIntInput("Enter song id: ");
        if (id == null) {
            return;
        }

        String title = getStringInput("Enter new song title: ");
        if (title == null) {
            return;
        }

        String genre = getStringInput("Enter new genre: ");
        if (genre == null) {
            return;
        }

        String length = getStringInput("Enter new song length (e.g. 3:45): ");
        if (length == null) {
            return;
        }

        Integer albumId = getIntInput("Enter new album id: ");
        if (albumId == null) {
            return;
        }

        boolean updated = songService.updateSong(id, title, genre, length, albumId);

        if (updated) {
            System.out.println("\nSong ID " + id + " updated successfully!");
        } else {
            System.out.println("\nFailed to update song ID " + id + ".");
        }
    }

    public void promptDeleteSong() {
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
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumName());
        }
    }
}
