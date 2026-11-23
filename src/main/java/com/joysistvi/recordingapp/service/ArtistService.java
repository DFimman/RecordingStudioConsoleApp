package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;
import java.util.List;

public interface ArtistService {
    List<Artist> getAllActiveArtists();
    List<Artist> getAllArchivedArtists();
    boolean createArtist(String name);
    boolean updateArtist(int id, String name);
    boolean deleteArtist(int id);
    boolean archiveArtist(int id);
    boolean restoreArtist(int id);
    List<Artist> searchArtists(String keyword);

    // Read current details before editing one field
    default Artist findById(int id) {
        if (id <= 0) return null;
        List<Artist> artists = getAllActiveArtists();
        if (artists == null) return null;
        for (Artist artist : artists) {
            if (artist.getId() == id) return artist;
        }
        return null;
    }
}
