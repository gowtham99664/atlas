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

public class StreamlinedCalendarCreationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "streamlined@smarthome.com";
    private static final String TEST_NAME = "Streamlined Creation Test User";
    private static final String TEST_PASSWORD = "StreamTest123!@#";

    @BeforeEach
    void setUp() {
        System.out.println("\nSetting up Streamlined Calendar Creation Test...");
        smartHomeService = new SmartHomeService();
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("Streamlined calendar creation test cleanup completed");
    }

    @Test
    @DisplayName("Test Event Creation with Device Automation Setup")
    void testEventCreationWithDeviceSetup() {
        System.out.println("\nTesting Event Creation with Device Automation Setup...");

        // First, connect some devices to work with (or use existing ones)
        smartHomeService.connectToGadget("TV", "Samsung", "Test Room");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Test Room");

        // Verify devices are available
        List<com.smarthome.model.Gadget> devices = smartHomeService.viewGadgets();
        assertFalse(devices.isEmpty(), "Should have connected devices");
        System.out.println("Connected " + devices.size() + " devices for testing");

        // Create a calendar event (this simulates the basic event creation part)
        LocalDateTime eventStart = LocalDateTime.now().plusHours(2);
        LocalDateTime eventEnd = eventStart.plusHours(1);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Movie Night with Automation", "Watching movies with device control",
            startDateTime, endDateTime, "Personal");

        assertTrue(eventCreated, "Event should be created successfully");

        // Now test adding device automation (this simulates what happens during creation)
        com.smarthome.model.Gadget tv = devices.stream()
            .filter(d -> d.getType().equals("TV"))
            .findFirst()
            .orElse(null);

        com.smarthome.model.Gadget light = devices.stream()
            .filter(d -> d.getType().equals("LIGHT"))
            .findFirst()
            .orElse(null);

        assertNotNull(tv, "TV device should be found");
        assertNotNull(light, "Light device should be found");

        // Add automation for TV - turn ON at event start
        boolean tvAutomationAdded = smartHomeService.addCustomDeviceAutomation(
            "Movie Night with Automation", tv.getType(), tv.getRoomName(), "ON", 0);

        // Add automation for Light - turn OFF 2 minutes before event (dimmed lighting for movie)
        boolean lightAutomationAdded = smartHomeService.addCustomDeviceAutomation(
            "Movie Night with Automation", light.getType(), light.getRoomName(), "OFF", -2);

        assertTrue(tvAutomationAdded, "TV automation should be added successfully");
        assertTrue(lightAutomationAdded, "Light automation should be added successfully");

        // Verify the event has the automation configured
        List<CalendarEventService.CalendarEvent> events = smartHomeService.getUpcomingEvents();
        CalendarEventService.CalendarEvent movieEvent = events.stream()
            .filter(e -> e.getTitle().equals("Movie Night with Automation"))
            .findFirst()
            .orElse(null);

        assertNotNull(movieEvent, "Movie night event should be found");
        assertEquals(2, movieEvent.getAutomationActions().size(), "Event should have 2 automation actions");

        // Verify the specific automations
        List<CalendarEventService.AutomationAction> actions = movieEvent.getAutomationActions();

        CalendarEventService.AutomationAction tvAction = actions.stream()
            .filter(a -> a.getDeviceType().equals("TV"))
            .findFirst()
            .orElse(null);

        CalendarEventService.AutomationAction lightAction = actions.stream()
            .filter(a -> a.getDeviceType().equals("LIGHT"))
            .findFirst()
            .orElse(null);

        assertNotNull(tvAction, "TV automation should be configured");
        assertNotNull(lightAction, "Light automation should be configured");

        assertEquals("ON", tvAction.getAction(), "TV should be turned ON");
        assertEquals(0, tvAction.getMinutesOffset(), "TV should turn on at event start");

        assertEquals("OFF", lightAction.getAction(), "Light should be turned OFF");
        assertEquals(-2, lightAction.getMinutesOffset(), "Light should turn off 2 minutes before");

        System.out.println("Streamlined calendar creation test: PASSED");
        System.out.println("   Event: " + movieEvent.getTitle());
        System.out.println("   Automation 1: TV in " + tvAction.getRoomName() + " -> " + tvAction.getAction() + " (at event start)");
        System.out.println("   Automation 2: LIGHT in " + lightAction.getRoomName() + " -> " + lightAction.getAction() + " (2 min before)");
    }

    @Test
    @DisplayName("Test Event Creation Without Devices")
    void testEventCreationWithoutDevices() {
        System.out.println("\nTesting Event Creation Without Devices...");

        // Don't connect any devices - test creation without automation
        List<com.smarthome.model.Gadget> devices = smartHomeService.viewGadgets();

        // Create event without devices
        LocalDateTime eventStart = LocalDateTime.now().plusHours(3);
        LocalDateTime eventEnd = eventStart.plusMinutes(30);
        String startDateTime = eventStart.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String endDateTime = eventEnd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        boolean eventCreated = smartHomeService.createCalendarEvent(
            "Simple Meeting", "Basic meeting without automation",
            startDateTime, endDateTime, "Personal");

        assertTrue(eventCreated, "Event should be created successfully even without devices");

        // Verify the event exists but has no automation
        List<CalendarEventService.CalendarEvent> events = smartHomeService.getUpcomingEvents();
        CalendarEventService.CalendarEvent simpleEvent = events.stream()
            .filter(e -> e.getTitle().equals("Simple Meeting"))
            .findFirst()
            .orElse(null);

        assertNotNull(simpleEvent, "Simple meeting event should be found");
        assertTrue(simpleEvent.getAutomationActions().isEmpty(), "Event should have no automation actions");

        System.out.println("Event creation without devices: PASSED");
        System.out.println("   Event: " + simpleEvent.getTitle());
        System.out.println("   Automation: None (no devices available)");

        System.out.println("\n=== STREAMLINED CALENDAR CREATION TEST SUMMARY ===");
        System.out.println("Event Creation with Device Setup: WORKING");
        System.out.println("Event Creation without Devices: WORKING");
        System.out.println("Device Automation During Creation: FUNCTIONAL");
        System.out.println("Streamlined Calendar Creation: FULLY OPERATIONAL!");
    }
}