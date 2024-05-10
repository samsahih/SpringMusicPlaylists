package com.musicplaylists.demo.musicplaylists.dtos;

import jakarta.validation.constraints.NotEmpty;

public class PlaylistUpdateDTO {
    @NotEmpty(message = "New playlist name is mandatory")
    public String newName;

    public String newDescription;
}