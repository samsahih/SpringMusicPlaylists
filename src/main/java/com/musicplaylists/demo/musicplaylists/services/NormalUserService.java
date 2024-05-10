package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.NormalUserRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalUserService {

    private final UserRepository userRepository;
    private final NormalUserRepository normalUserRepository;

    @Autowired
    public NormalUserService(UserRepository userRepository, NormalUserRepository normalUserRepository) {
        this.userRepository = userRepository;
        this.normalUserRepository = normalUserRepository;
    }

    @Transactional
    public NormalUser findNormalUserByUsername(String username) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the corresponding normal user
        NormalUser normalUser = normalUserRepository.findByUserId(user.getId());
        if (normalUser == null) {
            throw new IllegalArgumentException("Normal user not found");
        }

        return normalUser;
    }
}
