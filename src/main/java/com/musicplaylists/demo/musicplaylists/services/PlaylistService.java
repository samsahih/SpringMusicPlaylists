package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationDTO;
import com.musicplaylists.demo.musicplaylists.dtos.PlaylistUpdateDTO;
import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationResponseDTO;
import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.Playlist;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.PlaylistRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final NormalUserService normalUserService;
    private final UserRepository userRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, NormalUserService normalUserService, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.normalUserService = normalUserService;
        this.userRepository = userRepository;
    }

    @Transactional
    public PlaylistCreationResponseDTO createPlaylist(PlaylistCreationDTO playlistDTO) {
        // Find normal user
        NormalUser normalUser = normalUserService.findNormalUserByUsername(playlistDTO.username);
        if (normalUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check if a playlist with the same name and user ID already exists
        Playlist existingPlaylist = playlistRepository.findByNameAndUser(playlistDTO.playlistName, normalUser);
        if (existingPlaylist != null) {
            throw new IllegalArgumentException("Playlist already exists");
        }

        // Create a new playlist
        Playlist playlist = new Playlist(normalUser, playlistDTO.playlistName, playlistDTO.description);
        playlistRepository.save(playlist);

        // Create the response DTO
        PlaylistCreationResponseDTO responseDTO = new PlaylistCreationResponseDTO();
        responseDTO.name = playlist.getName();
        responseDTO.description = playlist.getDescription();

        return responseDTO;
    }

    @Transactional
    public void deletePlaylist(Long playlistId, String userName) {
        User requestUser = userRepository.findByUsername(userName);
        if(requestUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the playlist by id
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if the right user is used
        if (!Objects.equals(requestUser.getId(), playlist.getUser().getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        playlistRepository.delete(playlist);
    }

    @Transactional
    public List<Playlist> getUserPlaylists(String username) {
        // Find normal user
        NormalUser normalUser = normalUserService.findNormalUserByUsername(username);

        return new ArrayList<>(normalUser.getPlaylists());
    }
}