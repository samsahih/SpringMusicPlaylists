package com.musicplaylists.demo.musicplaylists.dtos;

import java.time.LocalDate;

public class SubscriptionResponseDTO {

    public LocalDate startDate;

    public LocalDate endDate;

    public boolean active;

    public UserResponseDTO user;
}
