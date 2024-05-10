package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.UserLoginDTO;
import com.musicplaylists.demo.musicplaylists.dtos.UserLoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserAuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public UserLoginResponseDTO login(UserLoginDTO loginReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.username, loginReq.password)
        );
        if (authentication.isAuthenticated()) {
            return new UserLoginResponseDTO(loginReq.username, jwtUtil.generateToken(loginReq.username));
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
