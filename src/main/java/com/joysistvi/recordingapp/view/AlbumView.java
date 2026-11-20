package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;
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
        System.out.print("Enter album name: ");
        String name = scanner.nextLine();

        System.out.print("Enter release year: ");
        int year = scanner.nextInt();
        System.out.print("Enter artist id: ");
        int artistId = scanner.nextInt();
        scanner.nextLine();

        boolean created = albumService.createAlbum(name, year, artistId);

        if (created) {
            System.out.println("\nAlbum " + name.trim() + " created successfully!");
        } else {
            System.out.println("\nFailed to create album.");
        }
    }

    public void promptUpdateAlbum() {
        System.out.println("\n----- Update Album -----");
        System.out.print("Enter album id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new album name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new release year: ");
        int year = scanner.nextInt();
        System.out.print("Enter new artist id: ");
        int artistId = scanner.nextInt();
        scanner.nextLine();

        boolean updated = albumService.updateAlbum(id, name, year, artistId);

        if (updated) {
            System.out.println("\nAlbum ID " + id + " updated successfully!");
        } else {
            System.out.println("\nFailed to update album ID " + id + ".");
        }
    }

    public void promptDeleteAlbum() {
        System.out.println("\n----- Delete Album -----");
        System.out.print("Enter album id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean deleted = albumService.deleteAlbum(id);

        if (deleted) {
            System.out.println("\nAlbum ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo album found with ID " + id + ".");
        }
    }

    public void promptArchiveAlbum() {
        System.out.println("\n----- Archive Album -----");
        System.out.print("Enter album id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

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

        System.out.print("Enter album id to restore: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean restored = albumService.restoreAlbum(id);

        if (restored) {
            System.out.println("\nAlbum ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo archived album found with ID " + id + ".");
        }
    }

    public void promptSearchAlbums() {
        System.out.println("\n----- Search Albums -----");
        System.out.print("Enter keyword (search): ");
        String keyword = scanner.nextLine();

        List<Album> albums = albumService.searchAlbums(keyword);
        printAlbums(albums, "Search Albums");
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
