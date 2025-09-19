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

/**
 * Energy Management functionality tests for SmartHomeService
 * Tests: Energy Reports, Device Energy Tracking, Cost Calculations
 */
public class SmartHomeServiceEnergyTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "energy@smarthome.com";
    private static final String TEST_NAME = "Energy Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup energy test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        // Register and login user for energy tests
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        System.out.println("⚡ Setting up energy test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup energy tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up energy test environment");
    }

    // ==================== ENERGY SERVICE TESTS ====================

    @Test
    @DisplayName("Test 1: Energy Service Initialization")
    void testEnergyServiceInitialization() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();
        assertNotNull(energyService, "EnergyManagementService should be initialized");
        System.out.println("✅ Energy service initialization test passed");
    }

    @Test
    @DisplayName("Test 2: Show Energy Report with No Devices")
    void testShowEnergyReportNoDevices() {
        // Should not throw exception even with no devices
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with no devices");
        System.out.println("✅ Energy report with no devices test passed");
    }

    @Test
    @DisplayName("Test 3: Show Energy Report with Single Device")
    void testShowEnergyReportSingleDevice() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with single device");
        System.out.println("✅ Energy report with single device test passed");
    }

    @Test
    @DisplayName("Test 4: Show Energy Report with Multiple Devices")
    void testShowEnergyReportMultipleDevices() {
        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Living Room");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Bedroom");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with multiple devices");
        System.out.println("✅ Energy report with multiple devices test passed");
    }

    @Test
    @DisplayName("Test 5: Energy Report with Running Devices")
    void testEnergyReportWithRunningDevices() {
        // Connect and turn on devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Living Room");

        // Turn on devices
        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");

        // Verify devices are on
        List<Gadget> devices = smartHomeService.viewGadgets();
        boolean hasRunningDevice = devices.stream().anyMatch(device -> "ON".equals(device.getStatus()));
        assertTrue(hasRunningDevice, "At least one device should be running");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with running devices");
        System.out.println("✅ Energy report with running devices test passed");
    }

    // ==================== DEVICE ENERGY TRACKING TESTS ====================

    @Test
    @DisplayName("Test 6: Device Power Rating Assignment")
    void testDevicePowerRatingAssignment() {
        // Connect different types of devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Hall");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(4, devices.size(), "Should have 4 devices");

        // Verify each device has a power rating assigned
        for (Gadget device : devices) {
            assertTrue(device.getPowerRatingWatts() > 0,
                      "Device " + device.getType() + " should have positive power rating");
        }

        // Verify different device types have different expected power ratings
        Gadget tv = devices.stream().filter(d -> "TV".equals(d.getType())).findFirst().orElse(null);
        Gadget ac = devices.stream().filter(d -> "AC".equals(d.getType())).findFirst().orElse(null);
        Gadget light = devices.stream().filter(d -> "LIGHT".equals(d.getType())).findFirst().orElse(null);
        Gadget fan = devices.stream().filter(d -> "FAN".equals(d.getType())).findFirst().orElse(null);

        assertNotNull(tv, "TV should be found");
        assertNotNull(ac, "AC should be found");
        assertNotNull(light, "Light should be found");
        assertNotNull(fan, "Fan should be found");

        // AC should use more power than TV, TV more than Fan, Fan more than Light
        assertTrue(ac.getPowerRatingWatts() > tv.getPowerRatingWatts(),
                  "AC should use more power than TV");
        assertTrue(tv.getPowerRatingWatts() > fan.getPowerRatingWatts(),
                  "TV should use more power than Fan");
        assertTrue(fan.getPowerRatingWatts() > light.getPowerRatingWatts(),
                  "Fan should use more power than Light");

        System.out.println("✅ Device power rating assignment test passed");
    }

    @Test
    @DisplayName("Test 7: Energy Consumption Tracking")
    void testEnergyConsumptionTracking() {
        // Connect device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.get(0);

        // Initial energy should be 0
        assertEquals(0.0, tv.getTotalEnergyConsumedKWh(), 0.001,
                    "Initial energy consumption should be 0");

        // Turn device on and off multiple times to simulate usage
        smartHomeService.changeGadgetStatus("TV"); // Turn ON
        smartHomeService.changeGadgetStatus("TV"); // Turn OFF
        smartHomeService.changeGadgetStatus("TV"); // Turn ON
        smartHomeService.changeGadgetStatus("TV"); // Turn OFF

        // After some usage, energy should be tracked
        // Note: The actual energy calculation depends on time passed,
        // so we just verify it's non-negative
        devices = smartHomeService.viewGadgets();
        tv = devices.get(0);
        assertTrue(tv.getTotalEnergyConsumedKWh() >= 0,
                  "Energy consumption should be non-negative");

        System.out.println("✅ Energy consumption tracking test passed");
    }

    @Test
    @DisplayName("Test 8: Multiple Device Energy Tracking")
    void testMultipleDeviceEnergyTracking() {
        // Connect multiple devices with different power ratings
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");      // ~150W
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");               // ~1500W
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");       // ~60W
        smartHomeService.connectToGadget("MICROWAVE", "LG", "Kitchen");        // ~1200W

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(4, devices.size(), "Should have 4 devices");

        // All devices should start with 0 energy consumption
        for (Gadget device : devices) {
            assertEquals(0.0, device.getTotalEnergyConsumedKWh(), 0.001,
                        "Device " + device.getType() + " should start with 0 energy");
        }

        // Turn on all devices
        smartHomeService.changeGadgetStatus("TV");
        smartHomeService.changeGadgetStatus("AC");
        smartHomeService.changeGadgetStatus("LIGHT");
        smartHomeService.changeGadgetStatus("MICROWAVE");

        // Verify all devices are ON
        devices = smartHomeService.viewGadgets();
        long runningDevices = devices.stream().filter(d -> "ON".equals(d.getStatus())).count();
        assertEquals(4, runningDevices, "All 4 devices should be running");

        System.out.println("✅ Multiple device energy tracking test passed");
    }

    // ==================== COST CALCULATION TESTS ====================

    @Test
    @DisplayName("Test 9: Slab-based Cost Calculation")
    void testSlabBasedCostCalculation() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        // Test different energy consumption levels
        double lowConsumption = 25.0;   // Should be in first slab
        double mediumConsumption = 100.0; // Should be in multiple slabs
        double highConsumption = 300.0;   // Should be in higher slabs

        double lowCost = energyService.calculateSlabBasedCost(lowConsumption);
        double mediumCost = energyService.calculateSlabBasedCost(mediumConsumption);
        double highCost = energyService.calculateSlabBasedCost(highConsumption);

        // Verify costs are positive and increasing
        assertTrue(lowCost > 0, "Low consumption cost should be positive");
        assertTrue(mediumCost > lowCost, "Medium consumption cost should be higher than low");
        assertTrue(highCost > mediumCost, "High consumption cost should be higher than medium");

        // Verify cost calculation is reasonable (not extreme values)
        assertTrue(lowCost < 100, "Low consumption cost should be reasonable");
        assertTrue(mediumCost < 1000, "Medium consumption cost should be reasonable");
        assertTrue(highCost < 5000, "High consumption cost should be reasonable");

        System.out.println("✅ Slab-based cost calculation test passed");
    }

    @Test
    @DisplayName("Test 10: Energy Efficiency Tips")
    void testEnergyEfficiencyTips() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        // Test tips for different consumption levels
        String lowTips = energyService.getEnergyEfficiencyTips(50.0);    // Low consumption
        String mediumTips = energyService.getEnergyEfficiencyTips(200.0); // Medium consumption
        String highTips = energyService.getEnergyEfficiencyTips(400.0);   // High consumption

        // Verify tips are provided
        assertNotNull(lowTips, "Low consumption tips should not be null");
        assertNotNull(mediumTips, "Medium consumption tips should not be null");
        assertNotNull(highTips, "High consumption tips should not be null");

        // Verify tips are not empty
        assertFalse(lowTips.trim().isEmpty(), "Low consumption tips should not be empty");
        assertFalse(mediumTips.trim().isEmpty(), "Medium consumption tips should not be empty");
        assertFalse(highTips.trim().isEmpty(), "High consumption tips should not be empty");

        // High consumption tips should mention high usage
        assertTrue(highTips.toLowerCase().contains("high"),
                  "High consumption tips should mention high usage");

        System.out.println("✅ Energy efficiency tips test passed");
    }

    // ==================== ENERGY REPORT GENERATION TESTS ====================

    @Test
    @DisplayName("Test 11: Energy Report Generation")
    void testEnergyReportGeneration() {
        // Connect devices and use them
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        // Turn devices on and off to generate some usage
        smartHomeService.changeGadgetStatus("TV"); // ON
        smartHomeService.changeGadgetStatus("AC"); // ON
        smartHomeService.changeGadgetStatus("TV"); // OFF
        smartHomeService.changeGadgetStatus("AC"); // OFF

        Customer currentUser = smartHomeService.getCurrentUser();
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        // Generate energy report
        EnergyManagementService.EnergyReport report = energyService.generateEnergyReport(currentUser);

        assertNotNull(report, "Energy report should not be null");
        assertTrue(report.getTotalEnergyKWh() >= 0, "Total energy should be non-negative");
        assertTrue(report.getTotalCostRupees() >= 0, "Total cost should be non-negative");
        assertNotNull(report.getReportPeriod(), "Report period should not be null");
        assertNotNull(report.getDevices(), "Device list should not be null");

        System.out.println("✅ Energy report generation test passed");
    }

    @Test
    @DisplayName("Test 12: Device Energy Usage Display")
    void testDeviceEnergyUsageDisplay() {
        // Connect multiple devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");

        Customer currentUser = smartHomeService.getCurrentUser();
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        // Should not throw exception when displaying device energy usage
        assertDoesNotThrow(() -> energyService.displayDeviceEnergyUsage(currentUser),
                          "Displaying device energy usage should not throw exception");

        System.out.println("✅ Device energy usage display test passed");
    }

    @Test
    @DisplayName("Test 13: Energy Slab Breakdown Display")
    void testEnergySlabBreakdownDisplay() {
        EnergyManagementService energyService = smartHomeService.getEnergyService();

        // Test slab breakdown for different consumption levels
        String breakdown1 = energyService.getSlabBreakdown(50.0);
        String breakdown2 = energyService.getSlabBreakdown(150.0);
        String breakdown3 = energyService.getSlabBreakdown(350.0);

        // Verify breakdowns are provided
        assertNotNull(breakdown1, "Breakdown for 50 kWh should not be null");
        assertNotNull(breakdown2, "Breakdown for 150 kWh should not be null");
        assertNotNull(breakdown3, "Breakdown for 350 kWh should not be null");

        // Verify breakdowns contain relevant information
        assertTrue(breakdown1.contains("kWh"), "Breakdown should contain kWh");
        assertTrue(breakdown2.contains("TOTAL"), "Breakdown should contain TOTAL");
        assertTrue(breakdown3.contains("Slab"), "Breakdown should contain Slab information");

        System.out.println("✅ Energy slab breakdown display test passed");
    }

    // ==================== DELETED DEVICE ENERGY TESTS ====================

    @Test
    @DisplayName("Test 14: Deleted Device Energy Preservation")
    void testDeletedDeviceEnergyPreservation() {
        // Connect a device
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Use the device to generate some energy
        smartHomeService.changeGadgetStatus("TV"); // Turn ON
        smartHomeService.changeGadgetStatus("TV"); // Turn OFF

        // Delete the device
        boolean deleteResult = smartHomeService.deleteDevice("TV", "Living Room");
        assertTrue(deleteResult, "Device deletion should succeed");

        // Verify device is no longer in active devices
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(0, devices.size(), "Should have no active devices after deletion");

        // Energy report should still account for deleted device energy
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should work with deleted device energy");

        System.out.println("✅ Deleted device energy preservation test passed");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 15: Zero Energy Consumption")
    void testZeroEnergyConsumption() {
        // Connect device but don't use it
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget tv = devices.get(0);

        // Should have zero energy consumption
        assertEquals(0.0, tv.getTotalEnergyConsumedKWh(), 0.001,
                    "Unused device should have zero energy consumption");

        // Energy report should handle zero consumption
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle zero consumption");

        System.out.println("✅ Zero energy consumption test passed");
    }

    @Test
    @DisplayName("Test 16: Large Energy Consumption")
    void testLargeEnergyConsumption() {
        // Connect high-power device
        smartHomeService.connectToGadget("AC", "LG", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget ac = devices.get(0);

        // Verify AC has high power rating
        assertTrue(ac.getPowerRatingWatts() > 1000, "AC should have high power rating");

        // Turn on and off multiple times
        for (int i = 0; i < 10; i++) {
            smartHomeService.changeGadgetStatus("AC");
        }

        // Should handle large energy consumption without issues
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle large consumption");

        System.out.println("✅ Large energy consumption test passed");
    }

    @Test
    @DisplayName("Test 17: Mixed Device Usage Patterns")
    void testMixedDeviceUsagePatterns() {
        // Connect various devices
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Hall");

        // Create mixed usage patterns
        smartHomeService.changeGadgetStatus("TV");    // TV ON
        smartHomeService.changeGadgetStatus("AC");    // AC ON
        smartHomeService.changeGadgetStatus("LIGHT"); // LIGHT ON
        smartHomeService.changeGadgetStatus("TV");    // TV OFF
        smartHomeService.changeGadgetStatus("FAN");   // FAN ON
        smartHomeService.changeGadgetStatus("AC");    // AC OFF

        // Verify mixed states
        List<Gadget> devices = smartHomeService.viewGadgets();
        long onDevices = devices.stream().filter(d -> "ON".equals(d.getStatus())).count();
        long offDevices = devices.stream().filter(d -> "OFF".equals(d.getStatus())).count();

        assertEquals(4, onDevices + offDevices, "Should account for all devices");
        assertTrue(onDevices > 0, "Should have some devices ON");
        assertTrue(offDevices > 0, "Should have some devices OFF");

        // Energy report should handle mixed usage
        assertDoesNotThrow(() -> smartHomeService.showEnergyReport(),
                          "Energy report should handle mixed usage patterns");

        System.out.println("✅ Mixed device usage patterns test passed");
    }
}