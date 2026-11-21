package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AlbumView {

    private final AlbumService albumService;
    private final Scanner scanner;

    public AlbumView(AlbumService albumService, Scanner scanner) {
        this.albumService = albumService;
        this.scanner = scanner;
    }

    public void displayAllAlbums() {
        displayAllAlbums(false);
    }

    public void displayAllAlbums(boolean isArchived) {
        List<Album> albums = isArchived ? albumService.getAllArchivedAlbums() : albumService.getAllActiveAlbums();
        printAlbums(albums, isArchived ? "Archived Albums" : "Albums");
    }

    public void promptCreateAlbum() {
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
        System.out.println("\n----- Update Album -----");
        Integer id = getIntInput("Enter album id: ");
        if (id == null) {
            return;
        }

        String name = getStringInput("Enter new album name: ");
        if (name == null) {
            return;
        }

        Integer year = getIntInput("Enter new release year: ");
        if (year == null) {
            return;
        }
        Integer artistId = getIntInput("Enter new artist id: ");
        if (artistId == null) {
            return;
        }

        boolean updated = albumService.updateAlbum(id, name, year, artistId);

        if (updated) {
            System.out.println("\nAlbum ID " + id + " updated successfully!");
        } else {
            System.out.println("\nFailed to update album ID " + id + ".");
        }
    }

    public void promptDeleteAlbum() {
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
        System.out.println("\n----- Search Albums -----");
        String keyword = getStringInput("Enter keyword (search): ");
        if (keyword == null) {
            return;
        }

        List<Album> albums = albumService.searchAlbums(keyword);
        printAlbums(albums, "Search Albums");
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
                    album.getName(),
                    album.getYear(),
                    album.getArtistName());
        }
    }
}
