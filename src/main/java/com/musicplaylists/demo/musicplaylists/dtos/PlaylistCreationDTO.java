package com.musicplaylists.demo.musicplaylists.dtos;

import jakarta.validation.constraints.NotEmpty;

public class PlaylistCreationDTO {
    @NotEmpty(message = "Username is mandatory")
    public String username;

    @NotEmpty(message = "Playlist name is mandatory")
    public String playlistName;

    public String description;
}