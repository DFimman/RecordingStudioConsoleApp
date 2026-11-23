package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Playlist;
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
}
