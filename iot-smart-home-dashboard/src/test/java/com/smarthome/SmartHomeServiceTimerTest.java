package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
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

/**
 * Timer and Scheduling functionality tests for SmartHomeService
 * Tests: Device Timers, Scheduling, Calendar Events, Automated Tasks
 */
public class SmartHomeServiceTimerTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "timer@smarthome.com";
    private static final String TEST_NAME = "Timer Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @BeforeEach
    @DisplayName("Setup timer test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        // Register and login user for timer tests
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        System.out.println("⏰ Setting up timer test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup timer tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up timer test environment");
    }

    // ==================== TIMER SERVICE TESTS ====================

    @Test
    @DisplayName("Test 1: Timer Service Initialization")
    void testTimerServiceInitialization() {
        TimerService timerService = smartHomeService.getTimerService();
        assertNotNull(timerService, "TimerService should be initialized");
        System.out.println("✅ Timer service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Force Timer Check")
    void testForceTimerCheck() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.forceTimerCheck(),
                          "Force timer check should not throw exception");
        System.out.println("✅ Force timer check test passed");
    }

    // ==================== DEVICE TIMER SCHEDULING TESTS ====================

    @Test
    @DisplayName("Test 3: Schedule Valid Device Timer")
    void testScheduleValidDeviceTimer() {
        // Connect a device first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Schedule timer for future time
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertTrue(result, "Scheduling valid device timer should succeed");

        System.out.println("✅ Schedule valid device timer test passed");
    }

    @Test
    @DisplayName("Test 4: Schedule Timer for Non-existent Device")
    void testScheduleTimerNonExistentDevice() {
        // Don't connect any device

        // Try to schedule timer for non-existent device
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer for non-existent device should fail");

        System.out.println("✅ Schedule timer for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 5: Schedule Timer for Past Time")
    void testScheduleTimerPastTime() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to schedule timer for past time
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        String timeString = pastTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer for past time should fail");

        System.out.println("✅ Schedule timer for past time test passed");
    }

    @Test
    @DisplayName("Test 6: Schedule Timer with Invalid Action")
    void testScheduleTimerInvalidAction() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to schedule timer with invalid action
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "INVALID", timeString);
        assertFalse(result, "Scheduling timer with invalid action should fail");

        System.out.println("✅ Schedule timer with invalid action test passed");
    }

    @Test
    @DisplayName("Test 7: Schedule Timer with Invalid Date Format")
    void testScheduleTimerInvalidDateFormat() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to schedule timer with invalid date format
        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", "invalid-date");
        assertFalse(result, "Scheduling timer with invalid date format should fail");

        System.out.println("✅ Schedule timer with invalid date format test passed");
    }

    @Test
    @DisplayName("Test 8: Schedule Multiple Timers for Same Device")
    void testScheduleMultipleTimersSameDevice() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Schedule ON timer
        LocalDateTime onTime = LocalDateTime.now().plusHours(1);
        String onTimeString = onTime.format(formatter);
        boolean onResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", onTimeString);
        assertTrue(onResult, "Scheduling ON timer should succeed");

        // Schedule OFF timer for same device
        LocalDateTime offTime = LocalDateTime.now().plusHours(2);
        String offTimeString = offTime.format(formatter);
        boolean offResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "OFF", offTimeString);
        assertTrue(offResult, "Scheduling OFF timer should succeed");

        System.out.println("✅ Schedule multiple timers for same device test passed");
    }

    @Test
    @DisplayName("Test 9: Schedule Timers for Multiple Devices")
    void testScheduleTimersMultipleDevices() {
        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");

        // Schedule timers for different devices
        LocalDateTime time1 = LocalDateTime.now().plusHours(1);
        LocalDateTime time2 = LocalDateTime.now().plusHours(2);
        LocalDateTime time3 = LocalDateTime.now().plusHours(3);

        boolean result1 = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", time1.format(formatter));
        boolean result2 = smartHomeService.scheduleDeviceTimer("AC", "Bedroom", "ON", time2.format(formatter));
        boolean result3 = smartHomeService.scheduleDeviceTimer("LIGHT", "Kitchen", "OFF", time3.format(formatter));

        assertTrue(result1, "TV timer should be scheduled");
        assertTrue(result2, "AC timer should be scheduled");
        assertTrue(result3, "Light timer should be scheduled");

        System.out.println("✅ Schedule timers for multiple devices test passed");
    }

    // ==================== TIMER DISPLAY TESTS ====================

    @Test
    @DisplayName("Test 10: Show Scheduled Timers with No Timers")
    void testShowScheduledTimersNoTimers() {
        // Should not throw exception even with no timers
        assertDoesNotThrow(() -> smartHomeService.showScheduledTimers(),
                          "Showing scheduled timers with no timers should not throw exception");

        System.out.println("✅ Show scheduled timers with no timers test passed");
    }

    @Test
    @DisplayName("Test 11: Show Scheduled Timers with Active Timers")
    void testShowScheduledTimersWithActiveTimers() {
        // Connect device and schedule timer
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showScheduledTimers(),
                          "Showing scheduled timers with active timers should not throw exception");

        System.out.println("✅ Show scheduled timers with active timers test passed");
    }

    @Test
    @DisplayName("Test 12: Get Scheduled Timers with Devices")
    void testGetScheduledTimersWithDevices() {
        // Connect devices and schedule timers
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        LocalDateTime time1 = LocalDateTime.now().plusHours(1);
        LocalDateTime time2 = LocalDateTime.now().plusHours(2);

        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", time1.format(formatter));
        smartHomeService.scheduleDeviceTimer("AC", "Bedroom", "OFF", time2.format(formatter));

        // Get scheduled timers
        List<Gadget> timerDevices = smartHomeService.getScheduledTimersWithDevices();
        assertNotNull(timerDevices, "Timer devices list should not be null");

        System.out.println("✅ Get scheduled timers with devices test passed");
    }

    // ==================== TIMER CANCELLATION TESTS ====================

    @Test
    @DisplayName("Test 13: Cancel Valid Device Timer")
    void testCancelValidDeviceTimer() {
        // Connect device and schedule timer
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        // Cancel the timer
        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertTrue(result, "Canceling valid device timer should succeed");

        System.out.println("✅ Cancel valid device timer test passed");
    }

    @Test
    @DisplayName("Test 14: Cancel Non-existent Timer")
    void testCancelNonExistentTimer() {
        // Connect device but don't schedule timer
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to cancel non-existent timer
        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertFalse(result, "Canceling non-existent timer should fail");

        System.out.println("✅ Cancel non-existent timer test passed");
    }

    @Test
    @DisplayName("Test 15: Cancel Timer for Non-existent Device")
    void testCancelTimerNonExistentDevice() {
        // Don't connect any device

        // Try to cancel timer for non-existent device
        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertFalse(result, "Canceling timer for non-existent device should fail");

        System.out.println("✅ Cancel timer for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 16: Cancel Timer with Invalid Action")
    void testCancelTimerInvalidAction() {
        // Connect device and schedule timer
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        // Try to cancel with invalid action
        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "INVALID");
        assertFalse(result, "Canceling timer with invalid action should fail");

        System.out.println("✅ Cancel timer with invalid action test passed");
    }

    // ==================== CALENDAR EVENT TESTS ====================

    @Test
    @DisplayName("Test 17: Create Valid Calendar Event")
    void testCreateValidCalendarEvent() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        boolean result = smartHomeService.createCalendarEvent(
            "Test Meeting",
            "Test meeting description",
            startTime.format(formatter),
            endTime.format(formatter),
            "meeting"
        );

        assertTrue(result, "Creating valid calendar event should succeed");
        System.out.println("✅ Create valid calendar event test passed");
    }

    @Test
    @DisplayName("Test 18: Create Calendar Event with Past Time")
    void testCreateCalendarEventPastTime() {
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = pastTime.plusHours(2);

        boolean result = smartHomeService.createCalendarEvent(
            "Past Meeting",
            "Past meeting description",
            pastTime.format(formatter),
            endTime.format(formatter),
            "meeting"
        );

        assertFalse(result, "Creating calendar event with past time should fail");
        System.out.println("✅ Create calendar event with past time test passed");
    }

    @Test
    @DisplayName("Test 19: Create Calendar Event with Invalid Date Format")
    void testCreateCalendarEventInvalidDateFormat() {
        boolean result = smartHomeService.createCalendarEvent(
            "Invalid Meeting",
            "Invalid meeting description",
            "invalid-start-date",
            "invalid-end-date",
            "meeting"
        );

        assertFalse(result, "Creating calendar event with invalid date format should fail");
        System.out.println("✅ Create calendar event with invalid date format test passed");
    }

    @Test
    @DisplayName("Test 20: Create Multiple Calendar Events")
    void testCreateMultipleCalendarEvents() {
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1);

        // Create multiple events
        boolean result1 = smartHomeService.createCalendarEvent(
            "Morning Meeting",
            "Morning team meeting",
            baseTime.withHour(9).format(formatter),
            baseTime.withHour(10).format(formatter),
            "meeting"
        );

        boolean result2 = smartHomeService.createCalendarEvent(
            "Lunch Break",
            "Lunch with colleagues",
            baseTime.withHour(12).format(formatter),
            baseTime.withHour(13).format(formatter),
            "meal"
        );

        boolean result3 = smartHomeService.createCalendarEvent(
            "Movie Night",
            "Family movie time",
            baseTime.withHour(20).format(formatter),
            baseTime.withHour(22).format(formatter),
            "movie"
        );

        assertTrue(result1, "Morning meeting should be created");
        assertTrue(result2, "Lunch break should be created");
        assertTrue(result3, "Movie night should be created");

        System.out.println("✅ Create multiple calendar events test passed");
    }

    @Test
    @DisplayName("Test 21: Show Upcoming Events")
    void testShowUpcomingEvents() {
        // Create a future event
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        smartHomeService.createCalendarEvent(
            "Future Event",
            "Future event description",
            futureTime.format(formatter),
            futureTime.plusHours(1).format(formatter),
            "meeting"
        );

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showUpcomingEvents(),
                          "Showing upcoming events should not throw exception");

        System.out.println("✅ Show upcoming events test passed");
    }

    @Test
    @DisplayName("Test 22: Show Event Automation")
    void testShowEventAutomation() {
        // Create an event with automation
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        smartHomeService.createCalendarEvent(
            "Automated Meeting",
            "Meeting with device automation",
            futureTime.format(formatter),
            futureTime.plusHours(1).format(formatter),
            "meeting"
        );

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showEventAutomation("Automated Meeting"),
                          "Showing event automation should not throw exception");

        System.out.println("✅ Show event automation test passed");
    }

    // ==================== TIMER WITHOUT LOGIN TESTS ====================

    @Test
    @DisplayName("Test 23: Timer Operations Without Login")
    void testTimerOperationsWithoutLogin() {
        // Logout first
        smartHomeService.logout();

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        // Try timer operations without login
        boolean scheduleResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(scheduleResult, "Scheduling timer without login should fail");

        boolean cancelResult = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertFalse(cancelResult, "Canceling timer without login should fail");

        boolean eventResult = smartHomeService.createCalendarEvent(
            "Test Event", "Description", timeString, timeString, "meeting"
        );
        assertFalse(eventResult, "Creating calendar event without login should fail");

        System.out.println("✅ Timer operations without login test passed");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 24: Schedule Timer with Minimal Future Time")
    void testScheduleTimerMinimalFutureTime() {
        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Schedule timer just 2 minutes in future (should be allowed)
        LocalDateTime minimalFutureTime = LocalDateTime.now().plusMinutes(2);
        String timeString = minimalFutureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertTrue(result, "Scheduling timer with minimal future time should succeed");

        System.out.println("✅ Schedule timer with minimal future time test passed");
    }

    @Test
    @DisplayName("Test 25: Schedule Timer Very Close to Current Time")
    void testScheduleTimerVeryCloseToCurrentTime() {
        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Schedule timer just 30 seconds in future (should fail - too close)
        LocalDateTime veryCloseTime = LocalDateTime.now().plusSeconds(30);
        String timeString = veryCloseTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer very close to current time should fail");

        System.out.println("✅ Schedule timer very close to current time test passed");
    }

    @Test
    @DisplayName("Test 26: Calendar Event End Time Before Start Time")
    void testCalendarEventEndTimeBeforeStartTime() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).plusHours(2);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1); // Before start time

        boolean result = smartHomeService.createCalendarEvent(
            "Invalid Time Event",
            "Event with end time before start time",
            startTime.format(formatter),
            endTime.format(formatter),
            "meeting"
        );

        assertFalse(result, "Calendar event with end time before start time should fail");
        System.out.println("✅ Calendar event end time before start time test passed");
    }

    @Test
    @DisplayName("Test 27: Schedule Timer for Device in Different Room")
    void testScheduleTimerDeviceDifferentRoom() {
        // Connect device in Living Room
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to schedule timer for TV in Bedroom (different room)
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Bedroom", "ON", timeString);
        assertFalse(result, "Scheduling timer for device in different room should fail");

        System.out.println("✅ Schedule timer for device in different room test passed");
    }

    @Test
    @DisplayName("Test 28: Overlapping Timer Schedules")
    void testOverlappingTimerSchedules() {
        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime baseTime = LocalDateTime.now().plusHours(1);

        // Schedule ON timer
        boolean onResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", baseTime.format(formatter));
        assertTrue(onResult, "ON timer should be scheduled");

        // Schedule OFF timer for same time (should be allowed - different actions)
        boolean offResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "OFF", baseTime.format(formatter));
        assertTrue(offResult, "OFF timer should be scheduled");

        System.out.println("✅ Overlapping timer schedules test passed");
    }
}