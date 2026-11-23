package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
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
}
