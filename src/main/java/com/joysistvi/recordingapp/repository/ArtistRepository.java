package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Artist;
import java.util.List;

// Repository(Data Tier) -> Data Access Logic and Data Persistence
public interface ArtistRepository {

    public Artist findById(int id);
    public List<Artist> findAllActive();
    public List<Artist> findAllArchived();
    public boolean save(Artist artists);
    public boolean update(Artist artists);
    public boolean deleteById(int id);
    public boolean archiveById(int id);
    public boolean restoreById(int id);
    public List<Artist> searchByName(String name);

}
