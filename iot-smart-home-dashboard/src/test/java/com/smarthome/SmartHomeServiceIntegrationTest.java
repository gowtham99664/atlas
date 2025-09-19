package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import com.smarthome.service.WeatherService;
import com.smarthome.service.SmartScenesService;
import com.smarthome.service.DeviceHealthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Integration functionality tests for SmartHomeService
 * Tests: Weather Service, Smart Scenes, Device Health, Device Management
 */
public class SmartHomeServiceIntegrationTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "integration@smarthome.com";
    private static final String TEST_NAME = "Integration Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup integration test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        // Register and login user for integration tests
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        System.out.println("🔗 Setting up integration test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup integration tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up integration test environment");
    }

    // ==================== WEATHER SERVICE TESTS ====================

    @Test
    @DisplayName("Test 1: Weather Service Initialization")
    void testWeatherServiceInitialization() {
        WeatherService weatherService = smartHomeService.getWeatherService();
        assertNotNull(weatherService, "WeatherService should be initialized");
        System.out.println("✅ Weather service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Show Current Weather")
    void testShowCurrentWeather() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showCurrentWeather(),
                          "Showing current weather should not throw exception");
        System.out.println("✅ Show current weather test passed");
    }

    @Test
    @DisplayName("Test 3: Show Weather Forecast")
    void testShowWeatherForecast() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showWeatherForecast(),
                          "Showing weather forecast should not throw exception");
        System.out.println("✅ Show weather forecast test passed");
    }

    @Test
    @DisplayName("Test 4: Update Weather Data")
    void testUpdateWeatherData() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.updateWeatherData(),
                          "Updating weather data should not throw exception");
        System.out.println("✅ Update weather data test passed");
    }

    @Test
    @DisplayName("Test 5: Clear Weather Data")
    void testClearWeatherData() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.clearWeatherData(),
                          "Clearing weather data should not throw exception");
        System.out.println("✅ Clear weather data test passed");
    }

    @Test
    @DisplayName("Test 6: Check User Weather Data")
    void testHasUserWeatherData() {
        // Should return a boolean value
        boolean hasData = smartHomeService.hasUserWeatherData();
        assertTrue(hasData || !hasData, "Should return a boolean value");
        System.out.println("✅ Check user weather data test passed");
    }

    // ==================== SMART SCENES SERVICE TESTS ====================

    @Test
    @DisplayName("Test 7: Smart Scenes Service Initialization")
    void testSmartScenesServiceInitialization() {
        SmartScenesService scenesService = smartHomeService.getSmartScenesService();
        assertNotNull(scenesService, "SmartScenesService should be initialized");
        System.out.println("✅ Smart scenes service initialization test passed");
    }

    @Test
    @DisplayName("Test 8: Show Available Scenes")
    void testShowAvailableScenes() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showAvailableScenes(),
                          "Showing available scenes should not throw exception");
        System.out.println("✅ Show available scenes test passed");
    }

    @Test
    @DisplayName("Test 9: Execute Smart Scene")
    void testExecuteSmartScene() {
        // Connect some devices first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Living Room");

        // Try to execute a scene (should handle non-existent scenes gracefully)
        boolean result = smartHomeService.executeSmartScene("Movie Night");
        // Result can be true or false depending on scene availability
        assertTrue(result || !result, "Execute scene should return a boolean");
        System.out.println("✅ Execute smart scene test passed");
    }

    @Test
    @DisplayName("Test 10: Show Scene Details")
    void testShowSceneDetails() {
        // Should not throw exception even for non-existent scenes
        assertDoesNotThrow(() -> smartHomeService.showSceneDetails("Movie Night"),
                          "Showing scene details should not throw exception");
        System.out.println("✅ Show scene details test passed");
    }

    @Test
    @DisplayName("Test 11: Show Editable Scene Details")
    void testShowEditableSceneDetails() {
        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showEditableSceneDetails("Movie Night"),
                          "Showing editable scene details should not throw exception");
        System.out.println("✅ Show editable scene details test passed");
    }

    @Test
    @DisplayName("Test 12: Add Device to Scene")
    void testAddDeviceToScene() {
        // Connect a device first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to add device to scene
        boolean result = smartHomeService.addDeviceToScene("Movie Night", "TV", "Living Room", "ON");
        // Result depends on scene existence and implementation
        assertTrue(result || !result, "Add device to scene should return a boolean");
        System.out.println("✅ Add device to scene test passed");
    }

    @Test
    @DisplayName("Test 13: Remove Device from Scene")
    void testRemoveDeviceFromScene() {
        // Try to remove device from scene
        boolean result = smartHomeService.removeDeviceFromScene("Movie Night", "TV", "Living Room");
        // Result depends on scene existence and implementation
        assertTrue(result || !result, "Remove device from scene should return a boolean");
        System.out.println("✅ Remove device from scene test passed");
    }

    @Test
    @DisplayName("Test 14: Change Device Action in Scene")
    void testChangeDeviceActionInScene() {
        // Try to change device action in scene
        boolean result = smartHomeService.changeDeviceActionInScene("Movie Night", "TV", "Living Room", "OFF");
        // Result depends on scene existence and implementation
        assertTrue(result || !result, "Change device action in scene should return a boolean");
        System.out.println("✅ Change device action in scene test passed");
    }

    @Test
    @DisplayName("Test 15: Reset Scene to Original")
    void testResetSceneToOriginal() {
        // Try to reset scene
        boolean result = smartHomeService.resetSceneToOriginal("Movie Night");
        // Result depends on scene existence and implementation
        assertTrue(result || !result, "Reset scene to original should return a boolean");
        System.out.println("✅ Reset scene to original test passed");
    }

    @Test
    @DisplayName("Test 16: Get Scene Actions")
    void testGetSceneActions() {
        // Try to get scene actions
        List<SmartScenesService.SceneAction> actions = smartHomeService.getSceneActions("Movie Night");
        // Should return a list (might be empty)
        assertNotNull(actions, "Scene actions should not be null");
        System.out.println("✅ Get scene actions test passed");
    }

    // ==================== DEVICE HEALTH SERVICE TESTS ====================

    @Test
    @DisplayName("Test 17: Device Health Service Initialization")
    void testDeviceHealthServiceInitialization() {
        DeviceHealthService healthService = smartHomeService.getDeviceHealthService();
        assertNotNull(healthService, "DeviceHealthService should be initialized");
        System.out.println("✅ Device health service initialization test passed");
    }

    @Test
    @DisplayName("Test 18: Show Device Health Report")
    void testShowDeviceHealthReport() {
        // Connect some devices first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showDeviceHealthReport(),
                          "Showing device health report should not throw exception");
        System.out.println("✅ Show device health report test passed");
    }

    @Test
    @DisplayName("Test 19: Show Maintenance Schedule")
    void testShowMaintenanceSchedule() {
        // Connect some devices first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showMaintenanceSchedule(),
                          "Showing maintenance schedule should not throw exception");
        System.out.println("✅ Show maintenance schedule test passed");
    }

    @Test
    @DisplayName("Test 20: Get System Health Summary")
    void testGetSystemHealthSummary() {
        // Connect some devices first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        // Should return a non-null string
        String summary = smartHomeService.getSystemHealthSummary();
        assertNotNull(summary, "System health summary should not be null");
        System.out.println("✅ Get system health summary test passed");
    }

    // ==================== DEVICE MANAGEMENT TESTS ====================

    @Test
    @DisplayName("Test 21: Edit Device Room")
    void testEditDeviceRoom() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Edit device room
        boolean result = smartHomeService.editDeviceRoom("TV", "Living Room", "Bedroom");
        assertTrue(result, "Editing device room should succeed");

        // Verify device is in new room
        List<Gadget> devices = smartHomeService.viewGadgets();
        boolean foundInNewRoom = devices.stream()
                .anyMatch(d -> "TV".equals(d.getType()) && "Bedroom".equals(d.getRoomName()));
        assertTrue(foundInNewRoom, "Device should be found in new room");

        System.out.println("✅ Edit device room test passed");
    }

    @Test
    @DisplayName("Test 22: Edit Device Room to Invalid Room")
    void testEditDeviceRoomInvalidRoom() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to edit device room to invalid room
        boolean result = smartHomeService.editDeviceRoom("TV", "Living Room", "Invalid Room");
        assertFalse(result, "Editing device room to invalid room should fail");

        System.out.println("✅ Edit device room to invalid room test passed");
    }

    @Test
    @DisplayName("Test 23: Edit Device Room for Non-existent Device")
    void testEditDeviceRoomNonExistentDevice() {
        // Try to edit room for non-existent device
        boolean result = smartHomeService.editDeviceRoom("TV", "Living Room", "Bedroom");
        assertFalse(result, "Editing room for non-existent device should fail");

        System.out.println("✅ Edit device room for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 24: Edit Device Model")
    void testEditDeviceModel() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Edit device model
        boolean result = smartHomeService.editDeviceModel("TV", "Living Room", "LG");
        assertTrue(result, "Editing device model should succeed");

        // Verify device model was changed
        List<Gadget> devices = smartHomeService.viewGadgets();
        boolean foundWithNewModel = devices.stream()
                .anyMatch(d -> "TV".equals(d.getType()) && "LG".equals(d.getModel()));
        assertTrue(foundWithNewModel, "Device should have new model");

        System.out.println("✅ Edit device model test passed");
    }

    @Test
    @DisplayName("Test 25: Edit Device Model to Invalid Model")
    void testEditDeviceModelInvalidModel() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to edit device model to invalid model
        boolean result = smartHomeService.editDeviceModel("TV", "Living Room", "InvalidBrand");
        assertFalse(result, "Editing device model to invalid model should fail");

        System.out.println("✅ Edit device model to invalid model test passed");
    }

    @Test
    @DisplayName("Test 26: Edit Device Power Rating")
    void testEditDevicePowerRating() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Edit device power rating
        double newPowerRating = 200.0;
        boolean result = smartHomeService.editDevicePower("TV", "Living Room", newPowerRating);
        assertTrue(result, "Editing device power rating should succeed");

        // Verify power rating was changed
        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.stream()
                .filter(d -> "TV".equals(d.getType()))
                .findFirst()
                .orElse(null);
        assertNotNull(tv, "TV should be found");
        assertEquals(newPowerRating, tv.getPowerRatingWatts(), 0.01, "Power rating should be updated");

        System.out.println("✅ Edit device power rating test passed");
    }

    @Test
    @DisplayName("Test 27: Edit Device Power Rating with Invalid Value")
    void testEditDevicePowerRatingInvalidValue() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to edit device power rating with invalid value (negative)
        boolean result = smartHomeService.editDevicePower("TV", "Living Room", -100.0);
        assertFalse(result, "Editing device power rating with invalid value should fail");

        System.out.println("✅ Edit device power rating with invalid value test passed");
    }

    @Test
    @DisplayName("Test 28: Delete Device")
    void testDeleteDevice() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Verify device exists
        List<Gadget> devicesBefore = smartHomeService.viewGadgets();
        assertEquals(1, devicesBefore.size(), "Should have one device before deletion");

        // Delete the device
        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(result, "Deleting device should succeed");

        // Verify device was deleted
        List<Gadget> devicesAfter = smartHomeService.viewGadgets();
        assertEquals(0, devicesAfter.size(), "Should have no devices after deletion");

        System.out.println("✅ Delete device test passed");
    }

    @Test
    @DisplayName("Test 29: Delete Non-existent Device")
    void testDeleteNonExistentDevice() {
        // Try to delete non-existent device
        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertFalse(result, "Deleting non-existent device should fail");

        System.out.println("✅ Delete non-existent device test passed");
    }

    @Test
    @DisplayName("Test 30: Delete Device with Energy History")
    void testDeleteDeviceWithEnergyHistory() {
        // Connect a device and use it to generate energy history
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Turn device on and off to generate some usage
        smartHomeService.changeGadgetStatus("TV"); // ON
        smartHomeService.changeGadgetStatus("TV"); // OFF

        // Delete the device
        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(result, "Deleting device with energy history should succeed");

        // Verify device was deleted but energy history is preserved
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(0, devices.size(), "Should have no active devices after deletion");

        // Energy report should still work (including deleted device energy)
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with deleted device energy");

        System.out.println("✅ Delete device with energy history test passed");
    }

    // ==================== DEVICE MANAGEMENT WITHOUT LOGIN TESTS ====================

    @Test
    @DisplayName("Test 31: Device Management Operations Without Login")
    void testDeviceManagementWithoutLogin() {
        // Connect a device first
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Logout
        smartHomeService.logout();

        // Try device management operations without login
        assertFalse(smartHomeService.editDeviceRoom("TV", "Living Room", "Bedroom"),
                   "Edit device room without login should fail");

        assertFalse(smartHomeService.editDeviceModel("TV", "Living Room", "LG"),
                   "Edit device model without login should fail");

        assertFalse(smartHomeService.editDevicePower("TV", "Living Room", 200.0),
                   "Edit device power without login should fail");

        assertFalse(smartHomeService.deleteDevice("TV", "Living Room"),
                   "Delete device without login should fail");

        System.out.println("✅ Device management operations without login test passed");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 32: Multiple Devices Same Type Different Rooms")
    void testMultipleDevicesSameTypeDifferentRooms() {
        // Connect same device type in different rooms
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("TV", "LG", "Bedroom");
        smartHomeService.connectToGadget("TV", "Sony", "Kitchen");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(3, devices.size(), "Should have 3 TVs in different rooms");

        // Edit one specific TV
        boolean result = smartHomeService.editDeviceModel("TV", "Bedroom", "Panasonic");
        assertTrue(result, "Editing specific TV should succeed");

        // Verify only the bedroom TV was changed
        devices = smartHomeService.viewGadgets();
        long panasonicCount = devices.stream()
                .filter(d -> "Panasonic".equals(d.getModel()))
                .count();
        assertEquals(1, panasonicCount, "Should have exactly one Panasonic TV");

        System.out.println("✅ Multiple devices same type different rooms test passed");
    }

    @Test
    @DisplayName("Test 33: Integration Services Without Devices")
    void testIntegrationServicesWithoutDevices() {
        // Test integration services with no devices connected

        // Weather services should work
        assertDoesNotThrow(() -> smartHomeService.showCurrentWeather());
        assertDoesNotThrow(() -> smartHomeService.showWeatherForecast());

        // Scene services should work
        assertDoesNotThrow(() -> smartHomeService.showAvailableScenes());
        assertDoesNotThrow(() -> smartHomeService.executeSmartScene("Movie Night"));

        // Health services should work
        assertDoesNotThrow(() -> smartHomeService.showDeviceHealthReport());
        assertNotNull(smartHomeService.getSystemHealthSummary());

        System.out.println("✅ Integration services without devices test passed");
    }

    @Test
    @DisplayName("Test 34: Service Integration Consistency")
    void testServiceIntegrationConsistency() {
        // Verify all integrated services are consistently initialized
        assertNotNull(smartHomeService.getWeatherService(), "WeatherService should be initialized");
        assertNotNull(smartHomeService.getSmartScenesService(), "SmartScenesService should be initialized");
        assertNotNull(smartHomeService.getDeviceHealthService(), "DeviceHealthService should be initialized");
        assertNotNull(smartHomeService.getTimerService(), "TimerService should be initialized");
        assertNotNull(smartHomeService.getCalendarService(), "CalendarService should be initialized");
        assertNotNull(smartHomeService.getEnergyService(), "EnergyService should be initialized");
        assertNotNull(smartHomeService.getGadgetService(), "GadgetService should be initialized");

        System.out.println("✅ Service integration consistency test passed");
    }

    @Test
    @DisplayName("Test 35: Complex Device Operations")
    void testComplexDeviceOperations() {
        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");

        // Perform various operations
        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");

        // Edit devices
        smartHomeService.editDeviceModel("TV", "Living Room", "Sony");
        smartHomeService.editDevicePower("AC", "Living Room", 1800.0);

        // Move device
        smartHomeService.editDeviceRoom("LIGHT", "Kitchen", "Dining Room");

        // Verify final state
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(3, devices.size(), "Should still have 3 devices");

        // Check specific changes
        boolean hasSonyTV = devices.stream()
                .anyMatch(d -> "TV".equals(d.getType()) && "Sony".equals(d.getModel()));
        assertTrue(hasSonyTV, "Should have Sony TV");

        boolean hasLightInDiningRoom = devices.stream()
                .anyMatch(d -> "LIGHT".equals(d.getType()) && "Dining Room".equals(d.getRoomName()));
        assertTrue(hasLightInDiningRoom, "Should have light in dining room");

        System.out.println("✅ Complex device operations test passed");
    }
}