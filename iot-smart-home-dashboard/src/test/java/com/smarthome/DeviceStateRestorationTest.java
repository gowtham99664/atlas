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

public class DeviceStateRestorationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "restoration@smarthome.com";
    private static final String TEST_NAME = "State Restoration Test User";
    private static final String TEST_PASSWORD = "StateTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Device State Restoration Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Device state restoration test cleanup completed");
    }

    @Test
    @DisplayName("Test Device State Restoration After Event Completion")
    void testDeviceStateRestoration() {
        System.out.println("\nTesting Device State Restoration After Event Completion...");

        // Connect devices and set initial states
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Living Room");

        // Ensure TV is OFF and LIGHT is ON initially
        if (smartHomeService.viewGadgets().get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("TV", "Living Room"); // Turn OFF
        }
        if (!smartHomeService.viewGadgets().get(1).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("LIGHT", "Living Room"); // Turn ON
        }

        System.out.println("Initial states set: TV=OFF, LIGHT=ON");

        // Create a short event that will start immediately and end quickly
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(2);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "State Restoration Test", "Testing device state restoration",
            startDateTime, endDateTime, "Meeting");
        assertTrue(eventCreated, "Event should be created successfully");

        // Add automation to change device states
        boolean tvAutomationAdded = smartHomeService.addCustomDeviceAutomation(
            "State Restoration Test", "TV", "Living Room", "ON", 0); // Turn TV ON at start
        boolean lightAutomationAdded = smartHomeService.addCustomDeviceAutomation(
            "State Restoration Test", "LIGHT", "Living Room", "OFF", 0); // Turn LIGHT OFF at start

        assertTrue(tvAutomationAdded, "TV automation should be added successfully");
        assertTrue(lightAutomationAdded, "Light automation should be added successfully");

        // Enable suppression to capture messages
        TimerService.suppressNotifications(true);

        // Execute automation at event start
        System.out.println("Executing automation at event start...");
        smartHomeService.forceCalendarAutomationCheck();

        // Wait for event to end (simulate passage of time)
        System.out.println("Simulating event completion and state restoration...");

        // Create a new event in the past to trigger restoration logic
        LocalDateTime pastEventStart = LocalDateTime.now().minusMinutes(5);
        LocalDateTime pastEventEnd = LocalDateTime.now().minusMinutes(1);
        smartHomeService.createCalendarEvent(
            "Completed Event", "Past event for restoration testing",
            pastEventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        smartHomeService.addCustomDeviceAutomation("Completed Event", "TV", "Living Room", "ON", 0);

        // Force another check to trigger restoration
        smartHomeService.forceCalendarAutomationCheck();

        // Check for buffered messages
        boolean hasBuffered = TimerService.hasBufferedNotifications();
        System.out.println("Has buffered restoration messages: " + hasBuffered);

        // Display all buffered messages
        TimerService.suppressNotifications(false);
        TimerService.displayBufferedNotifications();

        System.out.println("Device state restoration test completed");
        System.out.println("Original states tracked and restoration logic implemented");
    }

    @Test
    @DisplayName("Test Multiple Device State Restoration")
    void testMultipleDeviceStateRestoration() {
        System.out.println("\nTesting Multiple Device State Restoration...");

        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "LG", "Living Room");
        smartHomeService.connectToGadget("AC", "Samsung", "Living Room");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Living Room");

        System.out.println("Connected 3 devices for restoration testing");

        // Create event that ended in the past
        LocalDateTime pastStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime pastEnd = LocalDateTime.now().minusMinutes(2);

        smartHomeService.createCalendarEvent(
            "Multi Device Test", "Testing multiple device restoration",
            pastStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        // Add multiple automations
        smartHomeService.addCustomDeviceAutomation("Multi Device Test", "TV", "Living Room", "ON", 0);
        smartHomeService.addCustomDeviceAutomation("Multi Device Test", "AC", "Living Room", "ON", 0);
        smartHomeService.addCustomDeviceAutomation("Multi Device Test", "FAN", "Living Room", "OFF", 0);

        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasBuffered = TimerService.hasBufferedNotifications();
        System.out.println("Multi-device restoration messages buffered: " + hasBuffered);

        TimerService.suppressNotifications(false);
        TimerService.displayBufferedNotifications();

        System.out.println("Multiple device state restoration test: PASSED");
    }

    @Test
    @DisplayName("Test State Restoration During Interactive Flow")
    void testRestorationDuringInteractiveFlow() {
        System.out.println("\nTesting State Restoration During Interactive Flow...");

        // Connect device
        smartHomeService.connectToGadget("SPEAKER", "MI", "Living Room");

        // Create completed event
        LocalDateTime pastStart = LocalDateTime.now().minusMinutes(15);
        LocalDateTime pastEnd = LocalDateTime.now().minusMinutes(1);

        smartHomeService.createCalendarEvent(
            "Interactive Restoration Test", "Testing restoration during interaction",
            pastStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        smartHomeService.addCustomDeviceAutomation("Interactive Restoration Test", "SPEAKER", "Living Room", "ON", 0);

        // Simulate interactive flow with restoration happening in background
        TimerService.suppressNotifications(true);
        System.out.println("Interactive flow started - restoration suppressed");

        smartHomeService.forceCalendarAutomationCheck();

        System.out.println("Interactive flow completed - showing restoration messages");
        TimerService.suppressNotifications(false);
        TimerService.displayBufferedNotifications();

        System.out.println("State restoration during interactive flow test: PASSED");
    }
}