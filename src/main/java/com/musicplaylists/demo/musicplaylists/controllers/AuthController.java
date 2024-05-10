package com.musicplaylists.demo.musicplaylists.controllers;

import com.musicplaylists.demo.musicplaylists.dtos.UserLoginDTO;
import com.musicplaylists.demo.musicplaylists.dtos.UserRegistrationDTO;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.services.ProfileUtils;
import com.musicplaylists.demo.musicplaylists.services.UserAuthService;
import com.musicplaylists.demo.musicplaylists.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.musicplaylists.demo.musicplaylists.services.ValidationUtils.handleValidationResult;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final ProfileUtils profileUtils;

    @Autowired
    public AuthController(UserAuthService userAuthService, UserService userService, ProfileUtils profileUtils) {
        this.userAuthService = userAuthService;
        this.userService = userService;
        this.profileUtils = profileUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO loginReq, BindingResult bindingResult)  {
        return handleValidationResult(bindingResult, () -> ResponseEntity.ok(userAuthService.login(loginReq)), profileUtils);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDTO registrationDTO, BindingResult bindingResult) {
        return handleValidationResult(bindingResult, () -> {
            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }, profileUtils);
    }
}
