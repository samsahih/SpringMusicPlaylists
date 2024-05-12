package com.musicplaylists.demo.musicplaylists.unittests.services;

import com.musicplaylists.demo.musicplaylists.dtos.UserRegistrationDTO;
import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import com.musicplaylists.demo.musicplaylists.entities.Subscription;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.entities.UserType;
import com.musicplaylists.demo.musicplaylists.repositories.NormalUserRepository;
import com.musicplaylists.demo.musicplaylists.repositories.SubscriptionRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import com.musicplaylists.demo.musicplaylists.services.NormalUserService;
import com.musicplaylists.demo.musicplaylists.services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private NormalUserRepository normalUserRepository;

    @Mock
    private NormalUserService normalUserService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "testpassword";
        String confirmPassword = "testpassword"; // Matching password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;
        registrationDTO.userType = UserType.NORMAL;

        User mockedUser = new User(); // Create a mocked user object
        mockedUser.setId(1L); // Set a valid ID

        // Mock the behavior of userRepository.save(user) to return the mockedUser object
        when(userRepository.save(any(User.class))).thenReturn(mockedUser);

        // Act
        User user = userService.registerUser(registrationDTO);

        // Assert
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());

        if (user.getUserType() == UserType.NORMAL) {
            NormalUser normalUser = new NormalUser();
            normalUser.setId(1L); // Set a valid ID for the normalUser
            Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusMonths(1), true);
            normalUser.setSubscription(subscription);

            assertNotNull(normalUser.getSubscription());
            assertTrue(normalUser.getSubscription().isActive());
        }
    }

    @Test
    public void testRegisterUser_NoUsername_ThrowException() {
        // Arrange
        String username = "";
        String email = "testuser@example.com";
        String password = "testpassword";
        String confirmPassword = "testpassword"; // Matching password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(registrationDTO);

        // Act & Assert
        assertTrue(violations.stream().anyMatch(violation -> "Username is mandatory".equals(violation.getMessage())));
    }

    @Test
    public void testRegisterUser_NoEmail_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "";
        String password = "testpassword";
        String confirmPassword = "testpassword"; // Matching password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(registrationDTO);

        // Act & Assert
        assertTrue(violations.stream().anyMatch(violation -> "Email is mandatory".equals(violation.getMessage())));
    }

    @Test
    public void testRegisterUser_NoPassword_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "";
        String confirmPassword = ""; // Matching password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(registrationDTO);

        // Act & Assert
        assertTrue(violations.stream().anyMatch(violation -> "Password is mandatory".equals(violation.getMessage())));
    }

    @Test
    public void testRegisterUser_IncorrectEmailFormat_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "invalidemail"; // Invalid email format
        String password = "testpassword";
        String confirmPassword = "testpassword"; // Matching password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(registrationDTO);

        // Act & Assert
        assertTrue(violations.stream().anyMatch(violation -> "Invalid email format".equals(violation.getMessage())));
    }

    @Test
    public void testRegisterUser_PasswordsNotEqual_ThrowException() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "testpassword";
        String confirmPassword = "differentpassword"; // Different password for testing

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;

        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(registrationDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registrationDTO));
    }

    @Test
    public void testUpdateUserSubscriptionStatus_Success() {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "testpassword";
        String confirmPassword = "testpassword";
        boolean active = true;

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.username = username;
        registrationDTO.email = email;
        registrationDTO.password = password;
        registrationDTO.confirmPassword = confirmPassword;
        registrationDTO.userType = UserType.NORMAL;

        User mockedUser = new User(); // Create a mocked user object
        mockedUser.setId(1L); // Set a valid ID

        // Create a mocked NormalUser object
        NormalUser normalUser = new NormalUser();
        Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusMonths(1), !active); // Create a subscription with the opposite of the desired status
        normalUser.setSubscription(subscription);
        normalUser.setId(1L);

        // Set up the relationship between User and NormalUser
        mockedUser.setNormalUser(normalUser);
        normalUser.setUser(mockedUser);

        when(normalUserService.findNormalUserByUsername("testuser")).thenReturn(normalUser);
        // Act
        userService.updateUserSubscriptionStatus(username, active);

        // Assert
        assertTrue(normalUser.getSubscription().isActive() == active);
    }

    @Test
    public void testUpdateUserSubscriptionStatus_UserNotFound_ThrowException() {
        // Arrange
        String username = "nonexistentuser";
        boolean active = true; // Set the desired status

        // Configure Mockito to use lenient strictness
        Mockito.lenient().when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.updateUserSubscriptionStatus(username, active));
    }
}
