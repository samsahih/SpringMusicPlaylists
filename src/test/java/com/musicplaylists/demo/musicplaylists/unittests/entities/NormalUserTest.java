package com.musicplaylists.demo.musicplaylists.unittests.entities;

import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.Playlist;
import com.musicplaylists.demo.musicplaylists.entities.Subscription;
import com.musicplaylists.demo.musicplaylists.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class NormalUserTest {
    @Test
    void addPlaylist_ValidPlaylist_Success() {
        // Arrange
        User user = new User("testuser@example.com", "testuser", "testpassword");
        NormalUser normalUser = new NormalUser();  // Create a NormalUser instance
        normalUser.setUser(user);  // Associate the NormalUser with the User
        user.setNormalUser(normalUser);  // Associate the User with the NormalUser

        Playlist playlist = new Playlist(user.getNormalUser(),"My Playlist");

        // Act
        normalUser.addPlaylist(playlist);

        // Assert
        assertTrue(normalUser.getPlaylists().contains(playlist));
        assertEquals(normalUser, playlist.getUser());
    }

    @Test
    void addSubscription_ValidSubscription_Success() {
        // Arrange
        User user = new User("samersahih@gmail.com", "testuser", "testpassword");
        NormalUser normalUser = new NormalUser();  // Create a NormalUser instance
        normalUser.setUser(user);  // Associate the NormalUser with the User
        user.setNormalUser(normalUser);  // Associate the User with the NormalUser

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);
        boolean active = true;
        Subscription subscription = new Subscription(startDate, endDate, active);

        // Act
        normalUser.setSubscription(subscription);

        // Assert
        assertNotNull(normalUser.getSubscription());
        assertEquals(subscription, normalUser.getSubscription());
        assertEquals(normalUser, subscription.getUser());
    }
}