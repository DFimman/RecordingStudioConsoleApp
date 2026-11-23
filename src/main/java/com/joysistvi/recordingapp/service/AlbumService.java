package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Album;
import java.util.List;

public interface AlbumService {

    public List<Album> getAllActiveAlbums();
    public List<Album> getAllArchivedAlbums();
    public boolean createAlbum(String name, int year, int artistId);
    public boolean updateAlbum(int id, String name, int year, int artistId);
    public boolean deleteAlbum(int id);
    public boolean archiveAlbum(int id);
    public boolean restoreAlbum(int id);
    public List<Album> searchAlbums(String keyword);

    // Look up the current values before updating a selected field
    default Album findById(int id) {
        if (id <= 0) return null;
        List<Album> records = getAllActiveAlbums();
        if (records == null) return null;
        for (Album record : records) {
            if (record.getId() == id) return record;
        }
        return null;
    }
}
