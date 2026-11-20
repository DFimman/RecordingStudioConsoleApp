package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository {

    private final DbConnection dbConnection;

    public ArtistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Artist> findAllActive() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists WHERE is_archived = 0";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                artists.add(new Artist(id, name));

            }
        } catch (SQLException e) {
            System.out.println("Read Artists Error: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public boolean save(Artist artists) {
        String name = artists.getName();

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        String query = "INSERT INTO artists (name) VALUES (?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name.trim());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nCreate Artist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Artist> findAllArchived() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists WHERE is_archived = 1";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                artists.add(new Artist(id, name));

            }
        } catch (SQLException e) {
            System.out.println("Read Archived Artists Error: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public boolean update(Artist artists){
        int id = artists.getId();
        String name = artists.getName();

        if (id <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name.trim());
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate() > 0;


        } catch (SQLException e) {
            System.out.println("\nUpdate Artist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id){
        if (id <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }

        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nDelete Artist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id){
        if (id <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }

        String query = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nArchive Artist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id){
        if (id <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }

        String query = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRestore Artist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Artist> searchByName(String keyword){
        List<Artist> artists = new ArrayList<>();

        String query = "SELECT id, name FROM artists " +
                "WHERE is_archived = 0 AND name LIKE ? " +
                "ORDER BY id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            String cleanKeyword = keyword.trim();
            prep.setString(1, "%" + cleanKeyword + "%");

            try (ResultSet res = prep.executeQuery()) {
                boolean found = false;
                while (res.next()) {
                    found = true;
                    int id = res.getInt("id");
                    String name = res.getString("name");
                    artists.add(new Artist(id,name));
                }

                if (!found) {
                    System.out.println("No matching active artists found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("\nSearch Artist Error: " + e.getMessage());
        }
        return artists;
    }
}
