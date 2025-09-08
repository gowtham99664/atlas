package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DeviceHealthService {
    
    private static DeviceHealthService instance;
    private final Random random;
    
    private DeviceHealthService() {
        this.random = new Random();
    }
    
    public static synchronized DeviceHealthService getInstance() {
        if (instance == null) {
            instance = new DeviceHealthService();
        }
        return instance;
    }
    
    public static class DeviceHealth {
        private String deviceType;
        private String model;
        private String roomName;
        private int healthScore;
        private String healthStatus;
        private List<String> issues;
        private List<String> recommendations;
        private LocalDateTime lastCheckTime;
        private int estimatedLifespanMonths;
        private double energyEfficiencyScore;
        
        public DeviceHealth(String deviceType, String model, String roomName) {
            this.deviceType = deviceType;
            this.model = model;
            this.roomName = roomName;
            this.issues = new ArrayList<>();
            this.recommendations = new ArrayList<>();
            this.lastCheckTime = LocalDateTime.now();
        }
        
        // Getters
        public String getDeviceType() { return deviceType; }
        public String getModel() { return model; }
        public String getRoomName() { return roomName; }
        public int getHealthScore() { return healthScore; }
        public String getHealthStatus() { return healthStatus; }
        public List<String> getIssues() { return issues; }
        public List<String> getRecommendations() { return recommendations; }
        public LocalDateTime getLastCheckTime() { return lastCheckTime; }
        public int getEstimatedLifespanMonths() { return estimatedLifespanMonths; }
        public double getEnergyEfficiencyScore() { return energyEfficiencyScore; }
        
        // Setters
        public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
        public void addIssue(String issue) { this.issues.add(issue); }
        public void addRecommendation(String recommendation) { this.recommendations.add(recommendation); }
        public void setEstimatedLifespanMonths(int months) { this.estimatedLifespanMonths = months; }
        public void setEnergyEfficiencyScore(double score) { this.energyEfficiencyScore = score; }
    }
    
    public static class SystemHealthReport {
        private int totalDevices;
        private int healthyDevices;
        private int warningDevices;
        private int criticalDevices;
        private List<DeviceHealth> deviceHealthList;
        private double overallSystemHealth;
        private List<String> systemRecommendations;
        
        public SystemHealthReport() {
            this.deviceHealthList = new ArrayList<>();
            this.systemRecommendations = new ArrayList<>();
        }
        
        // Getters
        public int getTotalDevices() { return totalDevices; }
        public int getHealthyDevices() { return healthyDevices; }
        public int getWarningDevices() { return warningDevices; }
        public int getCriticalDevices() { return criticalDevices; }
        public List<DeviceHealth> getDeviceHealthList() { return deviceHealthList; }
        public double getOverallSystemHealth() { return overallSystemHealth; }
        public List<String> getSystemRecommendations() { return systemRecommendations; }
        
        // Setters
        public void setTotalDevices(int totalDevices) { this.totalDevices = totalDevices; }
        public void setHealthyDevices(int healthyDevices) { this.healthyDevices = healthyDevices; }
        public void setWarningDevices(int warningDevices) { this.warningDevices = warningDevices; }
        public void setCriticalDevices(int criticalDevices) { this.criticalDevices = criticalDevices; }
        public void addDeviceHealth(DeviceHealth health) { this.deviceHealthList.add(health); }
        public void setOverallSystemHealth(double overallSystemHealth) { this.overallSystemHealth = overallSystemHealth; }
        public void addSystemRecommendation(String recommendation) { this.systemRecommendations.add(recommendation); }
    }
    
    public SystemHealthReport generateHealthReport(Customer user) {
        SystemHealthReport report = new SystemHealthReport();
        List<Gadget> devices = user.getGadgets();
        
        if (devices == null || devices.isEmpty()) {
            return report;
        }
        
        int totalHealthScore = 0;
        int healthyCount = 0, warningCount = 0, criticalCount = 0;
        
        for (Gadget device : devices) {
            DeviceHealth health = analyzeDeviceHealth(device);
            report.addDeviceHealth(health);
            
            totalHealthScore += health.getHealthScore();
            
            if (health.getHealthScore() >= 80) {
                healthyCount++;
            } else if (health.getHealthScore() >= 60) {
                warningCount++;
            } else {
                criticalCount++;
            }
        }
        
        report.setTotalDevices(devices.size());
        report.setHealthyDevices(healthyCount);
        report.setWarningDevices(warningCount);
        report.setCriticalDevices(criticalCount);
        
        if (devices.size() > 0) {
            report.setOverallSystemHealth(totalHealthScore / (double) devices.size());
        }
        
        generateSystemRecommendations(report);
        return report;
    }
    
    private DeviceHealth analyzeDeviceHealth(Gadget device) {
        DeviceHealth health = new DeviceHealth(device.getType(), device.getModel(), device.getRoomName());
        
        // Simulate device health analysis based on usage patterns and device type
        int baseHealth = 85 + random.nextInt(15); // Base health 85-100
        
        // Adjust based on usage time
        long totalMinutes = device.getTotalUsageMinutes();
        if (totalMinutes > 10080) { // More than 1 week of usage
            baseHealth -= 5;
            health.addIssue("High usage detected");
            health.addRecommendation("Consider reducing usage or scheduling maintenance");
        }
        
        // Adjust based on device type
        switch (device.getType().toUpperCase()) {
            case "AC":
                if (baseHealth > 70) {
                    health.addRecommendation("Clean AC filters monthly for optimal performance");
                }
                health.setEstimatedLifespanMonths(120); // 10 years
                break;
            case "REFRIGERATOR":
                if (device.getPowerRatingWatts() > 250) {
                    health.addIssue("High power consumption detected");
                    baseHealth -= 10;
                }
                health.setEstimatedLifespanMonths(180); // 15 years
                break;
            case "GEYSER":
                health.addRecommendation("Check for water leakage and heating efficiency");
                health.setEstimatedLifespanMonths(96); // 8 years
                break;
            case "WASHING_MACHINE":
                if (totalMinutes > 500) {
                    health.addRecommendation("Schedule professional maintenance check");
                }
                health.setEstimatedLifespanMonths(144); // 12 years
                break;
            case "TV":
                health.addRecommendation("Keep ventilation clear for optimal cooling");
                health.setEstimatedLifespanMonths(84); // 7 years
                break;
            default:
                health.setEstimatedLifespanMonths(60); // 5 years default
        }
        
        // Simulate potential issues
        if (random.nextInt(100) < 15) { // 15% chance of connectivity issue
            health.addIssue("Intermittent connectivity detected");
            health.addRecommendation("Check Wi-Fi signal strength in " + device.getRoomName());
            baseHealth -= 10;
        }
        
        if (random.nextInt(100) < 10) { // 10% chance of firmware issue
            health.addIssue("Firmware update available");
            health.addRecommendation("Update device firmware for security and performance");
            baseHealth -= 5;
        }
        
        if (device.isOn()) {
            double currentSessionHours = device.getCurrentSessionUsageHours();
            if (currentSessionHours > 8) { // Running for more than 8 hours
                health.addIssue("Extended continuous operation");
                health.addRecommendation("Consider giving device a break to prevent overheating");
                baseHealth -= 8;
            }
        }
        
        // Set energy efficiency score (ensure <= 100%)
        double expectedPower = getExpectedPowerRating(device.getType());
        double efficiencyScore = Math.max(0, 100 - ((device.getPowerRatingWatts() - expectedPower) / expectedPower * 100));
        health.setEnergyEfficiencyScore(Math.min(100, efficiencyScore));
        
        // Adjust health based on energy efficiency
        if (health.getEnergyEfficiencyScore() < 70) {
            health.addIssue("Below average energy efficiency");
            health.addRecommendation("Consider upgrading to energy-efficient model");
            baseHealth -= 5;
        }
        
        health.setHealthScore(Math.max(0, Math.min(100, baseHealth)));
        
        // Add specific reasons for low health scores
        if (health.getHealthScore() < 60) {
            addLowHealthReasons(health, device);
        }
        
        // Set health status
        if (health.getHealthScore() >= 80) {
            health.setHealthStatus("EXCELLENT");
        } else if (health.getHealthScore() >= 60) {
            health.setHealthStatus("GOOD");
        } else if (health.getHealthScore() >= 40) {
            health.setHealthStatus("WARNING");
        } else {
            health.setHealthStatus("CRITICAL");
        }
        
        return health;
    }
    
    private void addLowHealthReasons(DeviceHealth health, Gadget device) {
        int healthScore = health.getHealthScore();
        
        // Specific reasons based on health score ranges
        if (healthScore < 40) {
            health.addIssue("[CRITICAL] Health score below 40% - Multiple system failures detected");
            health.addRecommendation("[URGENT] Schedule immediate professional inspection");
            health.addRecommendation("[ACTION] Consider device replacement if repair costs exceed 60% of new device");
        } else if (healthScore < 50) {
            health.addIssue("[SEVERE] Health score below 50% - Significant performance degradation");
            health.addRecommendation("[PRIORITY] Professional maintenance required within 1 week");
            health.addRecommendation("[CHECK] Verify power supply and connections");
        } else if (healthScore < 60) {
            health.addIssue("[WARNING] Health score below 60% - Performance issues detected");
            health.addRecommendation("[SCHEDULE] Maintenance check within 2 weeks recommended");
        }
        
        // Additional specific reasons based on device state and usage
        long usageMinutes = device.getCurrentTotalUsageMinutes();
        double usageHours = usageMinutes / 60.0;
        
        if (usageHours > 100) {
            health.addIssue("Excessive usage: " + String.format("%.1f", usageHours) + " hours total runtime");
            health.addRecommendation("Reduce daily usage or implement scheduled breaks");
        }
        
        if (device.getPowerRatingWatts() > getExpectedPowerRating(device.getType()) * 1.3) {
            health.addIssue("Power consumption 30% above normal for " + device.getType());
            health.addRecommendation("Check for efficiency degradation or component wear");
        }
        
        if (health.getEnergyEfficiencyScore() < 50) {
            health.addIssue("Very poor energy efficiency: " + String.format("%.1f", health.getEnergyEfficiencyScore()) + "%");
            health.addRecommendation("Energy efficiency critically low - replacement recommended");
        }
        
        // Device-specific low health reasons
        String deviceType = device.getType().toUpperCase();
        switch (deviceType) {
            case "AC":
                if (healthScore < 60) {
                    health.addIssue("AC performance degraded - possible refrigerant leak or dirty filters");
                    health.addRecommendation("Check and clean/replace filters, inspect for refrigerant leaks");
                }
                break;
            case "REFRIGERATOR":
                if (healthScore < 60) {
                    health.addIssue("Refrigerator efficiency compromised - possible seal/coil issues");
                    health.addRecommendation("Check door seals, clean condenser coils, verify temperature settings");
                }
                break;
            case "GEYSER":
                if (healthScore < 60) {
                    health.addIssue("Water heater performance declining - possible sediment buildup");
                    health.addRecommendation("Flush tank, check heating element, inspect for mineral deposits");
                }
                break;
            case "WASHING_MACHINE":
                if (healthScore < 60) {
                    health.addIssue("Washing machine showing wear signs - mechanical issues possible");
                    health.addRecommendation("Check drum balance, inspect hoses, clean filter and lint trap");
                }
                break;
            case "TV":
                if (healthScore < 60) {
                    health.addIssue("Display device showing performance issues - possible overheating");
                    health.addRecommendation("Ensure proper ventilation, check for dust buildup, reduce brightness");
                }
                break;
        }
    }
    
    private double getExpectedPowerRating(String deviceType) {
        switch (deviceType.toUpperCase()) {
            case "TV": return 150.0;
            case "AC": return 1500.0;
            case "FAN": return 75.0;
            case "REFRIGERATOR": return 200.0;
            case "WASHING_MACHINE": return 500.0;
            case "GEYSER": return 2000.0;
            case "MICROWAVE": return 1200.0;
            default: return 50.0;
        }
    }
    
    private void generateSystemRecommendations(SystemHealthReport report) {
        if (report.getCriticalDevices() > 0) {
            report.addSystemRecommendation("[URGENT] " + report.getCriticalDevices() + " device(s) need immediate attention");
        }
        
        if (report.getWarningDevices() > 0) {
            report.addSystemRecommendation("[WARNING] " + report.getWarningDevices() + " device(s) require maintenance soon");
        }
        
        if (report.getOverallSystemHealth() > 90) {
            report.addSystemRecommendation("[EXCELLENT] System health! Keep up the good maintenance");
        } else if (report.getOverallSystemHealth() > 75) {
            report.addSystemRecommendation("[GOOD] System health overall, minor optimizations recommended");
        } else if (report.getOverallSystemHealth() > 60) {
            report.addSystemRecommendation("[INFO] System needs attention, schedule maintenance for optimal performance");
        } else {
            report.addSystemRecommendation("[ACTION] System requires comprehensive maintenance and potential upgrades");
        }
        
        // Additional recommendations based on device mix
        long totalDevices = report.getTotalDevices();
        if (totalDevices > 10) {
            report.addSystemRecommendation("[TIP] Consider smart power strips for large device setups");
        }
        
        report.addSystemRecommendation("[SCHEDULE] Monthly health checks for proactive maintenance");
        report.addSystemRecommendation("[SETUP] Enable device notifications for real-time health monitoring");
    }
    
    public void displayHealthReport(SystemHealthReport report) {
        System.out.println("\n=== Smart Home Health Dashboard ===");
        System.out.println("Health Report Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println();
        
        // System Overview
        System.out.println("[SYSTEM OVERVIEW]:");
        System.out.printf("Total Devices: %d\n", report.getTotalDevices());
        System.out.printf("Overall Health Score: %.1f%%\n", report.getOverallSystemHealth());
        System.out.println();
        
        System.out.printf("[OK] Healthy: %d devices\n", report.getHealthyDevices());
        System.out.printf("[!] Warning: %d devices\n", report.getWarningDevices());
        System.out.printf("[!!] Critical: %d devices\n", report.getCriticalDevices());
        System.out.println();
        
        // Device Details
        System.out.println("[DEVICE HEALTH DETAILS]:");
        System.out.println("+----+-------------------------+---------+-----------+-----------+----------+");
        System.out.printf("| %-2s | %-23s | %-7s | %-9s | %-9s | %-8s |\n", 
                         "#", "Device", "Health", "Status", "Efficiency", "Lifespan");
        System.out.println("+----+-------------------------+---------+-----------+-----------+----------+");
        
        int deviceIndex = 1;
        for (DeviceHealth device : report.getDeviceHealthList()) {
            String deviceName = String.format("%s %s (%s)", device.getDeviceType(), 
                                            device.getModel(), device.getRoomName());
            if (deviceName.length() > 23) {
                deviceName = deviceName.substring(0, 20) + "...";
            }
            
            String healthStatus = device.getHealthStatus();
            if (healthStatus.length() > 9) {
                healthStatus = healthStatus.substring(0, 6) + "...";
            }
            
            System.out.printf("| %-2d | %-23s | %6d%% | %-9s | %8.1f%% | %7d mo |\n", 
                            deviceIndex++,
                            deviceName,
                            device.getHealthScore(),
                            healthStatus,
                            device.getEnergyEfficiencyScore(),
                            device.getEstimatedLifespanMonths());
            
            // Show issues in table format
            if (!device.getIssues().isEmpty()) {
                for (String issue : device.getIssues()) {
                    String displayIssue = issue.length() > 23 ? issue.substring(0, 20) + "..." : issue;
                    System.out.printf("| %-2s | %-23s | %-7s | %-9s | %-9s | %-8s |\n", 
                                    "", "  Issue: " + displayIssue, "", "", "", "");
                }
            }
            
            // Show recommendations in table format
            if (!device.getRecommendations().isEmpty()) {
                for (String rec : device.getRecommendations()) {
                    String displayRec = rec.length() > 23 ? rec.substring(0, 20) + "..." : rec;
                    System.out.printf("| %-2s | %-23s | %-7s | %-9s | %-9s | %-8s |\n", 
                                    "", "  Fix: " + displayRec, "", "", "", "");
                }
            }
        }
        System.out.println("+----+-------------------------+---------+-----------+-----------+----------+");
        
        // System Recommendations
        System.out.println("[SYSTEM RECOMMENDATIONS]:");
        for (String recommendation : report.getSystemRecommendations()) {
            System.out.println("  " + recommendation);
        }
    }
    
    private String getHealthIcon(int healthScore) {
        if (healthScore >= 80) return "[OK]";
        else if (healthScore >= 60) return "[!]";
        else return "[!!]";
    }
    
    public void displayMaintenanceSchedule(Customer user) {
        System.out.println("\n=== Maintenance Schedule ===");
        System.out.println("Recommended maintenance timeline for your devices:");
        System.out.println();
        
        List<Gadget> devices = user.getGadgets();
        if (devices == null || devices.isEmpty()) {
            System.out.println("No devices found.");
            return;
        }
        
        for (Gadget device : devices) {
            System.out.printf("[*] %s %s (%s)\n", device.getType(), device.getModel(), device.getRoomName());
            
            switch (device.getType().toUpperCase()) {
                case "AC":
                    System.out.println("   [Monthly] Clean/replace filters");
                    System.out.println("   [Quarterly] Check refrigerant levels");
                    System.out.println("   [Annually] Professional service");
                    break;
                case "REFRIGERATOR":
                    System.out.println("   [Monthly] Clean coils and vents");
                    System.out.println("   [Quarterly] Check door seals");
                    System.out.println("   [Annually] Temperature calibration");
                    break;
                case "GEYSER":
                    System.out.println("   [Monthly] Check for leaks");
                    System.out.println("   [Quarterly] Flush tank");
                    System.out.println("   [Annually] Replace anode rod");
                    break;
                case "WASHING_MACHINE":
                    System.out.println("   [Monthly] Clean drum and filter");
                    System.out.println("   [Quarterly] Check hoses");
                    System.out.println("   [Annually] Professional inspection");
                    break;
                default:
                    System.out.println("   [Monthly] General cleaning");
                    System.out.println("   [Quarterly] Check connections");
                    System.out.println("   [Annually] Update firmware");
            }
            System.out.println();
        }
    }
    
    public String getHealthSummary(Customer user) {
        SystemHealthReport report = generateHealthReport(user);
        
        if (report.getTotalDevices() == 0) {
            return "No devices to monitor";
        }
        
        String healthLevel;
        if (report.getOverallSystemHealth() >= 90) {
            healthLevel = "EXCELLENT";
        } else if (report.getOverallSystemHealth() >= 75) {
            healthLevel = "GOOD";
        } else if (report.getOverallSystemHealth() >= 60) {
            healthLevel = "FAIR";
        } else {
            healthLevel = "NEEDS ATTENTION";
        }
        
        return String.format("System Health: %.1f%% (%s) | Healthy: %d | Warning: %d | Critical: %d", 
                           report.getOverallSystemHealth(), healthLevel,
                           report.getHealthyDevices(), report.getWarningDevices(), report.getCriticalDevices());
    }
}