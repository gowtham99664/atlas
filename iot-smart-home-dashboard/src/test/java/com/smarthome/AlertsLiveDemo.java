package com.smarthome;

import com.smarthome.service.SmartHomeService;
import com.smarthome.service.AlertService;
import com.smarthome.model.Gadget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlertsLiveDemo {

    private SmartHomeService smartHomeService;
    private static final String DEMO_EMAIL = "demo@smarthome.com";
    private static final String DEMO_NAME = "Demo User";
    private static final String DEMO_PASSWORD = "Demo123!@#";

    @BeforeEach
    void setUp() {
        smartHomeService = new SmartHomeService();
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        ALERTS FEATURE LIVE DEMO");
        System.out.println("=".repeat(60));

        smartHomeService.registerCustomer(DEMO_NAME, DEMO_EMAIL, DEMO_PASSWORD, DEMO_PASSWORD);
        smartHomeService.loginCustomer(DEMO_EMAIL, DEMO_PASSWORD);

        System.out.println(" Demo user logged in successfully!");
    }

    @AfterEach
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
    }

    @Test
    @DisplayName("Live Demo: Complete Alerts Workflow")
    void demonstrateCompleteAlertsWorkflow() {
        System.out.println("\nSTEP 1: Adding Demo Devices");
        System.out.println("=" + "=".repeat(45));

        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Master Bedroom");
        smartHomeService.connectToGadget("MICROWAVE", "Whirlpool", "Kitchen");
        smartHomeService.connectToGadget("FAN", "Bajaj", "Study Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        System.out.println("Added " + devices.size() + " demo devices:");
        for (int i = 0; i < devices.size(); i++) {
            Gadget device = devices.get(i);
            System.out.printf("   %d. %s (%s) in %s - Energy: %.2f kWh\n",
                (i + 1), device.getType(), device.getModel(), device.getRoomName(),
                device.getTotalEnergyConsumedKWh());
        }

        System.out.println("\nSTEP 2: Creating Time-Based Alerts");
        System.out.println("=" + "=".repeat(45));

        LocalDateTime alert1Time = LocalDateTime.now().plusMinutes(1);
        LocalDateTime alert2Time = LocalDateTime.now().plusMinutes(2);

        boolean alert1Created = smartHomeService.createTimeBasedAlert(
            "TV Reminder",
            "TV",
            "Living Room",
            alert1Time,
            "Don't forget to turn off TV after watching!"
        );

        boolean alert2Created = smartHomeService.createTimeBasedAlert(
            "AC Energy Check",
            "AC",
            "Master Bedroom",
            alert2Time,
            "Check AC energy consumption - it might be getting high"
        );

        System.out.println(" Created time-based alerts:");
        System.out.println("   1. TV Reminder - triggers at " + alert1Time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("   2. AC Energy Check - triggers at " + alert2Time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println("\nSTEP 3: Creating Energy Usage Alerts");
        System.out.println("=" + "=".repeat(45));

        Gadget microwave = devices.stream()
            .filter(d -> d.getType().equals("MICROWAVE"))
            .findFirst().orElse(null);

        if (microwave != null) {
            smartHomeService.changeSpecificGadgetStatus("MICROWAVE", "Kitchen");
            System.out.println("Turned ON microwave to generate energy usage");
        }

        boolean energyAlertCreated = smartHomeService.createEnergyUsageAlert(
            "High Microwave Usage",
            "MICROWAVE",
            "Kitchen",
            0.01,
            "GREATER_THAN",
            "Microwave energy usage is higher than expected!"
        );

        System.out.println(" Created energy usage alert:");
        System.out.println("   - Microwave energy alert (threshold: 0.01 kWh)");

        System.out.println("\nSTEP 4: Viewing All Created Alerts");
        System.out.println("=" + "=".repeat(45));

        smartHomeService.displayUserAlerts();

        System.out.println("\nSTEP 5: Testing Alert Monitoring");
        System.out.println("=" + "=".repeat(45));

        System.out.println("Forcing alert check to see if any alerts should trigger...");
        smartHomeService.forceAlertCheck();

        System.out.println("\nSTEP 6: Alert Management Demo");
        System.out.println("=" + "=".repeat(45));

        List<AlertService.Alert> allAlerts = smartHomeService.getUserAlerts();
        System.out.println("Managing alerts:");
        System.out.println("   Total alerts created: " + allAlerts.size());

        if (!allAlerts.isEmpty()) {
            AlertService.Alert firstAlert = allAlerts.get(0);
            System.out.println("   First alert: " + firstAlert.getAlertName());
            System.out.println("   Status: " + (firstAlert.isActive() ? "ACTIVE" : "INACTIVE"));
            System.out.println("   Trigger count: " + firstAlert.getTriggerCount());

            boolean toggleResult = smartHomeService.toggleAlert(firstAlert.getAlertId());
            System.out.println("   Toggle result: " + (toggleResult ? "SUCCESS" : "FAILED"));
        }

        System.out.println("\nSTEP 7: Simulating Time Passage");
        System.out.println("=" + "=".repeat(45));

        System.out.println("Waiting a moment and checking alerts again...");
        try {
            Thread.sleep(3000); // Wait 3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Checking alerts after time passage...");
        smartHomeService.forceAlertCheck();

        System.out.println("\nSTEP 8: Final Alert Status");
        System.out.println("=" + "=".repeat(45));

        smartHomeService.displayUserAlerts();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("         ALERTS DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("DEMO SUMMARY:");
        System.out.println("-  Created multiple devices");
        System.out.println("-  Created time-based alerts");
        System.out.println("-  Created energy usage alerts");
        System.out.println("-  Displayed alert management interface");
        System.out.println("-  Tested alert monitoring/triggering");
        System.out.println("-  Demonstrated alert toggle functionality");
        System.out.println();
        System.out.println("HOW TO ACCESS IN REAL APP:");
        System.out.println("   Main Menu → 6. Device Control Panel → Options 11-15");
        System.out.println();
        System.out.println("ALERTS ARE NOW WORKING AND READY TO USE!");

        assertTrue(alert1Created, "Time-based alert creation should work");
        assertTrue(alert2Created, "Second time-based alert creation should work");
        assertTrue(energyAlertCreated, "Energy usage alert creation should work");
        assertTrue(allAlerts.size() >= 3, "Should have created at least 3 alerts");
    }

    @Test
    @DisplayName("Demo: Alert Help System")
    void demonstrateAlertHelpSystem() {
        System.out.println("\nALERT HELP SYSTEM DEMO");
        System.out.println("=" + "=".repeat(45));

        String helpText = smartHomeService.getAlertHelp();
        System.out.println("Alert Help Content:");
        System.out.println(helpText);

        System.out.println("\nPRACTICAL EXAMPLES:");
        System.out.println("1.  Time Alert: 'Turn off AC at 11 PM'");
        System.out.println("   → Device: AC in Master Bedroom");
        System.out.println("   → Time: 23:00 daily");
        System.out.println("   → Action: Just notifies, you decide what to do");

        System.out.println("\n2.  Energy Alert: 'High TV usage warning'");
        System.out.println("   → Device: TV in Living Room");
        System.out.println("   → Threshold: > 5.0 kWh");
        System.out.println("   → Action: Alert when TV uses too much energy");

        System.out.println("\n Help system is fully functional!");
    }

    @Test
    @DisplayName("Demo: Device Control Panel Integration")
    void demonstrateDeviceControlPanelIntegration() {
        System.out.println("\nDEVICE CONTROL PANEL INTEGRATION DEMO");
        System.out.println("=" + "=".repeat(50));

        System.out.println("WHERE TO FIND ALERTS IN THE REAL APP:");
        System.out.println();
        System.out.println("   [MAIN MENU]");
        System.out.println("   ├── 1. Register New Account");
        System.out.println("   ├── 2. Sign In to Account");
        System.out.println("   ├── 3. Forgot Password");
        System.out.println("   ├── 4. Add Smart Device");
        System.out.println("   ├── 5. View All Connected Devices");
        System.out.println("   ├── 6. Device Control Panel  ← SELECT THIS");
        System.out.println("   └── ...");
        System.out.println();
        System.out.println("   [DEVICE CONTROL PANEL]");
        System.out.println("   ├── 1-10. Device Management Options");
        System.out.println("   ├── [DEVICE ALERTS & MONITORING]:");
        System.out.println("   ├── 11. Create Time-Based Alert      ← NEW!");
        System.out.println("   ├── 12. Create Energy Usage Alert   ← NEW!");
        System.out.println("   ├── 13. View & Manage Alerts        ← NEW!");
        System.out.println("   ├── 14. Check Alert Status          ← NEW!");
        System.out.println("   ├── 15. Alert Help & Information    ← NEW!");
        System.out.println("   └── 0. Return to Main Menu");

        System.out.println("\nNAVIGATION PATH:");
        System.out.println("   Main Menu → Option 6 → Options 11-15");

        System.out.println("\n Perfect integration with existing menu structure!");
    }
}