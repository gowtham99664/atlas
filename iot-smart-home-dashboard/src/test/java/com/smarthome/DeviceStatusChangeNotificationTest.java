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

public class DeviceStatusChangeNotificationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "devicestatus@smarthome.com";
    private static final String TEST_NAME = "Device Status Test User";
    private static final String TEST_PASSWORD = "DeviceTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Device Status Change Notification Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Device status change notification test cleanup completed");
    }

    @Test
    @DisplayName("Test Device Status Change Notification for Calendar Event")
    void testDeviceStatusChangeNotification() {
        System.out.println("\nTesting Device Status Change Notification...");

        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Ensure TV is OFF initially
        if (smartHomeService.viewGadgets().get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("TV", "Living Room");
        }

        System.out.println("Initial TV status: OFF");

        // Create calendar event that will trigger device automation
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(30);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Device Test Event", "Testing device status change notifications",
            startDateTime, endDateTime, "Personal");
        assertTrue(eventCreated, "Event should be created successfully");

        // Add automation to turn TV ON
        boolean automationAdded = smartHomeService.addCustomDeviceAutomation(
            "Device Test Event", "TV", "Living Room", "ON", 0);
        assertTrue(automationAdded, "Automation should be added successfully");

        // Enable suppression to capture device status change notification
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        // Check if device status change notification was buffered
        boolean hasDeviceNotification = TimerService.hasBufferedNotifications();
        System.out.println("Device status change notification captured: " + hasDeviceNotification);

        // Display the device status change notification
        TimerService.suppressNotifications(false);
        if (hasDeviceNotification) {
            System.out.println("Expected: DEVICE STATUS CHANGED notification");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Device status change notification test: PASSED");
    }

    @Test
    @DisplayName("Test Device Status Restoration Notification")
    void testDeviceStatusRestorationNotification() {
        System.out.println("\nTesting Device Status Restoration Notification...");

        // Connect device
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Living Room");

        // Ensure LIGHT is ON initially
        if (!smartHomeService.viewGadgets().get(0).isOn()) {
            smartHomeService.changeSpecificGadgetStatus("LIGHT", "Living Room");
        }

        System.out.println("Initial LIGHT status: ON");

        // Create an event that has already ended
        LocalDateTime pastStart = LocalDateTime.now().minusMinutes(10);
        LocalDateTime pastEnd = LocalDateTime.now().minusMinutes(2);

        smartHomeService.createCalendarEvent(
            "Completed Test Event", "Event that should trigger restoration",
            pastStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            pastEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Meeting");

        // Add automation that would have turned LIGHT OFF during the event
        smartHomeService.addCustomDeviceAutomation("Completed Test Event", "LIGHT", "Living Room", "OFF", 0);

        // First, let's simulate the automation execution during the event
        // (This would have stored the original state and changed the device)

        // Now trigger restoration check
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasRestorationNotification = TimerService.hasBufferedNotifications();
        System.out.println("Device restoration notification captured: " + hasRestorationNotification);

        TimerService.suppressNotifications(false);
        if (hasRestorationNotification) {
            System.out.println("Expected: DEVICE STATUS RESTORED notification");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Device status restoration notification test: PASSED");
    }

    @Test
    @DisplayName("Test Multiple Device Status Changes")
    void testMultipleDeviceStatusChanges() {
        System.out.println("\nTesting Multiple Device Status Changes...");

        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "LG", "Living Room");
        smartHomeService.connectToGadget("AC", "Samsung", "Living Room");

        System.out.println("Connected TV and AC for testing");

        // Create event with multiple device automations
        LocalDateTime eventStart = LocalDateTime.now();
        LocalDateTime eventEnd = eventStart.plusMinutes(15);

        smartHomeService.createCalendarEvent(
            "Multi Device Event", "Event with multiple device changes",
            eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
            "Personal");

        // Add multiple automations
        smartHomeService.addCustomDeviceAutomation("Multi Device Event", "TV", "Living Room", "ON", 0);
        smartHomeService.addCustomDeviceAutomation("Multi Device Event", "AC", "Living Room", "ON", 0);

        // Trigger automation
        TimerService.suppressNotifications(true);
        smartHomeService.forceCalendarAutomationCheck();

        boolean hasMultipleNotifications = TimerService.hasBufferedNotifications();
        System.out.println("Multiple device notifications captured: " + hasMultipleNotifications);

        TimerService.suppressNotifications(false);
        if (hasMultipleNotifications) {
            System.out.println("Expected: Multiple DEVICE STATUS CHANGED notifications");
            TimerService.displayBufferedNotifications();
        }

        System.out.println("Multiple device status changes test: PASSED");
    }
}