package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.util.SessionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService {
    
    private final ScheduledExecutorService scheduler;
    private final CustomerService customerService;
    private static TimerService instance;
    
    private TimerService(CustomerService customerService) {
        this.scheduler = Executors.newScheduledThreadPool(5);
        this.customerService = customerService;
        startTimerMonitoring();
    }
    
    public static synchronized TimerService getInstance(CustomerService customerService) {
        if (instance == null) {
            instance = new TimerService(customerService);
        }
        return instance;
    }
    
    public static class TimerTask {
        private final String deviceType;
        private final String roomName;
        private final String action;
        private final LocalDateTime scheduledTime;
        private final String userEmail;
        
        public TimerTask(String deviceType, String roomName, String action, LocalDateTime scheduledTime, String userEmail) {
            this.deviceType = deviceType;
            this.roomName = roomName;
            this.action = action;
            this.scheduledTime = scheduledTime;
            this.userEmail = userEmail;
        }
        
        public String getDeviceType() { return deviceType; }
        public String getRoomName() { return roomName; }
        public String getAction() { return action; }
        public LocalDateTime getScheduledTime() { return scheduledTime; }
        public String getUserEmail() { return userEmail; }
    }
    
    public boolean scheduleDeviceTimer(Customer customer, String deviceType, String roomName, 
                                     String action, LocalDateTime scheduledTime) {
        try {
            Gadget device = customer.findGadget(deviceType, roomName);
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
                return false;
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (scheduledTime.isBefore(now)) {
                System.out.printf("[ERROR] Cannot schedule timer for past time!\n");
                System.out.printf("Current time: %s\n", now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                System.out.printf("Requested time: %s\n", scheduledTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                return false;
            }
            
            if (scheduledTime.isBefore(now.plusMinutes(1))) {
                System.out.printf("[ERROR] Timer must be scheduled at least 1 minute in the future!\n");
                System.out.printf("Minimum allowed time: %s\n", now.plusMinutes(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                return false;
            }
            
            if (action.equalsIgnoreCase("ON")) {
                device.setScheduledOnTime(scheduledTime);
            } else if (action.equalsIgnoreCase("OFF")) {
                device.setScheduledOffTime(scheduledTime);
            } else {
                System.out.println("[ERROR] Invalid action! Use 'ON' or 'OFF'");
                return false;
            }
            
            device.setTimerEnabled(true);
            
            boolean updated = customerService.updateCustomer(customer);
            if (updated) {
                System.out.println("[SUCCESS] Timer scheduled for " + device.getType() + " " + device.getModel() + 
                                 " in " + device.getRoomName() + " to turn " + action.toUpperCase() + 
                                 " at " + scheduledTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                return true;
            } else {
                System.out.println("[ERROR] Failed to save timer schedule!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error scheduling timer: " + e.getMessage());
            return false;
        }
    }
    
    public void displayScheduledTimers(Customer customer) {
        System.out.println("\n=== Scheduled Timers ===");
        
        List<Gadget> devicesWithTimers = new ArrayList<>();
        for (Gadget device : customer.getGadgets()) {
            if (device.isTimerEnabled() && 
                (device.getScheduledOnTime() != null || device.getScheduledOffTime() != null)) {
                devicesWithTimers.add(device);
            }
        }
        
        if (devicesWithTimers.isEmpty()) {
            System.out.println("No timers scheduled.");
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        System.out.println("+----+-------------------------+--------+-------------------+----------------------+");
        System.out.printf("| %-2s | %-23s | %-6s | %-17s | %-20s |\n", 
                         "#", "Device", "Action", "Scheduled Time", "Status");
        System.out.println("+----+-------------------------+--------+-------------------+----------------------+");
        
        int timerIndex = 1;
        for (Gadget device : devicesWithTimers) {
            String deviceName = String.format("%s %s (%s)", device.getType(), device.getModel(), device.getRoomName());
            if (deviceName.length() > 23) {
                deviceName = deviceName.substring(0, 20) + "...";
            }
            
            if (device.getScheduledOnTime() != null) {
                String countdown = getCountdownString(now, device.getScheduledOnTime());
                String scheduledTime = device.getScheduledOnTime().format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));
                String status = countdown.length() > 20 ? countdown.substring(0, 17) + "..." : countdown;
                
                System.out.printf("| %-2d | %-23s | %-6s | %-17s | %-20s |\n", 
                                timerIndex++, deviceName, "ON", scheduledTime, status);
            }
            
            if (device.getScheduledOffTime() != null) {
                String countdown = getCountdownString(now, device.getScheduledOffTime());
                String scheduledTime = device.getScheduledOffTime().format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));
                String status = countdown.length() > 20 ? countdown.substring(0, 17) + "..." : countdown;
                
                // Only show device name for the first entry if both ON and OFF timers exist
                String displayDeviceName = device.getScheduledOnTime() != null ? "" : deviceName;
                
                System.out.printf("| %-2s | %-23s | %-6s | %-17s | %-20s |\n", 
                                device.getScheduledOnTime() != null ? "" : String.valueOf(timerIndex++), 
                                displayDeviceName, "OFF", scheduledTime, status);
            }
        }
        System.out.println("+----+-------------------------+--------+-------------------+----------------------+");
    }
    
    private String getCountdownString(LocalDateTime now, LocalDateTime scheduledTime) {
        if (scheduledTime.isBefore(now)) {
            return "[OVERDUE]";
        }
        
        long totalMinutes = ChronoUnit.MINUTES.between(now, scheduledTime);
        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;
        
        if (days > 0) {
            return String.format("[%dd %dh %dm remaining]", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("[%dh %dm remaining]", hours, minutes);
        } else {
            return String.format("[%dm remaining]", minutes);
        }
    }
    
    public boolean cancelTimer(Customer customer, String deviceType, String roomName, String action) {
        try {
            Gadget device = customer.findGadget(deviceType, roomName);
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
                return false;
            }
            
            if (action.equalsIgnoreCase("ON")) {
                device.setScheduledOnTime(null);
            } else if (action.equalsIgnoreCase("OFF")) {
                device.setScheduledOffTime(null);
            } else {
                System.out.println("[ERROR] Invalid action! Use 'ON' or 'OFF'");
                return false;
            }
            
            if (device.getScheduledOnTime() == null && device.getScheduledOffTime() == null) {
                device.setTimerEnabled(false);
            }
            
            boolean updated = customerService.updateCustomer(customer);
            if (updated) {
                System.out.println("[SUCCESS] Timer cancelled for " + device.getType() + " " + device.getModel() + 
                                 " in " + device.getRoomName() + " (" + action.toUpperCase() + " timer)");
                return true;
            } else {
                System.out.println("[ERROR] Failed to cancel timer!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error cancelling timer: " + e.getMessage());
            return false;
        }
    }
    
    private void startTimerMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkAndExecuteScheduledTasks();
            } catch (Exception e) {
                System.err.println("Error in timer monitoring: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
    
    private void checkAndExecuteScheduledTasks() {
        LocalDateTime now = LocalDateTime.now();
        
        try {
            List<Customer> allCustomers = getAllCustomersWithTimers();
            
            for (Customer customer : allCustomers) {
                for (Gadget device : customer.getGadgets()) {
                    if (!device.isTimerEnabled()) continue;
                    
                    if (device.getScheduledOnTime() != null && 
                        now.isAfter(device.getScheduledOnTime()) && 
                        now.isBefore(device.getScheduledOnTime().plusMinutes(2))) {
                        
                        device.turnOn();
                        device.setScheduledOnTime(null);
                        
                        if (device.getScheduledOffTime() == null) {
                            device.setTimerEnabled(false);
                        }
                        
                        customerService.updateCustomer(customer);
                        System.out.println("[TIMER EXECUTED] " + device.getType() + " in " + 
                                         device.getRoomName() + " turned ON automatically");
                    }
                    
                    if (device.getScheduledOffTime() != null && 
                        now.isAfter(device.getScheduledOffTime()) && 
                        now.isBefore(device.getScheduledOffTime().plusMinutes(2))) {
                        
                        device.turnOff();
                        device.setScheduledOffTime(null);
                        
                        if (device.getScheduledOnTime() == null) {
                            device.setTimerEnabled(false);
                        }
                        
                        customerService.updateCustomer(customer);
                        System.out.println("[TIMER EXECUTED] " + device.getType() + " in " + 
                                         device.getRoomName() + " turned OFF automatically");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking scheduled tasks: " + e.getMessage());
        }
    }
    
    private List<Customer> getAllCustomersWithTimers() {
        List<Customer> customers = new ArrayList<>();
        SessionManager sessionManager = SessionManager.getInstance();
        Customer currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            customers.add(currentUser);
        }
        return customers;
    }
    
    public LocalDateTime parseDateTime(String dateTimeStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
    
    public String getTimerHelp() {
        StringBuilder help = new StringBuilder();
        help.append("\n=== Timer Scheduling Help ===\n");
        help.append("Date Format: DD-MM-YYYY HH:MM (24-hour format)\n");
        help.append("Examples:\n");
        help.append("- 25-12-2024 18:30 (Christmas evening 6:30 PM)\n");
        help.append("- 01-01-2025 00:00 (New Year midnight)\n");
        help.append("- 15-03-2024 07:00 (March 15, 7:00 AM)\n");
        help.append("\nActions: ON or OFF\n");
        help.append("\nUsage Tips:\n");
        help.append("- Schedule AC to turn ON before you arrive home\n");
        help.append("- Set Geyser timers for morning hot water\n");
        help.append("- Auto-turn OFF lights late at night\n");
        help.append("- Schedule devices during off-peak electricity hours\n");
        
        return help.toString();
    }
    
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}