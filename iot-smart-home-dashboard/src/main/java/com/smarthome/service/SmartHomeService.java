package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SmartHomeService {
    
    private final CustomerService customerService;
    private final GadgetService gadgetService;
    private final SessionManager sessionManager;
    private final EnergyManagementService energyService;
    private final TimerService timerService;
    private final CalendarEventService calendarService;
    private final WeatherService weatherService;
    private final SmartScenesService smartScenesService;
    private final DeviceHealthService deviceHealthService;
    
    public SmartHomeService() {
        this.customerService = new CustomerService();
        this.gadgetService = new GadgetService();
        this.sessionManager = SessionManager.getInstance();
        this.energyService = new EnergyManagementService();
        this.timerService = TimerService.getInstance(customerService);
        this.calendarService = CalendarEventService.getInstance();
        this.weatherService = WeatherService.getInstance();
        this.smartScenesService = SmartScenesService.getInstance();
        this.deviceHealthService = DeviceHealthService.getInstance();
    }
    
    public boolean registerCustomer(String fullName, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return false;
        }
        
        if (!customerService.isValidName(fullName)) {
            System.out.println("Invalid name! Name should contain only letters and spaces (minimum 2 characters).");
            return false;
        }
        
        if (!customerService.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return false;
        }
        
        if (!customerService.isValidPassword(password)) {
            System.out.println("[ERROR] Invalid password! Please ensure your password meets all requirements:");
            System.out.println(customerService.getPasswordRequirements());
            return false;
        }
        
        boolean success = customerService.registerCustomer(fullName, email, password);
        if (success) {
            System.out.println("[SUCCESS] Thank you! Customer registration successful.");
        } else {
            System.out.println("[ERROR] Registration failed! Email might already be registered.");
        }
        
        return success;
    }
    
    public boolean loginCustomer(String email, String password) {
        Customer customer = customerService.authenticateCustomer(email, password);
        
        if (customer != null) {
            sessionManager.login(customer);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout() {
        String currentUserEmail = sessionManager.getCurrentUser() != null ? 
                                  sessionManager.getCurrentUser().getEmail() : "unknown";
        sessionManager.logout();
        System.out.println("[SUCCESS] Logged out successfully from account: " + currentUserEmail);
        System.out.println("[INFO] You can now register a new account or login with different credentials.");
    }
    
    public boolean connectToGadget(String type, String model, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            
            Gadget existingGadget = currentUser.findGadget(type, roomName);
            if (existingGadget != null) {
                System.out.println("A " + type + " already exists in " + roomName + ". You can only have one " + type + " per room.");
                return false;
            }
            
            Gadget gadget = gadgetService.createGadget(type, model, roomName);
            gadget.ensurePowerRating();
            currentUser.addGadget(gadget);
            
            boolean updated = customerService.updateCustomer(currentUser);
            
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("[SUCCESS] Successfully connected to " + gadget.getType() + " " + gadget.getModel() + " in " + gadget.getRoomName());
                return true;
            } else {
                System.out.println("[ERROR] Failed to update customer data!");
                return false;
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected error occurred. Please try again.");
            return false;
        }
    }
    
    public List<Gadget> viewGadgets() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return null;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        List<Gadget> allGadgets = new ArrayList<>();
        
        if (currentUser.isPartOfGroup()) {
            // Add current user's devices
            if (currentUser.getGadgets() != null) {
                allGadgets.addAll(currentUser.getGadgets());
            }
            
            // Add group members' devices
            int groupDeviceCount = 0;
            for (String memberEmail : currentUser.getGroupMembers()) {
                Customer groupMember = customerService.findCustomerByEmail(memberEmail);
                if (groupMember != null && groupMember.getGadgets() != null) {
                    allGadgets.addAll(groupMember.getGadgets());
                    groupDeviceCount += groupMember.getGadgets().size();
                }
            }
            
            System.out.println("\n=== Group Gadgets ===");
            System.out.println("[INFO] Showing your devices + " + currentUser.getGroupMembers().size() + " group member(s)' devices");
            if (groupDeviceCount > 0) {
                System.out.println("[INFO] Group members contributed " + groupDeviceCount + " additional devices");
            }
        } else {
            allGadgets = currentUser.getGadgets();
            System.out.println("\n=== Your Gadgets ===");
            System.out.println("[INFO] Showing only your personal devices (not part of any group)");
            System.out.println("[INFO] Use 'Add People to Group' to share devices with others");
        }
        
        if (allGadgets == null || allGadgets.isEmpty()) {
            System.out.println("No gadgets found! Please connect to some gadgets first.");
            return allGadgets;
        }
        
        for (Gadget gadget : allGadgets) {
            gadget.ensurePowerRating();
        }
        
        displayAutoAlignedTable(allGadgets);
        
        return allGadgets;
    }
    
    private String getTimerCountdown(LocalDateTime now, LocalDateTime scheduledTime) {
        if (scheduledTime.isBefore(now)) {
            return "(OVERDUE)";
        }
        
        long totalMinutes = ChronoUnit.MINUTES.between(now, scheduledTime);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        
        if (hours > 0) {
            return String.format("(%dh %dm)", hours, minutes);
        } else {
            return String.format("(%dm)", minutes);
        }
    }
    
    public boolean changeGadgetStatus(String gadgetType) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        
        try {
            if (gadgetType == null || gadgetType.trim().isEmpty()) {
                System.out.println("Gadget type cannot be empty!");
                return false;
            }
            
            gadgetType = gadgetType.trim().toUpperCase();
            
            Customer currentUser = sessionManager.getCurrentUser();
            List<Gadget> gadgets = viewGadgets();
            
            if (gadgets == null || gadgets.isEmpty()) {
                System.out.println("No gadgets found! Please connect to some gadgets first.");
                return false;
            }
            
            Gadget targetGadget = null;
            for (Gadget gadget : gadgets) {
                if (gadget.getType().equalsIgnoreCase(gadgetType)) {
                    targetGadget = gadget;
                    break;
                }
            }
            
            if (targetGadget == null) {
                System.out.println("Gadget type '" + gadgetType + "' not found!");
                System.out.println("Available gadgets: " + gadgets.stream()
                        .map(Gadget::getType)
                        .distinct()
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None"));
                return false;
            }
            
            targetGadget.ensurePowerRating();
            String previousStatus = targetGadget.getStatus();
            targetGadget.toggleStatus();
            String newStatus = targetGadget.getStatus();
            
            boolean updated = customerService.updateCustomer(currentUser);
            
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                
                if ("ON".equals(newStatus)) {
                    System.out.println("[SUCCESS] Switched on successful");
                } else {
                    System.out.println("[SUCCESS] Switched off successful");
                }
                
                System.out.println("\n=== All Gadgets Status ===");
                viewGadgets();
                return true;
            } else {
                targetGadget.setStatus(previousStatus);
                System.out.println("[ERROR] Failed to update gadget status!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Error changing gadget status. Please try again.");
            return false;
        }
    }
    
    public boolean changeSpecificGadgetStatus(String gadgetType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        
        try {
            if (gadgetType == null || gadgetType.trim().isEmpty()) {
                System.out.println("Gadget type cannot be empty!");
                return false;
            }
            
            if (roomName == null || roomName.trim().isEmpty()) {
                System.out.println("Room name cannot be empty!");
                return false;
            }
            
            gadgetType = gadgetType.trim().toUpperCase();
            roomName = roomName.trim();
            
            Customer currentUser = sessionManager.getCurrentUser();
            List<Gadget> gadgets = viewGadgets();
            
            if (gadgets == null || gadgets.isEmpty()) {
                System.out.println("No gadgets found! Please connect to some gadgets first.");
                return false;
            }
            
            Gadget targetGadget = null;
            for (Gadget gadget : gadgets) {
                if (gadget.getType().equalsIgnoreCase(gadgetType) && 
                    gadget.getRoomName().equalsIgnoreCase(roomName)) {
                    targetGadget = gadget;
                    break;
                }
            }
            
            if (targetGadget == null) {
                System.out.println("Gadget '" + gadgetType + "' in room '" + roomName + "' not found!");
                return false;
            }
            
            targetGadget.ensurePowerRating();
            String previousStatus = targetGadget.getStatus();
            targetGadget.toggleStatus();
            String newStatus = targetGadget.getStatus();
            
            boolean updated = customerService.updateCustomer(currentUser);
            
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                
                if ("ON".equals(newStatus)) {
                    System.out.println("[SUCCESS] " + targetGadget.getType() + " " + targetGadget.getModel() + 
                                     " in " + targetGadget.getRoomName() + " switched ON");
                } else {
                    System.out.println("[SUCCESS] " + targetGadget.getType() + " " + targetGadget.getModel() + 
                                     " in " + targetGadget.getRoomName() + " switched OFF");
                }
                
                System.out.println("\n=== All Gadgets Status ===");
                viewGadgets();
                return true;
            } else {
                targetGadget.setStatus(previousStatus);
                System.out.println("[ERROR] Failed to update gadget status!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Error changing gadget status. Please try again.");
            return false;
        }
    }

    public Customer getCurrentUser() {
        return sessionManager.getCurrentUser();
    }
    
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
    
    public GadgetService getGadgetService() {
        return gadgetService;
    }
    
    
    public boolean initiatePasswordReset(String email) {
        return customerService.initiatePasswordReset(email);
    }
    
    public boolean resetPassword(String email, String newPassword) {
        return customerService.resetPassword(email, newPassword);
    }
    
    public boolean addPersonToGroup(String memberEmail) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            String currentUserEmail = currentUser.getEmail();
            
            if (memberEmail.toLowerCase().trim().equals(currentUserEmail.toLowerCase().trim())) {
                System.out.println("[ERROR] You cannot add yourself to the group!");
                return false;
            }
            
            Customer targetUser = customerService.findCustomerByEmail(memberEmail);
            if (targetUser == null) {
                System.out.println("[ERROR] No registered user found with email: " + memberEmail);
                return false;
            }
            
            if (currentUser.isGroupMember(memberEmail)) {
                System.out.println("[ERROR] " + memberEmail + " is already in your group!");
                return false;
            }
            
            currentUser.addGroupMember(memberEmail);
            targetUser.addGroupMember(currentUserEmail);
            
            boolean currentUserUpdated = customerService.updateCustomer(currentUser);
            boolean targetUserUpdated = customerService.updateCustomer(targetUser);
            
            if (currentUserUpdated && targetUserUpdated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("[SUCCESS] " + memberEmail + " has been added to your group!");
                System.out.println("[INFO] Both you and " + memberEmail + " can now see each other's devices.");
                System.out.println("[INFO] " + memberEmail + " can also see your devices when they login.");
                return true;
            } else {
                System.out.println("[ERROR] Failed to update group membership in database!");
                // Rollback changes if only one update succeeded
                if (currentUserUpdated && !targetUserUpdated) {
                    currentUser.removeGroupMember(memberEmail);
                    customerService.updateCustomer(currentUser);
                }
                if (!currentUserUpdated && targetUserUpdated) {
                    targetUser.removeGroupMember(currentUserEmail);
                    customerService.updateCustomer(targetUser);
                }
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error adding person to group: " + e.getMessage());
            return false;
        }
    }
    
    public void showEnergyReport() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        EnergyManagementService.EnergyReport report = energyService.generateEnergyReport(currentUser);
        
        System.out.println("\n=== " + report.getReportPeriod() + " ===");
        System.out.printf("Total Energy Consumption: %.2f kWh\n", report.getTotalEnergyKWh());
        System.out.printf("Total Cost: Rs. %.2f\n", report.getTotalCostRupees());
        
        energyService.displayDeviceEnergyUsage(report.getDevices());
        System.out.println(energyService.getSlabBreakdown(report.getTotalEnergyKWh()));
        System.out.println(energyService.getEnergyEfficiencyTips(report.getTotalEnergyKWh()));
    }
    
    public boolean scheduleDeviceTimer(String deviceType, String roomName, String action, String dateTime) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            LocalDateTime scheduledTime = timerService.parseDateTime(dateTime);
            
            return timerService.scheduleDeviceTimer(currentUser, deviceType, roomName, action, scheduledTime);
        } catch (Exception e) {
            System.out.println("[ERROR] Invalid date format! " + timerService.getTimerHelp());
            return false;
        }
    }
    
    public void showScheduledTimers() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        timerService.displayScheduledTimers(currentUser);
    }
    
    public List<Gadget> getScheduledTimersWithDevices() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return new ArrayList<>();
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        List<Gadget> devicesWithTimers = new ArrayList<>();
        
        for (Gadget device : currentUser.getGadgets()) {
            if (device.isTimerEnabled() && 
                (device.getScheduledOnTime() != null || device.getScheduledOffTime() != null)) {
                devicesWithTimers.add(device);
            }
        }
        
        return devicesWithTimers;
    }
    
    public boolean cancelDeviceTimer(String deviceType, String roomName, String action) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        return timerService.cancelTimer(currentUser, deviceType, roomName, action);
    }
    
    public boolean createCalendarEvent(String title, String description, String startDateTime, String endDateTime, String eventType) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            LocalDateTime startTime = timerService.parseDateTime(startDateTime);
            LocalDateTime endTime = timerService.parseDateTime(endDateTime);
            
            return calendarService.createEvent(currentUser.getEmail(), title, description, startTime, endTime, eventType);
        } catch (Exception e) {
            System.out.println("[ERROR] Invalid date format! Use DD-MM-YYYY HH:MM");
            return false;
        }
    }
    
    public void showUpcomingEvents() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        calendarService.displayUpcomingEvents(currentUser.getEmail());
    }
    
    public void showEventAutomation(String eventTitle) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        calendarService.displayEventAutomation(currentUser.getEmail(), eventTitle);
    }
    
    public void showCurrentWeather() {
        weatherService.displayCurrentWeather();
        weatherService.displayWeatherBasedSuggestions();
    }
    
    public void showWeatherForecast() {
        weatherService.displayWeatherForecast();
    }
    
    public TimerService getTimerService() {
        return timerService;
    }
    
    public CalendarEventService getCalendarService() {
        return calendarService;
    }
    
    public WeatherService getWeatherService() {
        return weatherService;
    }
    
    public EnergyManagementService getEnergyService() {
        return energyService;
    }
    
    public boolean editDeviceRoom(String deviceType, String currentRoom, String newRoom) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            Gadget device = currentUser.findGadget(deviceType, currentRoom);
            
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + currentRoom);
                return false;
            }
            
            Gadget existingDeviceInNewRoom = currentUser.findGadget(deviceType, newRoom);
            if (existingDeviceInNewRoom != null) {
                System.out.println("[ERROR] A " + deviceType + " already exists in " + newRoom + "!");
                return false;
            }
            
            device.ensurePowerRating();
            device.setRoomName(newRoom);
            
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                return true;
            } else {
                System.out.println("[ERROR] Failed to update device information!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error editing device: " + e.getMessage());
            return false;
        }
    }
    
    public boolean editDeviceModel(String deviceType, String roomName, String newModel) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            Gadget device = currentUser.findGadget(deviceType, roomName);
            
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
                return false;
            }
            
            device.ensurePowerRating();
            device.setModel(newModel);
            
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                return true;
            } else {
                System.out.println("[ERROR] Failed to update device information!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error editing device: " + e.getMessage());
            return false;
        }
    }
    
    public boolean editDevicePower(String deviceType, String roomName, double newPowerRating) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            Gadget device = currentUser.findGadget(deviceType, roomName);
            
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
                return false;
            }
            
            if (newPowerRating <= 0) {
                System.out.println("[ERROR] Power rating must be greater than 0!");
                return false;
            }
            
            device.setPowerRatingWatts(newPowerRating);
            
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                return true;
            } else {
                System.out.println("[ERROR] Failed to update device information!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error editing device: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteDevice(String deviceType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            Gadget device = currentUser.findGadget(deviceType, roomName);
            
            if (device == null) {
                System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
                return false;
            }
            
            currentUser.getGadgets().removeIf(gadget -> 
                gadget.getType().equalsIgnoreCase(deviceType) && 
                gadget.getRoomName().equalsIgnoreCase(roomName));
            
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                return true;
            } else {
                System.out.println("[ERROR] Failed to delete device from database!");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Error deleting device: " + e.getMessage());
            return false;
        }
    }
    
    public boolean executeSmartScene(String sceneName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        SmartScenesService.SceneExecutionResult result = smartScenesService.executeScene(sceneName, currentUser, customerService);
        
        smartScenesService.displaySceneExecutionResult(result);
        
        if (result.isFullySuccessful()) {
            sessionManager.updateCurrentUser(currentUser);
            return true;
        }
        
        return result.getSuccessfulActions() > 0;
    }
    
    public void showAvailableScenes() {
        smartScenesService.displayAvailableScenes();
    }
    
    public void showSceneDetails(String sceneName) {
        smartScenesService.displaySceneDetails(sceneName);
    }
    
    public void showDeviceHealthReport() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        DeviceHealthService.SystemHealthReport report = deviceHealthService.generateHealthReport(currentUser);
        deviceHealthService.displayHealthReport(report);
    }
    
    public void showMaintenanceSchedule() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        deviceHealthService.displayMaintenanceSchedule(currentUser);
    }
    
    public String getSystemHealthSummary() {
        if (!sessionManager.isLoggedIn()) {
            return "Not logged in";
        }
        
        Customer currentUser = sessionManager.getCurrentUser();
        return deviceHealthService.getHealthSummary(currentUser);
    }
    
    public SmartScenesService getSmartScenesService() {
        return smartScenesService;
    }
    
    public DeviceHealthService getDeviceHealthService() {
        return deviceHealthService;
    }
    
    private void displayAutoAlignedTable(List<Gadget> allGadgets) {
        TableDimensions dimensions = calculateTableDimensions(allGadgets);
        TableFormatStrings formats = createTableFormatStrings(dimensions);
        
        // Display table header
        System.out.println("Device List (Enter number to view detailed energy info):");
        System.out.println(formats.borderFormat);
        System.out.printf(formats.headerFormat + "\n", "#", "Device", "Power", "Status", "Usage Time", "Energy(kWh)");
        System.out.println(formats.borderFormat);
        
        // Display table rows
        displayTableRows(allGadgets, formats);
        System.out.println(formats.borderFormat);
    }
    
    private TableDimensions calculateTableDimensions(List<Gadget> allGadgets) {
        int numWidth = Math.max(2, String.valueOf(allGadgets.size()).length());
        int deviceWidth = "Device".length();
        int powerWidth = "Power".length();
        int statusWidth = "Status".length();
        int usageWidth = "Usage Time".length();
        int energyWidth = "Energy(kWh)".length();
        
        LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        
        for (Gadget gadget : allGadgets) {
            // Calculate dimensions for main row
            String deviceName = String.format("%s %s (%s)", gadget.getType(), gadget.getModel(), gadget.getRoomName());
            deviceWidth = Math.max(deviceWidth, deviceName.length());
            
            String powerStr = String.format("%.0fW", gadget.getPowerRatingWatts());
            powerWidth = Math.max(powerWidth, powerStr.length());
            
            String statusDisplay = gadget.isOn() ? "RUNNING" : "OFF";
            statusWidth = Math.max(statusWidth, statusDisplay.length());
            
            String usageTime = gadget.getCurrentUsageTimeFormatted();
            usageWidth = Math.max(usageWidth, usageTime.length());
            
            String energyStr = String.format("%.3f", gadget.getCurrentTotalEnergyConsumedKWh());
            energyWidth = Math.max(energyWidth, energyStr.length());
            
            // Calculate dimensions for session info
            if (gadget.isOn() && gadget.getLastOnTime() != null) {
                deviceWidth = Math.max(deviceWidth, "  Current Session:".length());
                String sessionTime = String.format("%.1fh", gadget.getCurrentSessionUsageHours());
                usageWidth = Math.max(usageWidth, sessionTime.length());
            }
            
            // Calculate dimensions for timer info
            if (gadget.isTimerEnabled()) {
                String timerInfo = buildTimerInfo(gadget, now, timeFormatter);
                String timerDisplay = "  Timer: " + timerInfo;
                deviceWidth = Math.max(deviceWidth, timerDisplay.length());
            }
        }
        
        // Set minimum widths for readability
        return new TableDimensions(
            numWidth,
            Math.max(deviceWidth, 25),
            Math.max(powerWidth, 8),
            Math.max(statusWidth, 9),
            Math.max(usageWidth, 11),
            Math.max(energyWidth, 11)
        );
    }
    
    private String buildTimerInfo(Gadget gadget, LocalDateTime now, DateTimeFormatter timeFormatter) {
        StringBuilder timerInfo = new StringBuilder();
        
        if (gadget.getScheduledOnTime() != null) {
            String countdown = getTimerCountdown(now, gadget.getScheduledOnTime());
            timerInfo.append("ON ").append(gadget.getScheduledOnTime().format(timeFormatter)).append(" ").append(countdown);
        }
        
        if (gadget.getScheduledOffTime() != null) {
            if (timerInfo.length() > 0) timerInfo.append(" | ");
            String countdown = getTimerCountdown(now, gadget.getScheduledOffTime());
            timerInfo.append("OFF ").append(gadget.getScheduledOffTime().format(timeFormatter)).append(" ").append(countdown);
        }
        
        return timerInfo.toString();
    }
    
    private TableFormatStrings createTableFormatStrings(TableDimensions dim) {
        String borderFormat = String.format("+%s+%s+%s+%s+%s+%s+",
            "-".repeat(dim.numWidth + 2),
            "-".repeat(dim.deviceWidth + 2),
            "-".repeat(dim.powerWidth + 2),
            "-".repeat(dim.statusWidth + 2),
            "-".repeat(dim.usageWidth + 2),
            "-".repeat(dim.energyWidth + 2)
        );
        
        String headerFormat = String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |",
            dim.numWidth, dim.deviceWidth, dim.powerWidth, dim.statusWidth, dim.usageWidth, dim.energyWidth);
        
        String rowFormat = String.format("| %%-%ds | %%-%ds | %%%ds | %%-%ds | %%%ds | %%%ds |",
            dim.numWidth, dim.deviceWidth, dim.powerWidth, dim.statusWidth, dim.usageWidth, dim.energyWidth);
        
        String emptyRowFormat = String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |",
            dim.numWidth, dim.deviceWidth, dim.powerWidth, dim.statusWidth, dim.usageWidth, dim.energyWidth);
        
        return new TableFormatStrings(borderFormat, headerFormat, rowFormat, emptyRowFormat);
    }
    
    private void displayTableRows(List<Gadget> allGadgets, TableFormatStrings formats) {
        LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        
        for (int i = 0; i < allGadgets.size(); i++) {
            Gadget gadget = allGadgets.get(i);
            
            // Main device row
            displayDeviceRow(gadget, i + 1, formats.rowFormat);
            
            // Current session row (if applicable)
            if (gadget.isOn() && gadget.getLastOnTime() != null) {
                String sessionTime = String.format("%.1fh", gadget.getCurrentSessionUsageHours());
                System.out.printf(formats.emptyRowFormat + "\n", "", "  Current Session:", "", "", sessionTime, "");
            }
            
            // Timer row (if applicable)
            if (gadget.isTimerEnabled()) {
                String timerInfo = buildTimerInfo(gadget, now, timeFormatter);
                String timerDisplay = "  Timer: " + timerInfo;
                System.out.printf(formats.emptyRowFormat + "\n", "", timerDisplay, "", "", "", "");
            }
        }
    }
    
    private void displayDeviceRow(Gadget gadget, int rowNumber, String rowFormat) {
        String statusDisplay = gadget.isOn() ? "RUNNING" : "OFF";
        String deviceName = String.format("%s %s (%s)", gadget.getType(), gadget.getModel(), gadget.getRoomName());
        String powerStr = String.format("%.0fW", gadget.getPowerRatingWatts());
        String energyStr = String.format("%.3f", gadget.getCurrentTotalEnergyConsumedKWh());
        
        System.out.printf(rowFormat + "\n",
            String.valueOf(rowNumber),
            deviceName,
            powerStr,
            statusDisplay,
            gadget.getCurrentUsageTimeFormatted(),
            energyStr);
    }
    
    // Helper classes for better code organization
    private static class TableDimensions {
        final int numWidth, deviceWidth, powerWidth, statusWidth, usageWidth, energyWidth;
        
        TableDimensions(int numWidth, int deviceWidth, int powerWidth, int statusWidth, int usageWidth, int energyWidth) {
            this.numWidth = numWidth;
            this.deviceWidth = deviceWidth;
            this.powerWidth = powerWidth;
            this.statusWidth = statusWidth;
            this.usageWidth = usageWidth;
            this.energyWidth = energyWidth;
        }
    }
    
    private static class TableFormatStrings {
        final String borderFormat, headerFormat, rowFormat, emptyRowFormat;
        
        TableFormatStrings(String borderFormat, String headerFormat, String rowFormat, String emptyRowFormat) {
            this.borderFormat = borderFormat;
            this.headerFormat = headerFormat;
            this.rowFormat = rowFormat;
            this.emptyRowFormat = emptyRowFormat;
        }
    }
}