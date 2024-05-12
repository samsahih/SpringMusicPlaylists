package com.musicplaylists.demo.musicplaylists.controllers;

import com.musicplaylists.demo.musicplaylists.dtos.SubscriptionResponseDTO;
import com.musicplaylists.demo.musicplaylists.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final UserService userService;

    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<SubscriptionResponseDTO> getUserSubscription(@PathVariable String username) {
        SubscriptionResponseDTO subscription = userService.getUserSubscription(username);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAllSubscriptions() {
        List<SubscriptionResponseDTO> subscriptions = userService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUserSubscriptionStatus(@PathVariable String username, @RequestParam boolean active) {
        userService.updateUserSubscriptionStatus(username, active);
        return ResponseEntity.noContent().build();
    }
}