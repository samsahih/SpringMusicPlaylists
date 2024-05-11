package com.musicplaylists.demo.musicplaylists.unittests.entities;

import com.musicplaylists.demo.musicplaylists.entities.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void createUser_ValidData_Success() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "testpassword";

        // Act
        User user = new User(email, username, password);

        // Assert
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
    }

    @Test
    void createUser_InvalidEmail_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "invalidemail"; // Invalid email format
        String password = "testpassword";

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> new User(email, username, password));
    }

    @Test
    void createUser_NoPassword_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = null; // Password not provided

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new User(email, username, password));
    }

}