package com.musicplaylists.demo.musicplaylists.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    @Test
    void createPlaylist_ValidData_Success() {
        // Arrange
        String name = "My Playlist";

        // Act
        Playlist playlist = new Playlist(null, name);

        // Assert
        assertNotNull(playlist);
        assertEquals(name, playlist.getName());
    }

    @Test
    void addSongToPlaylist_ValidSong_Success() {
        // Arrange
        Playlist playlist = new Playlist(null, "My Playlist");
        Song song = new Song();

        // Act
        playlist.addSong(song);

        // Assert
        assertTrue(playlist.getSongs().contains(song));
        assertTrue(song.getPlaylists().contains(playlist));
    }

    @Test
    void setUserToPlaylist_ValidUser_Success() {
        // Arrange
        User user = new User("testuser@example.com", "testuser", "testpassword");
        user.setId(1L);
        NormalUser normalUser = new NormalUser();
        normalUser.setId(1L);
        normalUser.setUser(user);

        Playlist playlist = new Playlist(null, "My Playlist");

        // Act
        playlist.setUser(normalUser);

        // Assert
        assertEquals(normalUser, playlist.getUser());
        assertTrue(normalUser.getPlaylists().contains(playlist));
    }
}