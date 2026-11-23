package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;

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
    // Read a user's active or archived playlists
    @Override
    public List<Playlist> findAllForUser(int userId, boolean archived) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.date_created, p.is_archived " +
                "FROM playlists p JOIN users u ON p.user_id = u.id " +
                "WHERE p.user_id = ? AND p.is_archived = ? ORDER BY p.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setBoolean(2, archived);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) playlists.add(readPlaylist(rs));
            }
        } catch (SQLException e) {
            System.out.println("\nRead User Playlists Error: " + e.getMessage());
        }
        return playlists;
    }

    @Override
    public Playlist findActiveByIdForUser(int playlistId, int userId) {
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.date_created, p.is_archived " +
                "FROM playlists p JOIN users u ON p.user_id = u.id " +
                "WHERE p.id = ? AND p.user_id = ? AND p.is_archived = 0";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return readPlaylist(rs);
            }
        } catch (SQLException e) {
            System.out.println("\nFind Playlist Error: " + e.getMessage());
        }
        return null;
    }

    private Playlist readPlaylist(ResultSet rs) throws SQLException {
        return new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("user_id"),
                rs.getString("username"), rs.getTimestamp("date_created"), rs.getBoolean("is_archived"));
    }

    @Override
    public boolean updateNameForUser(int playlistId, String name, int userId) {
        String query = "UPDATE playlists SET name = ? WHERE id = ? AND user_id = ? AND is_archived = 0";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, playlistId);
            statement.setInt(3, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nUpdate Owned Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteForUser(int playlistId, int userId) {
        String query = "DELETE FROM playlists WHERE id = ? AND user_id = ?";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nDelete Owned Playlist Error (remove its songs first): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveForUser(int playlistId, int userId) {
        String query = "UPDATE playlists SET is_archived = 1 WHERE id = ? AND user_id = ? AND is_archived = 0";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nArchive Owned Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreForUser(int playlistId, int userId) {
        String query = "UPDATE playlists SET is_archived = 0 WHERE id = ? AND user_id = ? AND is_archived = 1";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRestore Owned Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Playlist> searchByNameForUser(String keyword, int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.date_created, p.is_archived " +
                "FROM playlists p JOIN users u ON p.user_id = u.id " +
                "WHERE p.is_archived = 0 AND p.user_id = ? AND p.name LIKE ? ORDER BY p.id ASC";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, "%" + keyword + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) playlists.add(readPlaylist(rs));
            }
        } catch (SQLException e) {
            System.out.println("\nSearch Owned Playlists Error: " + e.getMessage());
        }
        return playlists;
    }

    @Override
    public List<Song> findSongsInPlaylist(int playlistId, int userId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.genre, s.length, s.album_id, a.name AS album_name " +
                "FROM playlist_songs ps JOIN playlists p ON ps.playlist_id = p.id " +
                "JOIN songs s ON ps.song_id = s.id JOIN albums a ON s.album_id = a.id " +
                "WHERE ps.playlist_id = ? AND p.user_id = ? AND p.is_archived = 0 AND s.is_archived = 0 " +
                "ORDER BY s.id ASC";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) songs.add(new Song(rs.getInt("id"), rs.getString("title"),
                        rs.getString("genre"), rs.getString("length"),
                        rs.getInt("album_id"), rs.getString("album_name")));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Playlist Songs Error: " + e.getMessage());
        }
        return songs;
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId, int userId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id) " +
                "SELECT p.id, s.id FROM playlists p JOIN songs s ON s.id = ? " +
                "WHERE p.id = ? AND p.user_id = ? AND p.is_archived = 0 AND s.is_archived = 0 " +
                "AND NOT EXISTS (SELECT 1 FROM playlist_songs ps WHERE ps.playlist_id = p.id AND ps.song_id = s.id)";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, songId);
            statement.setInt(2, playlistId);
            statement.setInt(3, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nAdd Song to Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId, int userId) {
        String query = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ? " +
                "AND EXISTS (SELECT 1 FROM playlists p WHERE p.id = ? AND p.user_id = ? AND p.is_archived = 0)";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.setInt(3, playlistId);
            statement.setInt(4, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRemove Song from Playlist Error: " + e.getMessage());
            return false;
        }
    }
}
