package com.musicplaylists.demo.musicplaylists.controllers;

import com.musicplaylists.demo.musicplaylists.dtos.SongCreationDTO;
import com.musicplaylists.demo.musicplaylists.entities.Song;
import com.musicplaylists.demo.musicplaylists.services.ProfileUtils;
import com.musicplaylists.demo.musicplaylists.services.SongService;
import com.musicplaylists.demo.musicplaylists.services.UserService;
import com.musicplaylists.demo.musicplaylists.services.ValidationUtils;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;
    private final ProfileUtils profileUtils;
    private final UserService userService;

    @Autowired
    public SongController(SongService songService, ProfileUtils profileUtils, UserService userService) {
        this.songService = songService;
        this.profileUtils = profileUtils;
        this.userService = userService;
    }

    @Operation(summary = "Create a new song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Song created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation errors occurred")
    })
    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody @Valid SongCreationDTO songDTO, BindingResult bindingResult, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ValidationUtils.handleValidationResult(bindingResult, () -> {
            Song song = songService.createSong(songDTO);
            return new ResponseEntity<>(song, HttpStatus.CREATED);
        }, profileUtils);
    }

    @Operation(summary = "Delete a song by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Song not found")
    })
    @DeleteMapping("/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable Long songId, Authentication authentication) {

        if (!userService.checkIfUserActive(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        songService.deleteSong(songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}