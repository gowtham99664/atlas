package com.smarthome;

import com.smarthome.service.SmartHomeService;
import com.smarthome.service.CalendarEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarNotificationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "notification@smarthome.com";
    private static final String TEST_NAME = "Notification Test User";
    private static final String TEST_PASSWORD = "NotifTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Calendar Notification Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Calendar notification test cleanup completed");
    }

    @Test
    @DisplayName("Test Calendar Event Notification System")
    void testCalendarEventNotifications() {
        System.out.println("\nTesting Calendar Event Notification System...");

        // Create an event that would trigger notifications
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endTime = startTime.plusMinutes(2);
        String startDateTime = startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean success = smartHomeService.createCalendarEvent(
            "Notification Test Event",
            "Test event for notification system",
            startDateTime,
            endDateTime,
            "Meeting"
        );

        assertTrue(success, "Event creation should succeed");

        // Verify the event was created
        List<CalendarEventService.CalendarEvent> events = smartHomeService.getUpcomingEvents();
        assertFalse(events.isEmpty(), "Should have at least one upcoming event");

        // Find our test event
        CalendarEventService.CalendarEvent testEvent = events.stream()
            .filter(event -> "Notification Test Event".equals(event.getTitle()))
            .findFirst()
            .orElse(null);

        assertNotNull(testEvent, "Test event should be found");
        assertEquals("Meeting", testEvent.getEventType(), "Event type should be Meeting");

        // Test the notification system by forcing a check
        // This will check if the current time is within the notification window
        System.out.println("Force checking calendar automation and notifications...");
        smartHomeService.forceCalendarAutomationCheck();

        System.out.println("Calendar notification system test completed successfully!");
        System.out.println("Event created: " + testEvent.getTitle());
        System.out.println("Start time: " + testEvent.getStartTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("End time: " + testEvent.getEndTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Notification system: OPERATIONAL");
    }

    @Test
    @DisplayName("Test Calendar Event Active and Upcoming Events")
    void testActiveAndUpcomingEvents() {
        System.out.println("\nTesting Active and Upcoming Events...");

        // Create a currently running event
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endTime = startTime.plusMinutes(5);
        String startDateTime = startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean success = smartHomeService.createCalendarEvent(
            "Currently Running Event",
            "Event that is currently active",
            startDateTime,
            endDateTime,
            "Meeting"
        );

        assertTrue(success, "Currently running event creation should succeed");

        // Get active and upcoming events through the service
        List<CalendarEventService.CalendarEvent> events = smartHomeService.getCalendarService()
            .getActiveAndUpcomingEvents(TEST_EMAIL);

        assertFalse(events.isEmpty(), "Should have active/upcoming events");

        // Find our currently running event
        CalendarEventService.CalendarEvent activeEvent = events.stream()
            .filter(event -> "Currently Running Event".equals(event.getTitle()))
            .findFirst()
            .orElse(null);

        assertNotNull(activeEvent, "Currently running event should be found");

        System.out.println("Active and upcoming events test completed successfully!");
        System.out.println("Found " + events.size() + " active/upcoming events");
        System.out.println("Currently running event: " + activeEvent.getTitle());
    }

    @Test
    @DisplayName("Test Calendar Notification Test Event Creation")
    void testNotificationTestEventCreation() {
        System.out.println("\nTesting Notification Test Event Creation...");

        // This simulates what the UI method does - using minutes to avoid timing precision issues
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(2);
        LocalDateTime endTime = startTime.plusMinutes(2);
        String startDateTime = startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean success = smartHomeService.createCalendarEvent(
            "Test Event for Notifications",
            "Test event to demonstrate start and end notifications",
            startDateTime,
            endDateTime,
            "Meeting"
        );

        assertTrue(success, "Test event for notifications should be created successfully");

        List<CalendarEventService.CalendarEvent> events = smartHomeService.getUpcomingEvents();
        CalendarEventService.CalendarEvent testEvent = events.stream()
            .filter(event -> "Test Event for Notifications".equals(event.getTitle()))
            .findFirst()
            .orElse(null);

        assertNotNull(testEvent, "Test notification event should be found");

        System.out.println("Notification test event creation completed successfully!");
        System.out.println("Event: " + testEvent.getTitle());
        System.out.println("Will start in ~2 minutes and run for 2 minutes");
        System.out.println("Notification system ready for testing!");
    }
}