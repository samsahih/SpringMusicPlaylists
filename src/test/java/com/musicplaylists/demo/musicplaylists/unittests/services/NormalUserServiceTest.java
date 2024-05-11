package com.musicplaylists.demo.musicplaylists.unittests.services;

import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.NormalUserRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import com.musicplaylists.demo.musicplaylists.services.NormalUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NormalUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NormalUserRepository normalUserRepository;

    @InjectMocks
    private NormalUserService normalUserService;

    @Test
    public void testFindNormalUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> normalUserService.findNormalUserByUsername(username));
        verify(normalUserRepository, never()).findByUserId(anyLong());
    }

    @Test
    public void testFindNormalUserByUsername_NormalUserNotFound() {
        // Arrange
        String username = "existinguser";
        User user = mock(User.class); // Mocking the User object
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(user.getId()).thenReturn(1L); // Mocking the behavior of getId method
        when(normalUserRepository.findByUserId(user.getId())).thenReturn(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> normalUserService.findNormalUserByUsername(username));
    }

    @Test
    public void testFindNormalUserByUsername_Success() {
        // Arrange
        String username = "existinguser";

        // Mocking the User object
        User user = mock(User.class);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Mocking the behavior of getId method to return a specific ID
        when(user.getId()).thenReturn(1L);

        // Mocking the NormalUser object
        NormalUser normalUser = mock(NormalUser.class);
        when(normalUserRepository.findByUserId(user.getId())).thenReturn(normalUser);

        // Act
        NormalUser result = normalUserService.findNormalUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(normalUser, result);
    }
}