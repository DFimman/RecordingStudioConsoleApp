package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.Scanner;


public class App {

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        Scanner scanner = new Scanner(System.in);

        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistView artistView = new ArtistView(artistService, scanner);

//        ARTIST METHODS TEST
//        artistView.displayAllArtist();
//
//        artistView.promptCreateArtist();
//        artistView.displayAllArtist();
//
//        artistView.promptUpdateArtist();
//        artistView.displayAllArtist();
//
//        artistView.promptDeleteArtist();
//        artistView.displayAllArtist();
//
//        artistView.promptArchiveArtist();
//        artistView.displayAllArtist();
//
//        artistView.promptRestoreArtist();
//        artistView.displayAllArtist();
//
//        artistView.promptSearchArtist();

    }
}

//          Repository
//             │
//             ▼
//          Service
//             │
//             ▼
//            View
//             │
//             ▼
//      Main Application
//
//Dependency Wiring:
//Repository --> Service --> View --> main()
