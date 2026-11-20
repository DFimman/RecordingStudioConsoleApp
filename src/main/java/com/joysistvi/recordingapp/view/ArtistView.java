package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;
import java.util.Scanner;

public class ArtistView {

    private final ArtistService artistService;
    private final Scanner scanner;

    public ArtistView(ArtistService artistService, Scanner scanner) {
        this.artistService = artistService;
        this.scanner = scanner;
    }

    public void displayAllArtist() {
        displayAllArtist(false);
    }

    public void displayAllArtist(boolean isArchived) {
        List<Artist> artists = isArchived ? artistService.getAllArchivedArtists() : artistService.getAllActiveArtists();

        String title = isArchived ? "Archived Artists" : "Artists";
        printArtists(artists, title);
    }

    public void promptCreateArtist() {
        System.out.println("\n----- Create New Artist -----");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine();

        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("Artist " + name.trim() + " created successfully");
        } else {
            System.out.println("Failed to create artist.");
        }
    }

    public void promptUpdateArtist(){
        System.out.println("\n----- Update Artist -----");
        System.out.print("Enter artist id: ");
        int id = scanner.nextInt();
        System.out.print("Enter new artist name: ");
        String name = scanner.next();

        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("\nArtist ID " + id + " updated successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptDeleteArtist(){
        System.out.println("\n----- Delete Artist -----");
        System.out.print("Enter artist id: ");
        int id = scanner.nextInt();

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("\nArtist ID " + id + " deleted successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptArchiveArtist(){
        System.out.println("\n----- Archive Artist -----");
        System.out.print("Enter artist id: ");
        int id = scanner.nextInt();

        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("\nArtist ID " + id + " archived successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptRestoreArtist(){
        System.out.println("\n----- Restore Artist -----");
        displayAllArtist(true);

        System.out.print("Enter artist id: ");
        int id = scanner.nextInt();

        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("\nArtist ID " + id + " restored successfully!");
        } else {
            System.out.println("\nNo artist found with ID " + id + ".");
        }
    }

    public void promptSearchArtist(){
        System.out.println("\n----- Search Artists -----");

        System.out.print("Enter Keyword (search): ");
        String keyword = scanner.nextLine().trim();

        List<Artist> artists = artistService.searchArtists(keyword);
        System.out.printf("%-4s| %-20s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()){
            System.out.println("");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-4d| %-20s%n",
                    artist.getId(),
                    artist.getName());
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
                    artist.getName());
        }
    }
}

