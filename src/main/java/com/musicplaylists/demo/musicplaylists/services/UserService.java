package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.SubscriptionResponseDTO;
import com.musicplaylists.demo.musicplaylists.dtos.UserRegistrationDTO;
import com.musicplaylists.demo.musicplaylists.dtos.UserResponseDTO;
import com.musicplaylists.demo.musicplaylists.entities.*;
import com.musicplaylists.demo.musicplaylists.repositories.AdminUserRepository;
import com.musicplaylists.demo.musicplaylists.repositories.NormalUserRepository;
import com.musicplaylists.demo.musicplaylists.repositories.SubscriptionRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NormalUserRepository normalUserRepository;
    private final AdminUserRepository adminUserRepository;
    private final NormalUserService normalUserService;

    @Autowired
    public UserService(UserRepository userRepository,
                       SubscriptionRepository subscriptionRepository,
                       NormalUserService normalUserService,
                       NormalUserRepository NormalUserRepository,
                       AdminUserRepository adminUserRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.normalUserService = normalUserService;
        this.normalUserRepository = NormalUserRepository;
        this.adminUserRepository = adminUserRepository;
    }

    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Validate password and confirmPassword
        if (!registrationDTO.password.equals(registrationDTO.confirmPassword)) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // Create a new user
        User user = new User(registrationDTO.email, registrationDTO.username, registrationDTO.password, registrationDTO.userType);
        userRepository.save(user);

        // If the user type is NormalUser, add a subscription
        if (registrationDTO.userType == UserType.NORMAL) {
            NormalUser normalUser = new NormalUser();
            normalUser.setUser(user);
            normalUserRepository.save(normalUser);

            // Create a subscription for the user
            Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusMonths(1), true);
            subscription.setUser(normalUser);
            subscriptionRepository.save(subscription);
        }
        else
        {
            AdminUser adminUser = new AdminUser();
            adminUser.setUser(user);
            adminUserRepository.save(adminUser);
        }

        return user;
    }

    @Transactional
    public void deleteUser(String username) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Delete the user
        userRepository.delete(user);
    }

    @Transactional
    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return StreamSupport.stream(subscriptionRepository.findAll().spliterator(), false)
                .map(this::mapSubscriptionToDTO)
                .toList();
    }

    @Transactional
    public SubscriptionResponseDTO getUserSubscription(String username) {
        NormalUser normalUser = normalUserService.findNormalUserByUsername(username);
        Subscription subscription = normalUser.getSubscription();
        return mapSubscriptionToDTO(subscription);
    }

    private SubscriptionResponseDTO mapSubscriptionToDTO(Subscription subscription) {
        SubscriptionResponseDTO SubscriptionResponseDTO = new SubscriptionResponseDTO();
        SubscriptionResponseDTO.startDate = subscription.getStartDate();
        SubscriptionResponseDTO.endDate = subscription.getEndDate();
        SubscriptionResponseDTO.active = subscription.isActive();
        SubscriptionResponseDTO.user = mapNormalUserToDTO(subscription.getUser());
        return SubscriptionResponseDTO;
    }

    private UserResponseDTO mapNormalUserToDTO(NormalUser user) {
        UserResponseDTO userDTO = new UserResponseDTO();

        // Map user properties to DTO
        User parentUser = user.getUser();
        userDTO.email = parentUser.getEmail();
        userDTO.username = parentUser.getUsername();

        return userDTO;
    }

    @Transactional
    public void updateUserSubscriptionStatus(String username, boolean active) {
        // Find the NormalUser by username
        NormalUser normalUser = normalUserService.findNormalUserByUsername(username);

        // Get the subscription associated with the NormalUser
        Subscription subscription = normalUser.getSubscription();
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription not found");
        }

        // Update the subscription status
        subscription.setActive(active);
        subscriptionRepository.save(subscription);
    }

    public boolean checkIfUserActive(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        NormalUser normalUser = normalUserService.findNormalUserByUsername(username);
        return user != null && normalUser.getSubscription().isActive();
    }
}