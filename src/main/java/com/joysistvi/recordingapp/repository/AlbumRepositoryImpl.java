package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumRepositoryImpl implements AlbumRepository {

    private final DbConnection dbConnection;

    public AlbumRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Read Active Albums
    // Find Active Album By ID
    @Override
    public Album findById(int id) {
        String query = "SELECT albums.id, albums.name, albums.year, albums.artist_id, " +
                "artists.name AS artist_name, albums.is_archived " +
                "FROM albums JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.id = ? AND albums.is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Album(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("year"),
                            resultSet.getInt("artist_id"),
                            resultSet.getString("artist_name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nFind Album Error: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Album> findAllActive() {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT albums.id, albums.name, albums.year, albums.artist_id, " +
                "artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int year = resultSet.getInt("year");
                int artistId = resultSet.getInt("artist_id");
                String artistName = resultSet.getString("artist_name");
                boolean archived = resultSet.getBoolean("is_archived");

                albums.add(new Album(id, name, year, artistId, artistName));
            }

        } catch (SQLException e) {
            System.out.println("Read Albums Error: " + e.getMessage());
        }

        return albums;
    }

    // Read Archived Albums
    @Override
    public List<Album> findAllArchived() {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT albums.id, albums.name, albums.year, albums.artist_id, " +
                "artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int year = resultSet.getInt("year");
                int artistId = resultSet.getInt("artist_id");
                String artistName = resultSet.getString("artist_name");
                boolean archived = resultSet.getBoolean("is_archived");

                albums.add(new Album(id, name, year, artistId, artistName));
            }

        } catch (SQLException e) {
            System.out.println("Read Archived Albums Error: " + e.getMessage());
        }

        return albums;
    }

    // Create Album
    @Override
    public boolean save(Album album) {
        String query = "INSERT INTO albums (name, year, artist_id) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, album.getName());
            preparedStatement.setInt(2, album.getYear());
            preparedStatement.setInt(3, album.getArtistId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nCreate Album Error: " + e.getMessage());
            return false;
        }
    }

    // Update Album
    @Override
    public boolean update(Album album) {
        String query = "UPDATE albums SET name = ?, year = ?, artist_id = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, album.getName());
            preparedStatement.setInt(2, album.getYear());
            preparedStatement.setInt(3, album.getArtistId());
            preparedStatement.setInt(4, album.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nUpdate Album Error: " + e.getMessage());
            return false;
        }
    }

    // Delete Album
    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM albums WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nDelete Album Error: " + e.getMessage());
            return false;
        }
    }

    // Archive Album (Soft Delete)
    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE albums SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nArchive Album Error: " + e.getMessage());
            return false;
        }
    }

    // Restore Album
    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE albums SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nRestore Album Error: " + e.getMessage());
            return false;
        }
    }

    // Search Albums
    @Override
    public List<Album> searchByName(String keyword) {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT albums.id, albums.name, albums.year, albums.artist_id, " +
                "artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.is_archived = 0 AND albums.name LIKE ? " +
                "ORDER BY albums.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String cleanKeyword = keyword.trim();
            preparedStatement.setString(1, "%" + cleanKeyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int year = resultSet.getInt("year");
                    int artistId = resultSet.getInt("artist_id");
                    String artistName = resultSet.getString("artist_name");
                    boolean archived = resultSet.getBoolean("is_archived");

                    albums.add(new Album(id, name, year, artistId, artistName));
                }
            }

        } catch (SQLException e) {
            System.out.println("\nSearch Album Error: " + e.getMessage());
        }

        return albums;
    }
}
