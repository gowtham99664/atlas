package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import com.smarthome.service.EnergyManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SmartHomeServiceEnergyTest {

    private SmartHomeService smartHomeService;
    private String testEmail;
    private static final String TEST_NAME = "Energy Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup energy test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        testEmail = "energy" + System.currentTimeMillis() + "@smarthome.com";
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        System.out.println("Setting up energy test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup energy tests")
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
        System.out.println("Cleaned up energy test environment");
    }


    @Test
    @DisplayName("Test 1: Energy Service Initialization")
    void testEnergyServiceInitialization() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();
        assertNotNull(energyService, "EnergyManagementService should be initialized");
        System.out.println(" Energy service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Show Energy Report with No Devices")
    void testShowEnergyReportNoDevices() {
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with no devices");
        System.out.println(" Energy report with no devices test passed");
    }

    @Test
    @DisplayName("Test 3: Show Energy Report with Single Device")
    void testShowEnergyReportSingleDevice() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with single device");
        System.out.println(" Energy report with single device test passed");
    }

    @Test
    @DisplayName("Test 4: Show Energy Report with Multiple Devices")
    void testShowEnergyReportMultipleDevices() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Bedroom");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with multiple devices");
        System.out.println(" Energy report with multiple devices test passed");
    }

    @Test
    @DisplayName("Test 5: Energy Report with Running Devices")
    void testEnergyReportWithRunningDevices() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Living Room");

        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");

        List<Gadget> devices = smartHomeService.viewGadgets();
        boolean hasRunningDevice = devices.stream().anyMatch(device -> "ON".equals(device.getStatus()));
        assertTrue(hasRunningDevice, "At least one device should be running");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with running devices");
        System.out.println(" Energy report with running devices test passed");
    }


    @Test
    @DisplayName("Test 6: Device Power Rating Assignment")
    void testDevicePowerRatingAssignment() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Master Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Study Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(4, devices.size(), "Should have 4 devices");

        for (Gadget device : devices) {
            assertTrue(device.getPowerRatingWatts() > 0,
                      "Device " + device.getType() + " should have positive power rating");
        }

        Gadget tv = devices.stream().filter(d -> "TV".equals(d.getType())).findFirst().orElse(null);
        Gadget ac = devices.stream().filter(d -> "AC".equals(d.getType())).findFirst().orElse(null);
        Gadget light = devices.stream().filter(d -> "LIGHT".equals(d.getType())).findFirst().orElse(null);
        Gadget fan = devices.stream().filter(d -> "FAN".equals(d.getType())).findFirst().orElse(null);

        assertNotNull(tv, "TV should be found");
        assertNotNull(ac, "AC should be found");
        assertNotNull(light, "Light should be found");
        assertNotNull(fan, "Fan should be found");

        assertTrue(ac.getPowerRatingWatts() > tv.getPowerRatingWatts(),
                  "AC should use more power than TV");
        assertTrue(tv.getPowerRatingWatts() > fan.getPowerRatingWatts(),
                  "TV should use more power than Fan");
        assertTrue(fan.getPowerRatingWatts() > light.getPowerRatingWatts(),
                  "Fan should use more power than Light");

        System.out.println(" Device power rating assignment test passed");
    }

    @Test
    @DisplayName("Test 7: Energy Consumption Tracking")
    void testEnergyConsumptionTracking() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.get(0);

        assertEquals(0.0, tv.getTotalEnergyConsumedKWh(), 0.001,
                    "Initial energy consumption should be 0");

        smartHomeService.changeGadgetStatus("TV"); // Turn ON
        smartHomeService.changeGadgetStatus("TV"); // Turn OFF
        smartHomeService.changeGadgetStatus("TV"); // Turn ON
        smartHomeService.changeGadgetStatus("TV"); // Turn OFF

        devices = smartHomeService.viewGadgets();
        tv = devices.get(0);
        assertTrue(tv.getTotalEnergyConsumedKWh() >= 0,
                  "Energy consumption should be non-negative");

        System.out.println(" Energy consumption tracking test passed");
    }

    @Test
    @DisplayName("Test 8: Multiple Device Energy Tracking")
    void testMultipleDeviceEnergyTracking() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Master Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");
        smartHomeService.connectToGadget("GEYSER", "Bajaj", "Kitchen");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(4, devices.size(), "Should have 4 devices");

        for (Gadget device : devices) {
            assertEquals(0.0, device.getTotalEnergyConsumedKWh(), 0.001,
                        "Device " + device.getType() + " should start with 0 energy");
        }

        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");
        smartHomeService.changeGadgetStatus("LIGHT");
        smartHomeService.changeGadgetStatus("GEYSER");

        devices = smartHomeService.viewGadgets();
        long runningDevices = devices.stream().filter(d -> "ON".equals(d.getStatus())).count();
        assertEquals(4, runningDevices, "All 4 devices should be running");

        System.out.println(" Multiple device energy tracking test passed");
    }


    @Test
    @DisplayName("Test 9: Slab-based Cost Calculation")
    void testSlabBasedCostCalculation() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        double lowConsumption = 25.0;
        double mediumConsumption = 100.0;
        double highConsumption = 300.0;

        double lowCost = energyService.calculateSlabBasedCost(lowConsumption);
        double mediumCost = energyService.calculateSlabBasedCost(mediumConsumption);
        double highCost = energyService.calculateSlabBasedCost(highConsumption);

        assertTrue(lowCost > 0, "Low consumption cost should be positive");
        assertTrue(mediumCost > lowCost, "Medium consumption cost should be higher than low");
        assertTrue(highCost > mediumCost, "High consumption cost should be higher than medium");

        assertTrue(lowCost < 100, "Low consumption cost should be reasonable");
        assertTrue(mediumCost < 1000, "Medium consumption cost should be reasonable");
        assertTrue(highCost < 5000, "High consumption cost should be reasonable");

        System.out.println(" Slab-based cost calculation test passed");
    }

    @Test
    @DisplayName("Test 10: Energy Efficiency Tips")
    void testEnergyEfficiencyTips() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        String lowTips = energyService.getEnergyEfficiencyTips(50.0);
        String mediumTips = energyService.getEnergyEfficiencyTips(200.0);
        String highTips = energyService.getEnergyEfficiencyTips(400.0);

        assertNotNull(lowTips, "Low consumption tips should not be null");
        assertNotNull(mediumTips, "Medium consumption tips should not be null");
        assertNotNull(highTips, "High consumption tips should not be null");

        assertFalse(lowTips.trim().isEmpty(), "Low consumption tips should not be empty");
        assertFalse(mediumTips.trim().isEmpty(), "Medium consumption tips should not be empty");
        assertFalse(highTips.trim().isEmpty(), "High consumption tips should not be empty");

        assertTrue(highTips.toLowerCase().contains("high"),
                  "High consumption tips should mention high usage");

        System.out.println(" Energy efficiency tips test passed");
    }


    @Test
    @DisplayName("Test 11: Energy Report Generation")
    void testEnergyReportGeneration() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");
        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");

        Customer currentUser = smartHomeService.getCurrentUser();
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        EnergyManagementService.EnergyReport report = energyService.generateEnergyReport(currentUser);

        assertNotNull(report, "Energy report should not be null");
        assertTrue(report.getTotalEnergyKWh() >= 0, "Total energy should be non-negative");
        assertTrue(report.getTotalCostRupees() >= 0, "Total cost should be non-negative");
        assertNotNull(report.getReportPeriod(), "Report period should not be null");
        assertNotNull(report.getDevices(), "Device list should not be null");

        System.out.println(" Energy report generation test passed");
    }

    @Test
    @DisplayName("Test 12: Device Energy Usage Display")
    void testDeviceEnergyUsageDisplay() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");

        Customer currentUser = smartHomeService.getCurrentUser();
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        assertDoesNotThrow(() -> energyService.displayDeviceEnergyUsage(currentUser),
                          "Displaying device energy usage should not throw exception");

        System.out.println(" Device energy usage display test passed");
    }

    @Test
    @DisplayName("Test 13: Energy Slab Breakdown Display")
    void testEnergySlabBreakdownDisplay() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        String breakdown1 = energyService.getSlabBreakdown(50.0);
        String breakdown2 = energyService.getSlabBreakdown(150.0);
        String breakdown3 = energyService.getSlabBreakdown(350.0);

        assertNotNull(breakdown1, "Breakdown for 50 kWh should not be null");
        assertNotNull(breakdown2, "Breakdown for 150 kWh should not be null");
        assertNotNull(breakdown3, "Breakdown for 350 kWh should not be null");

        assertTrue(breakdown1.contains("kWh"), "Breakdown should contain kWh");
        assertTrue(breakdown2.contains("TOTAL"), "Breakdown should contain TOTAL");
        assertTrue(breakdown3.contains("Slab"), "Breakdown should contain Slab information");

        System.out.println(" Energy slab breakdown display test passed");
    }


    @Test
    @DisplayName("Test 14: Deleted Device Energy Preservation")
    void testDeletedDeviceEnergyPreservation() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("TV");

        boolean deleteResult = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(deleteResult, "Device deletion should succeed");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(0, devices.size(), "Should have no active devices after deletion");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with deleted device energy");

        System.out.println(" Deleted device energy preservation test passed");
    }


    @Test
    @DisplayName("Test 15: Zero Energy Consumption")
    void testZeroEnergyConsumption() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.get(0);

        assertEquals(0.0, tv.getTotalEnergyConsumedKWh(), 0.001,
                    "Unused device should have zero energy consumption");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle zero consumption");

        System.out.println(" Zero energy consumption test passed");
    }

    @Test
    @DisplayName("Test 16: Large Energy Consumption")
    void testLargeEnergyConsumption() {
        smartHomeService.connectToGadget("AC", "LG", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget ac = devices.get(0);

        assertTrue(ac.getPowerRatingWatts() > 1000, "AC should have high power rating");

        for (int i = 0; i < 10; i++) {
            smartHomeService.changeGadgetStatus("AC");
        }

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle large consumption");

        System.out.println(" Large energy consumption test passed");
    }

    @Test
    @DisplayName("Test 17: Mixed Device Usage Patterns")
    void testMixedDeviceUsagePatterns() {
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Master Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Study Room");

        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");
        smartHomeService.changeGadgetStatus("LIGHT");
        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("FAN");
        smartHomeService.changeGadgetStatus("AC");

        List<Gadget> devices = smartHomeService.viewGadgets();
        long onDevices = devices.stream().filter(d -> "ON".equals(d.getStatus())).count();
        long offDevices = devices.stream().filter(d -> "OFF".equals(d.getStatus())).count();

        assertEquals(4, onDevices + offDevices, "Should account for all devices");
        assertTrue(onDevices > 0, "Should have some devices ON");
        assertTrue(offDevices > 0, "Should have some devices OFF");

        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle mixed usage patterns");

        System.out.println(" Mixed device usage patterns test passed");
    }
}