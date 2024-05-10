package com.musicplaylists.demo.musicplaylists.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public class SongCreationDTO {
    @NotEmpty(message = "Title is required")
    public String title;

    @NotEmpty(message = "Artist is required")
    public String artist;

    @PositiveOrZero(message = "Duration must be non-negative")
    public int duration;

    public String genre;
}