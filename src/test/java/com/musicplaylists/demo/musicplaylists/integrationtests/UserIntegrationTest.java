/*package com.musicplaylists.demo.musicplaylists.integrationtests;

import com.musicplaylists.demo.musicplaylists.entities.Subscription;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.SubscriptionRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void testUserRegistrationCreatesSubscription() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "testpassword";

        // Act
        User user = new User(email, username, password);
        userRepository.save(user);

        // Assert
        assertNotNull(user.getId());
        Subscription subscription = subscriptionRepository.findByUser(user);
        assertNotNull(subscription);
        assertEquals(user, subscription.getUser());
        assertNotNull(subscription.getStartDate());
        assertNotNull(subscription.getEndDate());
        assertTrue(subscription.isActive());
    }
}*/