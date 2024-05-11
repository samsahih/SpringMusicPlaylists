package com.musicplaylists.demo.musicplaylists.unittests.entities;

import com.musicplaylists.demo.musicplaylists.entities.Subscription;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    @Test
    void createSubscription_ValidData_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);
        boolean active = true;

        // Act
        Subscription subscription = new Subscription(startDate, endDate, active);

        // Assert
        assertNotNull(subscription);
        assertEquals(startDate, subscription.getStartDate());
        assertEquals(endDate, subscription.getEndDate());
        assertEquals(active, subscription.isActive());
    }

    @Test
    void createSubscription_ActiveStatus_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);
        boolean active = true;

        // Act
        Subscription subscription = new Subscription(startDate, endDate, active);

        // Assert
        assertTrue(subscription.isActive());
    }

    @Test
    void createSubscription_InactiveStatus_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusMonths(1); // End date in the past
        boolean active = false;

        // Act
        Subscription subscription = new Subscription(startDate, endDate, active);

        // Assert
        assertFalse(subscription.isActive());
    }
}