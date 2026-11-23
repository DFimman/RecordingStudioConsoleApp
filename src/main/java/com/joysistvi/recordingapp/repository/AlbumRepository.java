package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Album;
import java.util.List;

// Repository(Data Tier) -> Data Access Logic and Data Persistence
public interface AlbumRepository {

    public Album findById(int id);
    public List<Album> findAllActive();
    public List<Album> findAllArchived();
    public boolean save(Album album);
    public boolean update(Album album);
    public boolean deleteById(int id);
    public boolean archiveById(int id);
    public boolean restoreById(int id);
    public List<Album> searchByName(String keyword);
}
