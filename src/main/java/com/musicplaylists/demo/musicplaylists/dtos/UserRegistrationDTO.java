package com.musicplaylists.demo.musicplaylists.dtos;

import com.musicplaylists.demo.musicplaylists.entities.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserRegistrationDTO {

    @NotEmpty(message = "Username is mandatory")
    public String username;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    public String email;

    @NotEmpty(message = "Password is mandatory")
    public String password;

    @NotEmpty(message = "Confirm password is mandatory")
    public String confirmPassword;

    @Enumerated(EnumType.STRING)
    public UserType userType;
}