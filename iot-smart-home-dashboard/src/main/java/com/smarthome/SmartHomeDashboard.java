package com.smarthome;

import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import com.smarthome.util.DynamoDBConfig;
import com.smarthome.util.PasswordInputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SmartHomeDashboard {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final SmartHomeService smartHomeService = new SmartHomeService();
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to IoT Smart Home Dashboard ===\n");
        
        // Add shutdown hook for graceful cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[SYSTEM] Graceful shutdown initiated...");
            try {
                if (smartHomeService.isLoggedIn()) {
                    smartHomeService.logout();
                    System.out.println("[SYSTEM] User session closed.");
                }
                smartHomeService.getTimerService().shutdown();
                System.out.println("[SYSTEM] Timer service shutdown completed.");
            } catch (Exception e) {
                System.err.println("[SYSTEM] Warning during shutdown: " + e.getMessage());
            }
        }));
        
        try {
            showMainMenu();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DynamoDBConfig.shutdown();
            scanner.close();
        }
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== IoT Smart Home Enterprise Dashboard ===");
            System.out.println("[DEVICE MANAGEMENT]:");
            System.out.println("1. Register New Account");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Add/Manage Devices");
            System.out.println("5. View Device Status & Usage");
            System.out.println("6. Control Device Operations");
            System.out.println("7. Group Management");
            System.out.println();
            System.out.println("[ENERGY & AUTOMATION]:");
            System.out.println("8. Energy Management Report");
            System.out.println("9. Schedule Device Timers");
            System.out.println("10. View Scheduled Timers");
            System.out.println("11. Calendar Events & Automation");
            System.out.println("12. Weather-Based Suggestions");
            System.out.println();
            System.out.println("[SMART IOT FEATURES]:");
            System.out.println("13. Smart Scenes (One-Click Automation)");
            System.out.println("14. Device Health Monitoring");
            System.out.println("15. Usage Analytics & Insights");
            System.out.println();
            System.out.println("[SYSTEM]:");
            System.out.println("16. Logout");
            System.out.println("17. Exit");
            
            System.out.print("Choose an option: ");
            
            try {
                String inputLine = scanner.nextLine().trim();
                
                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }
                
                int choice = Integer.parseInt(inputLine);
                int exitOption = 17;
                
                switch (choice) {
                    case 1:
                        if (smartHomeService.isLoggedIn()) {
                            handleRegistrationWhileLoggedIn();
                        } else {
                            registerCustomer();
                        }
                        break;
                    case 2:
                        loginCustomer();
                        break;
                    case 3:
                        forgotPassword();
                        break;
                    case 4:
                        if (checkLoginStatus()) {
                            showGadgetControlMenu();
                        }
                        break;
                    case 5:
                        if (checkLoginStatus()) {
                            viewGadgets();
                        }
                        break;
                    case 6:
                        if (checkLoginStatus()) {
                            changeGadgetStatus();
                        }
                        break;
                    case 7:
                        if (checkLoginStatus()) {
                            showGroupManagementMenu();
                        }
                        break;
                    case 8:
                        if (checkLoginStatus()) {
                            showEnergyManagementReport();
                        }
                        break;
                    case 9:
                        if (checkLoginStatus()) {
                            scheduleDeviceTimer();
                        }
                        break;
                    case 10:
                        if (checkLoginStatus()) {
                            showScheduledTimers();
                        }
                        break;
                    case 11:
                        if (checkLoginStatus()) {
                            showCalendarEventsMenu();
                        }
                        break;
                    case 12:
                        showWeatherInformation();
                        break;
                    case 13:
                        if (checkLoginStatus()) {
                            showSmartScenesMenu();
                        }
                        break;
                    case 14:
                        if (checkLoginStatus()) {
                            showDeviceHealthMenu();
                        }
                        break;
                    case 15:
                        if (checkLoginStatus()) {
                            showUsageAnalytics();
                        }
                        break;
                    case 16:
                        if (checkLoginStatus()) {
                            smartHomeService.logout();
                        }
                        break;
                    case 17:
                        handleApplicationExit();
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-17.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-17.");
            }
        }
    }
    
    private static void handleRegistrationWhileLoggedIn() {
        while (true) {
            System.out.println("\n=== Account Registration ===");
            System.out.println("You are currently logged in to an account.");
            System.out.println("To register a new account, you need to logout first.");
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("1. Yes, logout and register new account");
            System.out.println("2. No, keep current session and return to menu");
            System.out.print("Choose an option (1-2): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        System.out.println("\n[INFO] Logging out from current session...");
                        smartHomeService.logout();
                        System.out.println("[SUCCESS] Logged out successfully!");
                        System.out.println("\nNow let's register your new account:");
                        registerCustomer();
                        return;
                        
                    case 2:
                        System.out.println("\nReturning to main menu with current session active.");
                        return;
                        
                    default:
                        System.out.println("Invalid option! Please choose 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
        }
    }
    
    private static void registerCustomer() {
        System.out.println("\n=== Register New Account ===");
        
        try {
            System.out.print("Enter your full Name: ");
            String fullName = getValidatedInput("Full Name");
            if (fullName == null) return;
            
            System.out.print("Enter your email: ");
            String email = getValidatedInput("Email");
            if (email == null) return;
            
            System.out.println("\n[Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");
            System.out.print("\nChoose Your password: ");
            String password = getValidatedInput("Password");
            if (password == null) return;
            
            System.out.print("Choose Your password again: ");
            String confirmPassword = getValidatedInput("Confirm Password");
            if (confirmPassword == null) return;
            
            boolean success = smartHomeService.registerCustomer(fullName, email, password, confirmPassword);
            
            if (success) {
                System.out.println("\n[SUCCESS] Registration successful! You can now login with your credentials.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Registration failed due to input error. Please try again.");
        }
    }
    
    private static void loginCustomer() {
        System.out.println("\n=== Login ===");
        
        try {
            System.out.print("Email: ");
            String email = getValidatedInput("Email");
            if (email == null) return;
            
            System.out.print("Password: ");
            String password = getValidatedInput("Password");
            if (password == null) return;
            
            smartHomeService.loginCustomer(email, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed due to input error. Please try again.");
        }
    }
    
    private static void handleApplicationExit() {
        System.out.println("\n=== Application Exit ===");
        
        // Check if user is logged in and logout if necessary
        if (smartHomeService.isLoggedIn()) {
            System.out.println("Logging out current session...");
            smartHomeService.logout();
            System.out.println("Session ended successfully.");
        }
        
        // Shutdown timer service properly
        try {
            smartHomeService.getTimerService().shutdown();
            System.out.println("Timer service shutdown completed.");
        } catch (Exception e) {
            System.err.println("Warning: Error shutting down timer service: " + e.getMessage());
        }
        
        System.out.println("\nThank you for using IoT Smart Home Enterprise Dashboard!");
        System.out.println("Application closed safely.");
    }
    
    private static boolean checkLoginStatus() {
        if (!smartHomeService.isLoggedIn()) {
            return handleLoginFlow();
        }
        return true;
    }
    
    private static boolean handleLoginFlow() {
        while (true) {
            System.out.println("\n=== Authentication Required ===");
            System.out.println("You need to be logged in to access this feature.");
            System.out.println();
            System.out.println("Do you have an account with us?");
            System.out.println("1. Yes, I have an account (Login)");
            System.out.println("2. No, I'm new here (Register)");
            System.out.println("3. Cancel and return to main menu");
            System.out.print("Choose an option (1-3): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        System.out.println("\n=== Login ===");
                        boolean loginSuccess = attemptLogin();
                        if (loginSuccess) {
                            System.out.println("[SUCCESS] Welcome back! You can now access all features.");
                            return true;
                        } else {
                            System.out.println("Would you like to try again?");
                            System.out.println("1. Yes, try login again");
                            System.out.println("2. No, return to main menu");
                            System.out.print("Choose (1-2): ");
                            try {
                                int retryChoice = Integer.parseInt(scanner.nextLine().trim());
                                if (retryChoice != 1) {
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        }
                        break;
                        
                    case 2:
                        System.out.println("\n=== Register New Account ===");
                        System.out.println("Great! Let's create your account.");
                        boolean registerSuccess = attemptRegistration();
                        if (registerSuccess) {
                            System.out.println("\n[INFO] Registration successful! Now please login with your credentials.");
                            boolean autoLoginSuccess = attemptLogin();
                            if (autoLoginSuccess) {
                                System.out.println("[SUCCESS] Welcome to IoT Smart Home Dashboard! You can now access all features.");
                                return true;
                            } else {
                                System.out.println("[INFO] Please try logging in from the main menu.");
                                return false;
                            }
                        } else {
                            System.out.println("Would you like to try registration again?");
                            System.out.println("1. Yes, try register again");
                            System.out.println("2. No, return to main menu");
                            System.out.print("Choose (1-2): ");
                            try {
                                int retryChoice = Integer.parseInt(scanner.nextLine().trim());
                                if (retryChoice != 1) {
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        }
                        break;
                        
                    case 3:
                        System.out.println("Returning to main menu...");
                        return false;
                        
                    default:
                        System.out.println("Invalid option! Please choose 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-3.");
            }
        }
    }
    
    private static boolean attemptLogin() {
        try {
            System.out.print("Email: ");
            String email = getValidatedInput("Email");
            if (email == null) return false;
            
            System.out.print("Password: ");
            String password = getValidatedInput("Password");
            if (password == null) return false;
            
            return smartHomeService.loginCustomer(email, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed due to input error. Please try again.");
            return false;
        }
    }
    
    private static boolean attemptRegistration() {
        try {
            System.out.print("Enter your full Name: ");
            String fullName = getValidatedInput("Full Name");
            if (fullName == null) return false;
            
            System.out.print("Enter your email: ");
            String email = getValidatedInput("Email");
            if (email == null) return false;
            
            System.out.println("\n[Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");
            System.out.print("\nChoose Your password: ");
            String password = getValidatedInput("Password");
            if (password == null) return false;
            
            System.out.print("Choose Your password again: ");
            String confirmPassword = getValidatedInput("Confirm Password");
            if (confirmPassword == null) return false;
            
            return smartHomeService.registerCustomer(fullName, email, password, confirmPassword);
        } catch (Exception e) {
            System.out.println("[ERROR] Registration failed due to input error. Please try again.");
            return false;
        }
    }
    
    private static void showGadgetControlMenu() {
        while (true) {
            System.out.println("\n=== Device Management Center ===");
            System.out.println("[DEVICE OPERATIONS]:");
            System.out.println("1. Add New Device");
            System.out.println("2. Edit Existing Device");
            System.out.println("3. Delete Device");
            System.out.println("4. View All Devices");
            System.out.println();
            
            System.out.println("[ADD DEVICE CATEGORIES]:");
            System.out.println("5. Entertainment (TV, Speaker)");
            System.out.println("6. Climate Control (AC, Fan, Air Purifier, Thermostat)");
            System.out.println("7. Lighting & Switches (Smart Light, Switch)");
            System.out.println("8. Security & Safety (Camera, Door Lock, Doorbell)");
            System.out.println("9. Kitchen & Appliances (Refrigerator, Microwave, Washing Machine, Geyser, Water Purifier)");
            System.out.println("10. Cleaning (Robotic Vacuum)");
            System.out.println();
            System.out.println("11. Return to Main Menu");
            
            System.out.print("Choose an option (1-11): ");
            
            try {
                String inputLine = scanner.nextLine().trim();
                
                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }
                
                int choice = Integer.parseInt(inputLine);
                
                switch (choice) {
                    case 1: showAddDeviceSubmenu(); break;
                    case 2: editDevice(); break;
                    case 3: deleteDevice(); break;
                    case 4: viewAllDevicesManagement(); break;
                    case 5: showEntertainmentDevices(); break;
                    case 6: showClimateDevices(); break;
                    case 7: showLightingDevices(); break;
                    case 8: showSecurityDevices(); break;
                    case 9: showKitchenDevices(); break;
                    case 10: showCleaningDevices(); break;
                    case 11: return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-11.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-11.");
            }
        }
    }
    
    private static void controlTV() {
        System.out.println("\n=== Control TV ===");
        
        try {
            String model = getValidatedGadgetInput("TV Model/Brand", "Samsung, Sony, LG, TCL, Hisense, Panasonic, Philips, MI, OnePlus, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("TV", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control TV. Please try again.");
        }
    }
    
    private static void controlAC() {
        System.out.println("\n=== Control AC ===");
        
        try {
            String model = getValidatedGadgetInput("AC Model", "LG, Voltas, Blue Star, Samsung, Daikin, Hitachi, Panasonic, Carrier, Godrej, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("AC", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control AC. Please try again.");
        }
    }
    
    private static void controlFan() {
        System.out.println("\n=== Control Fan ===");
        
        try {
            String model = getValidatedGadgetInput("Fan Model", "Atomberg, Crompton, Havells, Bajaj, Usha, Orient, Luminous, Polycab, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("FAN", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Fan. Please try again.");
        }
    }
    
    private static void controlSpeaker() {
        System.out.println("\n=== Control Smart Speaker ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Speaker Model", "Amazon Echo, Google Home, MI, Realme, JBL, Sony, Boat, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("SPEAKER", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Speaker. Please try again.");
        }
    }
    
    private static void controlAirPurifier() {
        System.out.println("\n=== Control Air Purifier ===");
        
        try {
            String model = getValidatedGadgetInput("Air Purifier Model", "MI, Realme, Honeywell, Philips, Kent, Eureka Forbes, Sharp, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("AIR_PURIFIER", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Air Purifier. Please try again.");
        }
    }
    
    private static void controlThermostat() {
        System.out.println("\n=== Control Smart Thermostat ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Thermostat Model", "Honeywell, Johnson Controls, Schneider Electric, Siemens, Nest, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("THERMOSTAT", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Thermostat. Please try again.");
        }
    }
    
    private static void controlLight() {
        System.out.println("\n=== Control Smart Light ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Light Model", "Philips Hue, MI, Syska, Havells, Wipro, Bajaj, Orient, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("LIGHT", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Light. Please try again.");
        }
    }
    
    private static void controlSwitch() {
        System.out.println("\n=== Control Smart Switch ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Switch Model", "Anchor, Havells, Legrand, Schneider Electric, Wipro, Polycab, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("SWITCH", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Switch. Please try again.");
        }
    }
    
    private static void controlCamera() {
        System.out.println("\n=== Control Security Camera ===");
        
        try {
            String model = getValidatedGadgetInput("Security Camera Model", "MI, Realme, TP-Link, D-Link, Hikvision, CP Plus, Godrej, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("CAMERA", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Security Camera. Please try again.");
        }
    }
    
    private static void controlLock() {
        System.out.println("\n=== Control Smart Door Lock ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Lock Model", "Godrej, Yale, Samsung, Philips, MI, Realme, Ultraloq, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("DOOR_LOCK", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Door Lock. Please try again.");
        }
    }
    
    private static void controlDoorbell() {
        System.out.println("\n=== Control Smart Doorbell ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Doorbell Model", "MI, Realme, Godrej, Yale, Ring, Honeywell, CP Plus, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("DOORBELL", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Smart Doorbell. Please try again.");
        }
    }
    
    private static void controlRefrigerator() {
        System.out.println("\n=== Control Refrigerator ===");
        
        try {
            String model = getValidatedGadgetInput("Refrigerator Model", "LG, Samsung, Whirlpool, Godrej, Haier, Panasonic, Bosch, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("REFRIGERATOR", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Refrigerator. Please try again.");
        }
    }
    
    private static void controlMicrowave() {
        System.out.println("\n=== Control Microwave ===");
        
        try {
            String model = getValidatedGadgetInput("Microwave Model", "LG, Samsung, IFB, Panasonic, Whirlpool, Godrej, Bajaj, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("MICROWAVE", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Microwave. Please try again.");
        }
    }
    
    private static void controlWashingMachine() {
        System.out.println("\n=== Control Washing Machine ===");
        
        try {
            String model = getValidatedGadgetInput("Washing Machine Model", "LG, Samsung, Whirlpool, IFB, Bosch, Godrej, Haier, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("WASHING_MACHINE", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Washing Machine. Please try again.");
        }
    }
    
    private static void controlGeyser() {
        System.out.println("\n=== Control Water Heater/Geyser ===");
        
        try {
            String model = getValidatedGadgetInput("Geyser Model", "Bajaj, Havells, Crompton, V-Guard, Racold, AO Smith, Haier, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("GEYSER", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Water Heater/Geyser. Please try again.");
        }
    }
    
    private static void controlWaterPurifier() {
        System.out.println("\n=== Control Water Purifier ===");
        
        try {
            String model = getValidatedGadgetInput("Water Purifier Model", "Kent, Aquaguard, Pureit, LivPure, Blue Star, Havells, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("WATER_PURIFIER", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Water Purifier. Please try again.");
        }
    }
    
    private static void controlVacuum() {
        System.out.println("\n=== Control Robotic Vacuum ===");
        
        try {
            String model = getValidatedGadgetInput("Robotic Vacuum Model", "MI Robot, Realme TechLife, Eureka Forbes, Kent, Black+Decker, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            boolean success = smartHomeService.connectToGadget("VACUUM", model, roomName);
            if (success) {
                handlePostDeviceAdditionFlow();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to control Robotic Vacuum. Please try again.");
        }
    }
    
    private static void viewGadgets() {
        System.out.println("\n=== View Gadgets ===");
        
        List<Gadget> gadgets = smartHomeService.viewGadgets();
        
        if (gadgets != null && !gadgets.isEmpty()) {
            System.out.print("\nEnter gadget number to check status (or 0 to return): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                if (choice > 0 && choice <= gadgets.size()) {
                    Gadget selectedGadget = gadgets.get(choice - 1);
                    System.out.println("\nGadget Status:");
                    System.out.println(selectedGadget.getType() + " " + selectedGadget.getModel() + 
                                     " in " + selectedGadget.getRoomName() + " - " + selectedGadget.getStatus());
                } else if (choice != 0) {
                    System.out.println("Invalid gadget number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    private static void changeGadgetStatus() {
        System.out.println("\n=== Control Device Status ===");
        
        List<Gadget> gadgets = smartHomeService.viewGadgets();
        
        if (gadgets != null && !gadgets.isEmpty()) {
            System.out.println("\nSelect device to control (enter number from above list):");
            System.out.print("Choose device number: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                if (choice >= 1 && choice <= gadgets.size()) {
                    Gadget selectedGadget = gadgets.get(choice - 1);
                    String gadgetType = selectedGadget.getType();
                    String roomName = selectedGadget.getRoomName();
                    
                    System.out.println("\nSelected: " + gadgetType + " " + selectedGadget.getModel() + 
                                     " in " + roomName);
                    
                    smartHomeService.changeSpecificGadgetStatus(gadgetType, roomName);
                } else {
                    System.out.println("Invalid option! Please choose between 1-" + gadgets.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-" + gadgets.size() + ".");
            }
        }
    }
    
    private static String getValidatedInput(String fieldName) {
        try {
            String input = scanner.nextLine();
            
            if (input == null) {
                if (!fieldName.isEmpty()) {
                    System.out.println(fieldName + " cannot be null. Please try again.");
                } else {
                    System.out.println("Input cannot be null. Please try again.");
                }
                return null;
            }
            
            input = input.trim();
            if (input.isEmpty()) {
                if (!fieldName.isEmpty()) {
                    System.out.println(fieldName + " cannot be empty. Please try again.");
                } else {
                    System.out.println("Input cannot be empty. Please try again.");
                }
                return null;
            }
            
            return input;
            
        } catch (Exception e) {
            if (!fieldName.isEmpty()) {
                System.out.println("Error reading input for " + fieldName + ". Please try again.");
            } else {
                System.out.println("Error reading input. Please try again.");
            }
            return null;
        }
    }
    
    private static String getValidatedGadgetInput(String fieldName, String validOptions) {
        System.out.println("Valid " + fieldName + "s: " + validOptions);
        System.out.print("Enter " + fieldName + ": ");
        return getValidatedInput("");
    }
    
    private static int getValidatedIntegerInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String inputLine = scanner.nextLine().trim();
                
                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }
                
                return Integer.parseInt(inputLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
    }
    
    private static void handlePostDeviceAdditionFlow() {
        while (true) {
            int choice = getValidatedIntegerInput("Do you want to add another device? (1 for Yes, 2 for No): ");
            switch (choice) {
                    case 1:
                        showGadgetControlMenu();
                        return;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid option! Please enter 1 for Yes or 2 for No.");
                }
        }
    }
    
    private static void forgotPassword() {
        System.out.println("\n=== Forgot Password ===");
        
        try {
            System.out.print("Enter your registered email: ");
            String email = getValidatedInput("Email");
            if (email == null) return;
            
            if (!smartHomeService.initiatePasswordReset(email)) {
                return;
            }
            
            System.out.println("\n[New Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");
            System.out.print("\nEnter your new password: ");
            String newPassword = getValidatedInput("New Password");
            if (newPassword == null) return;
            
            System.out.print("Confirm your new password: ");
            String confirmPassword = getValidatedInput("Confirm Password");
            if (confirmPassword == null) return;
            
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("[ERROR] Passwords do not match!");
                return;
            }
            
            boolean success = smartHomeService.resetPassword(email, newPassword);
            if (success) {
                System.out.println("\n[SUCCESS] You can now login with your new password!");
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Password reset failed due to an error. Please try again.");
        }
    }
    
    private static void showGroupManagementMenu() {
        while (true) {
            System.out.println("\n=== Group Management ===");
            System.out.println("1. View Group Information");
            System.out.println("2. Add Person to Group");
            System.out.println("3. Remove Person from Group (Admin Only)");
            System.out.println("4. Return to Main Menu");
            
            System.out.print("Choose an option (1-4): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        viewGroupInformation();
                        break;
                    case 2:
                        addPersonToGroup();
                        break;
                    case 3:
                        removePersonFromGroup();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-4.");
            }
        }
    }
    
    private static void viewGroupInformation() {
        smartHomeService.showGroupInfo();
    }
    
    private static void addPersonToGroup() {
        System.out.println("\n=== Add Person to Group ===");
        
        try {
            System.out.print("Enter email address to add to your group: ");
            String memberEmail = getValidatedInput("Email");
            if (memberEmail == null) return;
            
            smartHomeService.addPersonToGroup(memberEmail);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to add person to group. Please try again.");
        }
    }
    
    private static void removePersonFromGroup() {
        System.out.println("\n=== Remove Person from Group ===");
        
        // First show group info
        smartHomeService.showGroupInfo();
        
        try {
            System.out.print("\nEnter email address to remove from your group: ");
            String memberEmail = getValidatedInput("Email");
            if (memberEmail == null) return;
            
            System.out.println("\n[!] CONFIRMATION [!]");
            System.out.println("Are you sure you want to remove " + memberEmail + " from the group?");
            System.out.println("1. Yes, Remove Member");
            System.out.println("2. No, Cancel");
            
            System.out.print("Choose option (1-2): ");
            int confirmation = Integer.parseInt(scanner.nextLine().trim());
            
            if (confirmation == 1) {
                boolean success = smartHomeService.removePersonFromGroup(memberEmail);
                if (success) {
                    System.out.println("[*] Member removed successfully!");
                }
            } else {
                System.out.println("Remove operation cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Remove operation cancelled.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to remove person from group. Please try again.");
        }
    }
    
    private static void showEnergyManagementReport() {
        System.out.println("\n=== Energy Management System ===");
        smartHomeService.showEnergyReport();
    }
    
    private static void scheduleDeviceTimer() {
        System.out.println("\n=== Schedule Device Timer ===");
        System.out.println(smartHomeService.getTimerService().getTimerHelp());
        
        List<Gadget> gadgets = smartHomeService.viewGadgets();
        
        if (gadgets == null || gadgets.isEmpty()) {
            System.out.println("No registered devices found! Please add some devices first.");
            return;
        }
        
        System.out.println("\nSelect a device to schedule timer:");
        System.out.print("Choose device number: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            if (choice >= 1 && choice <= gadgets.size()) {
                Gadget selectedGadget = gadgets.get(choice - 1);
                String deviceType = selectedGadget.getType();
                String roomName = selectedGadget.getRoomName();
                
                String currentStatus = selectedGadget.getStatus();
                String action = selectedGadget.isOn() ? "OFF" : "ON";
                
                System.out.println("\nSelected: " + deviceType + " " + selectedGadget.getModel() + 
                                 " in " + roomName + " [Current Status: " + currentStatus + "]");
                System.out.println("Timer will turn device " + action + " (opposite of current status)");
                
                System.out.print("Enter date and time (DD-MM-YYYY HH:MM): ");
                String dateTime = getValidatedInput("Date Time");
                if (dateTime == null) return;
                
                smartHomeService.scheduleDeviceTimer(deviceType, roomName, action, dateTime);
            } else {
                System.out.println("Invalid option! Please choose between 1-" + gadgets.size() + ".");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number between 1-" + gadgets.size() + ".");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to schedule timer. Please try again.");
        }
    }
    
    private static void showScheduledTimers() {
        System.out.println("\n=== Scheduled Timers Management ===");
        List<Gadget> timersWithDevices = smartHomeService.getScheduledTimersWithDevices();
        
        if (timersWithDevices == null || timersWithDevices.isEmpty()) {
            System.out.println("No timers scheduled.");
            System.out.println("\n1. Return to Main Menu");
            System.out.print("Choose an option: ");
            try {
                scanner.nextLine().trim();
            } catch (Exception e) {
                // Ignore
            }
            return;
        }
        
        System.out.println("\n=== Timer Actions ===");
        System.out.println("1. Cancel Timer");
        System.out.println("2. Return to Main Menu");
        System.out.print("Choose an option: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 1) {
                cancelTimerBySelection(timersWithDevices);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Returning to main menu.");
        }
    }
    
    private static void cancelTimerBySelection(List<Gadget> timersWithDevices) {
        try {
            System.out.println("\n=== Cancel Timer - Select Device & Action ===");
            
            // Build list of timer entries
            List<TimerEntry> timerEntries = new ArrayList<>();
            for (Gadget device : timersWithDevices) {
                if (device.getScheduledOnTime() != null) {
                    timerEntries.add(new TimerEntry(device, "ON", device.getScheduledOnTime()));
                }
                if (device.getScheduledOffTime() != null) {
                    timerEntries.add(new TimerEntry(device, "OFF", device.getScheduledOffTime()));
                }
            }
            
            if (timerEntries.isEmpty()) {
                System.out.println("No timers found to cancel.");
                return;
            }
            
            // Display timer options
            for (int i = 0; i < timerEntries.size(); i++) {
                TimerEntry entry = timerEntries.get(i);
                System.out.printf("%d. %s %s in %s - Turn %s at: %s\n", 
                    (i + 1), 
                    entry.device.getType(), 
                    entry.device.getModel(),
                    entry.device.getRoomName(), 
                    entry.action,
                    entry.scheduledTime.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            }
            
            System.out.print("Choose timer to cancel (1-" + timerEntries.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= timerEntries.size()) {
                TimerEntry selectedEntry = timerEntries.get(choice - 1);
                
                boolean success = smartHomeService.cancelDeviceTimer(
                    selectedEntry.device.getType(),
                    selectedEntry.device.getRoomName(),
                    selectedEntry.action
                );
                
                if (success) {
                    System.out.println("[SUCCESS] Timer cancelled successfully!");
                }
            } else {
                System.out.println("Invalid choice! Please choose between 1-" + timerEntries.size());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to cancel timer. Please try again.");
        }
    }
    
    private static class TimerEntry {
        public final Gadget device;
        public final String action;
        public final java.time.LocalDateTime scheduledTime;
        
        public TimerEntry(Gadget device, String action, java.time.LocalDateTime scheduledTime) {
            this.device = device;
            this.action = action;
            this.scheduledTime = scheduledTime;
        }
    }
    
    private static void showCalendarEventsMenu() {
        while (true) {
            System.out.println("\n=== Calendar Events & Automation ===");
            System.out.println("1. Create New Event");
            System.out.println("2. View Upcoming Events");
            System.out.println("3. View Event Automation Details");
            System.out.println("4. Event Types Help");
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: createCalendarEvent(); break;
                    case 2: showUpcomingEvents(); break;
                    case 3: showEventAutomationDetails(); break;
                    case 4: showEventTypesHelp(); break;
                    case 5: return;
                    default: System.out.println("Invalid option! Please choose between 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-5.");
            }
        }
    }
    
    private static void createCalendarEvent() {
        System.out.println("\n=== Create Calendar Event ===");
        System.out.println(smartHomeService.getCalendarService().getCalendarHelp());
        
        try {
            System.out.print("Enter event title: ");
            String title = getValidatedInput("Event Title");
            if (title == null) return;
            
            System.out.print("Enter event description: ");
            String description = getValidatedInput("Description");
            if (description == null) return;
            
            System.out.print("Enter start date and time (DD-MM-YYYY HH:MM): ");
            String startDateTime = getValidatedInput("Start Date Time");
            if (startDateTime == null) return;
            
            System.out.print("Enter end date and time (DD-MM-YYYY HH:MM): ");
            String endDateTime = getValidatedInput("End Date Time");
            if (endDateTime == null) return;
            
            System.out.println("Available event types:");
            List<String> eventTypes = smartHomeService.getCalendarService().getEventTypes();
            for (int i = 0; i < eventTypes.size(); i++) {
                System.out.println((i + 1) + ". " + eventTypes.get(i));
            }
            
            System.out.print("Choose event type (1-" + eventTypes.size() + "): ");
            String eventType;
            try {
                int typeChoice = Integer.parseInt(scanner.nextLine().trim());
                if (typeChoice < 1 || typeChoice > eventTypes.size()) {
                    System.out.println("Invalid choice! Please choose between 1-" + eventTypes.size());
                    return;
                }
                eventType = eventTypes.get(typeChoice - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-" + eventTypes.size());
                return;
            }
            
            smartHomeService.createCalendarEvent(title, description, startDateTime, endDateTime, eventType);
            
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create calendar event. Please try again.");
        }
    }
    
    private static void showUpcomingEvents() {
        smartHomeService.showUpcomingEvents();
    }
    
    private static void showEventAutomationDetails() {
        try {
            System.out.print("Enter event title to view automation details: ");
            String eventTitle = getValidatedInput("Event Title");
            if (eventTitle == null) return;
            
            smartHomeService.showEventAutomation(eventTitle);
            
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to show event automation details.");
        }
    }
    
    private static void showEventTypesHelp() {
        System.out.println(smartHomeService.getCalendarService().getCalendarHelp());
    }
    
    private static void showWeatherInformation() {
        while (true) {
            System.out.println("\n=== Weather-Based Smart Automation ===");
            System.out.println("1. Current Weather & Suggestions");
            System.out.println("2. 5-Day Weather Forecast");
            System.out.println("3. Weather Automation Help");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: smartHomeService.showCurrentWeather(); break;
                    case 2: smartHomeService.showWeatherForecast(); break;
                    case 3: showWeatherHelp(); break;
                    case 4: return;
                    default: System.out.println("Invalid option! Please choose between 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-4.");
            }
        }
    }
    
    private static void showWeatherHelp() {
        System.out.println(smartHomeService.getWeatherService().getWeatherHelp());
    }
    
    private static void showAddDeviceSubmenu() {
        while (true) {
            System.out.println("\n=== Add New Device ===");
            System.out.println("1. Entertainment (TV, Speaker)");
            System.out.println("2. Climate Control (AC, Fan, Air Purifier, Thermostat)");
            System.out.println("3. Lighting & Switches (Smart Light, Switch)");
            System.out.println("4. Security & Safety (Camera, Door Lock, Doorbell)");
            System.out.println("5. Kitchen & Appliances (Refrigerator, Microwave, Washing Machine, Geyser, Water Purifier)");
            System.out.println("6. Cleaning (Robotic Vacuum)");
            System.out.println("7. Return to Device Management");
            
            System.out.print("Choose device category (1-7): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: showEntertainmentDevices(); return;
                    case 2: showClimateDevices(); return;
                    case 3: showLightingDevices(); return;
                    case 4: showSecurityDevices(); return;
                    case 5: showKitchenDevices(); return;
                    case 6: showCleaningDevices(); return;
                    case 7: return;
                    default: System.out.println("Invalid option! Please choose between 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-7.");
            }
        }
    }
    
    private static void showEntertainmentDevices() {
        while (true) {
            System.out.println("\n=== Entertainment Devices ===");
            System.out.println("1. Add TV");
            System.out.println("2. Add Smart Speaker");
            System.out.println("3. Return");
            
            System.out.print("Choose option (1-3): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlTV(); return;
                    case 2: controlSpeaker(); return;
                    case 3: return;
                    default: System.out.println("Invalid option! Please choose between 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-3.");
            }
        }
    }
    
    private static void showClimateDevices() {
        while (true) {
            System.out.println("\n=== Climate Control Devices ===");
            System.out.println("1. Add AC");
            System.out.println("2. Add Fan");
            System.out.println("3. Add Air Purifier");
            System.out.println("4. Add Smart Thermostat");
            System.out.println("5. Return");
            
            System.out.print("Choose option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlAC(); return;
                    case 2: controlFan(); return;
                    case 3: controlAirPurifier(); return;
                    case 4: controlThermostat(); return;
                    case 5: return;
                    default: System.out.println("Invalid option! Please choose between 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-5.");
            }
        }
    }
    
    private static void showLightingDevices() {
        while (true) {
            System.out.println("\n=== Lighting & Switch Devices ===");
            System.out.println("1. Add Smart Light");
            System.out.println("2. Add Smart Switch");
            System.out.println("3. Return");
            
            System.out.print("Choose option (1-3): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlLight(); return;
                    case 2: controlSwitch(); return;
                    case 3: return;
                    default: System.out.println("Invalid option! Please choose between 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-3.");
            }
        }
    }
    
    private static void showSecurityDevices() {
        while (true) {
            System.out.println("\n=== Security & Safety Devices ===");
            System.out.println("1. Add Security Camera");
            System.out.println("2. Add Smart Door Lock");
            System.out.println("3. Add Smart Doorbell");
            System.out.println("4. Return");
            
            System.out.print("Choose option (1-4): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlCamera(); return;
                    case 2: controlLock(); return;
                    case 3: controlDoorbell(); return;
                    case 4: return;
                    default: System.out.println("Invalid option! Please choose between 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-4.");
            }
        }
    }
    
    private static void showKitchenDevices() {
        while (true) {
            System.out.println("\n=== Kitchen & Appliance Devices ===");
            System.out.println("1. Add Refrigerator");
            System.out.println("2. Add Microwave");
            System.out.println("3. Add Washing Machine");
            System.out.println("4. Add Water Heater/Geyser");
            System.out.println("5. Add Water Purifier");
            System.out.println("6. Return");
            
            System.out.print("Choose option (1-6): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlRefrigerator(); return;
                    case 2: controlMicrowave(); return;
                    case 3: controlWashingMachine(); return;
                    case 4: controlGeyser(); return;
                    case 5: controlWaterPurifier(); return;
                    case 6: return;
                    default: System.out.println("Invalid option! Please choose between 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-6.");
            }
        }
    }
    
    private static void showCleaningDevices() {
        while (true) {
            System.out.println("\n=== Cleaning Devices ===");
            System.out.println("1. Add Robotic Vacuum");
            System.out.println("2. Return");
            
            System.out.print("Choose option (1-2): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlVacuum(); return;
                    case 2: return;
                    default: System.out.println("Invalid option! Please choose between 1-2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-2.");
            }
        }
    }
    
    private static void viewAllDevicesManagement() {
        smartHomeService.viewGadgets();
    }
    
    private static void editDevice() {
        System.out.println("\n=== Edit Device ===");
        
        java.util.List<com.smarthome.model.Gadget> devices = smartHomeService.viewGadgets();
        if (devices == null || devices.isEmpty()) {
            System.out.println("No devices available to edit.");
            return;
        }
        
        System.out.print("Enter device number to edit: ");
        try {
            int deviceIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (deviceIndex >= 0 && deviceIndex < devices.size()) {
                com.smarthome.model.Gadget device = devices.get(deviceIndex);
                
                System.out.println("\n=== Editing: " + device.getType() + " " + device.getModel() + " ===");
                System.out.println("1. Change Room Location");
                System.out.println("2. Change Device Model");
                System.out.println("3. Change Power Rating");
                System.out.println("4. Cancel Edit");
                
                System.out.print("Choose what to edit (1-4): ");
                int editChoice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (editChoice) {
                    case 1:
                        System.out.print("Enter new room name: ");
                        String newRoom = getValidatedInput("Room Name");
                        if (newRoom != null) {
                            boolean success = smartHomeService.editDeviceRoom(device.getType(), device.getRoomName(), newRoom);
                            if (success) {
                                System.out.println("[SUCCESS] Device room updated successfully!");
                            }
                        }
                        break;
                    case 2:
                        System.out.print("Enter new model name: ");
                        String newModel = getValidatedInput("Model Name");
                        if (newModel != null) {
                            boolean success = smartHomeService.editDeviceModel(device.getType(), device.getRoomName(), newModel);
                            if (success) {
                                System.out.println("[SUCCESS] Device model updated successfully!");
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Enter new power rating (Watts): ");
                        try {
                            double newPower = Double.parseDouble(scanner.nextLine().trim());
                            boolean success = smartHomeService.editDevicePower(device.getType(), device.getRoomName(), newPower);
                            if (success) {
                                System.out.println("[SUCCESS] Power rating updated successfully!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("[ERROR] Invalid power rating! Please enter a valid number.");
                        }
                        break;
                    case 4:
                        System.out.println("Edit cancelled.");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } else {
                System.out.println("[ERROR] Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        }
    }
    
    private static void deleteDevice() {
        System.out.println("\n=== Delete Device ===");
        
        java.util.List<com.smarthome.model.Gadget> devices = smartHomeService.viewGadgets();
        if (devices == null || devices.isEmpty()) {
            System.out.println("No devices available to delete.");
            return;
        }
        
        System.out.print("Enter device number to delete: ");
        try {
            int deviceIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (deviceIndex >= 0 && deviceIndex < devices.size()) {
                com.smarthome.model.Gadget device = devices.get(deviceIndex);
                
                System.out.println("\n[!] DELETE CONFIRMATION [!]");
                System.out.println("Device: " + device.getType() + " " + device.getModel() + " (" + device.getRoomName() + ")");
                System.out.println("Power Rating: " + device.getPowerRatingWatts() + "W");
                System.out.println("Usage Time: " + device.getUsageTimeFormatted());
                System.out.println("Energy Consumed: " + String.format("%.3f kWh", device.getTotalEnergyConsumedKWh()));
                System.out.println();
                System.out.println("[!] WARNING: This action cannot be undone!");
                System.out.println("All usage history and energy data will be permanently lost.");
                System.out.println();
                System.out.println("Are you sure you want to delete this device?");
                System.out.println("1. Yes, Delete Device");
                System.out.println("2. No, Cancel Delete");
                
                System.out.print("Choose option (1-2): ");
                
                int confirmation = Integer.parseInt(scanner.nextLine().trim());
                
                if (confirmation == 1) {
                    boolean success = smartHomeService.deleteDevice(device.getType(), device.getRoomName());
                    if (success) {
                        System.out.println("[SUCCESS] Device deleted successfully!");
                        System.out.println("[*] All associated data has been removed from the system.");
                    } else {
                        System.out.println("[ERROR] Failed to delete device!");
                    }
                } else if (confirmation == 2) {
                    System.out.println("Delete operation cancelled. Device preserved.");
                } else {
                    System.out.println("Invalid option! Delete operation cancelled.");
                }
            } else {
                System.out.println("[ERROR] Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        }
    }
    
    private static void showSmartScenesMenu() {
        while (true) {
            System.out.println("\n=== Smart Scenes (One-Click Automation) ===");
            System.out.println("[AVAILABLE SCENES]:");
            System.out.println("1. Execute Scene");
            System.out.println("2. View Available Scenes");
            System.out.println("3. View Scene Details");
            System.out.println("4. Return to Main Menu");
            
            int choice = getValidatedIntegerInput("Choose an option (1-4): ");
            
            switch (choice) {
                case 1:
                    executeSmartScene();
                    break;
                case 2:
                    smartHomeService.showAvailableScenes();
                    break;
                case 3:
                    viewSceneDetails();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option! Please choose between 1-4.");
            }
        }
    }
    
    private static void executeSmartScene() {
        System.out.println("\n=== Execute Smart Scene ===");
        smartHomeService.showAvailableScenes();
        
        try {
            List<String> sceneNames = smartHomeService.getSmartScenesService().getAvailableSceneNames();
            System.out.print("Choose scene to execute (1-" + sceneNames.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= sceneNames.size()) {
                String sceneName = sceneNames.get(choice - 1);
                
                System.out.println("\n[*] Executing scene: " + sceneName.toUpperCase());
                boolean success = smartHomeService.executeSmartScene(sceneName);
                
                if (success || smartHomeService.getSmartScenesService().getAvailableSceneNames().contains(sceneName.toUpperCase())) {
                    System.out.println("\n=== Updated Device Status ===");
                    smartHomeService.viewGadgets();
                }
            } else {
                System.out.println("Invalid choice! Please choose between 1-" + sceneNames.size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number between 1-8.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to execute scene. Please try again.");
        }
    }
    
    private static void viewSceneDetails() {
        try {
            System.out.println("\n=== View Scene Details ===");
            List<String> sceneNames = smartHomeService.getSmartScenesService().getAvailableSceneNames();
            
            for (int i = 0; i < sceneNames.size(); i++) {
                System.out.println((i + 1) + ". " + sceneNames.get(i));
            }
            
            System.out.print("Choose scene to view details (1-" + sceneNames.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= sceneNames.size()) {
                String sceneName = sceneNames.get(choice - 1);
                smartHomeService.showSceneDetails(sceneName);
            } else {
                System.out.println("Invalid choice! Please choose between 1-" + sceneNames.size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number between 1-8.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to show scene details.");
        }
    }
    
    private static void showDeviceHealthMenu() {
        while (true) {
            System.out.println("\n=== Device Health Monitoring ===");
            System.out.println("[HEALTH DASHBOARD]:");
            System.out.println("1. System Health Report");
            System.out.println("2. Device Maintenance Schedule");
            System.out.println("3. Health Summary");
            System.out.println("4. Return to Main Menu");
            
            System.out.print("Choose an option (1-4): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        smartHomeService.showDeviceHealthReport();
                        break;
                    case 2:
                        smartHomeService.showMaintenanceSchedule();
                        break;
                    case 3:
                        showHealthSummary();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-4.");
            }
        }
    }
    
    private static void showHealthSummary() {
        System.out.println("\n=== Quick Health Summary ===");
        String summary = smartHomeService.getSystemHealthSummary();
        System.out.println("[*] " + summary);
        System.out.println("\n[TIP] Use 'System Health Report' for detailed analysis and recommendations.");
    }
    
    private static void showUsageAnalytics() {
        while (true) {
            System.out.println("\n=== Usage Analytics & Insights ===");
            System.out.println("[ANALYTICS DASHBOARD]:");
            System.out.println("1. Energy Consumption Analysis");
            System.out.println("2. Device Usage Patterns");
            System.out.println("3. Cost Analysis & Projections");
            System.out.println("4. Efficiency Recommendations");
            System.out.println("5. Peak Usage Times");
            System.out.println("6. Return to Main Menu");
            
            System.out.print("Choose an option (1-6): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        showEnergyAnalysis();
                        break;
                    case 2:
                        showUsagePatterns();
                        break;
                    case 3:
                        showCostAnalysis();
                        break;
                    case 4:
                        showEfficiencyRecommendations();
                        break;
                    case 5:
                        showPeakUsageTimes();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-6.");
            }
        }
    }
    
    private static void showEnergyAnalysis() {
        System.out.println("\n=== Energy Consumption Analysis ===");
        smartHomeService.showEnergyReport();
        
        System.out.println("\n[INSIGHTS]:");
        System.out.println("- High consumption devices contribute most to your bill");
        System.out.println("- Consider scheduling high-power devices during off-peak hours");
        System.out.println("- Regular maintenance can improve energy efficiency by 10-15%");
    }
    
    private static void showUsagePatterns() {
        System.out.println("\n=== Device Usage Patterns ===");
        
        if (!smartHomeService.isLoggedIn()) {
            System.out.println("Please login first!");
            return;
        }
        
        var devices = smartHomeService.viewGadgets();
        if (devices == null || devices.isEmpty()) {
            System.out.println("No devices found for pattern analysis.");
            return;
        }
        
        System.out.println("\n[USAGE PATTERN ANALYSIS]:");
        
        for (var device : devices) {
            double totalHours = device.getTotalUsageMinutes() / 60.0;
            String usageCategory;
            
            if (totalHours > 50) {
                usageCategory = "Heavy User";
            } else if (totalHours > 20) {
                usageCategory = "Moderate User";
            } else if (totalHours > 5) {
                usageCategory = "Light User";
            } else {
                usageCategory = "Minimal Use";
            }
            
            System.out.printf("[*] %s (%s): %.1f hours total - %s\\n", 
                            device.getType(), device.getRoomName(), totalHours, usageCategory);
            
            if (device.isOn()) {
                System.out.printf("   [->] Currently running for %.1f hours\\n", device.getCurrentSessionUsageHours());
            }
        }
        
        System.out.println("\n[RECOMMENDATIONS]:");
        System.out.println("- Set timers for heavy-use devices to optimize consumption");
        System.out.println("- Use smart scenes to coordinate device usage efficiently");
        System.out.println("- Monitor devices that run continuously for potential issues");
    }
    
    private static void showCostAnalysis() {
        System.out.println("\n=== Cost Analysis & Projections ===");
        smartHomeService.showEnergyReport();
        
        System.out.println("\n[COST PROJECTIONS]:");
        System.out.println("Based on current usage patterns:");
        
        if (smartHomeService.isLoggedIn()) {
            var energyService = smartHomeService.getEnergyService();
            var currentUser = smartHomeService.getCurrentUser();
            var report = energyService.generateEnergyReport(currentUser);
            
            double monthlyProjection = report.getTotalCostRupees() * 4.33; // weeks to months
            double yearlyProjection = monthlyProjection * 12;
            
            System.out.printf("[Monthly] Projection: Rs.%.2f\\n", monthlyProjection);
            System.out.printf("[Yearly] Projection: Rs.%.2f\\n", yearlyProjection);
            
            System.out.println("\n[SAVINGS OPPORTUNITIES]:");
            System.out.println("- Switching to energy-efficient devices could save 20-30%");
            System.out.println("- Using timers and scenes could reduce consumption by 15%");
            System.out.println("- Off-peak usage scheduling could lower costs by 10%");
        }
    }
    
    private static void showEfficiencyRecommendations() {
        System.out.println("\n=== Efficiency Recommendations ===");
        
        smartHomeService.showDeviceHealthReport();
        
        System.out.println("\n[OPTIMIZATION STRATEGIES]:");
        System.out.println("1. IMMEDIATE ACTIONS:");
        System.out.println("   - Turn off devices when not in use");
        System.out.println("   - Use power strips to eliminate standby power");
        System.out.println("   - Set appropriate thermostat temperatures");
        
        System.out.println("\n2. SHORT-TERM IMPROVEMENTS:");
        System.out.println("   - Schedule regular maintenance for all devices");
        System.out.println("   - Use smart scenes for coordinated device control");
        System.out.println("   - Install smart plugs for better monitoring");
        
        System.out.println("\n3. LONG-TERM UPGRADES:");
        System.out.println("   - Replace old devices with energy-efficient models");
        System.out.println("   - Consider solar panels for renewable energy");
        System.out.println("   - Upgrade to smart thermostats and lighting");
    }
    
    private static void showPeakUsageTimes() {
        System.out.println("\n=== Peak Usage Time Analysis ===");
        System.out.println("[*] Analyzing your device usage patterns...");
        
        System.out.println("\n[TYPICAL PEAK USAGE PERIODS]:");
        System.out.println("[Morning Peak] (6:00 - 10:00 AM):");
        System.out.println("   - Geyser, Microwave, TV, Lights");
        System.out.println("   - Average consumption: High");
        
        System.out.println("\n[Evening Peak] (6:00 - 10:00 PM):");
        System.out.println("   - AC, TV, Lights, Entertainment systems");
        System.out.println("   - Average consumption: Very High");
        
        System.out.println("\n[Night Period] (10:00 PM - 6:00 AM):");
        System.out.println("   - Refrigerator, Security systems, Bedroom AC");
        System.out.println("   - Average consumption: Low-Medium");
        
        System.out.println("\n[LOAD BALANCING TIPS]:");
        System.out.println("- Schedule washing machine/dishwasher during off-peak hours");
        System.out.println("- Use timers to avoid simultaneous operation of high-power devices");
        System.out.println("- Consider battery storage for peak-hour backup");
        System.out.println("- Smart scenes can automatically manage peak load distribution");
    }
    
}