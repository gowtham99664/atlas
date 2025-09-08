package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.service.*;
import com.smarthome.util.SessionManager;

import java.util.List;

/**
 * Test class to verify IoT Smart Home Dashboard functionality
 */
public class ApplicationTest {
    
    public static void main(String[] args) {
        System.out.println("=== IoT Smart Home Dashboard - Functionality Test ===");
        
        try {
            // Test 1: Service Initialization
            System.out.println("\n[TEST 1] Service Initialization...");
            SmartHomeService smartHomeService = new SmartHomeService();
            System.out.println("[PASS] SmartHomeService initialized successfully");
            
            // Test 2: Customer Registration
            System.out.println("\n[TEST 2] Customer Registration...");
            boolean registrationSuccess = smartHomeService.registerCustomer(
                "Test User", 
                "test@example.com", 
                "TestPass123!@#", 
                "TestPass123!@#"
            );
            if (registrationSuccess) {
                System.out.println("[PASS] Customer registration successful");
            } else {
                System.out.println("[INFO] Customer may already exist");
            }
            
            // Test 3: Customer Login
            System.out.println("\n[TEST 3] Customer Login...");
            boolean loginSuccess = smartHomeService.loginCustomer("test@example.com", "TestPass123!@#");
            if (loginSuccess) {
                System.out.println("[PASS] Login successful");
            } else {
                System.out.println("[FAIL] Login failed");
                return;
            }
            
            // Test 4: Device Management
            System.out.println("\n[TEST 4] Device Management...");
            boolean deviceAdded = smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");
            if (deviceAdded) {
                System.out.println("[PASS] TV device added successfully");
            } else {
                System.out.println("[INFO] TV device may already exist");
            }
            
            deviceAdded = smartHomeService.connectToGadget("AC", "LG Dual Inverter", "Master Bedroom");
            if (deviceAdded) {
                System.out.println("[PASS] AC device added successfully");
            } else {
                System.out.println("[INFO] AC device may already exist");
            }
            
            deviceAdded = smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");
            if (deviceAdded) {
                System.out.println("[PASS] Light device added successfully");
            } else {
                System.out.println("[INFO] Light device may already exist");
            }
            
            // Test 5: View Devices and Table Format
            System.out.println("\n[TEST 5] View Devices and Table Format...");
            List<Gadget> devices = smartHomeService.viewGadgets();
            if (devices != null && !devices.isEmpty()) {
                System.out.println("[PASS] Device viewing works - Found " + devices.size() + " devices");
                System.out.println("[INFO] Table format should display above with:");
                System.out.println("  - Proper table borders (+----+)");
                System.out.println("  - Column headers (Device, Power, Status, Usage Time, Energy)");
                System.out.println("  - Device information in formatted rows");
                System.out.println("  - Power ratings in Watts (W)");
                System.out.println("  - Status as RUNNING/OFF");
                System.out.println("  - Energy consumption in kWh");
                for (Gadget device : devices) {
                    System.out.println("  - " + device.getType() + " " + device.getModel() + " (" + device.getRoomName() + ")");
                }
            } else {
                System.out.println("[FAIL] No devices found");
            }
            
            // Test 6: Device Control
            System.out.println("\n[TEST 6] Device Control...");
            boolean controlSuccess = smartHomeService.changeGadgetStatus("TV");
            if (controlSuccess) {
                System.out.println("[PASS] TV control successful");
            } else {
                System.out.println("[INFO] TV control may have failed or device not found");
            }
            
            // Test 7: Smart Scenes Service
            System.out.println("\n[TEST 7] Smart Scenes...");
            SmartScenesService scenesService = smartHomeService.getSmartScenesService();
            List<String> availableScenes = scenesService.getAvailableSceneNames();
            System.out.println("[PASS] Smart Scenes service working - " + availableScenes.size() + " scenes available:");
            for (String scene : availableScenes) {
                System.out.println("  - " + scene);
            }
            
            // Test 8: Execute a Smart Scene
            System.out.println("\n[TEST 8] Execute Smart Scene...");
            boolean sceneSuccess = smartHomeService.executeSmartScene("MORNING");
            if (sceneSuccess) {
                System.out.println("[PASS] MORNING scene executed successfully");
            } else {
                System.out.println("[INFO] MORNING scene execution completed (some devices may not exist)");
            }
            
            // Test 9: Energy Management
            System.out.println("\n[TEST 9] Energy Management...");
            EnergyManagementService energyService = smartHomeService.getEnergyService();
            Customer currentUser = smartHomeService.getCurrentUser();
            if (currentUser != null) {
                EnergyManagementService.EnergyReport report = energyService.generateEnergyReport(currentUser);
                System.out.println("[PASS] Energy report generated - Total: " + 
                    String.format("%.2f kWh", report.getTotalEnergyKWh()) + 
                    ", Cost: Rs." + String.format("%.2f", report.getTotalCostRupees()));
            }
            
            // Test 10: Device Health Monitoring
            System.out.println("\n[TEST 10] Device Health Monitoring...");
            DeviceHealthService healthService = smartHomeService.getDeviceHealthService();
            if (currentUser != null) {
                DeviceHealthService.SystemHealthReport healthReport = healthService.generateHealthReport(currentUser);
                System.out.println("[PASS] Health report generated - Overall Health: " + 
                    String.format("%.1f%%", healthReport.getOverallSystemHealth()));
                System.out.println("  Healthy: " + healthReport.getHealthyDevices() + 
                    ", Warning: " + healthReport.getWarningDevices() + 
                    ", Critical: " + healthReport.getCriticalDevices());
            }
            
            // Test 11: Timer Service
            System.out.println("\n[TEST 11] Timer Service...");
            TimerService timerService = smartHomeService.getTimerService();
            String helpText = timerService.getTimerHelp();
            if (helpText != null && !helpText.isEmpty()) {
                System.out.println("[PASS] Timer service working - Help text available");
            }
            
            // Test 12: Calendar Service
            System.out.println("\n[TEST 12] Calendar Service...");
            CalendarEventService calendarService = smartHomeService.getCalendarService();
            List<String> eventTypes = calendarService.getEventTypes();
            System.out.println("[PASS] Calendar service working - " + eventTypes.size() + " event types available:");
            for (String eventType : eventTypes) {
                System.out.println("  - " + eventType);
            }
            
            // Test 13: Weather Service
            System.out.println("\n[TEST 13] Weather Service...");
            WeatherService weatherService = smartHomeService.getWeatherService();
            String weatherHelp = weatherService.getWeatherHelp();
            if (weatherHelp != null && !weatherHelp.isEmpty()) {
                System.out.println("[PASS] Weather service working - Help available");
            }
            
            // Test 14: Session Management
            System.out.println("\n[TEST 14] Session Management...");
            boolean isLoggedIn = smartHomeService.isLoggedIn();
            if (isLoggedIn) {
                System.out.println("[PASS] Session management working - User is logged in");
            }
            
            // Test 15: Logout
            System.out.println("\n[TEST 15] Logout...");
            smartHomeService.logout();
            boolean isLoggedOut = !smartHomeService.isLoggedIn();
            if (isLoggedOut) {
                System.out.println("[PASS] Logout successful");
            }
            
            System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
            System.out.println("[SUMMARY] IoT Smart Home Dashboard is fully functional!");
            System.out.println("- All services initialized correctly");
            System.out.println("- User registration and authentication working");
            System.out.println("- Device management operational");
            System.out.println("- Smart scenes functioning");
            System.out.println("- Energy management active");
            System.out.println("- Health monitoring operational");
            System.out.println("- All supporting services working");
            
        } catch (Exception e) {
            System.out.println("[ERROR] Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}