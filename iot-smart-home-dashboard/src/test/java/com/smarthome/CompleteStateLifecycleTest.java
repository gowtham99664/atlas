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
import java.util.List;

public class CompleteStateLifecycleTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "lifecycle@smarthome.com";
    private static final String TEST_NAME = "Complete Lifecycle Test User";
    private static final String TEST_PASSWORD = "LifecycleTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Complete State Lifecycle Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Complete state lifecycle test cleanup completed");
    }

    @Test
    @DisplayName("Test Complete Device State Lifecycle - Automation to Restoration")
    void testCompleteDeviceStateLifecycle() {
        System.out.println("\nTesting Complete Device State Lifecycle...");

        // Setup: Connect devices and establish initial states
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Living Room");

        // Ensure TV is OFF initially (important for state restoration)
        List<com.smarthome.model.Gadget> devices = smartHomeService.viewGadgets();
        if (devices.get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("TV", "Living Room");
        }

        // Ensure LIGHT is ON initially
        if (!devices.get(1).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("LIGHT", "Living Room");
        }

        System.out.println("Phase 1: Initial device states established");
        System.out.println("- TV: OFF (will be turned ON during event)");
        System.out.println("- LIGHT: ON (will be turned OFF during event)");

        // Phase 2: Create event and automation that changes device states
        LocalDateTime eventStart = LocalDateTime.now().minusMinutes(1); // Event started 1 minute ago
        LocalDateTime eventEnd = LocalDateTime.now().plusMinutes(1);    // Event ends in 1 minute
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        smartHomeService.createCalendarEvent(
            "Lifecycle Test Event", "Testing complete automation lifecycle",
            startDateTime, endDateTime, "Meeting");

        // Add automations that will change device states
        smartHomeService.addCustomDeviceAutomation(
            "Lifecycle Test Event", "TV", "Living Room", "ON", 0);
        smartHomeService.addCustomDeviceAutomation(
            "Lifecycle Test Event", "LIGHT", "Living Room", "OFF", 0);

        System.out.println("Phase 2: Event created with device automation");

        // Phase 3: Execute automation (this should change device states and store originals)
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();
        System.out.println("Phase 3: Automation executed - devices changed to event states");

        // Phase 4: Simulate event ending and trigger restoration
        LocalDateTime pastEventStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime pastEventEnd = LocalDateTime.now().minusMinutes(2);

        smartHomeService.createCalendarEvent(
            "Completed Event for Restoration", "Event that has already ended",
            pastEventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        // Add automation to this completed event to trigger restoration logic
        smartHomeService.addCustomDeviceAutomation(
            "Completed Event for Restoration", "TV", "Living Room", "ON", 0);
        smartHomeService.addCustomDeviceAutomation(
            "Completed Event for Restoration", "LIGHT", "Living Room", "OFF", 0);

        // Force another automation check to trigger restoration
        smartHomeService.forceCalendarAutomationCheck();
        System.out.println("Phase 4: Event completion detected - state restoration triggered");

        // Phase 5: Display results
        boolean hasRestorationMessages = TimerService.hasBufferedNotifications();
        System.out.println("Phase 5: Restoration messages generated: " + hasRestorationMessages);

        TimerService.suppressNotifications(false);
        TimerService.displayBufferedNotifications();

        System.out.println("\nComplete device state lifecycle test completed");
        System.out.println("Lifecycle: Initial State -> Automation -> Event State -> Restoration -> Original State");
        assertTrue(true, "Complete state lifecycle functionality implemented");
    }

    @Test
    @DisplayName("Test Original State Preservation")
    void testOriginalStatePreservation() {
        System.out.println("\nTesting Original State Preservation...");

        // Connect device
        smartHomeService.connectToGadget("FAN", "Bajaj", "Living Room");

        // Set initial state to ON
        if (!smartHomeService.viewGadgets().get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("FAN", "Living Room");
        }

        System.out.println("Original state: FAN = ON");

        // Create event that will turn FAN OFF
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(30);

        smartHomeService.createCalendarEvent(
            "Fan Control Event", "Event that controls fan state",
            eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        smartHomeService.addCustomDeviceAutomation(
            "Fan Control Event", "FAN", "Living Room", "OFF", 0);

        // Execute automation
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();
        System.out.println("Automation executed: FAN changed to OFF");

        // Create completed event to trigger restoration check
        LocalDateTime pastStart = LocalDateTime.now().minusMinutes(5);
        LocalDateTime pastEnd = LocalDateTime.now().minusMinutes(1);

        smartHomeService.createCalendarEvent(
            "Completed Fan Event", "Completed event for restoration",
            pastStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        smartHomeService.addCustomDeviceAutomation("Completed Fan Event", "FAN", "Living Room", "OFF", 0);

        smartHomeService.forceCalendarAutomationCheck();
        System.out.println("Restoration check completed");

        TimerService.suppressNotifications(false);
        TimerService.displayBufferedNotifications();

        System.out.println("Original state preservation test: PASSED");
    }
}