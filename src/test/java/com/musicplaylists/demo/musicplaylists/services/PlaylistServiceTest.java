package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationDTO;
import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationResponseDTO;
import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.Playlist;
import com.musicplaylists.demo.musicplaylists.repositories.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private NormalUserService normalUserService;

    @InjectMocks
    private PlaylistService playlistService;

    @Test
    public void testCreatePlaylist_Success() {
        // Arrange
        PlaylistCreationDTO playlistDTO = new PlaylistCreationDTO();
        playlistDTO.username = "testuser";
        playlistDTO.playlistName = "Test Playlist";

        NormalUser normalUser = new NormalUser();
        when(normalUserService.findNormalUserByUsername("testuser")).thenReturn(normalUser);

        Playlist playlist = new Playlist(normalUser, "Test Playlist");
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        // Act
        PlaylistCreationResponseDTO createdPlaylist = playlistService.createPlaylist(playlistDTO);

        // Assert
        assertNotNull(createdPlaylist);
        assertEquals("Test Playlist", createdPlaylist.name);
    }
}