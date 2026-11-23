package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.PlaylistService;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static com.joysistvi.recordingapp.view.ConsoleOutput.clearScreen;
import static com.joysistvi.recordingapp.view.ConsoleOutput.truncate;

public class PlaylistView {
    private final PlaylistService playlistService;
    private final SongService songService;
    private final Scanner scanner;

    public PlaylistView(PlaylistService playlistService, SongService songService, Scanner scanner) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.scanner = scanner;
    }

    public PlaylistView(PlaylistService playlistService, Scanner scanner) {
        this(playlistService, null, scanner);
    }

    // Playlist menu is only opened with an authenticated user's ID.
    public void handleMenu(int currentUserId) {
        if (currentUserId <= 0) return;
        while (true) {
            clearScreen();
            System.out.println("\n=== MY PLAYLISTS ===");
            System.out.println("1. View My Playlists");
            System.out.println("2. Create Playlist");
            System.out.println("3. Update Playlist");
            System.out.println("4. Search My Playlists");
            System.out.println("5. Archive Playlist");
            System.out.println("6. Restore Playlist");
            System.out.println("7. Delete Playlist");
            System.out.println("0. Back");
            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) return;
            switch (choice) {
                case 1 -> displayAllPlaylists(currentUserId, false);
                case 2 -> promptCreatePlaylist(currentUserId);
                case 3 -> promptUpdatePlaylist(currentUserId);
                case 4 -> promptSearchPlaylists(currentUserId);
                case 5 -> promptArchivePlaylist(currentUserId);
                case 6 -> promptRestorePlaylist(currentUserId);
                case 7 -> promptDeletePlaylist(currentUserId);
                case 0 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    // Do not expose the old unrestricted management menu.
    public void handleMenu() {
        System.out.println("\nPlease log in before managing your playlists.");
    }

    public void displayAllPlaylists(int userId, boolean archived) {
        clearScreen();
        List<Playlist> playlists = playlistService.getPlaylistsForUser(userId, archived);
        printPlaylists(playlists, archived ? "My Archived Playlists" : "My Playlists");
    }

    public void promptCreatePlaylist(int userId) {
        clearScreen();
        System.out.println("\n----- Create Playlist -----");
        String name = getStringInput("Enter playlist name: ");
        if (name == null) return;
        // Use the current session's ID, never ask for a user ID.
        boolean created = playlistService.createPlaylist(name, userId);
        System.out.println(created ? "\nPlaylist created successfully!" : "\nFailed to create playlist.");
    }

    public void promptUpdatePlaylist(int userId) {
        clearScreen();
        System.out.println("\n----- Update Playlist -----");
        Integer id = getIntInput("Enter playlist id: ");
        if (id == null) return;
        Playlist existing = playlistService.getPlaylistForUser(id, userId);
        if (existing == null) {
            System.out.println("\nPlaylist not found or doesn't belong to you.");
            return;
        }
        System.out.println("Current name: " + existing.getName());
        System.out.println("1. Change Name");
        System.out.println("0. Cancel");
        Integer choice = promptForInt("Which detail to change? ");
        if (choice == null || choice == 0) return;
        if (choice != 1) {
            System.out.println("Invalid option.");
            return;
        }
        String name = getStringInput("Enter new playlist name: ");
        if (name == null) return;
        boolean updated = playlistService.updatePlaylistNameForUser(id, name, userId);
        System.out.println(updated ? "\nPlaylist updated successfully!" : "\nFailed to update playlist.");
    }

    public void promptSearchPlaylists(int userId) {
        clearScreen();
        System.out.println("\n----- Search My Playlists -----");
        String keyword = getStringInput("Enter keyword (search): ");
        if (keyword == null) return;
        printPlaylists(playlistService.searchPlaylistsForUser(keyword, userId), "Search Results");
    }

    public void promptArchivePlaylist(int userId) {
        clearScreen();
        System.out.println("\n----- Archive Playlist -----");
        List<Playlist> playlists = playlistService.getPlaylistsForUser(userId, false);
        printPlaylists(playlists, "My Active Playlists");
        if (playlists == null || playlists.isEmpty()) return;
        Integer id = getIntInput("Enter playlist id: ");
        if (id == null) return;
        boolean archived = playlistService.archivePlaylistForUser(id, userId);
        System.out.println(archived ? "\nPlaylist archived successfully!" : "\nUnable to archive that playlist.");
    }

    public void promptRestorePlaylist(int userId) {
        clearScreen();
        System.out.println("\n----- Restore Playlist -----");
        List<Playlist> archived = playlistService.getPlaylistsForUser(userId, true);
        printPlaylists(archived, "My Archived Playlists");
        if (archived == null || archived.isEmpty()) return;
        Integer id = getIntInput("Enter playlist id to restore: ");
        if (id == null) return;
        boolean restored = playlistService.restorePlaylistForUser(id, userId);
        System.out.println(restored ? "\nPlaylist restored successfully!" : "\nUnable to restore that playlist.");
    }

    public void promptDeletePlaylist(int userId) {
        clearScreen();
        System.out.println("\n----- Delete Playlist -----");
        List<Playlist> playlists = playlistService.getPlaylistsForUser(userId, false);
        printPlaylists(playlists, "My Active Playlists");
        if (playlists == null || playlists.isEmpty()) return;
        Integer id = getIntInput("Enter playlist id to delete permanently: ");
        if (id == null) return;
        boolean deleted = playlistService.deletePlaylistForUser(id, userId);
        System.out.println(deleted ? "\nPlaylist deleted successfully!" :
                "\nDelete failed. Remove songs from the playlist first, or check its ID.");
    }

    // Add or remove songs in the playlist_songs table
    public void handleManageSongsInPlaylist(int userId) {
        if (userId <= 0) return;
        clearScreen();
        System.out.println("\n=== MANAGE SONGS IN MY PLAYLIST ===");
        List<Playlist> playlists = playlistService.getPlaylistsForUser(userId, false);
        printPlaylists(playlists, "My Active Playlists");
        if (playlists == null || playlists.isEmpty()) return;
        Integer playlistId = getIntInput("Enter playlist ID: ");
        if (playlistId == null) return;
        Playlist existing = playlistService.getPlaylistForUser(playlistId, userId);
        if (existing == null) {
            System.out.println("\nPlaylist not found or doesn't belong to you.");
            return;
        }
        while (true) {
            clearScreen();
            System.out.println("\n=== " + truncate(existing.getName(), 40) + " ===");
            System.out.println("1. View Songs in Playlist");
            System.out.println("2. Add Song");
            System.out.println("3. Remove Song");
            System.out.println("0. Back");
            Integer choice = promptForInt("Choose an option: ");
            if (choice == null) return;
            switch (choice) {
                case 1 -> printSongs(playlistService.getSongsInPlaylist(playlistId, userId), "Playlist Songs");
                case 2 -> {
                    if (songService == null) {
                        System.out.println("Song browsing is unavailable.");
                        break;
                    }
                    printSongs(songService.getAllActiveSongs(), "Available Songs");
                    Integer songId = getIntInput("Enter song ID to add: ");
                    if (songId == null) return;
                    boolean added = playlistService.addSongToPlaylist(playlistId, songId, userId);
                    System.out.println(added ? "\nSong added!" : "\nUnable to add song (already in playlist, invalid, or archived).");
                }
                case 3 -> {
                    List<Song> songs = playlistService.getSongsInPlaylist(playlistId, userId);
                    printSongs(songs, "Playlist Songs");
                    if (songs == null || songs.isEmpty()) break;
                    Integer songId = getIntInput("Enter song ID to remove: ");
                    if (songId == null) return;
                    boolean removed = playlistService.removeSongFromPlaylist(playlistId, songId, userId);
                    System.out.println(removed ? "\nSong removed!" : "\nUnable to remove song.");
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
            if (!ConsoleOutput.pause(scanner)) return;
        }
    }

    private String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String value = scanner.nextLine();
                if (value.trim().isEmpty()) {
                    System.out.println("\nValidation Error: Input cannot be blank.");
                    continue;
                }
                return value;
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nInput Error: Unable to read input. Operation cancelled.");
                return null;
            }
        }
    }

    private Integer getIntInput(String prompt) {
        while (true) {
            Integer value = promptForInt(prompt);
            if (value == null) return null;
            if (value > 0) return value;
            System.out.println("\nValidation Error: Please enter a positive number.");
        }
    }

    private Integer promptForInt(String prompt) {
        while (true) {
            String value = getStringInput(prompt);
            if (value == null) return null;
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Error: Please enter a whole number.");
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
            String created = playlist.getDateCreated() == null ? "" : playlist.getDateCreated().toString();
            System.out.printf("%-4d| %-30s| %-20s| %-20s%n", playlist.getId(),
                    truncate(playlist.getName(), 30), truncate(playlist.getUsername(), 20), truncate(created, 20));
        }
    }

    private void printSongs(List<Song> songs, String title) {
        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-4s| %-25s| %-12s| %-8s| %-20s%n", "ID", "Title", "Genre", "Length", "Album");
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }
        for (Song song : songs) {
            System.out.printf("%-4d| %-25s| %-12s| %-8s| %-20s%n", song.getId(),
                    truncate(song.getTitle(), 25), truncate(song.getGenre(), 12),
                    truncate(song.getLength(), 8), truncate(song.getAlbumName(), 20));
        }
    }
}
