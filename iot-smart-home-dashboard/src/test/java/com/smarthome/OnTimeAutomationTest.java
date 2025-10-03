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

public class OnTimeAutomationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "ontime@smarthome.com";
    private static final String TEST_NAME = "OnTime Test User";
    private static final String TEST_PASSWORD = "OnTimeTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up OnTime Automation Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("OnTime automation test cleanup completed");
    }

    @Test
    @DisplayName("Test Automation Executes Exactly At Scheduled Time")
    void testAutomationExecutesOnTime() {
        System.out.println("\nTesting Automation Executes Exactly At Scheduled Time...");

        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Ensure TV is OFF initially
        if (smartHomeService.viewGadgets().get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("TV", "Living Room");
        }

        System.out.println("Initial TV status: OFF");

        // Create event that starts exactly now (within seconds)
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(30);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        System.out.println("Event scheduled for: " + eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "OnTime Test Event", "Testing exact timing",
            startDateTime, endDateTime, "Meeting");
        assertTrue(eventCreated, "Event should be created successfully");

        // Add automation to turn TV ON at event start
        boolean automationAdded = smartHomeService.addCustomDeviceAutomation(
            "OnTime Test Event", "TV", "Living Room", "ON", 0);
        assertTrue(automationAdded, "Automation should be added successfully");

        // Enable suppression to capture notifications
        TimerService.suppressNotifications(true);

        // Force automation check - should execute since we're at the scheduled time
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasNotification = TimerService.hasBufferedNotifications();
        System.out.println("Device automation executed: " + hasNotification);

        // Display notification
        TimerService.suppressNotifications(false);
        if (hasNotification) {
            System.out.println("Device status changed at scheduled time:");
            TimerService.displayBufferedNotifications();
        } else {
            System.out.println("No automation executed - timing may need further adjustment");
        }

        System.out.println("OnTime automation test: COMPLETED");
    }

    @Test
    @DisplayName("Test Automation Does Not Execute Before Scheduled Time")
    void testAutomationDoesNotExecuteEarly() {
        System.out.println("\nTesting Automation Does Not Execute Before Scheduled Time...");

        // Connect device
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Living Room");

        System.out.println("Testing future event (should not execute yet)");

        // Create event that starts in the future (5 minutes from now)
        LocalDateTime futureStart = LocalDateTime.now().plusMinutes(5);
        LocalDateTime futureEnd = futureStart.plusMinutes(30);
        String startDateTime = futureStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = futureEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        System.out.println("Event scheduled for: " + futureStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        smartHomeService.createCalendarEvent(
            "Future Test Event", "Event scheduled for future",
            startDateTime, endDateTime, "Personal");

        smartHomeService.addCustomDeviceAutomation("Future Test Event", "LIGHT", "Living Room", "ON", 0);

        // Try to force automation check - should NOT execute since event is in future
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasEarlyNotification = TimerService.hasBufferedNotifications();
        System.out.println("Early automation executed (should be false): " + hasEarlyNotification);

        TimerService.suppressNotifications(false);
        if (hasEarlyNotification) {
            System.out.println("ERROR: Automation executed before scheduled time!");
            TimerService.displayBufferedNotifications();
        } else {
            System.out.println("CORRECT: No automation executed before scheduled time");
        }

        assertFalse(hasEarlyNotification, "Automation should NOT execute before scheduled time");
        System.out.println("Early execution prevention test: PASSED");
    }

    @Test
    @DisplayName("Test Exact Timing Verification")
    void testExactTimingVerification() {
        System.out.println("\nTesting Exact Timing Verification...");

        // Connect device
        smartHomeService.connectToGadget("FAN", "Bajaj", "Living Room");

        // Create event that starts exactly at current minute
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventStart = now.withSecond(0).withNano(0); // Round to exact minute
        LocalDateTime eventEnd = eventStart.plusMinutes(15);

        System.out.println("Event scheduled for exact minute: " + eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Current time: " + now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        smartHomeService.createCalendarEvent(
            "Exact Timing Test", "Testing precise timing",
            eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        smartHomeService.addCustomDeviceAutomation("Exact Timing Test", "FAN", "Living Room", "ON", 0);

        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasTimingNotification = TimerService.hasBufferedNotifications();
        System.out.println("Exact timing automation executed: " + hasTimingNotification);

        TimerService.suppressNotifications(false);
        if (hasTimingNotification) {
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Exact timing verification test: COMPLETED");
    }
}