package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import java.util.List;

public interface SongService {

    public List<Song> getAllActiveSongs();
    public List<Song> getAllArchivedSongs();
    public boolean createSong(String title, String genre, String length, int albumId);
    public boolean updateSong(int id, String title, String genre, String length, int albumId);
    public boolean deleteSong(int id);
    public boolean archiveSong(int id);
    public boolean restoreSong(int id);
    public List<Song> searchSongs(String keyword);

    // Look up the current values before updating a selected field
    default Song findById(int id) {
        if (id <= 0) return null;
        List<Song> records = getAllActiveSongs();
        if (records == null) return null;
        for (Song record : records) {
            if (record.getId() == id) return record;
        }
        return null;
    }
}
