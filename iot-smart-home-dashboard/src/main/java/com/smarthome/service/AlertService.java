package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AlertService {
    private static AlertService instance;
    private final Map<String, List<Alert>> userAlerts;

    private AlertService() {
        this.userAlerts = new HashMap<>();
    }

    public static synchronized AlertService getInstance() {
        if (instance == null) {
            instance = new AlertService();
        }
        return instance;
    }

    public static class Alert {
        private String alertId;
        private String alertName;
        private String deviceType;
        private String roomName;
        private AlertType alertType;
        private String condition;
        private String message;
        private boolean isActive;
        private LocalDateTime createdTime;
        private LocalDateTime lastTriggered;
        private int triggerCount;
        private boolean autoDeleteAfterTrigger;

        public Alert(String alertId, String alertName, String deviceType, String roomName,
                    AlertType alertType, String condition, String message) {
            this.alertId = alertId;
            this.alertName = alertName;
            this.deviceType = deviceType;
            this.roomName = roomName;
            this.alertType = alertType;
            this.condition = condition;
            this.message = message;
            this.isActive = true;
            this.createdTime = LocalDateTime.now();
            this.triggerCount = 0;
            this.autoDeleteAfterTrigger = true; // Default: auto-delete after trigger
        }

        // Getters and setters
        public String getAlertId() { return alertId; }
        public String getAlertName() { return alertName; }
        public String getDeviceType() { return deviceType; }
        public String getRoomName() { return roomName; }
        public AlertType getAlertType() { return alertType; }
        public String getCondition() { return condition; }
        public String getMessage() { return message; }
        public boolean isActive() { return isActive; }
        public LocalDateTime getCreatedTime() { return createdTime; }
        public LocalDateTime getLastTriggered() { return lastTriggered; }
        public int getTriggerCount() { return triggerCount; }

        public void setActive(boolean active) { this.isActive = active; }
        public void setLastTriggered(LocalDateTime lastTriggered) { this.lastTriggered = lastTriggered; }
        public void incrementTriggerCount() { this.triggerCount++; }
        public boolean isAutoDeleteAfterTrigger() { return autoDeleteAfterTrigger; }
        public void setAutoDeleteAfterTrigger(boolean autoDeleteAfterTrigger) { this.autoDeleteAfterTrigger = autoDeleteAfterTrigger; }
    }

    public enum AlertType {
        TIME_BASED, ENERGY_USAGE, DEVICE_STATUS
    }

    public static class TimeBasedAlert extends Alert {
        private LocalDateTime triggerTime;
        private boolean isRecurring;
        private String recurrencePattern;

        public TimeBasedAlert(String alertId, String alertName, String deviceType, String roomName,
                             LocalDateTime triggerTime, String message) {
            super(alertId, alertName, deviceType, roomName, AlertType.TIME_BASED,
                  "Trigger at " + triggerTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), message);
            this.triggerTime = triggerTime;
            this.isRecurring = false;
        }

        public LocalDateTime getTriggerTime() { return triggerTime; }
        public boolean isRecurring() { return isRecurring; }
        public String getRecurrencePattern() { return recurrencePattern; }
        public void setRecurring(boolean recurring) { this.isRecurring = recurring; }
        public void setRecurrencePattern(String pattern) { this.recurrencePattern = pattern; }
    }

    public static class EnergyUsageAlert extends Alert {
        private double energyThreshold;
        private String comparisonType; // "GREATER_THAN", "LESS_THAN", "EQUALS"

        public EnergyUsageAlert(String alertId, String alertName, String deviceType, String roomName,
                               double energyThreshold, String comparisonType, String message) {
            super(alertId, alertName, deviceType, roomName, AlertType.ENERGY_USAGE,
                  "Energy " + comparisonType.toLowerCase().replace("_", " ") + " " + energyThreshold + " kWh", message);
            this.energyThreshold = energyThreshold;
            this.comparisonType = comparisonType;
        }

        public double getEnergyThreshold() { return energyThreshold; }
        public String getComparisonType() { return comparisonType; }
    }

    public boolean createTimeBasedAlert(String userEmail, String alertName, String deviceType,
                                       String roomName, LocalDateTime triggerTime, String message) {
        try {
            String alertId = generateAlertId();
            TimeBasedAlert alert = new TimeBasedAlert(alertId, alertName, deviceType, roomName, triggerTime, message);

            userAlerts.computeIfAbsent(userEmail, k -> new ArrayList<>()).add(alert);

            System.out.println("[SUCCESS] Time-based alert created: " + alertName);
            System.out.println("  Device: " + deviceType + " in " + roomName);
            System.out.println("  Trigger: " + triggerTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            return true;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create time-based alert: " + e.getMessage());
            return false;
        }
    }

    public boolean createEnergyUsageAlert(String userEmail, String alertName, String deviceType,
                                         String roomName, double energyThreshold, String comparisonType, String message) {
        try {
            String alertId = generateAlertId();
            EnergyUsageAlert alert = new EnergyUsageAlert(alertId, alertName, deviceType, roomName,
                                                         energyThreshold, comparisonType, message);

            userAlerts.computeIfAbsent(userEmail, k -> new ArrayList<>()).add(alert);

            System.out.println("[SUCCESS] Energy usage alert created: " + alertName);
            System.out.println("  Device: " + deviceType + " in " + roomName);
            System.out.println("  Condition: Energy " + comparisonType.toLowerCase().replace("_", " ") + " " + energyThreshold + " kWh");
            return true;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create energy usage alert: " + e.getMessage());
            return false;
        }
    }

    public List<Alert> getUserAlerts(String userEmail) {
        return userAlerts.getOrDefault(userEmail, new ArrayList<>());
    }

    public List<Alert> getActiveAlerts(String userEmail) {
        List<Alert> alerts = userAlerts.get(userEmail);
        if (alerts == null) return new ArrayList<>();

        return alerts.stream()
                .filter(Alert::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public boolean deleteAlert(String userEmail, String alertId) {
        List<Alert> alerts = userAlerts.get(userEmail);
        if (alerts == null) return false;

        boolean removed = alerts.removeIf(alert -> alert.getAlertId().equals(alertId));
        if (removed) {
            System.out.println("[SUCCESS] Alert deleted successfully");
            if (alerts.isEmpty()) {
                userAlerts.remove(userEmail);
            }
        }
        return removed;
    }

    public boolean toggleAlert(String userEmail, String alertId) {
        List<Alert> alerts = userAlerts.get(userEmail);
        if (alerts == null) return false;

        for (Alert alert : alerts) {
            if (alert.getAlertId().equals(alertId)) {
                alert.setActive(!alert.isActive());
                System.out.println("[SUCCESS] Alert " + (alert.isActive() ? "activated" : "deactivated"));
                return true;
            }
        }
        return false;
    }

    public void displayUserAlerts(String userEmail) {
        List<Alert> alerts = getUserAlerts(userEmail);

        if (alerts.isEmpty()) {
            System.out.println("\n=== Your Device Alerts ===");
            System.out.println("No alerts configured yet.");
            System.out.println("\nTip: Create alerts to monitor your devices automatically!");
            return;
        }

        System.out.println("\n=== Your Device Alerts ===");
        System.out.println(String.format("Total Alerts: %d | Active: %d | Inactive: %d",
                          alerts.size(),
                          (int) alerts.stream().filter(Alert::isActive).count(),
                          (int) alerts.stream().filter(a -> !a.isActive()).count()));

        System.out.println("\n+----+-------------------------+---------------+-------------------+--------+-------+");
        System.out.printf("| %-2s | %-23s | %-13s | %-17s | %-6s | %-5s |\n",
                         "#", "Alert Name", "Device", "Condition", "Status", "Count");
        System.out.println("+----+-------------------------+---------------+-------------------+--------+-------+");

        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            String deviceInfo = alert.getDeviceType() + " (" + alert.getRoomName() + ")";
            if (deviceInfo.length() > 13) {
                deviceInfo = deviceInfo.substring(0, 10) + "...";
            }

            String condition = alert.getCondition();
            if (condition.length() > 17) {
                condition = condition.substring(0, 14) + "...";
            }

            String status = alert.isActive() ? "ACTIVE" : "OFF";

            System.out.printf("| %-2d | %-23s | %-13s | %-17s | %-6s | %-5d |\n",
                            (i + 1),
                            alert.getAlertName().length() > 23 ? alert.getAlertName().substring(0, 20) + "..." : alert.getAlertName(),
                            deviceInfo,
                            condition,
                            status,
                            alert.getTriggerCount());
        }
        System.out.println("+----+-------------------------+---------------+-------------------+--------+-------+");
    }

    public void checkTimeBasedAlerts(String userEmail, LocalDateTime currentTime) {
        List<Alert> alerts = getActiveAlerts(userEmail);

        for (Alert alert : alerts) {
            if (alert instanceof TimeBasedAlert) {
                TimeBasedAlert timeAlert = (TimeBasedAlert) alert;

                if (currentTime.isAfter(timeAlert.getTriggerTime()) || currentTime.isEqual(timeAlert.getTriggerTime())) {
                    if (timeAlert.getLastTriggered() == null ||
                        timeAlert.getLastTriggered().isBefore(timeAlert.getTriggerTime())) {

                        triggerAlert(alert, "Time-based alert triggered at " +
                                    currentTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), userEmail);

                        if (!timeAlert.isRecurring()) {
                            alert.setActive(false);
                        }
                    }
                }
            }
        }
    }

    public void checkEnergyUsageAlerts(String userEmail, Customer customer) {
        List<Alert> alerts = getActiveAlerts(userEmail);

        for (Alert alert : alerts) {
            if (alert instanceof EnergyUsageAlert) {
                EnergyUsageAlert energyAlert = (EnergyUsageAlert) alert;

                Gadget device = customer.findGadget(alert.getDeviceType(), alert.getRoomName());
                if (device != null) {
                    double currentEnergy = device.getTotalEnergyConsumedKWh();
                    boolean shouldTrigger = false;

                    switch (energyAlert.getComparisonType()) {
                        case "GREATER_THAN":
                            shouldTrigger = currentEnergy > energyAlert.getEnergyThreshold();
                            break;
                        case "LESS_THAN":
                            shouldTrigger = currentEnergy < energyAlert.getEnergyThreshold();
                            break;
                        case "EQUALS":
                            shouldTrigger = Math.abs(currentEnergy - energyAlert.getEnergyThreshold()) < 0.01;
                            break;
                    }

                    if (shouldTrigger) {
                        triggerAlert(alert, String.format("Energy usage alert: %s has consumed %.2f kWh (threshold: %.2f kWh)",
                                    device.getType() + " in " + device.getRoomName(),
                                    currentEnergy,
                                    energyAlert.getEnergyThreshold()), userEmail);
                    }
                }
            }
        }
    }

    private void triggerAlert(Alert alert, String triggerReason, String userEmail) {
        alert.setLastTriggered(LocalDateTime.now());
        alert.incrementTriggerCount();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("        DEVICE ALERT TRIGGERED!");
        System.out.println("=".repeat(60));
        System.out.println("Alert Name: " + alert.getAlertName());
        System.out.println("Device: " + alert.getDeviceType() + " in " + alert.getRoomName());
        System.out.println("Type: " + alert.getAlertType().toString().replace("_", " "));
        System.out.println("Condition: " + alert.getCondition());
        System.out.println("Message: " + alert.getMessage());
        System.out.println("Triggered: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Reason: " + triggerReason);
        System.out.println("=".repeat(60));

        // Auto-delete alert if configured to do so
        if (alert.isAutoDeleteAfterTrigger()) {
            boolean deleted = deleteAlert(userEmail, alert.getAlertId());
            if (deleted) {
                System.out.println("✅ Alert automatically deleted after triggering");
            } else {
                System.out.println("⚠️ Alert triggered but auto-deletion failed");
            }
            System.out.println("=".repeat(60));
            System.out.println("This was a one-time alert and has been automatically removed.");
        } else {
            System.out.println("What would you like to do about this alert?");
            System.out.println("(This alert is for notification only - no automatic actions will be taken)");
        }
        System.out.print("\nPress Enter to continue or enter your choice: ");
    }

    private String generateAlertId() {
        return "ALERT_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getAlertHelp() {
        StringBuilder help = new StringBuilder();
        help.append("\n=== Device Alerts Help ===\n");
        help.append("Alerts notify you when specific conditions are met.\n\n");
        help.append("ALERT TYPES:\n");
        help.append("1. Time-Based Alerts:\n");
        help.append("   - Trigger at specific date/time\n");
        help.append("   - Example: Remind to turn off AC at 11 PM\n");
        help.append("   - Format: DD-MM-YYYY HH:MM\n\n");
        help.append("2. Energy Usage Alerts:\n");
        help.append("   - Trigger when energy consumption meets threshold\n");
        help.append("   - Example: Alert when TV uses more than 5 kWh\n");
        help.append("   - Comparison types: Greater than, Less than, Equals\n\n");
        help.append("IMPORTANT NOTES:\n");
        help.append("- Alerts are notifications only\n");
        help.append("- No automatic device actions are taken\n");
        help.append("- You decide what action to take when alerted\n");
        help.append("- Alerts can be activated/deactivated anytime\n");
        return help.toString();
    }
}