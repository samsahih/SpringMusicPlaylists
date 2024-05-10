package com.musicplaylists.demo.musicplaylists.entities;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    void createSong_ValidData_Success() {
        // Arrange
        String title = "Song Title";

        // Act
        Song song = new Song();
        song.setTitle(title);

        // Assert
        assertNotNull(song);
        assertEquals(title, song.getTitle());
    }

    @Test
    void addSongToPlaylist_ValidSong_Success() {
        // Arrange
        Playlist playlist = new Playlist();
        playlist.setName("My Playlist");
        Song song = new Song();
        song.setTitle("Song Title");

        // Act
        playlist.addSong(song);

        // Assert
        assertTrue(playlist.getSongs().contains(song));
        assertTrue(song.getPlaylists().contains(playlist));
    }
}