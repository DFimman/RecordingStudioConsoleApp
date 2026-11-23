package com.joysistvi.recordingapp.model;

import java.sql.Timestamp;

// class for representing playlist db table
public class Playlist {

    private int id;
    private String name;
    private int userId;
    private String username;
    private Timestamp dateCreated;
    private boolean archived;

    public Playlist(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public Playlist(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Playlist(int id, String name, int userId, String username, Timestamp dateCreated, boolean archived) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.dateCreated = dateCreated;
        this.archived = archived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
