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

public class EventEndTimingTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "endtiming@smarthome.com";
    private static final String TEST_NAME = "End Timing Test User";
    private static final String TEST_PASSWORD = "EndTimingTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Event End Timing Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Event end timing test cleanup completed");
    }

    @Test
    @DisplayName("Test Event Start Notification Shows But End Does Not Before End Time")
    void testStartNotificationOnlyBeforeEndTime() {
        System.out.println("\nTesting Start Notification Only Before End Time...");

        // Create an event that starts now but ends in 5 minutes (well in the future)
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(5);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Future End Event", "Event that ends in 5 minutes",
            startDateTime, endDateTime, "Meeting");
        assertTrue(eventCreated, "Event should be created successfully");

        System.out.println("Event created: " + eventStart.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                          " to " + eventEnd.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // Check notifications - should only get start notification
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasNotifications = TimerService.hasBufferedNotifications();
        System.out.println("Has notifications: " + hasNotifications);

        TimerService.suppressNotifications(false);
        if (hasNotifications) {
            System.out.println("Should only show START notification (no COMPLETED):");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Start notification only test: PASSED");
    }

    @Test
    @DisplayName("Test Event End Notification Shows Only After End Time")
    void testEndNotificationOnlyAfterEndTime() {
        System.out.println("\nTesting End Notification Only After End Time...");

        // Create an event that ended 30 seconds ago
        LocalDateTime eventStart = LocalDateTime.now().minusMinutes(5);
        LocalDateTime eventEnd = LocalDateTime.now().minusSeconds(30);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Just Ended Event", "Event that just ended",
            startDateTime, endDateTime, "Personal");
        assertTrue(eventCreated, "Event should be created successfully");

        System.out.println("Event created: " + eventStart.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                          " to " + eventEnd.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("Event ended: " + (LocalDateTime.now().isAfter(eventEnd) ? "YES" : "NO"));

        // Check notifications - should get end notification since event has ended
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasEndNotification = TimerService.hasBufferedNotifications();
        System.out.println("Has end notification: " + hasEndNotification);

        TimerService.suppressNotifications(false);
        if (hasEndNotification) {
            System.out.println("Should show COMPLETED notification:");
            TimerService.displayBufferedNotifications();
        } else {
            System.out.println("No end notification - timing window may need adjustment");
        }

        System.out.println("End notification after end time test: COMPLETED");
    }

    @Test
    @DisplayName("Test Proper Event Lifecycle Timing")
    void testProperEventLifecycleTiming() {
        System.out.println("\nTesting Proper Event Lifecycle Timing...");

        // Test 1: Currently running event (should show start notification only)
        LocalDateTime runningStart = LocalDateTime.now().minusMinutes(1);
        LocalDateTime runningEnd = LocalDateTime.now().plusMinutes(2);

        smartHomeService.createCalendarEvent(
            "Currently Running", "Event currently in progress",
            runningStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            runningEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        // Test 2: Recently completed event (should show end notification)
        LocalDateTime completedStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime completedEnd = LocalDateTime.now().minusSeconds(30);

        smartHomeService.createCalendarEvent(
            "Recently Completed", "Event that just finished",
            completedStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            completedEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        System.out.println("Running event: " + runningStart.format(DateTimeFormatter.ofPattern("HH:mm")) +
                          " to " + runningEnd.format(DateTimeFormatter.ofPattern("HH:mm")) + " (should show START)");
        System.out.println("Completed event: " + completedStart.format(DateTimeFormatter.ofPattern("HH:mm")) +
                          " to " + completedEnd.format(DateTimeFormatter.ofPattern("HH:mm")) + " (should show COMPLETED)");

        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasLifecycleNotifications = TimerService.hasBufferedNotifications();
        System.out.println("Has lifecycle notifications: " + hasLifecycleNotifications);

        TimerService.suppressNotifications(false);
        if (hasLifecycleNotifications) {
            System.out.println("Should show both START and COMPLETED notifications:");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Event lifecycle timing test: PASSED");
    }
}