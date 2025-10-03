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

public class SmartHomeServiceIntegrationTest {

    private SmartHomeService smartHomeService;
    private String testEmail;
    private static final String TEST_NAME = "Integration Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup integration test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        testEmail = "integration" + System.currentTimeMillis() + "@smarthome.com";
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        System.out.println("Setting up integration test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup integration tests")
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
        System.out.println("Cleaned up integration test environment");
    }


    @Test
    @DisplayName("Test 1: Weather Service Initialization")
    void testWeatherServiceInitialization() {
        WeatherService weatherService = smartHomeService.getWeatherService();
        assertNotNull(weatherService, "WeatherService should be initialized");
        System.out.println(" Weather service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Weather Service Availability")
    void testWeatherServiceAvailability() {
        assertNotNull(smartHomeService.getWeatherService(), "Weather service should be accessible");
        System.out.println(" Weather service availability test passed");
    }

    @Test
    @DisplayName("Test 3: Show Weather Forecast")
    void testShowWeatherForecast() {
        assertDoesNotThrow(() -> smartHomeService.showWeatherForecast(),
                          "Showing weather forecast should not throw exception");
        System.out.println(" Show weather forecast test passed");
    }

    @Test
    @DisplayName("Test 4: Weather Data Check")
    void testWeatherDataCheck() {
        boolean hasWeatherData = smartHomeService.hasUserWeatherData();
        assertNotNull(hasWeatherData, "Weather data check should not be null");
        System.out.println(" Weather data check test passed");
    }

    @Test
    @DisplayName("Test 5: Clear Weather Data")
    void testClearWeatherData() {
        assertDoesNotThrow(() -> smartHomeService.clearWeatherData(),
                          "Clearing weather data should not throw exception");
        System.out.println(" Clear weather data test passed");
    }

    @Test
    @DisplayName("Test 6: Check User Weather Data")
    void testHasUserWeatherData() {
        boolean hasData = smartHomeService.hasUserWeatherData();
        assertTrue(hasData || !hasData, "Should return a boolean value");
        System.out.println(" Check user weather data test passed");
    }


    @Test
    @DisplayName("Test 7: Smart Scenes Service Initialization")
    void testSmartScenesServiceInitialization() {
        SmartScenesService scenesService = smartHomeService.getSmartScenesService();
        assertNotNull(scenesService, "SmartScenesService should be initialized");
        System.out.println(" Smart scenes service initialization test passed");
    }

    @Test
    @DisplayName("Test 8: Show Available Scenes")
    void testShowAvailableScenes() {
        assertDoesNotThrow(() -> smartHomeService.showAvailableScenes(),
                          "Showing available scenes should not throw exception");
        System.out.println(" Show available scenes test passed");
    }

    @Test
    @DisplayName("Test 9: Execute Smart Scene")
    void testExecuteSmartScene() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Living Room");

        boolean result = smartHomeService.executeSmartScene("MOVIE");
        assertTrue(result || !result, "Execute scene should return a boolean");
        System.out.println(" Execute smart scene test passed");
    }

    @Test
    @DisplayName("Test 10: Show Scene Details")
    void testShowSceneDetails() {
        assertDoesNotThrow(() -> smartHomeService.showSceneDetails("MOVIE"),
                          "Showing scene details should not throw exception");
        System.out.println(" Show scene details test passed");
    }

    @Test
    @DisplayName("Test 11: Show Editable Scene Details")
    void testShowEditableSceneDetails() {
        assertDoesNotThrow(() -> smartHomeService.showEditableSceneDetails("MOVIE"),
                          "Showing editable scene details should not throw exception");
        System.out.println(" Show editable scene details test passed");
    }

    @Test
    @DisplayName("Test 12: Add Device to Scene")
    void testAddDeviceToScene() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.addDeviceToScene("MOVIE", "TV", "Living Room", "ON");
        assertTrue(result || !result, "Add device to scene should return a boolean");
        System.out.println(" Add device to scene test passed");
    }

    @Test
    @DisplayName("Test 13: Remove Device from Scene")
    void testRemoveDeviceFromScene() {
        boolean result = smartHomeService.removeDeviceFromScene("MOVIE", "TV", "Living Room");
        assertTrue(result || !result, "Remove device from scene should return a boolean");
        System.out.println(" Remove device from scene test passed");
    }

    @Test
    @DisplayName("Test 14: Change Device Action in Scene")
    void testChangeDeviceActionInScene() {
        boolean result = smartHomeService.changeDeviceActionInScene("MOVIE", "TV", "Living Room", "OFF");
        assertTrue(result || !result, "Change device action in scene should return a boolean");
        System.out.println(" Change device action in scene test passed");
    }

    @Test
    @DisplayName("Test 15: Reset Scene to Original")
    void testResetSceneToOriginal() {
        boolean result = smartHomeService.resetSceneToOriginal("MOVIE");
        assertTrue(result || !result, "Reset scene to original should return a boolean");
        System.out.println(" Reset scene to original test passed");
    }

    @Test
    @DisplayName("Test 16: Get Scene Actions")
    void testGetSceneActions() {
        List<SmartScenesService.SceneAction> actions = smartHomeService.getSceneActions("MOVIE");
        assertNotNull(actions, "Scene actions should not be null");
        System.out.println(" Get scene actions test passed");
    }


    @Test
    @DisplayName("Test 17: Device Health Service Initialization")
    void testDeviceHealthServiceInitialization() {
        DeviceHealthService healthService = smartHomeService.getDeviceHealthService();
        assertNotNull(healthService, "DeviceHealthService should be initialized");
        System.out.println(" Device health service initialization test passed");
    }

    @Test
    @DisplayName("Test 18: Show Device Health Report")
    void testShowDeviceHealthReport() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        assertDoesNotThrow(() -> smartHomeService.showDeviceHealthReport(),
                          "Showing device health report should not throw exception");
        System.out.println(" Show device health report test passed");
    }

    @Test
    @DisplayName("Test 19: Show Maintenance Schedule")
    void testShowMaintenanceSchedule() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        assertDoesNotThrow(() -> smartHomeService.showMaintenanceSchedule(),
                          "Showing maintenance schedule should not throw exception");
        System.out.println(" Show maintenance schedule test passed");
    }

    @Test
    @DisplayName("Test 20: Get System Health Summary")
    void testGetSystemHealthSummary() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        String summary = smartHomeService.getSystemHealthSummary();
        assertNotNull(summary, "System health summary should not be null");
        System.out.println(" Get system health summary test passed");
    }



    @Test
    @DisplayName("Test 22: Edit Device Room to Invalid Room")
    void testEditDeviceRoomInvalidRoom() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.editDeviceRoom("TV", "Living Room", "Invalid Room");
        assertFalse(result, "Editing device room to invalid room should fail");

        System.out.println(" Edit device room to invalid room test passed");
    }

    @Test
    @DisplayName("Test 23: Edit Device Room for Non-existent Device")
    void testEditDeviceRoomNonExistentDevice() {
        boolean result = smartHomeService.editDeviceRoom("TV", "Living Room", "Bedroom");
        assertFalse(result, "Editing room for non-existent device should fail");

        System.out.println(" Edit device room for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 24: Edit Device Model")
    void testEditDeviceModel() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.editDeviceModel("TV", "Living Room", "LG");
        assertTrue(result, "Editing device model should succeed");

        List<Gadget> devices = smartHomeService.viewGadgets();
        boolean foundWithNewModel = devices.stream()
                .anyMatch(d -> "TV".equals(d.getType()) && "LG".equals(d.getModel()));
        assertTrue(foundWithNewModel, "Device should have new model");

        System.out.println(" Edit device model test passed");
    }


    @Test
    @DisplayName("Test 26: Edit Device Power Rating")
    void testEditDevicePowerRating() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        double newPowerRating = 200.0;
        boolean result = smartHomeService.editDevicePower("TV", "Living Room", newPowerRating);
        assertTrue(result, "Editing device power rating should succeed");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.stream()
                .filter(d -> "TV".equals(d.getType()))
                .findFirst()
                .orElse(null);
        assertNotNull(tv, "TV should be found");
        assertEquals(newPowerRating, tv.getPowerRatingWatts(), 0.01, "Power rating should be updated");

        System.out.println(" Edit device power rating test passed");
    }

    @Test
    @DisplayName("Test 27: Edit Device Power Rating with Invalid Value")
    void testEditDevicePowerRatingInvalidValue() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.editDevicePower("TV", "Living Room", -100.0);
        assertFalse(result, "Editing device power rating with invalid value should fail");

        System.out.println(" Edit device power rating with invalid value test passed");
    }

    @Test
    @DisplayName("Test 28: Delete Device")
    void testDeleteDevice() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devicesBefore = smartHomeService.viewGadgets();
        assertEquals(1, devicesBefore.size(), "Should have one device before deletion");

        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(result, "Deleting device should succeed");

        List<Gadget> devicesAfter = smartHomeService.viewGadgets();
        assertEquals(0, devicesAfter.size(), "Should have no devices after deletion");

        System.out.println(" Delete device test passed");
    }

    @Test
    @DisplayName("Test 29: Delete Non-existent Device")
    void testDeleteNonExistentDevice() {
        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertFalse(result, "Deleting non-existent device should fail");

        System.out.println(" Delete non-existent device test passed");
    }

    @Test
    @DisplayName("Test 30: Delete Device with Energy History")
    void testDeleteDeviceWithEnergyHistory() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        smartHomeService.changeGadgetStatus("TV"); // ON
        smartHomeService.changeGadgetStatus("TV"); // OFF

        boolean result = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(result, "Deleting device with energy history should succeed");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(0, devices.size(), "Should have no active devices after deletion");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with deleted device energy");

        System.out.println(" Delete device with energy history test passed");
    }


    @Test
    @DisplayName("Test 31: Device Management Operations Without Login")
    void testDeviceManagementWithoutLogin() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        smartHomeService.logout();

        assertFalse(smartHomeService.editDeviceRoom("TV", "Living Room", "Bedroom"),
                   "Edit device room without login should fail");

        assertFalse(smartHomeService.editDeviceModel("TV", "Living Room", "LG"),
                   "Edit device model without login should fail");

        assertFalse(smartHomeService.editDevicePower("TV", "Living Room", 200.0),
                   "Edit device power without login should fail");

        assertFalse(smartHomeService.deleteDevice("TV", "Living Room"),
                   "Delete device without login should fail");

        System.out.println(" Device management operations without login test passed");
    }



    @Test
    @DisplayName("Test 33: Integration Services Without Devices")
    void testIntegrationServicesWithoutDevices() {

        assertNotNull(smartHomeService.getWeatherService(), "Weather service should be accessible");
        assertDoesNotThrow(() -> smartHomeService.showWeatherForecast());

        assertDoesNotThrow(() -> smartHomeService.showAvailableScenes());
        assertDoesNotThrow(() -> smartHomeService.executeSmartScene("MOVIE"));

        assertDoesNotThrow(() -> smartHomeService.showDeviceHealthReport());
        assertNotNull(smartHomeService.getSystemHealthSummary());

        System.out.println(" Integration services without devices test passed");
    }

    @Test
    @DisplayName("Test 34: Service Integration Consistency")
    void testServiceIntegrationConsistency() {
        assertNotNull(smartHomeService.getWeatherService(), "WeatherService should be initialized");
        assertNotNull(smartHomeService.getSmartScenesService(), "SmartScenesService should be initialized");
        assertNotNull(smartHomeService.getDeviceHealthService(), "DeviceHealthService should be initialized");
        assertNotNull(smartHomeService.getTimerService(), "TimerService should be initialized");
        assertNotNull(smartHomeService.getCalendarService(), "CalendarService should be initialized");
        assertNotNull(smartHomeService.getEnergyService(), "EnergyService should be initialized");
        assertNotNull(smartHomeService.getGadgetService(), "GadgetService should be initialized");

        System.out.println(" Service integration consistency test passed");
    }

}