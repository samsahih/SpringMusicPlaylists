package com.musicplaylists.demo.musicplaylists.dtos;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDTO {
    @NotEmpty(message = "Username is mandatory")
    public String username;

    @NotEmpty(message = "Password is mandatory")
    public String password;

    public UserLoginDTO(String email, String password) {
        this.username = email;
        this.password = password;
    }
}
