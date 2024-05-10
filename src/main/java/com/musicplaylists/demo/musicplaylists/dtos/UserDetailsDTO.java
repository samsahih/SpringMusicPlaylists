package com.musicplaylists.demo.musicplaylists.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDetailsDTO {

    @NotEmpty(message = "Username is mandatory")
    public String username;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    public String email;

    @NotEmpty(message = "Password is mandatory")
    public String password;
}