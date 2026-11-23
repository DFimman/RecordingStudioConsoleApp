package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import java.util.List;

public interface PlaylistService {

    public List<Playlist> getAllActivePlaylists();
    public List<Playlist> getAllArchivedPlaylists();
    public boolean createPlaylist(String name, int userId);
    public boolean updatePlaylist(int id, String name, int userId);
    public boolean deletePlaylist(int id);
    public boolean archivePlaylist(int id);
    public boolean restorePlaylist(int id);
    public List<Playlist> searchPlaylists(String keyword);

    List<Playlist> getPlaylistsForUser(int userId, boolean archived);
    Playlist getPlaylistForUser(int playlistId, int userId);
    boolean updatePlaylistNameForUser(int playlistId, String name, int userId);
    boolean deletePlaylistForUser(int playlistId, int userId);
    boolean archivePlaylistForUser(int playlistId, int userId);
    boolean restorePlaylistForUser(int playlistId, int userId);
    List<Playlist> searchPlaylistsForUser(String keyword, int userId);
    List<Song> getSongsInPlaylist(int playlistId, int userId);
    boolean addSongToPlaylist(int playlistId, int songId, int userId);
    boolean removeSongFromPlaylist(int playlistId, int songId, int userId);
}
