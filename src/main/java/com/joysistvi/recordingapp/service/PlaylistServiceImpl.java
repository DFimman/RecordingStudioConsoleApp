package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.PlaylistRepository;

import java.util.Collections;
import java.util.List;

// Application Tier: Business Rule / Validation
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Playlist> getAllActivePlaylists() {
        return playlistRepository.findAllActive();
    }

    @Override
    public List<Playlist> getAllArchivedPlaylists() {
        return playlistRepository.findAllArchived();
    }

    @Override
    public boolean createPlaylist(String name, int userId) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Playlist name is required.");
            return false;
        }
        if (name.trim().length() > 100) {
            System.out.println("\nValidation Error: Playlist name must not exceed 100 characters.");
            return false;
        }
        if (userId <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        Playlist playlist = new Playlist(name.trim(), userId);
        return playlistRepository.save(playlist);
    }

    @Override
    public boolean updatePlaylist(int id, String name, int userId) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Playlist name is required.");
            return false;
        }
        if (name.trim().length() > 100) {
            System.out.println("\nValidation Error: Playlist name must not exceed 100 characters.");
            return false;
        }
        if (userId <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        Playlist playlist = new Playlist(id, name.trim(), userId);
        return playlistRepository.update(playlist);
    }

    @Override
    public boolean deletePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }
        return playlistRepository.deleteById(id);
    }

    @Override
    public boolean archivePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }
        return playlistRepository.archiveById(id);
    }

    @Override
    public boolean restorePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }
        return playlistRepository.restoreById(id);
    }

    @Override
    public List<Playlist> searchPlaylists(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return Collections.emptyList();
        }
        return playlistRepository.searchByName(keyword.trim());
    }
    // Only access playlists belonging to the logged-in user
    @Override
    public List<Playlist> getPlaylistsForUser(int userId, boolean archived) {
        if (userId <= 0) return Collections.emptyList();
        return playlistRepository.findAllForUser(userId, archived);
    }

    @Override
    public Playlist getPlaylistForUser(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return null;
        return playlistRepository.findActiveByIdForUser(playlistId, userId);
    }

    @Override
    public boolean updatePlaylistNameForUser(int playlistId, String name, int userId) {
        if (playlistId <= 0 || userId <= 0) {
            System.out.println("\nValidation Error: IDs must be positive.");
            return false;
        }
        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            System.out.println("\nValidation Error: Playlist name must be 1-100 characters.");
            return false;
        }
        return playlistRepository.updateNameForUser(playlistId, name.trim(), userId);
    }

    @Override
    public boolean deletePlaylistForUser(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return false;
        return playlistRepository.deleteForUser(playlistId, userId);
    }

    @Override
    public boolean archivePlaylistForUser(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return false;
        return playlistRepository.archiveForUser(playlistId, userId);
    }

    @Override
    public boolean restorePlaylistForUser(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return false;
        return playlistRepository.restoreForUser(playlistId, userId);
    }

    @Override
    public List<Playlist> searchPlaylistsForUser(String keyword, int userId) {
        if (userId <= 0 || keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return playlistRepository.searchByNameForUser(keyword.trim(), userId);
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return Collections.emptyList();
        return playlistRepository.findSongsInPlaylist(playlistId, userId);
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId, int userId) {
        if (playlistId <= 0 || songId <= 0 || userId <= 0) {
            System.out.println("\nValidation Error: IDs must be positive.");
            return false;
        }
        return playlistRepository.addSongToPlaylist(playlistId, songId, userId);
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId, int userId) {
        if (playlistId <= 0 || songId <= 0 || userId <= 0) {
            System.out.println("\nValidation Error: IDs must be positive.");
            return false;
        }
        return playlistRepository.removeSongFromPlaylist(playlistId, songId, userId);
    }
}
