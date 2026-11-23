package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.SongRepository;

import java.util.Collections;
import java.util.List;

// Application Tier: Business Rule / Validation
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    // Find active song by ID
    @Override
    public Song findById(int id) {
        if (id <= 0) {
            return null;
        }

        return songRepository.findById(id);
    }

    @Override
    public List<Song> getAllActiveSongs() {
        return songRepository.findAllActive();
    }

    @Override
    public List<Song> getAllArchivedSongs() {
        return songRepository.findAllArchived();
    }

    @Override
    public boolean createSong(String title, String genre, String length, int albumId) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song title is required.");
            return false;
        }
        if (title.trim().length() > 25) {
            System.out.println("\nValidation Error: Song title must not exceed 25 characters.");
            return false;
        }
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("\nValidation Error: Genre is required.");
            return false;
        }
        if (genre.trim().length() > 10) {
            System.out.println("\nValidation Error: Genre must not exceed 10 characters.");
            return false;
        }
        if (length == null || length.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song length is required.");
            return false;
        }
        if (length.trim().length() > 5 || !length.trim().matches("\\d+:[0-5]\\d")) {
            System.out.println("\nValidation Error: Song length must use minutes:seconds (e.g. 3:45).");
            return false;
        }
        if (albumId <= 0) {
            System.out.println("\nValidation Error: Album ID must be greater than 0.");
            return false;
        }

        Song song = new Song(title.trim(), genre.trim(), length.trim(), albumId);
        return songRepository.save(song);
    }

    @Override
    public boolean updateSong(int id, String title, String genre, String length, int albumId) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }
        if (title == null || title.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song title is required.");
            return false;
        }
        if (title.trim().length() > 25) {
            System.out.println("\nValidation Error: Song title must not exceed 25 characters.");
            return false;
        }
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("\nValidation Error: Genre is required.");
            return false;
        }
        if (genre.trim().length() > 10) {
            System.out.println("\nValidation Error: Genre must not exceed 10 characters.");
            return false;
        }
        if (length == null || length.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song length is required.");
            return false;
        }
        if (length.trim().length() > 5 || !length.trim().matches("\\d+:[0-5]\\d")) {
            System.out.println("\nValidation Error: Song length must use minutes:seconds (e.g. 3:45).");
            return false;
        }
        if (albumId <= 0) {
            System.out.println("\nValidation Error: Album ID must be greater than 0.");
            return false;
        }

        Song song = new Song(id, title.trim(), genre.trim(), length.trim(), albumId);
        return songRepository.update(song);
    }

    @Override
    public boolean deleteSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }
        return songRepository.deleteById(id);
    }

    @Override
    public boolean archiveSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }
        return songRepository.archiveById(id);
    }

    @Override
    public boolean restoreSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }
        return songRepository.restoreById(id);
    }

    @Override
    public List<Song> searchSongs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return Collections.emptyList();
        }

        return songRepository.searchByTitle(keyword.trim());
    }
}
