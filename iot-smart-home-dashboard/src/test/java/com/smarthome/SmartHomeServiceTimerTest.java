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

public class SmartHomeServiceTimerTest {

    private SmartHomeService smartHomeService;
    private String testEmail;
    private static final String TEST_NAME = "Timer Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @BeforeEach
    @DisplayName("Setup timer test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        testEmail = "timer" + System.currentTimeMillis() + "@smarthome.com";
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        System.out.println("Setting up timer test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup timer tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            List<Gadget> devices = smartHomeService.viewGadgets();
            if (devices != null) {
                for (Gadget device : new java.util.ArrayList<>(devices)) {
                    smartHomeService.deleteDevice(device.getType(), device.getRoomName());
                }
            }
            smartHomeService.logout();
        }
        System.out.println("Cleaned up timer test environment");
    }


    @Test
    @DisplayName("Test 1: Timer Service Initialization")
    void testTimerServiceInitialization() {
        TimerService timerService = smartHomeService.getTimerService();
        assertNotNull(timerService, "TimerService should be initialized");
        System.out.println(" Timer service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Force Timer Check")
    void testForceTimerCheck() {
        assertDoesNotThrow(() -> smartHomeService.forceTimerCheck(),
                          "Force timer check should not throw exception");
        System.out.println(" Force timer check test passed");
    }


    @Test
    @DisplayName("Test 3: Schedule Valid Device Timer")
    void testScheduleValidDeviceTimer() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertTrue(result, "Scheduling valid device timer should succeed");

        System.out.println(" Schedule valid device timer test passed");
    }

    @Test
    @DisplayName("Test 4: Schedule Timer for Non-existent Device")
    void testScheduleTimerNonExistentDevice() {

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer for non-existent device should fail");

        System.out.println(" Schedule timer for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 5: Schedule Timer for Past Time")
    void testScheduleTimerPastTime() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        String timeString = pastTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer for past time should fail");

        System.out.println(" Schedule timer for past time test passed");
    }

    @Test
    @DisplayName("Test 6: Schedule Timer with Invalid Action")
    void testScheduleTimerInvalidAction() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "INVALID", timeString);
        assertFalse(result, "Scheduling timer with invalid action should fail");

        System.out.println(" Schedule timer with invalid action test passed");
    }

    @Test
    @DisplayName("Test 7: Schedule Timer with Invalid Date Format")
    void testScheduleTimerInvalidDateFormat() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", "invalid-date");
        assertFalse(result, "Scheduling timer with invalid date format should fail");

        System.out.println(" Schedule timer with invalid date format test passed");
    }

    @Test
    @DisplayName("Test 8: Schedule Multiple Timers for Same Device")
    void testScheduleMultipleTimersSameDevice() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime onTime = LocalDateTime.now().plusHours(1);
        String onTimeString = onTime.format(formatter);
        boolean onResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", onTimeString);
        assertTrue(onResult, "Scheduling ON timer should succeed");

        LocalDateTime offTime = LocalDateTime.now().plusHours(2);
        String offTimeString = offTime.format(formatter);
        boolean offResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "OFF", offTimeString);
        assertTrue(offResult, "Scheduling OFF timer should succeed");

        System.out.println(" Schedule multiple timers for same device test passed");
    }



    @Test
    @DisplayName("Test 10: Show Scheduled Timers with No Timers")
    void testShowScheduledTimersNoTimers() {
        assertDoesNotThrow(() -> smartHomeService.showScheduledTimers(),
                          "Showing scheduled timers with no timers should not throw exception");

        System.out.println(" Show scheduled timers with no timers test passed");
    }

    @Test
    @DisplayName("Test 11: Show Scheduled Timers with Active Timers")
    void testShowScheduledTimersWithActiveTimers() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        assertDoesNotThrow(() -> smartHomeService.showScheduledTimers(),
                          "Showing scheduled timers with active timers should not throw exception");

        System.out.println(" Show scheduled timers with active timers test passed");
    }

    @Test
    @DisplayName("Test 12: Get Scheduled Timers with Devices")
    void testGetScheduledTimersWithDevices() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        LocalDateTime time1 = LocalDateTime.now().plusHours(1);
        LocalDateTime time2 = LocalDateTime.now().plusHours(2);

        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", time1.format(formatter));
        smartHomeService.scheduleDeviceTimer("AC", "Bedroom", "OFF", time2.format(formatter));

        List<Gadget> timerDevices = smartHomeService.getScheduledTimersWithDevices();
        assertNotNull(timerDevices, "Timer devices list should not be null");

        System.out.println(" Get scheduled timers with devices test passed");
    }


    @Test
    @DisplayName("Test 13: Cancel Valid Device Timer")
    void testCancelValidDeviceTimer() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertTrue(result, "Canceling valid device timer should succeed");

        System.out.println(" Cancel valid device timer test passed");
    }


    @Test
    @DisplayName("Test 15: Cancel Timer for Non-existent Device")
    void testCancelTimerNonExistentDevice() {

        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertFalse(result, "Canceling timer for non-existent device should fail");

        System.out.println(" Cancel timer for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 16: Cancel Timer with Invalid Action")
    void testCancelTimerInvalidAction() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", futureTime.format(formatter));

        boolean result = smartHomeService.cancelDeviceTimer("TV", "Living Room", "INVALID");
        assertFalse(result, "Canceling timer with invalid action should fail");

        System.out.println(" Cancel timer with invalid action test passed");
    }


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
        System.out.println(" Create valid calendar event test passed");
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
        System.out.println(" Create calendar event with invalid date format test passed");
    }

    @Test
    @DisplayName("Test 20: Create Multiple Calendar Events")
    void testCreateMultipleCalendarEvents() {
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1);

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

        System.out.println(" Create multiple calendar events test passed");
    }

    @Test
    @DisplayName("Test 21: Show Upcoming Events")
    void testShowUpcomingEvents() {
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        smartHomeService.createCalendarEvent(
            "Future Event",
            "Future event description",
            futureTime.format(formatter),
            futureTime.plusHours(1).format(formatter),
            "meeting"
        );

        assertDoesNotThrow(() -> smartHomeService.showUpcomingEvents(),
                          "Showing upcoming events should not throw exception");

        System.out.println(" Show upcoming events test passed");
    }

    @Test
    @DisplayName("Test 22: Show Event Automation")
    void testShowEventAutomation() {
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        smartHomeService.createCalendarEvent(
            "Automated Meeting",
            "Meeting with device automation",
            futureTime.format(formatter),
            futureTime.plusHours(1).format(formatter),
            "meeting"
        );

        assertDoesNotThrow(() -> smartHomeService.showEventAutomation("Automated Meeting"),
                          "Showing event automation should not throw exception");

        System.out.println(" Show event automation test passed");
    }


    @Test
    @DisplayName("Test 23: Timer Operations Without Login")
    void testTimerOperationsWithoutLogin() {
        smartHomeService.logout();

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean scheduleResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(scheduleResult, "Scheduling timer without login should fail");

        boolean cancelResult = smartHomeService.cancelDeviceTimer("TV", "Living Room", "ON");
        assertFalse(cancelResult, "Canceling timer without login should fail");

        boolean eventResult = smartHomeService.createCalendarEvent(
            "Test Event", "Description", timeString, timeString, "meeting"
        );
        assertFalse(eventResult, "Creating calendar event without login should fail");

        System.out.println(" Timer operations without login test passed");
    }


    @Test
    @DisplayName("Test 24: Schedule Timer with Minimal Future Time")
    void testScheduleTimerMinimalFutureTime() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime minimalFutureTime = LocalDateTime.now().plusMinutes(2);
        String timeString = minimalFutureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertTrue(result, "Scheduling timer with minimal future time should succeed");

        System.out.println(" Schedule timer with minimal future time test passed");
    }

    @Test
    @DisplayName("Test 25: Schedule Timer Very Close to Current Time")
    void testScheduleTimerVeryCloseToCurrentTime() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime veryCloseTime = LocalDateTime.now().plusSeconds(30);
        String timeString = veryCloseTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", timeString);
        assertFalse(result, "Scheduling timer very close to current time should fail");

        System.out.println(" Schedule timer very close to current time test passed");
    }


    @Test
    @DisplayName("Test 27: Schedule Timer for Device in Different Room")
    void testScheduleTimerDeviceDifferentRoom() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);
        String timeString = futureTime.format(formatter);

        boolean result = smartHomeService.scheduleDeviceTimer("TV", "Bedroom", "ON", timeString);
        assertFalse(result, "Scheduling timer for device in different room should fail");

        System.out.println(" Schedule timer for device in different room test passed");
    }

    @Test
    @DisplayName("Test 28: Overlapping Timer Schedules")
    void testOverlappingTimerSchedules() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        LocalDateTime baseTime = LocalDateTime.now().plusHours(1);

        boolean onResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "ON", baseTime.format(formatter));
        assertTrue(onResult, "ON timer should be scheduled");

        boolean offResult = smartHomeService.scheduleDeviceTimer("TV", "Living Room", "OFF", baseTime.format(formatter));
        assertTrue(offResult, "OFF timer should be scheduled");

        System.out.println(" Overlapping timer schedules test passed");
    }
}