package com.smarthome.service;
import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.model.DeletedDeviceEnergyRecord;
import com.smarthome.util.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean checkEmailAvailability(String email) {
        return customerService.isEmailAvailable(email);
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
            if (currentUser.getGadgets() != null) {
                allGadgets.addAll(currentUser.getGadgets());
            }
            int groupDeviceCount = 0;
            List<Customer> groupMemberObjects = new ArrayList<>();
            for (String memberEmail : currentUser.getGroupMembers()) {
                Customer groupMember = customerService.findCustomerByEmail(memberEmail);
                if (groupMember != null) {
                    groupMemberObjects.add(groupMember);
                }
            }
            List<Gadget> accessibleGroupDevices = currentUser.getAccessibleGroupDevices(groupMemberObjects);
            allGadgets.addAll(accessibleGroupDevices);
            groupDeviceCount = accessibleGroupDevices.size();
            System.out.println("\n=== Group Gadgets ===");
            System.out.println("[INFO] Group size: " + currentUser.getGroupSize() + " member(s) | Admin: " + currentUser.getGroupCreator());
            System.out.println("[INFO] Your role: " + (currentUser.isGroupAdmin() ? "Admin" : "Member"));
            System.out.println("[INFO] Showing your devices + devices you have permission to access");
            if (groupDeviceCount > 0) {
                System.out.println("[INFO] You have access to " + groupDeviceCount + " group member devices");
            } else {
                System.out.println("[INFO] No group devices shared with you. Ask admin for device access permissions.");
            }
        } else {
            allGadgets = currentUser.getGadgets();
            System.out.println("\n=== Your Gadgets ===");
            System.out.println("[INFO] Showing only your personal devices (not part of any group)");
            System.out.println("[INFO] Use 'Group Management' to create a group and share devices with others");
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
            Customer gadgetOwner = null;
            if (currentUser.getGadgets() != null) {
                for (Gadget gadget : currentUser.getGadgets()) {
                    if (gadget.getType().equalsIgnoreCase(gadgetType)) {
                        targetGadget = gadget;
                        gadgetOwner = currentUser;
                        break;
                    }
                }
            }
            if (targetGadget == null && currentUser.isPartOfGroup()) {
                for (String memberEmail : currentUser.getGroupMembers()) {
                    Customer member = customerService.findCustomerByEmail(memberEmail);
                    if (member != null && member.getGadgets() != null) {
                        for (Gadget gadget : member.getGadgets()) {
                            if (gadget.getType().equalsIgnoreCase(gadgetType)) {
                                targetGadget = gadget;
                                gadgetOwner = member;
                                break;
                            }
                        }
                        if (targetGadget != null) break;
                    }
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
            boolean updated = customerService.updateCustomer(gadgetOwner);
            if (gadgetOwner.getEmail().equals(currentUser.getEmail())) {
                sessionManager.updateCurrentUser(gadgetOwner);
            }
            if (updated) {
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
            Customer gadgetOwner = null;
            if (currentUser.getGadgets() != null) {
                for (Gadget gadget : currentUser.getGadgets()) {
                    if (gadget.getType().equalsIgnoreCase(gadgetType) && 
                        gadget.getRoomName().equalsIgnoreCase(roomName)) {
                        targetGadget = gadget;
                        gadgetOwner = currentUser;
                        break;
                    }
                }
            }
            if (targetGadget == null && currentUser.isPartOfGroup()) {
                for (String memberEmail : currentUser.getGroupMembers()) {
                    Customer member = customerService.findCustomerByEmail(memberEmail);
                    if (member != null && member.getGadgets() != null) {
                        for (Gadget gadget : member.getGadgets()) {
                            if (gadget.getType().equalsIgnoreCase(gadgetType) && 
                                gadget.getRoomName().equalsIgnoreCase(roomName)) {
                                targetGadget = gadget;
                                gadgetOwner = member;
                                break;
                            }
                        }
                        if (targetGadget != null) break;
                    }
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
            boolean updated = customerService.updateCustomer(gadgetOwner);
            if (gadgetOwner.getEmail().equals(currentUser.getEmail())) {
                sessionManager.updateCurrentUser(gadgetOwner);
            }
            if (updated) {
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
            if (currentUser.getGroupCreator() == null) {
                currentUser.setGroupCreator(currentUserEmail);
            }
            if (targetUser.getGroupCreator() == null) {
                targetUser.setGroupCreator(currentUserEmail);
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
                System.out.println("[INFO] Group size is now: " + currentUser.getGroupSize() + " member(s)");
                return true;
            } else {
                System.out.println("[ERROR] Failed to update group membership in database!");
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
        energyService.displayDeviceEnergyUsage(currentUser);
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
    }
    public void showWeatherForecast() {
        weatherService.displayWeatherForecast();
    }
    public void updateWeatherData() {
        weatherService.updateWeatherData();
    }
    public void clearWeatherData() {
        weatherService.clearWeatherData();
    }
    public boolean hasUserWeatherData() {
        return weatherService.hasUserWeatherData();
    }
    public void forceTimerCheck() {
        timerService.forceTimerCheck();
        System.out.println("[INFO] Manual timer check completed. Any due timers have been executed.");
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
            if (device.getTotalEnergyConsumedKWh() > 0 ||
                (device.isOn() && device.getLastOnTime() != null && device.getCurrentSessionUsageHours() > 0)) {
                DeletedDeviceEnergyRecord energyRecord = new DeletedDeviceEnergyRecord(device);
                currentUser.addDeletedDeviceRecord(energyRecord);
                System.out.println("[INFO] Preserving energy history: " + String.format("%.3f kWh", energyRecord.getTotalEnergyConsumedKWh()));
                System.out.println("[INFO] Device usage time: " + energyRecord.getFormattedUsageTime());
            }
            currentUser.getGadgets().removeIf(gadget ->
                gadget.getType().equalsIgnoreCase(deviceType) &&
                gadget.getRoomName().equalsIgnoreCase(roomName));
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("[SUCCESS] Device deleted successfully!");
                System.out.println("[IMPORTANT] Energy consumption history preserved for accurate monthly billing.");
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
    public void showEditableSceneDetails(String sceneName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        smartScenesService.displayEditableSceneDetails(currentUser.getEmail(), sceneName);
    }
    public boolean addDeviceToScene(String sceneName, String deviceType, String roomName, String action) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        Gadget device = currentUser.findGadget(deviceType, roomName);
        if (device == null) {
            System.out.println("[ERROR] Device not found: " + deviceType + " in " + roomName);
            return false;
        }
        String description = generateActionDescription(deviceType, roomName, action);
        SmartScenesService.SceneAction newAction = new SmartScenesService.SceneAction(deviceType, roomName, action, description);
        boolean success = smartScenesService.addDeviceToScene(currentUser.getEmail(), sceneName, newAction);
        if (success) {
            System.out.println("[SUCCESS] Device added to scene: " + deviceType + " in " + roomName + " → " + action);
        } else {
            System.out.println("[ERROR] Failed to add device to scene. Device may already exist in this scene.");
        }
        return success;
    }
    public boolean removeDeviceFromScene(String sceneName, String deviceType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        boolean success = smartScenesService.removeDeviceFromScene(currentUser.getEmail(), sceneName, deviceType, roomName);
        if (success) {
            System.out.println("[SUCCESS] Device removed from scene: " + deviceType + " in " + roomName);
        } else {
            System.out.println("[ERROR] Failed to remove device from scene. Device may not exist in this scene.");
        }
        return success;
    }
    public boolean changeDeviceActionInScene(String sceneName, String deviceType, String roomName, String newAction) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        boolean success = smartScenesService.changeDeviceAction(currentUser.getEmail(), sceneName, deviceType, roomName, newAction);
        if (success) {
            System.out.println("[SUCCESS] Device action changed: " + deviceType + " in " + roomName + " → " + newAction);
        } else {
            System.out.println("[ERROR] Failed to change device action. Device may not exist in this scene.");
        }
        return success;
    }
    public boolean resetSceneToOriginal(String sceneName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        boolean success = smartScenesService.resetSceneToOriginal(currentUser.getEmail(), sceneName);
        if (success) {
            System.out.println("[SUCCESS] Scene reset to original: " + sceneName);
        } else {
            System.out.println("[ERROR] Cannot reset this scene. It may be a custom scene or already in original state.");
        }
        return success;
    }
    public List<SmartScenesService.SceneAction> getSceneActions(String sceneName) {
        if (!sessionManager.isLoggedIn()) {
            return new ArrayList<>();
        }
        Customer currentUser = sessionManager.getCurrentUser();
        return smartScenesService.getSceneActions(currentUser.getEmail(), sceneName);
    }
    private String generateActionDescription(String deviceType, String roomName, String action) {
        String verb = action.equalsIgnoreCase("ON") ? "Turn on" : "Turn off";
        return String.format("%s %s in %s", verb, deviceType.toLowerCase(), roomName);
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
    public boolean removePersonFromGroup(String memberEmail) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            String currentUserEmail = currentUser.getEmail();
            if (!currentUser.isGroupAdmin()) {
                System.out.println("[ERROR] Only the group admin can remove members from the group!");
                System.out.println("[INFO] Group admin is: " + currentUser.getGroupCreator());
                return false;
            }
            if (memberEmail.toLowerCase().trim().equals(currentUserEmail.toLowerCase().trim())) {
                System.out.println("[ERROR] You cannot remove yourself from the group!");
                System.out.println("[INFO] To leave the group, use a different feature or delete the group.");
                return false;
            }
            if (!currentUser.isGroupMember(memberEmail)) {
                System.out.println("[ERROR] " + memberEmail + " is not in your group!");
                return false;
            }
            Customer targetUser = customerService.findCustomerByEmail(memberEmail);
            if (targetUser == null) {
                System.out.println("[ERROR] User not found: " + memberEmail);
                return false;
            }
            currentUser.removeGroupMember(memberEmail);
            targetUser.removeGroupMember(currentUserEmail);
            if (!targetUser.isPartOfGroup()) {
                targetUser.setGroupCreator(null);
            }
            boolean currentUserUpdated = customerService.updateCustomer(currentUser);
            boolean targetUserUpdated = customerService.updateCustomer(targetUser);
            if (currentUserUpdated && targetUserUpdated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("[SUCCESS] " + memberEmail + " has been removed from the group!");
                System.out.println("[INFO] " + memberEmail + " can no longer see your devices.");
                System.out.println("[INFO] Group size is now: " + currentUser.getGroupSize() + " member(s)");
                return true;
            } else {
                System.out.println("[ERROR] Failed to update group membership in database!");
                if (currentUserUpdated && !targetUserUpdated) {
                    currentUser.addGroupMember(memberEmail);
                    customerService.updateCustomer(currentUser);
                }
                if (!currentUserUpdated && targetUserUpdated) {
                    targetUser.addGroupMember(currentUserEmail);
                    customerService.updateCustomer(targetUser);
                }
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error removing person from group: " + e.getMessage());
            return false;
        }
    }
    public void showGroupInfo() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        if (currentUser.isPartOfGroup() && currentUser.getGroupCreator() == null) {
            fixGroupAdmin(currentUser);
        }
        if (!currentUser.isPartOfGroup()) {
            System.out.println("\n=== Group Information ===");
            System.out.println("[INFO] You are not part of any group.");
            System.out.println("[INFO] Use 'Group Management' to create a group and share devices.");
            return;
        }
        System.out.println("\n=== Group Information ===");
        System.out.println("Group Size: " + currentUser.getGroupSize() + " member(s)");
        System.out.println("Group Admin: " + currentUser.getGroupCreator());
        System.out.println("Your Role: " + (currentUser.isGroupAdmin() ? "Admin" : "Member"));
        System.out.println("\n[GROUP MEMBERS]:");
        System.out.println("1. " + currentUser.getEmail() + " (You)" + (currentUser.isGroupAdmin() ? " - Admin" : ""));
        int memberCount = 2;
        for (String memberEmail : currentUser.getGroupMembers()) {
            Customer member = customerService.findCustomerByEmail(memberEmail);
            String memberName = member != null ? member.getFullName() : "Unknown";
            System.out.println(memberCount + ". " + memberEmail + " (" + memberName + ")");
            memberCount++;
        }
        int totalDevices = 0;
        int yourDevices = currentUser.getGadgets() != null ? currentUser.getGadgets().size() : 0;
        totalDevices += yourDevices;
        int groupDevices = 0;
        for (String memberEmail : currentUser.getGroupMembers()) {
            Customer member = customerService.findCustomerByEmail(memberEmail);
            if (member != null && member.getGadgets() != null) {
                groupDevices += member.getGadgets().size();
            }
        }
        totalDevices += groupDevices;
        System.out.println("\n[DEVICE INFORMATION]:");
        System.out.println("Your devices: " + yourDevices);
        System.out.println("Group members' devices: " + groupDevices);
        System.out.println("Total accessible devices: " + totalDevices);
        if (currentUser.isGroupAdmin()) {
            System.out.println("\n[ADMIN OPTIONS]:");
            System.out.println("- You can remove members from the group");
            System.out.println("- Use 'Group Management' menu to manage members");
        }
    }
    private void fixGroupAdmin(Customer currentUser) {
        try {
            String currentUserEmail = currentUser.getEmail();
            currentUser.setGroupCreator(currentUserEmail);
            for (String memberEmail : currentUser.getGroupMembers()) {
                Customer member = customerService.findCustomerByEmail(memberEmail);
                if (member != null && member.getGroupCreator() == null) {
                    member.setGroupCreator(currentUserEmail);
                    customerService.updateCustomer(member);
                }
            }
            boolean updated = customerService.updateCustomer(currentUser);
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("[INFO] Group admin has been automatically assigned to: " + currentUserEmail);
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Could not fix group admin assignment: " + e.getMessage());
        }
    }
    private void displayAutoAlignedTable(List<Gadget> allGadgets) {
        TableDimensions dimensions = calculateTableDimensions(allGadgets);
        TableFormatStrings formats = createTableFormatStrings(dimensions);
        System.out.println("Device List (Enter number to view detailed energy info):");
        System.out.println(formats.borderFormat);
        System.out.printf(formats.headerFormat + "\n", "#", "Device", "Power", "Status", "Usage Time", "Energy(kWh)");
        System.out.println(formats.borderFormat);
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
            if (gadget.isOn() && gadget.getLastOnTime() != null) {
                deviceWidth = Math.max(deviceWidth, "  Current Session:".length());
                String sessionTime = String.format("%.1fh", gadget.getCurrentSessionUsageHours());
                usageWidth = Math.max(usageWidth, sessionTime.length());
            }
            if (gadget.isTimerEnabled()) {
                String timerInfo = buildTimerInfo(gadget, now, timeFormatter);
                String timerDisplay = "  Timer: " + timerInfo;
                deviceWidth = Math.max(deviceWidth, timerDisplay.length());
            }
        }
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
            displayDeviceRow(gadget, i + 1, formats.rowFormat);
            if (gadget.isOn() && gadget.getLastOnTime() != null) {
                String sessionTime = String.format("%.1fh", gadget.getCurrentSessionUsageHours());
                System.out.printf(formats.emptyRowFormat + "\n", "", "  Current Session:", "", "", sessionTime, "");
            }
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
    public boolean verifyCurrentPassword(String password) {
        if (!sessionManager.isLoggedIn()) {
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        return customerService.verifyPassword(currentUser.getEmail(), password);
    }
    public void showCurrentUserInfo() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        System.out.println("\n=== Current Profile Information ===");
        System.out.println("Full Name: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Account Status: Active");
        System.out.println("Total Devices: " + (currentUser.getGadgets() != null ? currentUser.getGadgets().size() : 0));
        if (currentUser.isPartOfGroup()) {
            System.out.println("Group Status: Member of group (Admin: " + currentUser.getGroupCreator() + ")");
        } else {
            System.out.println("Group Status: Not part of any group");
        }
    }
    public void showDetailedUserInfo() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        System.out.println("\n=== Detailed Account Information ===");
        System.out.println("Full Name: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Account Registration: Completed successfully");
        System.out.println("Failed Login Attempts: " + currentUser.getFailedLoginAttempts());
        System.out.println("Account Status: " + (currentUser.isAccountLocked() ? "Temporarily Locked" : "Active"));
        System.out.println("\n[DEVICE STATISTICS]:");
        if (currentUser.getGadgets() != null && !currentUser.getGadgets().isEmpty()) {
            System.out.println("Total Devices: " + currentUser.getGadgets().size());
            long activeDevices = currentUser.getGadgets().stream().filter(Gadget::isOn).count();
            System.out.println("Currently Active: " + activeDevices);
            System.out.println("Currently Inactive: " + (currentUser.getGadgets().size() - activeDevices));
        } else {
            System.out.println("No devices registered");
        }
        System.out.println("\n[GROUP INFORMATION]:");
        if (currentUser.isPartOfGroup()) {
            System.out.println("Group Status: Member");
            System.out.println("Group Admin: " + currentUser.getGroupCreator());
            System.out.println("Group Size: " + currentUser.getGroupSize() + " member(s)");
            System.out.println("Your Role: " + (currentUser.isGroupAdmin() ? "Admin" : "Member"));
        } else {
            System.out.println("Group Status: Not part of any group");
        }
        System.out.println("\n[SECURITY INFORMATION]:");
        System.out.println("Password: Securely encrypted (last updated information not tracked)");
        System.out.println("Login Security: " + (currentUser.getFailedLoginAttempts() == 0 ? "Good" : "Warning - recent failed attempts"));
    }
    public boolean updateUserFullName(String newName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        if (!customerService.isValidName(newName)) {
            System.out.println("[ERROR] Invalid name! Name should contain only letters and spaces (minimum 2 characters).");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        String oldName = currentUser.getFullName();
        currentUser.setFullName(newName);
        boolean success = customerService.updateCustomer(currentUser);
        if (success) {
            sessionManager.updateCurrentUser(currentUser);
            System.out.println("[INFO] Name changed from '" + oldName + "' to '" + newName + "'");
            return true;
        } else {
            System.out.println("[ERROR] Failed to update name in database!");
            return false;
        }
    }
    public boolean updateUserEmail(String newEmail) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        if (!customerService.isValidEmail(newEmail)) {
            System.out.println("[ERROR] Invalid email format!");
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        String oldEmail = currentUser.getEmail();
        currentUser.setEmail(newEmail.trim().toLowerCase());
        boolean success = customerService.updateCustomerEmail(oldEmail, currentUser);
        if (success) {
            sessionManager.updateCurrentUser(currentUser);
            System.out.println("[INFO] Email changed from '" + oldEmail + "' to '" + newEmail + "'");
            return true;
        } else {
            currentUser.setEmail(oldEmail);
            System.out.println("[ERROR] Failed to update email in database!");
            return false;
        }
    }
    public boolean updateUserPassword(String newPassword, String confirmPassword) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("[ERROR] Passwords do not match!");
            return false;
        }
        if (!customerService.isValidPassword(newPassword)) {
            System.out.println("[ERROR] Invalid password! Please ensure your password meets all requirements:");
            System.out.println(customerService.getPasswordRequirements());
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        boolean success = customerService.updatePassword(currentUser.getEmail(), newPassword);
        if (success) {
            System.out.println("[INFO] Password updated successfully for security.");
            return true;
        } else {
            System.out.println("[ERROR] Failed to update password in database!");
            return false;
        }
    }
    public boolean grantDevicePermission(String memberEmail, String deviceType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            if (!currentUser.isGroupAdmin()) {
                System.out.println("[ERROR] Only the group admin can grant device permissions!");
                System.out.println("[INFO] Group admin is: " + currentUser.getGroupCreator());
                return false;
            }
            if (!currentUser.isGroupMember(memberEmail)) {
                System.out.println("[ERROR] " + memberEmail + " is not in your group!");
                return false;
            }
            boolean success = currentUser.grantDevicePermission(memberEmail, deviceType, roomName, currentUser.getEmail());
            if (success) {
                boolean updated = customerService.updateCustomer(currentUser);
                if (updated) {
                    sessionManager.updateCurrentUser(currentUser);
                    System.out.println("[SUCCESS] Permission granted successfully!");
                    System.out.println("[INFO] " + memberEmail + " can now access " + deviceType + " in " + roomName);
                    return true;
                } else {
                    System.out.println("[ERROR] Failed to save permission to database!");
                    return false;
                }
            } else {
                System.out.println("[ERROR] Failed to grant permission. Device may not exist or permission already exists.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error granting device permission: " + e.getMessage());
            return false;
        }
    }
    public boolean revokeDevicePermission(String memberEmail, String deviceType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        try {
            Customer currentUser = sessionManager.getCurrentUser();
            if (!currentUser.isGroupAdmin()) {
                System.out.println("[ERROR] Only the group admin can revoke device permissions!");
                System.out.println("[INFO] Group admin is: " + currentUser.getGroupCreator());
                return false;
            }
            boolean success = currentUser.revokeDevicePermission(memberEmail, deviceType, roomName);
            if (success) {
                boolean updated = customerService.updateCustomer(currentUser);
                if (updated) {
                    sessionManager.updateCurrentUser(currentUser);
                    System.out.println("[SUCCESS] Permission revoked successfully!");
                    System.out.println("[INFO] " + memberEmail + " can no longer access " + deviceType + " in " + roomName);
                    return true;
                } else {
                    System.out.println("[ERROR] Failed to save changes to database!");
                    return false;
                }
            } else {
                System.out.println("[ERROR] No permission found to revoke.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error revoking device permission: " + e.getMessage());
            return false;
        }
    }
    public void showDevicePermissions() {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        if (!currentUser.isGroupAdmin()) {
            System.out.println("[ERROR] Only the group admin can view device permissions!");
            System.out.println("[INFO] Group admin is: " + currentUser.getGroupCreator());
            return;
        }
        System.out.println("\n=== Device Permissions Management ===");
        if (!currentUser.isPartOfGroup()) {
            System.out.println("[INFO] You are not part of any group.");
            return;
        }
        var permissions = currentUser.getDevicePermissions();
        if (permissions.isEmpty()) {
            System.out.println("[INFO] No device permissions have been granted yet.");
            System.out.println("[TIP] Use 'Grant Device Access' to share your devices with group members.");
            return;
        }
        System.out.println("[INFO] Current device permissions you have granted:");
        System.out.println();
        Map<String, List<com.smarthome.model.DevicePermission>> permissionsByMember = new HashMap<>();
        for (var permission : permissions) {
            permissionsByMember.computeIfAbsent(permission.getMemberEmail(), k -> new ArrayList<>()).add(permission);
        }
        int permissionNumber = 1;
        for (Map.Entry<String, List<com.smarthome.model.DevicePermission>> entry : permissionsByMember.entrySet()) {
            String memberEmail = entry.getKey();
            List<com.smarthome.model.DevicePermission> memberPermissions = entry.getValue();
            Customer member = customerService.findCustomerByEmail(memberEmail);
            String memberName = member != null ? member.getFullName() : "Unknown";
            System.out.println("[MEMBER] " + memberName + " (" + memberEmail + "):");
            for (var permission : memberPermissions) {
                System.out.printf("  %d. %s in %s - %s%s%n",
                    permissionNumber++,
                    permission.getDeviceType(),
                    permission.getRoomName(),
                    permission.isCanView() ? "View" : "",
                    permission.isCanControl() ? (permission.isCanView() ? " & Control" : "Control") : "");
            }
            System.out.println();
        }
        System.out.println("[INFO] Total permissions granted: " + permissions.size());
    }
    public List<Customer> getGroupMembersForPermissions() {
        if (!sessionManager.isLoggedIn()) {
            return new ArrayList<>();
        }
        Customer currentUser = sessionManager.getCurrentUser();
        if (!currentUser.isGroupAdmin() || !currentUser.isPartOfGroup()) {
            return new ArrayList<>();
        }
        List<Customer> members = new ArrayList<>();
        for (String memberEmail : currentUser.getGroupMembers()) {
            Customer member = customerService.findCustomerByEmail(memberEmail);
            if (member != null) {
                members.add(member);
            }
        }
        return members;
    }
    public boolean hasDevicePermission(String memberEmail, String deviceType, String roomName) {
        if (!sessionManager.isLoggedIn()) {
            return false;
        }
        Customer currentUser = sessionManager.getCurrentUser();
        return currentUser.hasDevicePermission(memberEmail, deviceType, roomName);
    }
}
