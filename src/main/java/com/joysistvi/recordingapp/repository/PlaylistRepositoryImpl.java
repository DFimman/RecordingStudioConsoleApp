package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final DbConnection dbConnection;

    public PlaylistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Read Active Playlists
    @Override
    public List<Playlist> findAllActive() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT playlists.id, playlists.name, playlists.user_id, " +
                "users.username AS username, playlists.date_created, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.is_archived = 0 ORDER BY playlists.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                Timestamp dateCreated = resultSet.getTimestamp("date_created");
                boolean archived = resultSet.getBoolean("is_archived");

                playlists.add(new Playlist(id, name, userId, username, dateCreated, archived));
            }

        } catch (SQLException e) {
            System.out.println("\nRead Playlists Error: " + e.getMessage());
        }

        return playlists;
    }

    // Read Archived Playlists
    @Override
    public List<Playlist> findAllArchived() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT playlists.id, playlists.name, playlists.user_id, " +
                "users.username AS username, playlists.date_created, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.is_archived = 1 ORDER BY playlists.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                Timestamp dateCreated = resultSet.getTimestamp("date_created");
                boolean archived = resultSet.getBoolean("is_archived");

                playlists.add(new Playlist(id, name, userId, username, dateCreated, archived));
            }

        } catch (SQLException e) {
            System.out.println("\nRead Archived Playlists Error: " + e.getMessage());
        }

        return playlists;
    }

    // Create Playlist
    @Override
    public boolean save(Playlist playlist) {
        String query = "INSERT INTO playlists (name, user_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getUserId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nCreate Playlist Error: " + e.getMessage());
            return false;
        }
    }

    // Update Playlist
    @Override
    public boolean update(Playlist playlist) {
        String query = "UPDATE playlists SET name = ?, user_id = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getUserId());
            preparedStatement.setInt(3, playlist.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nUpdate Playlist Error: " + e.getMessage());
            return false;
        }
    }

    // Delete Playlist
    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM playlists WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nDelete Playlist Error: " + e.getMessage());
            return false;
        }
    }

    // Archive Playlist (Soft Delete)
    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE playlists SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nArchive Playlist Error: " + e.getMessage());
            return false;
        }
    }

    // Restore Playlist
    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE playlists SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nRestore Playlist Error: " + e.getMessage());
            return false;
        }
    }

    // Search Playlists
    @Override
    public List<Playlist> searchByName(String keyword) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT playlists.id, playlists.name, playlists.user_id, " +
                "users.username AS username, playlists.date_created, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.is_archived = 0 AND playlists.name LIKE ? " +
                "ORDER BY playlists.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String cleanKeyword = keyword.trim();
            preparedStatement.setString(1, "%" + cleanKeyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int userId = resultSet.getInt("user_id");
                    String username = resultSet.getString("username");
                    Timestamp dateCreated = resultSet.getTimestamp("date_created");
                    boolean archived = resultSet.getBoolean("is_archived");

                    playlists.add(new Playlist(id, name, userId, username, dateCreated, archived));
                }
            }

        } catch (SQLException e) {
            System.out.println("\nSearch Playlists Error: " + e.getMessage());
        }

        return playlists;
    }
}
