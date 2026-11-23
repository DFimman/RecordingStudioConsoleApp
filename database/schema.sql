-- Recording Studio CLI App — schema only (no account data or sample records)
-- Based on recording_studio_db (MySQL / MariaDB).
-- Intended for a new, empty database. This is not a migration script.

CREATE DATABASE IF NOT EXISTS `recording_studio_db`
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `recording_studio_db`;

CREATE TABLE `artists` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `is_archived` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `role` VARCHAR(10) NOT NULL DEFAULT 'USER',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_archived` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `albums` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(25) NOT NULL,
    `year` YEAR DEFAULT NULL,
    `artist_id` INT NOT NULL,
    `is_archived` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `fk_albums_artist_id` (`artist_id`),
    CONSTRAINT `fk_albums_artist_id` FOREIGN KEY (`artist_id`)
      REFERENCES `artists` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `songs` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(25) NOT NULL,
    `genre` VARCHAR(10) DEFAULT NULL,
    `length` VARCHAR(5) NOT NULL,
    `album_id` INT NOT NULL,
    `is_archived` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `fk_songs_album_id` (`album_id`),
    CONSTRAINT `fk_songs_album_id` FOREIGN KEY (`album_id`)
      REFERENCES `albums` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `playlists` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` INT NOT NULL,
    `is_archived` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `fk_playlists_user_id` (`user_id`),
    CONSTRAINT `fk_playlists_user_id` FOREIGN KEY (`user_id`)
      REFERENCES `users` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `playlist_songs` (
    `playlist_id` INT NOT NULL,
    `song_id` INT NOT NULL,
    PRIMARY KEY (`playlist_id`, `song_id`),
    KEY `fk_playlist_songs_song_id` (`song_id`),
    CONSTRAINT `fk_playlist_songs_playlist_id` FOREIGN KEY (`playlist_id`)
      REFERENCES `playlists` (`id`) ON UPDATE CASCADE,
    CONSTRAINT `fk_playlist_songs_song_id` FOREIGN KEY (`song_id`)
      REFERENCES `songs` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
