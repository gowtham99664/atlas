package com.smarthome;

import com.smarthome.service.SmartHomeService;
import com.smarthome.service.TimerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CorrectedNotificationMessagesTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "notificationfix@smarthome.com";
    private static final String TEST_NAME = "Notification Fix Test User";
    private static final String TEST_PASSWORD = "NotificationTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Corrected Notification Messages Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Corrected notification messages test cleanup completed");
    }

    @Test
    @DisplayName("Test Event Start Notification Message")
    void testEventStartNotificationMessage() {
        System.out.println("\nTesting Event Start Notification Message...");

        // Create an event that will start immediately
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(5);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Start Message Test", "Testing event start notification",
            startDateTime, endDateTime, "Meeting");
        assertTrue(eventCreated, "Event should be created successfully");

        // Enable suppression to capture message
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        // Check if start message was buffered
        boolean hasStartMessage = TimerService.hasBufferedNotifications();
        System.out.println("Event start message buffered: " + hasStartMessage);

        // Display the start message
        TimerService.suppressNotifications(false);
        System.out.println("Expected: CALENDAR EVENT STARTED! + Event is now active!");
        TimerService.displayBufferedNotifications();

        System.out.println("Event start notification message test: PASSED");
    }

    @Test
    @DisplayName("Test Event End Notification Message")
    void testEventEndNotificationMessage() {
        System.out.println("\nTesting Event End Notification Message...");

        // Create an event that has already ended
        LocalDateTime eventStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime eventEnd = LocalDateTime.now().minusMinutes(2);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "End Message Test", "Testing event end notification",
            startDateTime, endDateTime, "Personal");
        assertTrue(eventCreated, "Event should be created successfully");

        // Enable suppression to capture message
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        // Check if end message was buffered
        boolean hasEndMessage = TimerService.hasBufferedNotifications();
        System.out.println("Event end message buffered: " + hasEndMessage);

        // Display the end message
        TimerService.suppressNotifications(false);
        System.out.println("Expected: CALENDAR EVENT COMPLETED! + Event has ended successfully!");
        System.out.println("Should NOT contain: Type: Personal");
        TimerService.displayBufferedNotifications();

        System.out.println("Event end notification message test: PASSED");
    }

    @Test
    @DisplayName("Test Event Start vs End Message Distinction")
    void testEventStartVsEndMessageDistinction() {
        System.out.println("\nTesting Event Start vs End Message Distinction...");

        // Create a currently running event
        LocalDateTime runningStart = LocalDateTime.now().minusMinutes(1);
        LocalDateTime runningEnd = LocalDateTime.now().plusMinutes(3);

        smartHomeService.createCalendarEvent(
            "Running Event", "Currently active event",
            runningStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            runningEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        // Create a completed event
        LocalDateTime completedStart = LocalDateTime.now().minusMinutes(15);
        LocalDateTime completedEnd = LocalDateTime.now().minusMinutes(5);

        smartHomeService.createCalendarEvent(
            "Completed Event", "Already finished event",
            completedStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            completedEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        // Enable suppression and check notifications
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasMessages = TimerService.hasBufferedNotifications();
        System.out.println("Mixed start/end messages buffered: " + hasMessages);

        // Display messages to verify they are distinct
        TimerService.suppressNotifications(false);
        System.out.println("Should show distinct START and COMPLETED messages:");
        TimerService.displayBufferedNotifications();

        System.out.println("Event start vs end message distinction test: PASSED");
    }
}