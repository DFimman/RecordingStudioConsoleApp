package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import java.util.List;

// Repository(Data Tier) -> Data Access Logic and Data Persistence
public interface PlaylistRepository {

    public List<Playlist> findAllActive();
    public List<Playlist> findAllArchived();
    public boolean save(Playlist playlist);
    public boolean update(Playlist playlist);
    public boolean deleteById(int id);
    public boolean archiveById(int id);
    public boolean restoreById(int id);
    public List<Playlist> searchByName(String keyword);

    // User-owned playlists (enforce ownership in SQL)
    List<Playlist> findAllForUser(int userId, boolean archived);
    Playlist findActiveByIdForUser(int playlistId, int userId);
    boolean updateNameForUser(int playlistId, String name, int userId);
    boolean deleteForUser(int playlistId, int userId);
    boolean archiveForUser(int playlistId, int userId);
    boolean restoreForUser(int playlistId, int userId);
    List<Playlist> searchByNameForUser(String keyword, int userId);

    // Playlist songs junction
    List<Song> findSongsInPlaylist(int playlistId, int userId);
    boolean addSongToPlaylist(int playlistId, int songId, int userId);
    boolean removeSongFromPlaylist(int playlistId, int songId, int userId);
}
