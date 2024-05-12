package com.musicplaylists.demo.musicplaylists.controllers;

import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationDTO;
import com.musicplaylists.demo.musicplaylists.dtos.PlaylistCreationResponseDTO;
import com.musicplaylists.demo.musicplaylists.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final SongService songService;
    private final ProfileUtils profileUtils;
    private final UserService userService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, SongService songService, ProfileUtils profileUtils, UserService userService) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.profileUtils = profileUtils;
        this.userService = userService;
    }

    @Operation(summary = "Create a new playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation errors occurred")
    })
    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody @Valid PlaylistCreationDTO playlistDTO, BindingResult bindingResult, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ValidationUtils.handleValidationResult(bindingResult, () -> {
            PlaylistCreationResponseDTO responseDTO = playlistService.createPlaylist(playlistDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }, profileUtils);
    }

    @Operation(summary = "Add a song to specified playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song added successfully"),
            @ApiResponse(responseCode = "500", description = "Playlist or song was not found")
    })
    @PostMapping("/song/{songId}")
    public ResponseEntity<?> addSongToPlaylists(@PathVariable Long songId, @RequestBody List<Long> playlistIds, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        songService.addSongToPlaylists(songId, playlistIds, authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete a playlist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Playlist not found")
    })
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable @Valid Long playlistId, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        playlistService.deletePlaylist(playlistId, authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Remove a song from specified playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song removed successfully"),
            @ApiResponse(responseCode = "500", description = "Playlist or song not found")
    })
    @DeleteMapping("/song/{songId}")
    public ResponseEntity<?> removeSongFromPlaylists(@PathVariable Long songId, @RequestBody List<Long> playlistIds, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        songService.removeSongFromPlaylists(songId, playlistIds, authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}