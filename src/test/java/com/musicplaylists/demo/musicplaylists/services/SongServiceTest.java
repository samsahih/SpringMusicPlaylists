package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.SongCreationDTO;
import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.Playlist;
import com.musicplaylists.demo.musicplaylists.entities.Song;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.PlaylistRepository;
import com.musicplaylists.demo.musicplaylists.repositories.SongRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class SongServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SongRepository songRepository;

    @MockBean
    private PlaylistRepository playlistRepository;

    @Test
    public void testCreateSong_Success() {
        // Arrange
        SongCreationDTO songDTO = new SongCreationDTO();
        songDTO.title = "Test Song";
        songDTO.artist = "Test Artist";
        songDTO.duration = 180;
        songDTO.genre = "Test Genre";

        when(songRepository.findByTitleAndArtist("Test Song", "Test Artist")).thenReturn(null);

        Song song = new Song();
        when(songRepository.save(any(Song.class))).thenReturn(song);

        SongService songService = new SongService(songRepository, playlistRepository, userRepository);

        // Act
        Song createdSong = songService.createSong(songDTO);

        // Assert
        assertNotNull(createdSong);
        assertEquals("Test Song", createdSong.getTitle());
        assertEquals("Test Artist", createdSong.getArtist());
        assertEquals(180, createdSong.getDuration());
        assertEquals("Test Genre", createdSong.getGenre());
        assertTrue(createdSong.getPlaylists().isEmpty());
    }

    @Test
    public void testAddSongToPlaylists_Success() {
        // Arrange
        Long songId = 1L;
        Long playlistId = 1L;
        String userName = "testUser";

        // Create a User instance with a valid ID
        User user = new User();
        user.setId(1L); // Set a valid ID for the User
        user.setUsername(userName);

        NormalUser normalUser = new NormalUser();
        normalUser.setId(1L);
        normalUser.setUser(user);

        Song song = new Song();
        Playlist playlist = new Playlist(user.getNormalUser(), "Test Playlist");
        playlist.setUser(normalUser);

        when(userRepository.findByUsername(userName)).thenReturn(user);
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        SongService songService = new SongService(songRepository, playlistRepository, userRepository);

        // Act
        songService.addSongToPlaylists(songId, Collections.singletonList(playlistId), userName);

        // Assert
        assertTrue(playlist.getSongs().contains(song));
    }

    @Test
    public void testRemoveSongFromPlaylists_Success() {
        // Arrange
        Long songId = 1L;
        Long playlistId = 1L;
        String userName = "testUser";

        User user = new User();
        user.setId(1L);
        user.setUsername(userName);

        NormalUser normalUser = new NormalUser();
        normalUser.setId(1L);
        normalUser.setUser(user);

        Song song = new Song();
        Playlist playlist = new Playlist(user.getNormalUser(), "Test Playlist");
        playlist.setUser(normalUser);
        playlist.addSong(song);

        when(userRepository.findByUsername(userName)).thenReturn(user);
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        SongService songService = new SongService(songRepository, playlistRepository, userRepository);

        // Act
        songService.removeSongFromPlaylists(songId, Collections.singletonList(playlistId), userName);

        // Assert
        assertFalse(playlist.getSongs().contains(song));
    }
}