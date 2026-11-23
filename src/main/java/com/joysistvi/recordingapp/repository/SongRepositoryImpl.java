package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongRepositoryImpl implements SongRepository {

    private final DbConnection dbConnection;

    public SongRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Read Active Songs
    @Override
    public List<Song> findAllActive() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, songs.album_id, " +
                "albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.is_archived = 0 ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String length = resultSet.getString("length");
                int albumId = resultSet.getInt("album_id");
                String albumName = resultSet.getString("album_name");

                Song song = new Song(id, title, genre, length, albumId, albumName);
                song.setArchived(resultSet.getBoolean("is_archived"));
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("\nRead Songs Error: " + e.getMessage());
        }

        return songs;
    }

    // Read Archived Songs
    @Override
    public List<Song> findAllArchived() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, songs.album_id, " +
                "albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.is_archived = 1 ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String length = resultSet.getString("length");
                int albumId = resultSet.getInt("album_id");
                String albumName = resultSet.getString("album_name");

                Song song = new Song(id, title, genre, length, albumId, albumName);
                song.setArchived(resultSet.getBoolean("is_archived"));
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("\nRead Archived Songs Error: " + e.getMessage());
        }

        return songs;
    }

    // Create Song
    @Override
    public boolean save(Song song) {
        String query = "INSERT INTO songs (title, genre, length, album_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getGenre());
            preparedStatement.setString(3, song.getLength());
            preparedStatement.setInt(4, song.getAlbumId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nCreate Song Error: " + e.getMessage());
            return false;
        }
    }

    // Update Song
    @Override
    public boolean update(Song song) {
        String query = "UPDATE songs SET title = ?, genre = ?, length = ?, album_id = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getGenre());
            preparedStatement.setString(3, song.getLength());
            preparedStatement.setInt(4, song.getAlbumId());
            preparedStatement.setInt(5, song.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nUpdate Song Error: " + e.getMessage());
            return false;
        }
    }

    // Delete Song
    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nDelete Song Error: " + e.getMessage());
            return false;
        }
    }

    // Archive Song (Soft Delete)
    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE songs SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nArchive Song Error: " + e.getMessage());
            return false;
        }
    }

    // Restore Song
    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE songs SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRestore Song Error: " + e.getMessage());
            return false;
        }
    }

    // Search Songs
    @Override
    public List<Song> searchByTitle(String keyword) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, songs.album_id, " +
                "albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.is_archived = 0 AND songs.title LIKE ? ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String cleanKeyword = keyword.trim();
            preparedStatement.setString(1, "%" + cleanKeyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String length = resultSet.getString("length");
                    int albumId = resultSet.getInt("album_id");
                    String albumName = resultSet.getString("album_name");

                    Song song = new Song(id, title, genre, length, albumId, albumName);
                    song.setArchived(resultSet.getBoolean("is_archived"));
                    songs.add(song);
                }
            }
        } catch (SQLException e) {
            System.out.println("\nSearch Song Error: " + e.getMessage());
        }

        return songs;
    }
}
