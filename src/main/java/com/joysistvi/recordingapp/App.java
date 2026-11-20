package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.repository.AlbumRepository;
import com.joysistvi.recordingapp.repository.AlbumRepositoryImpl;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.repository.UserRepositoryImpl;
import com.joysistvi.recordingapp.service.ArtistService;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.service.AlbumService;
import com.joysistvi.recordingapp.service.AlbumServiceImpl;
import com.joysistvi.recordingapp.service.UserService;
import com.joysistvi.recordingapp.service.UserServiceImpl;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.UserView;


import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        Scanner scanner = new Scanner(System.in);

        // ARTIST
        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistView artistView = new ArtistView(artistService, scanner);

        // ALBUM
        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        AlbumView albumView = new AlbumView(albumService, scanner);

        // USER
        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserView userView = new UserView(userService, scanner);

        // ARTIST METHODS TEST
//        artistView.displayAllArtist();
//        artistView.promptCreateArtist();
//        artistView.promptUpdateArtist();
//        artistView.promptDeleteArtist();
//        artistView.promptArchiveArtist();
//        artistView.promptRestoreArtist();
//        artistView.promptSearchArtist();

        // ALBUM METHODS TEST
//        albumView.displayAllAlbums();
//        albumView.promptCreateAlbum();
//        albumView.promptUpdateAlbum();
//        albumView.promptDeleteAlbum();
//        albumView.promptArchiveAlbum();
//        albumView.promptRestoreAlbum();
//        albumView.promptSearchAlbums();

        // USER METHODS TEST
//        userView.displayAllUsers();
//        userView.promptCreateUser();
//        userView.promptUpdateUser();
//        userView.promptDeleteUser();
//        userView.promptArchiveUser();
//        userView.promptRestoreUser();
//        userView.promptSearchUsers();
//        userView.promptLoginUser();

        // VALIDATION TESTS
//        artistService.createArtist("   ");
//        albumService.createAlbum("", 2020, 1);
//        albumService.createAlbum("Sample", 1800, 1);
//        userService.createUser("", "password123", "user@example.com");
//        userService.createUser("sample", "123", "user@example.com");
//        userService.createUser("sample", "password123", "invalid-email");
    }
}
