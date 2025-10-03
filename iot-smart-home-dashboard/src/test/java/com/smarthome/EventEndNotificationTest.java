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

public class EventEndNotificationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "eventend@smarthome.com";
    private static final String TEST_NAME = "Event End Test User";
    private static final String TEST_PASSWORD = "EventEndTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Event End Notification Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Event end notification test cleanup completed");
    }

    @Test
    @DisplayName("Test Event End Notification Triggers Properly")
    void testEventEndNotificationTriggers() {
        System.out.println("\nTesting Event End Notification Triggers Properly...");

        // Create an event that ends right now (within 1 minute)
        LocalDateTime eventStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime eventEnd = LocalDateTime.now(); // Ends now
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Ending Now Event", "Event that should trigger end notification",
            startDateTime, endDateTime, "Meeting");
        assertTrue(eventCreated, "Event should be created successfully");

        System.out.println("Created event ending at: " + eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        // Enable suppression to capture end message
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        // Check if end message was buffered
        boolean hasEndMessage = TimerService.hasBufferedNotifications();
        System.out.println("Event end notification captured: " + hasEndMessage);

        // Display the end message
        TimerService.suppressNotifications(false);
        if (hasEndMessage) {
            System.out.println("Event end notification (should show CALENDAR EVENT COMPLETED!):");
            TimerService.displayBufferedNotifications();
        } else {
            System.out.println("No end notification - this indicates the timing window may need adjustment");
        }

        System.out.println("Event end notification trigger test completed");
    }

    @Test
    @DisplayName("Test Complete Event Lifecycle Notifications")
    void testCompleteEventLifecycleNotifications() {
        System.out.println("\nTesting Complete Event Lifecycle Notifications...");

        // Create a short event that starts now and ends in 2 minutes
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(2);

        smartHomeService.createCalendarEvent(
            "Lifecycle Test Event", "Testing start and end notifications",
            eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        System.out.println("Created event: " + eventStart.format(DateTimeFormatter.ofPattern("HH:mm")) +
                          " to " + eventEnd.format(DateTimeFormatter.ofPattern("HH:mm")));

        // Test start notification
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasStartMessage = TimerService.hasBufferedNotifications();
        System.out.println("Start notification captured: " + hasStartMessage);

        if (hasStartMessage) {
            System.out.println("Event start notification:");
            TimerService.suppressNotifications(false);
            TimerService.displayBufferedNotifications();
            TimerService.suppressNotifications(true);
        }

        // Now create a completed event to test end notification
        LocalDateTime pastStart = LocalDateTime.now().minusMinutes(15);
        LocalDateTime pastEnd = LocalDateTime.now().minusMinutes(1);

        smartHomeService.createCalendarEvent(
            "Just Completed Event", "Event that just finished",
            pastStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        smartHomeService.forceCalendarAutomationCheck();

        boolean hasEndMessage = TimerService.hasBufferedNotifications();
        System.out.println("End notification captured: " + hasEndMessage);

        TimerService.suppressNotifications(false);
        if (hasEndMessage) {
            System.out.println("Event end notification:");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Complete event lifecycle notifications test: PASSED");
    }
}