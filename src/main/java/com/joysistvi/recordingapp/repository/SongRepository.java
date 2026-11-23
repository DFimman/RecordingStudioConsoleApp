package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Song;
import java.util.List;

// Repository(Data Tier) -> Data Access Logic and Data Persistence
public interface SongRepository {

    public Song findById(int id);
    public List<Song> findAllActive();
    public List<Song> findAllArchived();
    public boolean save(Song song);
    public boolean update(Song song);
    public boolean deleteById(int id);
    public boolean archiveById(int id);
    public boolean restoreById(int id);
    public List<Song> searchByTitle(String keyword);
}
