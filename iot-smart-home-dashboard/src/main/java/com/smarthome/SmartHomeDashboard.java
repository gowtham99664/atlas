package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import com.smarthome.util.DynamoDBConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SmartHomeDashboard {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SmartHomeService smartHomeService = new SmartHomeService();
    private static volatile boolean returnToMainMenu = false;

    private static final String[] TV_BRANDS = {
        "Samsung", "Sony", "LG", "TCL", "Hisense", "Panasonic", "Philips", "MI", "OnePlus",
        "Xiaomi", "Realme", "Redmi", "Vu", "Thomson", "Kodak", "Motorola", "Nokia",
        "Toshiba", "Sharp", "Haier", "BPL", "Videocon", "Intex", "Micromax", "Shinco"
    };

    private static final String[] AC_BRANDS = {
        "LG", "Voltas", "Blue Star", "Samsung", "Daikin", "Hitachi", "Panasonic", "Carrier",
        "Godrej", "Haier", "Whirlpool", "Lloyd", "O General", "Mitsubishi", "Toshiba",
        "Electrolux", "IFB", "Crompton", "Orient", "Bajaj", "Usha", "Havells", "Symphony"
    };

    private static final String[] FAN_BRANDS = {
        "Atomberg", "Crompton", "Havells", "Bajaj", "Usha", "Orient", "Luminous", "Polycab",
        "Anchor", "Khaitan", "Surya", "Almonard", "Activa", "Rico", "V-Guard", "Finolex",
        "Maharaja Whiteline", "Hindware", "Panasonic", "Philips", "Singer", "Westinghouse"
    };

    private static final String[] SPEAKER_BRANDS = {
        "Amazon Echo", "Google Home", "MI", "Realme", "JBL", "Sony", "Boat", "Bose",
        "Harman Kardon", "Marshall", "Ultimate Ears", "Skullcandy", "Philips", "Samsung",
        "Apple HomePod", "Alexa", "Portronics", "Zebronics", "F&D", "Creative", "Logitech"
    };

    private static final String[] AIR_PURIFIER_BRANDS = {
        "MI", "Realme", "Honeywell", "Philips", "Kent", "Eureka Forbes", "Sharp", "Dyson",
        "Xiaomi", "Blue Star", "LG", "Samsung", "Panasonic", "Coway", "Atlanta Healthcare",
        "Pure Air", "Havells", "Crompton", "V-Guard", "Livpure", "Aquaguard"
    };

    private static final String[] THERMOSTAT_BRANDS = {
        "Honeywell", "Johnson Controls", "Schneider Electric", "Siemens", "Nest", "Ecobee",
        "Emerson", "White Rodgers", "Lux", "Venstar", "Trane", "Carrier", "Lennox",
        "Rheem", "Goodman", "York", "Daikin", "Mitsubishi", "Bosch", "Danfoss"
    };

    private static final String[] LIGHT_BRANDS = {
        "Philips Hue", "MI", "Syska", "Havells", "Wipro", "Bajaj", "Orient", "Crompton",
        "Anchor", "Polycab", "Legrand", "Schneider Electric", "V-Guard", "Finolex",
        "Khaitan", "Surya", "Luminous", "Panasonic", "Osram", "GE", "Cree", "LIFX"
    };

    private static final String[] SWITCH_BRANDS = {
        "Anchor", "Havells", "Legrand", "Schneider Electric", "Wipro", "Polycab", "Crompton",
        "Syska", "V-Guard", "Finolex", "Goldmedal", "Roma", "GM Modular", "Simon",
        "MK Electric", "ABB", "Panasonic", "Philips", "Orient", "Bajaj"
    };

    private static final String[] CAMERA_BRANDS = {
        "MI", "Realme", "TP-Link", "D-Link", "Hikvision", "CP Plus", "Godrej", "Dahua",
        "Samsung", "Sony", "Panasonic", "Bosch", "Axis", "Honeywell", "Pelco", "Avigilon",
        "Reolink", "Arlo", "Ring", "Nest", "Wyze", "Eufy", "Blink", "Lorex"
    };

    private static final String[] LOCK_BRANDS = {
        "Godrej", "Yale", "Samsung", "Philips", "MI", "Realme", "Ultraloq", "August",
        "Schlage", "Kwikset", "Honeywell", "Lockly", "Eufy", "Aqara", "Danalock",
        "Igloohome", "TTlock", "Kaadas", "Dessmann", "Gateman", "Epic", "Hafele"
    };

    private static final String[] DOORBELL_BRANDS = {
        "MI", "Realme", "Godrej", "Yale", "Ring", "Honeywell", "CP Plus", "Nest",
        "Arlo", "Eufy", "Blink", "SkyBell", "August", "RemoBell", "Zmodo", "Amcrest",
        "Remo+", "VTech", "Panasonic", "Friedland", "Byron", "Grothe"
    };

    private static final String[] REFRIGERATOR_BRANDS = {
        "LG", "Samsung", "Whirlpool", "Godrej", "Haier", "Panasonic", "Bosch", "IFB",
        "Voltas", "Hitachi", "Sharp", "Electrolux", "Kelvinator", "Videocon", "BPL",
        "Onida", "Lloyd", "Carrier", "Blue Star", "Liebherr", "Siemens", "Miele"
    };

    private static final String[] MICROWAVE_BRANDS = {
        "LG", "Samsung", "IFB", "Panasonic", "Whirlpool", "Godrej", "Bajaj", "Morphy Richards",
        "Haier", "Bosch", "Sharp", "Electrolux", "Voltas", "Hitachi", "Onida", "BPL",
        "Kenstar", "Glen", "Prestige", "Pigeon", "Maharaja Whiteline", "Crompton"
    };

    private static final String[] WASHING_MACHINE_BRANDS = {
        "LG", "Samsung", "Whirlpool", "IFB", "Bosch", "Godrej", "Haier", "Panasonic",
        "Voltas", "Hitachi", "Sharp", "Electrolux", "Onida", "BPL", "Videocon",
        "Lloyd", "Carrier", "Kelvinator", "Intex", "MarQ", "Realme", "MI"
    };

    private static final String[] GEYSER_BRANDS = {
        "Bajaj", "Havells", "Crompton", "V-Guard", "Racold", "AO Smith", "Haier", "Orient",
        "Usha", "Maharaja Whiteline", "Morphy Richards", "Glen", "Kenstar", "Activa",
        "Hindware", "Jaquar", "Cera", "Polycab", "Anchor", "Syska", "Venus", "Singer"
    };

    private static final String[] WATER_PURIFIER_BRANDS = {
        "Kent", "Aquaguard", "Pureit", "LivPure", "Blue Star", "Havells", "Eureka Forbes",
        "HUL", "Tata Swach", "Voltas", "Panasonic", "Faber", "A.O. Smith", "Zero B",
        "Nasaka", "Ion Exchange", "Aqua Fresh", "Crystal", "Sure from Aquaguard", "Nova"
    };

    private static final String[] VACUUM_BRANDS = {
        "MI Robot", "Realme TechLife", "Eureka Forbes", "Kent", "Black+Decker", "iRobot Roomba",
        "Shark", "Bissell", "Hoover", "Dyson", "Tineco", "Roborock", "Neato", "Eufy",
        "ILIFE", "Proscenic", "Coredy", "Tesvor", "Lefant", "Amarey", "GOOVI", "yeedi"
    };

    private static final String[] ROOM_NAMES = {
        "Living Room", "Master Bedroom", "Kitchen", "Balcony", "Study Room", "Guest Room",
        "Dining Room", "Children's Room", "Home Office", "Library", "Den", "Family Room",
        "Basement", "Attic", "Garage", "Laundry Room", "Bathroom", "Powder Room",
        "Walk-in Closet", "Pantry", "Utility Room", "Porch", "Patio", "Terrace",
        "Drawing Room", "Hall", "Foyer", "Entrance", "Corridor", "Staircase"
    };
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to IoT Smart Home Dashboard ===\n");

        System.out.println("🔍 Testing DynamoDB connection...");
        DynamoDBConfig.testConnection();
        System.out.println();

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

        displayNavigationHelp();
        
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
            System.out.println("\n=== IoT Smart Home Dashboard ===");
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
            System.out.println("16. Settings");
            System.out.println("17. Logout");
            System.out.println("18. Exit");

            System.out.print("Choose an option: ");
            
            try {
                String inputLine = scanner.nextLine().trim();
                
                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }
                
                int choice = Integer.parseInt(inputLine);
                int exitOption = 18;

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
                            showSettingsMenu();
                        }
                        break;
                    case 17:
                        if (checkLoginStatus()) {
                            smartHomeService.logout();
                        }
                        break;
                    case 18:
                        handleApplicationExit();
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-18.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-18.");
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
        System.out.println("[Navigation] Enter '0' at any prompt to return to Main Menu");

        try {
            System.out.print("Enter your full Name: ");
            String fullName = getValidatedInputWithNavigation("Full Name");
            if (fullName == null || checkReturnToMainMenu()) return;

            System.out.print("Enter your email: ");
            String email = getValidatedInputWithNavigation("Email");
            if (email == null || checkReturnToMainMenu()) return;

            if (!smartHomeService.checkEmailAvailability(email)) {
                System.out.println("\n[ERROR] This email is already registered!");
                System.out.println("[INFO] Please use a different email address or try logging in if you already have an account.");
                return;
            }

            System.out.println("[INFO] Email is available! Continuing with registration...");

            System.out.println("\n[Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");
            System.out.print("\nChoose Your password: ");
            String password = getPasswordInputWithNavigation("Password");
            if (password == null || checkReturnToMainMenu()) return;

            System.out.print("Choose Your password again: ");
            String confirmPassword = getPasswordInputWithNavigation("Confirm Password");
            if (confirmPassword == null || checkReturnToMainMenu()) return;

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
            String password = getPasswordInput("Password");
            if (password == null) return;
            
            smartHomeService.loginCustomer(email, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed due to input error. Please try again.");
        }
    }
    
    private static void handleApplicationExit() {
        System.out.println("\n=== Application Exit ===");
        
        if (smartHomeService.isLoggedIn()) {
            System.out.println("Logging out current session...");
            smartHomeService.logout();
            System.out.println("Session ended successfully.");
        }
        
        try {
            smartHomeService.getTimerService().shutdown();
            System.out.println("Timer service shutdown completed.");
        } catch (Exception e) {
            System.err.println("Warning: Error shutting down timer service: " + e.getMessage());
        }
        
        System.out.println("\nThank you for using IoT Smart Home Dashboard!");
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
                            while (true) {
                                System.out.println("Would you like to try again?");
                                System.out.println("1. Yes, try login again");
                                System.out.println("2. No, return to main menu");
                                System.out.print("Choose (1-2): ");
                                try {
                                    int retryChoice = Integer.parseInt(scanner.nextLine().trim());

                                    switch (retryChoice) {
                                        case 1:
                                            break;
                                        case 2:
                                            return false;
                                        default:
                                            System.out.println("Invalid option! Please choose 1 or 2.");
                                            continue;
                                    }
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input! Please enter a number (1 or 2).");
                                }
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
                            while (true) {
                                System.out.println("Would you like to try registration again?");
                                System.out.println("1. Yes, try register again");
                                System.out.println("2. No, return to main menu");
                                System.out.print("Choose (1-2): ");
                                try {
                                    int retryChoice = Integer.parseInt(scanner.nextLine().trim());

                                    switch (retryChoice) {
                                        case 1:
                                            break;
                                        case 2:
                                            return false;
                                        default:
                                            System.out.println("Invalid option! Please choose 1 or 2.");
                                            continue;
                                    }
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input! Please enter a number (1 or 2).");
                                }
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
            String password = getPasswordInput("Password");
            if (password == null) return false;
            
            return smartHomeService.loginCustomer(email, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed due to input error. Please try again.");
            return false;
        }
    }
    
    private static boolean attemptRegistration() {
        try {
            System.out.println("[Navigation] Enter '0' at any prompt to return to Main Menu");

            System.out.print("Enter your full Name: ");
            String fullName = getValidatedInputWithNavigation("Full Name");
            if (fullName == null || checkReturnToMainMenu()) return false;

            System.out.print("Enter your email: ");
            String email = getValidatedInputWithNavigation("Email");
            if (email == null || checkReturnToMainMenu()) return false;

            if (!smartHomeService.checkEmailAvailability(email)) {
                System.out.println("\n[ERROR] This email is already registered!");
                System.out.println("[INFO] Please use a different email address or try logging in if you already have an account.");
                return false;
            }

            System.out.println("[INFO] Email is available! Continuing with registration...");

            System.out.println("\n[Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");
            System.out.print("\nChoose Your password: ");
            String password = getPasswordInputWithNavigation("Password");
            if (password == null || checkReturnToMainMenu()) return false;

            System.out.print("Choose Your password again: ");
            String confirmPassword = getPasswordInputWithNavigation("Confirm Password");
            if (confirmPassword == null || checkReturnToMainMenu()) return false;

            return smartHomeService.registerCustomer(fullName, email, password, confirmPassword);
        } catch (Exception e) {
            System.out.println("[ERROR] Registration failed due to input error. Please try again.");
            return false;
        }
    }
    
    private static void showGadgetControlMenu() {
        while (true) {
            if (checkReturnToMainMenu()) return;

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
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-10): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
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
                    default:
                        System.out.println("Invalid option! Please choose between 0-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-10.");
            }
        }
    }
    
    private static void controlTV() {
        System.out.println("\n=== Control TV ===");
        System.out.println("[Navigation] Enter '0' to return to Main Menu");

        try {
            String model = getValidatedGadgetInputWithNavigation("TV Model/Brand", "Samsung, Sony, LG, TCL, Hisense, Panasonic, Philips, MI, OnePlus, etc.");
            if (model == null || checkReturnToMainMenu()) return;

            String roomName = getValidatedGadgetInputWithNavigation("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null || checkReturnToMainMenu()) return;

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
            String newPassword = getPasswordInput("New Password");
            if (newPassword == null) return;

            System.out.print("Confirm your new password: ");
            String confirmPassword = getPasswordInput("Confirm Password");
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
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Group Management ===");
            System.out.println("[GROUP OPERATIONS]:");
            System.out.println("1. View Group Information");
            System.out.println("2. Add Person to Group");
            System.out.println("3. Remove Person from Group (Admin Only)");
            System.out.println();
            System.out.println("[DEVICE PERMISSIONS] (Admin Only):");
            System.out.println("4. Grant Device Access to Member");
            System.out.println("5. Revoke Device Access from Member");
            System.out.println("6. View All Device Permissions");
            System.out.println();
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-6): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

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
                        grantDeviceAccess();
                        break;
                    case 5:
                        revokeDeviceAccess();
                        break;
                    case 6:
                        viewDevicePermissions();
                        break;
                    default:
                        System.out.println("Invalid option! Please choose between 0-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-6.");
            }
        }
    }
    
    private static void viewGroupInformation() {
        smartHomeService.showGroupInfo();
    }
    
    private static void addPersonToGroup() {
        System.out.println("\n=== Add Person to Group ===");
        System.out.println("[Navigation] Enter '0' to return to Main Menu");

        try {
            System.out.print("Enter email address to add to your group: ");
            String memberEmail = getValidatedInputWithNavigation("Email");
            if (memberEmail == null || checkReturnToMainMenu()) return;

            smartHomeService.addPersonToGroup(memberEmail);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to add person to group. Please try again.");
        }
    }
    
    private static void removePersonFromGroup() {
        System.out.println("\n=== Remove Person from Group ===");
        
        smartHomeService.showGroupInfo();
        
        try {
            System.out.print("\nEnter email address to remove from your group: ");
            String memberEmail = getValidatedInput("Email");
            if (memberEmail == null) return;
            
            System.out.println("\n[!] CONFIRMATION [!]");
            System.out.println("Are you sure you want to remove " + memberEmail + " from the group?");
            System.out.println("1. Yes, Remove Member");
            System.out.println("2. No, Cancel");
            
            int confirmation;
            while (true) {
                System.out.print("Choose option (1-2): ");
                try {
                    confirmation = Integer.parseInt(scanner.nextLine().trim());
                    if (confirmation == 1 || confirmation == 2) {
                        break;
                    } else {
                        System.out.println("[ERROR] Invalid option! Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid input! Please enter 1 or 2.");
                }
            }

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

    private static void grantDeviceAccess() {
        System.out.println("\n=== Grant Device Access to Member ===");

        try {
            List<Gadget> userDevices = smartHomeService.viewGadgets();
            if (userDevices == null || userDevices.isEmpty()) {
                System.out.println("[ERROR] No devices found! Please add some devices first.");
                return;
            }

            List<Gadget> ownDevices = new ArrayList<>();
            String currentUserEmail = smartHomeService.getCurrentUser().getEmail();

            for (Gadget device : userDevices) {
                ownDevices.add(device);
            }

            if (ownDevices.isEmpty()) {
                System.out.println("[ERROR] No personal devices found to share!");
                return;
            }

            System.out.println("\nSelect device to grant access for:");
            for (int i = 0; i < ownDevices.size(); i++) {
                Gadget device = ownDevices.get(i);
                System.out.println((i + 1) + ". " + device.getType() + " " + device.getModel() + " in " + device.getRoomName());
            }

            System.out.print("Choose device number (1-" + ownDevices.size() + "): ");
            int deviceChoice = Integer.parseInt(scanner.nextLine().trim());

            if (deviceChoice < 1 || deviceChoice > ownDevices.size()) {
                System.out.println("[ERROR] Invalid device number!");
                return;
            }

            Gadget selectedDevice = ownDevices.get(deviceChoice - 1);

            List<Customer> groupMembers = smartHomeService.getGroupMembersForPermissions();
            if (groupMembers.isEmpty()) {
                System.out.println("[ERROR] No group members found or you are not a group admin!");
                return;
            }

            System.out.println("\nSelect group member to grant access:");
            for (int i = 0; i < groupMembers.size(); i++) {
                Customer member = groupMembers.get(i);
                System.out.println((i + 1) + ". " + member.getFullName() + " (" + member.getEmail() + ")");
            }

            int memberChoice;
            while (true) {
                System.out.print("Choose member number (1-" + groupMembers.size() + "): ");
                try {
                    memberChoice = Integer.parseInt(scanner.nextLine().trim());
                    if (memberChoice >= 1 && memberChoice <= groupMembers.size()) {
                        break;
                    } else {
                        System.out.println("[ERROR] Invalid member number! Please choose between 1-" + groupMembers.size() + ".");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid input! Please enter a valid number between 1-" + groupMembers.size() + ".");
                }
            }

            Customer selectedMember = groupMembers.get(memberChoice - 1);

            System.out.println("\n[GRANT PERMISSION]");
            System.out.println("Device: " + selectedDevice.getType() + " " + selectedDevice.getModel() + " in " + selectedDevice.getRoomName());
            System.out.println("To: " + selectedMember.getFullName() + " (" + selectedMember.getEmail() + ")");
            System.out.println();
            System.out.println("1. Yes, Grant Access");
            System.out.println("2. No, Cancel");

            int confirmation;
            while (true) {
                System.out.print("Choose (1-2): ");
                try {
                    confirmation = Integer.parseInt(scanner.nextLine().trim());
                    if (confirmation == 1 || confirmation == 2) {
                        break;
                    } else {
                        System.out.println("[ERROR] Invalid option! Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid input! Please enter 1 or 2.");
                }
            }

            if (confirmation == 1) {
                boolean success = smartHomeService.grantDevicePermission(
                    selectedMember.getEmail(),
                    selectedDevice.getType(),
                    selectedDevice.getRoomName()
                );

                if (success) {
                    System.out.println("\n[SUCCESS] Device access granted successfully!");
                }
            } else {
                System.out.println("Grant operation cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to grant device access. Please try again.");
        }
    }

    private static void revokeDeviceAccess() {
        System.out.println("\n=== Revoke Device Access from Member ===");

        try {
                smartHomeService.showDevicePermissions();

            System.out.println("\nEnter details to revoke permission:");

            System.out.print("Member email: ");
            String memberEmail = getValidatedInput("Member Email");
            if (memberEmail == null) return;

            System.out.print("Device type (e.g., TV, AC, LIGHT): ");
            String deviceType = getValidatedInput("Device Type");
            if (deviceType == null) return;

            System.out.print("Room name: ");
            String roomName = getValidatedInput("Room Name");
            if (roomName == null) return;

                if (!smartHomeService.hasDevicePermission(memberEmail, deviceType, roomName)) {
                System.out.println("[ERROR] No permission found for " + memberEmail + " to access " + deviceType + " in " + roomName);
                return;
            }

            System.out.println("\n[REVOKE PERMISSION]");
            System.out.println("Revoke access for " + memberEmail + " to " + deviceType + " in " + roomName + "?");
            System.out.println("1. Yes, Revoke Access");
            System.out.println("2. No, Cancel");
            int confirmation;
            while (true) {
                System.out.print("Choose (1-2): ");
                try {
                    confirmation = Integer.parseInt(scanner.nextLine().trim());
                    if (confirmation == 1 || confirmation == 2) {
                        break;
                    } else {
                        System.out.println("[ERROR] Invalid option! Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid input! Please enter 1 or 2.");
                }
            }

            if (confirmation == 1) {
                boolean success = smartHomeService.revokeDevicePermission(memberEmail, deviceType, roomName);

                if (success) {
                    System.out.println("\n[SUCCESS] Device access revoked successfully!");
                }
            } else {
                System.out.println("Revoke operation cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to revoke device access. Please try again.");
        }
    }

    private static void viewDevicePermissions() {
        smartHomeService.showDevicePermissions();
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
                
                String dateTime = null;
                while (dateTime == null) {
                    System.out.print("Enter date and time (DD-MM-YYYY HH:MM): ");
                    dateTime = getValidatedInput("Date Time");
                    
                    if (dateTime == null) {
                        System.out.println("\n=== Invalid Input ===");
                        System.out.println("Date Time cannot be empty. Please try again.");
                        System.out.println();
                        System.out.println("What would you like to do?");
                        System.out.println("1. Try entering date and time again");
                        System.out.println("2. Return to main menu");
                        System.out.print("Choose an option (1-2): ");
                        
                        try {
                            int userChoice = Integer.parseInt(scanner.nextLine().trim());
                            
                            switch (userChoice) {
                                case 1:
                                    break;
                                case 2:
                                    System.out.println("Returning to main menu...");
                                    return;
                                default:
                                    System.out.println("Invalid option! Trying again...");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Trying again...");
                        }
                    }
                }
                
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
            System.out.println("\n0. Return to Main Menu");
            System.out.print("Choose an option: ");
            try {
                scanner.nextLine().trim();
            } catch (Exception e) {
            }
            return;
        }

        System.out.println("\n=== Timer Actions ===");
        System.out.println("1. Cancel Timer");
        System.out.println("2. Force Execute Due Timers");
        System.out.println();
        System.out.println("0. Return to Main Menu");
        System.out.print("Choose an option (0-2): ");

        try {
            String inputLine = scanner.nextLine().trim();

            if (inputLine.isEmpty()) {
                System.out.println("Please enter a valid option number.");
                return;
            }

            if ("0".equals(inputLine)) {
                return;
            }

            int choice = Integer.parseInt(inputLine);
            switch (choice) {
                case 1:
                    cancelTimerBySelection(timersWithDevices);
                    break;
                case 2:
                    smartHomeService.forceTimerCheck();
                    break;
                default:
                    System.out.println("Invalid option! Please choose between 0-2.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number between 0-2.");
        }
    }
    
    private static void cancelTimerBySelection(List<Gadget> timersWithDevices) {
        try {
            System.out.println("\n=== Cancel Timer - Select Device & Action ===");
            
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
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Calendar Events & Automation ===");
            System.out.println("1. Create New Event");
            System.out.println("2. View Upcoming Events");
            System.out.println("3. View Event Automation Details");
            System.out.println("4. Event Types Help");
            System.out.println();
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option (0-4): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

                switch (choice) {
                    case 1: createCalendarEvent(); break;
                    case 2: showUpcomingEvents(); break;
                    case 3: showEventAutomationDetails(); break;
                    case 4: showEventTypesHelp(); break;
                    default: System.out.println("Invalid option! Please choose between 0-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-4.");
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
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Weather-Based Smart Automation ===");
            System.out.println("[WEATHER DATA]:");
            System.out.println("1. Current Weather & Suggestions");
            System.out.println("2. Update/Enter Weather Data");
            System.out.println("3. 5-Day Weather Forecast");
            System.out.println();
            System.out.println("[MANAGEMENT]:");
            System.out.println("4. Clear Weather Data");
            System.out.println("5. Weather Automation Help");
            System.out.println();
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option (0-5): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

                switch (choice) {
                    case 1:
                        smartHomeService.showCurrentWeather();
                        break;
                    case 2:
                        smartHomeService.updateWeatherData();
                        break;
                    case 3:
                        smartHomeService.showWeatherForecast();
                        break;
                    case 4:
                        smartHomeService.clearWeatherData();
                        break;
                    case 5:
                        showWeatherHelp();
                        break;
                    default:
                        System.out.println("Invalid option! Please choose between 0-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-5.");
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
        
        int deviceIndex;
        while (true) {
            System.out.print("Enter device number to delete: ");
            try {
                deviceIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (deviceIndex >= 0 && deviceIndex < devices.size()) {
                    break; // Valid choice
                } else {
                    System.out.println("[ERROR] Invalid device number! Please choose between 1-" + devices.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid number between 1-" + devices.size() + ".");
            }
        }

        try {
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
                
                int confirmation;
                while (true) {
                    System.out.print("Choose option (1-2): ");
                    try {
                        confirmation = Integer.parseInt(scanner.nextLine().trim());
                        if (confirmation == 1 || confirmation == 2) {
                            break;
                        } else {
                            System.out.println("[ERROR] Invalid option! Please choose 1 or 2.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid input! Please enter 1 or 2.");
                    }
                }

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
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Smart Scenes (One-Click Automation) ===");
            System.out.println("*** UPDATED VERSION - Dec 14, 2025 ***");
            System.out.println("[SCENE OPERATIONS]:");
            System.out.println("1. Execute Scene");
            System.out.println("2. View Available Scenes");
            System.out.println("3. View Scene Details");
            System.out.println();
            System.out.println("[SCENE CUSTOMIZATION]:");
            System.out.println("4. Edit Scene Devices");
            System.out.println("5. Reset Scene to Original");
            System.out.println();
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-5): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

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
                        editSceneDevices();
                        break;
                    case 5:
                        resetSceneToOriginal();
                        break;
                    default:
                        System.out.println("Invalid option! Please choose between 0-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-5.");
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

    private static void editSceneDevices() {
        try {
            System.out.println("\n=== Edit Scene Devices ===");
            List<String> sceneNames = smartHomeService.getSmartScenesService().getAvailableSceneNames();

            System.out.println("Available scenes to edit:");
            for (int i = 0; i < sceneNames.size(); i++) {
                System.out.println((i + 1) + ". " + sceneNames.get(i));
            }

            System.out.print("Choose scene to edit (1-" + sceneNames.size() + "): ");

            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= sceneNames.size()) {
                String sceneName = sceneNames.get(choice - 1);
                showSceneEditingMenu(sceneName);
            } else {
                System.out.println("Invalid choice! Please choose between 1-" + sceneNames.size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to access scene editing.");
        }
    }

    private static void showSceneEditingMenu(String sceneName) {
        while (true) {
            System.out.println("\n=== Editing Scene: " + sceneName.toUpperCase() + " ===");

            // Show current scene details
            smartHomeService.showEditableSceneDetails(sceneName);

            System.out.println("\n[EDIT OPTIONS]:");
            System.out.println("1. Add Device to Scene");
            System.out.println("2. Remove Device from Scene");
            System.out.println("3. Change Device Action (ON ↔ OFF)");
            System.out.println("4. View Current Scene Configuration");
            System.out.println();
            System.out.println("0. Return to Smart Scenes Menu");

            System.out.print("Choose editing option (0-4): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

                switch (choice) {
                    case 1:
                        addDeviceToScene(sceneName);
                        break;
                    case 2:
                        removeDeviceFromScene(sceneName);
                        break;
                    case 3:
                        changeDeviceActionInScene(sceneName);
                        break;
                    case 4:
                        smartHomeService.showEditableSceneDetails(sceneName);
                        break;
                    default:
                        System.out.println("Invalid option! Please choose between 0-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-4.");
            }
        }
    }

    private static void addDeviceToScene(String sceneName) {
        System.out.println("\n=== Add Device to Scene: " + sceneName.toUpperCase() + " ===");

        // First show user's available devices
        List<Gadget> userDevices = smartHomeService.viewGadgets();
        if (userDevices == null || userDevices.isEmpty()) {
            System.out.println("[ERROR] No devices found! Please add some devices first.");
            return;
        }

        System.out.println("\nSelect device to add to scene:");
        System.out.print("Enter device number: ");

        try {
            int deviceChoice = Integer.parseInt(scanner.nextLine().trim());

            if (deviceChoice >= 1 && deviceChoice <= userDevices.size()) {
                Gadget selectedDevice = userDevices.get(deviceChoice - 1);
                String deviceType = selectedDevice.getType();
                String roomName = selectedDevice.getRoomName();

                System.out.println("\nSelected: " + deviceType + " " + selectedDevice.getModel() + " in " + roomName);
                System.out.println("Choose action for this device in the scene:");
                System.out.println("1. Turn ON");
                System.out.println("2. Turn OFF");
                System.out.print("Choose action (1-2): ");

                int actionChoice = Integer.parseInt(scanner.nextLine().trim());
                String action = (actionChoice == 1) ? "ON" : "OFF";

                if (actionChoice == 1 || actionChoice == 2) {
                    boolean success = smartHomeService.addDeviceToScene(sceneName, deviceType, roomName, action);
                    if (success) {
                        System.out.println("\n[SUCCESS] Device added to scene successfully!");
                        System.out.println("[INFO] " + deviceType + " in " + roomName + " will be turned " + action + " when scene executes.");
                    } else {
                        System.out.println("\n[ERROR] Failed to add device to scene. Device may already exist in this scene.");
                    }
                } else {
                    System.out.println("[ERROR] Invalid action choice!");
                }
            } else {
                System.out.println("[ERROR] Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to add device to scene.");
        }
    }

    private static void removeDeviceFromScene(String sceneName) {
        System.out.println("\n=== Remove Device from Scene: " + sceneName.toUpperCase() + " ===");

        // Get current scene actions
        var sceneActions = smartHomeService.getSceneActions(sceneName);
        if (sceneActions == null || sceneActions.isEmpty()) {
            System.out.println("[ERROR] No devices found in this scene.");
            return;
        }

        System.out.println("Devices currently in scene:");
        for (int i = 0; i < sceneActions.size(); i++) {
            var action = sceneActions.get(i);
            System.out.println((i + 1) + ". " + action.getDeviceType() + " in " + action.getRoomName() + " → " + action.getAction());
        }

        System.out.print("Choose device to remove (1-" + sceneActions.size() + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (choice >= 1 && choice <= sceneActions.size()) {
                var selectedAction = sceneActions.get(choice - 1);
                String deviceType = selectedAction.getDeviceType();
                String roomName = selectedAction.getRoomName();

                System.out.println("\n[!] CONFIRMATION [!]");
                System.out.println("Remove " + deviceType + " in " + roomName + " from scene " + sceneName.toUpperCase() + "?");
                System.out.println("1. Yes, Remove Device");
                System.out.println("2. No, Cancel");
                System.out.print("Choose (1-2): ");

                int confirmation = Integer.parseInt(scanner.nextLine().trim());
                if (confirmation == 1) {
                    boolean success = smartHomeService.removeDeviceFromScene(sceneName, deviceType, roomName);
                    if (success) {
                        System.out.println("\n[SUCCESS] Device removed from scene successfully!");
                    }
                } else {
                    System.out.println("Remove operation cancelled.");
                }
            } else {
                System.out.println("[ERROR] Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to remove device from scene.");
        }
    }

    private static void changeDeviceActionInScene(String sceneName) {
        System.out.println("\n=== Change Device Action in Scene: " + sceneName.toUpperCase() + " ===");

        // Get current scene actions
        var sceneActions = smartHomeService.getSceneActions(sceneName);
        if (sceneActions == null || sceneActions.isEmpty()) {
            System.out.println("[ERROR] No devices found in this scene.");
            return;
        }

        System.out.println("Devices currently in scene:");
        for (int i = 0; i < sceneActions.size(); i++) {
            var action = sceneActions.get(i);
            System.out.println((i + 1) + ". " + action.getDeviceType() + " in " + action.getRoomName() + " → " + action.getAction());
        }

        System.out.print("Choose device to modify action (1-" + sceneActions.size() + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (choice >= 1 && choice <= sceneActions.size()) {
                var selectedAction = sceneActions.get(choice - 1);
                String deviceType = selectedAction.getDeviceType();
                String roomName = selectedAction.getRoomName();
                String currentAction = selectedAction.getAction();
                String newAction = currentAction.equals("ON") ? "OFF" : "ON";

                System.out.println("\nDevice: " + deviceType + " in " + roomName);
                System.out.println("Current Action: " + currentAction);
                System.out.println("New Action: " + newAction);
                System.out.println();
                System.out.println("1. Confirm Change");
                System.out.println("2. Cancel");
                System.out.print("Choose (1-2): ");

                int confirmation = Integer.parseInt(scanner.nextLine().trim());
                if (confirmation == 1) {
                    boolean success = smartHomeService.changeDeviceActionInScene(sceneName, deviceType, roomName, newAction);
                    if (success) {
                        System.out.println("\n[SUCCESS] Device action changed successfully!");
                        System.out.println("[INFO] " + deviceType + " in " + roomName + " will now be turned " + newAction + " when scene executes.");
                    }
                } else {
                    System.out.println("Action change cancelled.");
                }
            } else {
                System.out.println("[ERROR] Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to change device action.");
        }
    }

    private static void resetSceneToOriginal() {
        try {
            System.out.println("\n=== Reset Scene to Original ===");
            List<String> sceneNames = smartHomeService.getSmartScenesService().getAvailableSceneNames();

            System.out.println("Available scenes:");
            for (int i = 0; i < sceneNames.size(); i++) {
                System.out.println((i + 1) + ". " + sceneNames.get(i));
            }

            System.out.print("Choose scene to reset (1-" + sceneNames.size() + "): ");

            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= sceneNames.size()) {
                String sceneName = sceneNames.get(choice - 1);

                System.out.println("\n[!] RESET CONFIRMATION [!]");
                System.out.println("This will reset scene '" + sceneName.toUpperCase() + "' to its original configuration.");
                System.out.println("All your custom modifications will be lost.");
                System.out.println();
                System.out.println("Are you sure you want to continue?");
                System.out.println("1. Yes, Reset to Original");
                System.out.println("2. No, Cancel Reset");
                System.out.print("Choose (1-2): ");

                int confirmation = Integer.parseInt(scanner.nextLine().trim());
                if (confirmation == 1) {
                    boolean success = smartHomeService.resetSceneToOriginal(sceneName);
                    if (success) {
                        System.out.println("\n[SUCCESS] Scene reset to original configuration successfully!");
                        System.out.println("[INFO] All custom modifications have been removed.");
                    } else {
                        System.out.println("\n[ERROR] Cannot reset this scene. It may be a custom scene or already in original state.");
                    }
                } else {
                    System.out.println("Reset operation cancelled.");
                }
            } else {
                System.out.println("Invalid choice! Please choose between 1-" + sceneNames.size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to reset scene.");
        }
    }
    
    private static void showDeviceHealthMenu() {
        while (true) {
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Device Health Monitoring ===");
            System.out.println("[HEALTH DASHBOARD]:");
            System.out.println("1. System Health Report");
            System.out.println("2. Device Maintenance Schedule");
            System.out.println("3. Health Summary");
            System.out.println();
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-3): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

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
                    default:
                        System.out.println("Invalid option! Please choose between 0-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-3.");
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
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Usage Analytics & Insights ===");
            System.out.println("[ANALYTICS DASHBOARD]:");
            System.out.println("1. Energy Consumption Analysis");
            System.out.println("2. Device Usage Patterns");
            System.out.println("3. Cost Analysis & Projections");
            System.out.println("4. Efficiency Recommendations");
            System.out.println("5. Peak Usage Times");
            System.out.println();
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-5): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

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
                    default:
                        System.out.println("Invalid option! Please choose between 0-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-5.");
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

    private static void showSettingsMenu() {
        while (true) {
            if (checkReturnToMainMenu()) return;

            System.out.println("\n=== Settings ===");
            System.out.println("[ACCOUNT SETTINGS]:");
            System.out.println("1. Update User Profile");
            System.out.println("2. Change Password");
            System.out.println();
            System.out.println("[SYSTEM SETTINGS]:");
            System.out.println("3. View Account Information");
            System.out.println("4. Privacy & Security Info");
            System.out.println();
            System.out.println("0. Return to Main Menu");

            System.out.print("Choose an option (0-4): ");

            try {
                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a valid option number.");
                    continue;
                }

                    if ("0".equals(inputLine)) {
                    return;
                }

                int choice = Integer.parseInt(inputLine);

                switch (choice) {
                    case 1:
                        updateUserProfile();
                        break;
                    case 2:
                        changePassword();
                        break;
                    case 3:
                        viewAccountInformation();
                        break;
                    case 4:
                        showPrivacySecurityInfo();
                        break;
                    default:
                        System.out.println("Invalid option! Please choose between 0-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0-4.");
            }
        }
    }

    private static void updateUserProfile() {
        System.out.println("\n=== Update User Profile ===");
        System.out.println("[Navigation] Enter '0' to return to Main Menu");
        System.out.println("[Security] Password confirmation required to update profile");

        try {
            System.out.print("Enter your current password for confirmation: ");
            String currentPassword = getPasswordInputWithNavigation("Current Password");
            if (currentPassword == null || checkReturnToMainMenu()) return;

            if (!smartHomeService.verifyCurrentPassword(currentPassword)) {
                System.out.println("\n[ERROR] Invalid password! Profile update cancelled for security.");
                return;
            }

            System.out.println("\n[SUCCESS] Password confirmed! You can now update your profile.");

            smartHomeService.showCurrentUserInfo();

            while (true) {
                System.out.println("\n=== What would you like to update? ===");
                System.out.println("1. Update Full Name");
                System.out.println("2. Update Email Address");
                System.out.println("3. Update Password");
                System.out.println();
                System.out.println("0. Return to Settings Menu");

                System.out.print("Choose an option (0-3): ");

                String inputLine = getValidatedInputWithNavigation("");
                if (inputLine == null || checkReturnToMainMenu()) return;

                if ("0".equals(inputLine)) {
                    return;
                }

                try {
                    int choice = Integer.parseInt(inputLine);

                    switch (choice) {
                        case 1:
                            updateFullName();
                            break;
                        case 2:
                            updateEmailAddress();
                            break;
                        case 3:
                            updatePassword();
                            break;
                        default:
                            System.out.println("Invalid option! Please choose between 0-3.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number between 0-3.");
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Failed to update user profile. Please try again.");
        }
    }

    private static void updateFullName() {
        System.out.println("\n=== Update Full Name ===");
        try {
            System.out.print("Enter new full name: ");
            String newName = getValidatedInputWithNavigation("Full Name");
            if (newName == null || checkReturnToMainMenu()) return;

            boolean success = smartHomeService.updateUserFullName(newName);
            if (success) {
                System.out.println("\n[SUCCESS] Full name updated successfully!");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to update full name. Please try again.");
        }
    }

    private static void updateEmailAddress() {
        System.out.println("\n=== Update Email Address ===");
        try {
            System.out.print("Enter new email address: ");
            String newEmail = getValidatedInputWithNavigation("Email");
            if (newEmail == null || checkReturnToMainMenu()) return;

            if (!smartHomeService.checkEmailAvailability(newEmail)) {
                System.out.println("\n[ERROR] This email is already registered!");
                System.out.println("[INFO] Please use a different email address.");
                return;
            }

            System.out.println("[INFO] Email is available! Updating your email address...");

            boolean success = smartHomeService.updateUserEmail(newEmail);
            if (success) {
                System.out.println("\n[SUCCESS] Email address updated successfully!");
                System.out.println("[INFO] Please use your new email address for future logins.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to update email address. Please try again.");
        }
    }

    private static void updatePassword() {
        System.out.println("\n=== Update Password ===");
        try {
            System.out.println("\n[Password Requirements]:");
            System.out.println("- 8-128 characters long");
            System.out.println("- At least one uppercase letter (A-Z)");
            System.out.println("- At least one lowercase letter (a-z)");
            System.out.println("- At least one number (0-9)");
            System.out.println("- At least one special character (!@#$%^&*)");
            System.out.println("- Cannot be a common password");
            System.out.println("- Cannot have more than 2 repeating characters");

            System.out.print("\nEnter new password: ");
            String newPassword = getPasswordInputWithNavigation("New Password");
            if (newPassword == null || checkReturnToMainMenu()) return;

            System.out.print("Confirm new password: ");
            String confirmPassword = getPasswordInputWithNavigation("Confirm Password");
            if (confirmPassword == null || checkReturnToMainMenu()) return;

            boolean success = smartHomeService.updateUserPassword(newPassword, confirmPassword);
            if (success) {
                System.out.println("\n[SUCCESS] Password updated successfully!");
                System.out.println("[INFO] Please use your new password for future logins.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to update password. Please try again.");
        }
    }

    private static void changePassword() {
        System.out.println("\n=== Change Password ===");
        System.out.println("[Navigation] Enter '0' to return to Main Menu");

        try {
            System.out.print("Enter your current password: ");
            String currentPassword = getPasswordInputWithNavigation("Current Password");
            if (currentPassword == null || checkReturnToMainMenu()) return;

            if (!smartHomeService.verifyCurrentPassword(currentPassword)) {
                System.out.println("\n[ERROR] Invalid current password! Password change cancelled.");
                return;
            }

            updatePassword();
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to change password. Please try again.");
        }
    }

    private static void viewAccountInformation() {
        System.out.println("\n=== Account Information ===");
        smartHomeService.showDetailedUserInfo();
    }

    private static void showPrivacySecurityInfo() {
        System.out.println("\n=== Privacy & Security Information ===");
        System.out.println("[DATA SECURITY]:");
        System.out.println("- Your passwords are securely encrypted using bcrypt hashing");
        System.out.println("- Personal data is stored locally in encrypted format");
        System.out.println("- No data is shared with third parties");
        System.out.println();
        System.out.println("[ACCOUNT SECURITY]:");
        System.out.println("- Failed login attempts are monitored and logged");
        System.out.println("- Account lockout protection after multiple failed attempts");
        System.out.println("- Password strength requirements enforced");
        System.out.println("- Password confirmation required for profile changes");
        System.out.println();
        System.out.println("[DATA STORAGE]:");
        System.out.println("- Device data and usage statistics stored locally");
        System.out.println("- No cloud data transmission unless explicitly configured");
        System.out.println("- You have full control over your data");
        System.out.println();
        System.out.println("[RECOMMENDATIONS]:");
        System.out.println("- Use a strong, unique password for your account");
        System.out.println("- Regularly update your password");
        System.out.println("- Keep your system software updated");
        System.out.println("- Log out when using shared computers");
    }

    private static void displayNavigationHelp() {

    }

    private static void handleCtrlCNavigation() {
        System.out.println("\n=== Quick Navigation ===");
        System.out.println("To return to Main Menu:");
        System.out.println("Enter '0' and press Enter");
        System.out.print("Choice: ");

        try {
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                returnToMainMenu = true;
                System.out.println("Returning to Main Menu...");
            }
        } catch (Exception e) {
        }
    }

    private static boolean checkReturnToMainMenu() {
        if (returnToMainMenu) {
            returnToMainMenu = false;
            return true;
        }
        return false;
    }

    private static String getValidatedInputWithNavigation(String fieldName) {
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

            // Check for navigation command
            if ("0".equals(input)) {
                System.out.println("Returning to Main Menu...");
                returnToMainMenu = true;
                return null;
            }

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

    /**
     * Secure password input with masking (shows * instead of characters)
     * Uses System.console() when available, falls back to Scanner if not
     */
    private static String getPasswordInput(String fieldName) {
        try {
            java.io.Console console = System.console();

            if (console != null) {
                // Console available - use masked input
                char[] passwordChars = console.readPassword();
                if (passwordChars == null) {
                    if (!fieldName.isEmpty()) {
                        System.out.println(fieldName + " cannot be null. Please try again.");
                    } else {
                        System.out.println("Password cannot be null. Please try again.");
                    }
                    return null;
                }

                String password = new String(passwordChars);
                // Clear the password from memory for security
                java.util.Arrays.fill(passwordChars, ' ');

                if (password.trim().isEmpty()) {
                    if (!fieldName.isEmpty()) {
                        System.out.println(fieldName + " cannot be empty. Please try again.");
                    } else {
                        System.out.println("Password cannot be empty. Please try again.");
                    }
                    return null;
                }

                return password.trim();

            } else {
                // Console not available (IDE environment) - use regular input
                System.out.print("[Note: Password will be visible in this environment] ");
                return getValidatedInput(fieldName);
            }

        } catch (Exception e) {
            if (!fieldName.isEmpty()) {
                System.out.println("Error reading " + fieldName + ". Please try again.");
            } else {
                System.out.println("Error reading password. Please try again.");
            }
            return null;
        }
    }

    /**
     * Secure password input with navigation support and masking
     */
    private static String getPasswordInputWithNavigation(String fieldName) {
        try {
            java.io.Console console = System.console();

            if (console != null) {
                // Console available - use masked input
                char[] passwordChars = console.readPassword();
                if (passwordChars == null) {
                    if (!fieldName.isEmpty()) {
                        System.out.println(fieldName + " cannot be null. Please try again.");
                    } else {
                        System.out.println("Password cannot be null. Please try again.");
                    }
                    return null;
                }

                String password = new String(passwordChars);
                // Clear the password from memory for security
                java.util.Arrays.fill(passwordChars, ' ');

                // Check for navigation command
                if ("0".equals(password.trim())) {
                    System.out.println("Returning to Main Menu...");
                    returnToMainMenu = true;
                    return null;
                }

                if (password.trim().isEmpty()) {
                    if (!fieldName.isEmpty()) {
                        System.out.println(fieldName + " cannot be empty. Please try again.");
                    } else {
                        System.out.println("Password cannot be empty. Please try again.");
                    }
                    return null;
                }

                return password.trim();

            } else {
                // Console not available (IDE environment) - use regular input with navigation
                System.out.print("[Note: Password will be visible in this environment] ");
                return getValidatedInputWithNavigation(fieldName);
            }

        } catch (Exception e) {
            if (!fieldName.isEmpty()) {
                System.out.println("Error reading " + fieldName + ". Please try again.");
            } else {
                System.out.println("Error reading password. Please try again.");
            }
            return null;
        }
    }

    private static String getValidatedGadgetInputWithNavigation(String fieldName, String validOptions) {
        System.out.println("Valid " + fieldName + "s: " + validOptions);
        System.out.println("[TIP] Type 'etc' to see all available " + fieldName.toLowerCase() + "s");
        System.out.print("Enter " + fieldName + " (or '0' for Main Menu): ");

        String input = getValidatedInputWithNavigation("");
        if (input != null && "etc".equalsIgnoreCase(input.trim())) {
            return handleEtcInput(fieldName);
        }

        if (input != null) {
            String[] allOptions = getDeviceBrands(fieldName);
            if (allOptions != null) {
                    for (String option : allOptions) {
                    if (option.equalsIgnoreCase(input.trim())) {
                        return input.trim();
                    }
                }

                if (isValidCustomEntry(input, fieldName)) {
                    System.out.println("[INFO] Using custom " + fieldName.toLowerCase() + ": " + input);
                    return input.trim();
                } else {
                    System.out.println("[ERROR] Invalid " + fieldName.toLowerCase() + ": '" + input + "'");
                    System.out.println("[TIP] Must be 2-50 characters, contain letters, and use only valid characters");
                    return null;
                }
            }
        }

        return input;
    }

    private static String getValidatedGadgetInput(String fieldName, String validOptions) {
        System.out.println("Valid " + fieldName + "s: " + validOptions);
        System.out.println("[TIP] Type 'etc' to see all available " + fieldName.toLowerCase() + "s");
        System.out.print("Enter " + fieldName + ": ");

        String input = getValidatedInput("");
        if (input != null && "etc".equalsIgnoreCase(input.trim())) {
            return handleEtcInput(fieldName);
        }

        if (input != null) {
            String[] allOptions = getDeviceBrands(fieldName);
            if (allOptions != null) {
                    for (String option : allOptions) {
                    if (option.equalsIgnoreCase(input.trim())) {
                        return input.trim();
                    }
                }

                if (isValidCustomEntry(input, fieldName)) {
                    System.out.println("[INFO] Using custom " + fieldName.toLowerCase() + ": " + input);
                    return input.trim();
                } else {
                    System.out.println("[ERROR] Invalid " + fieldName.toLowerCase() + ": '" + input + "'");
                    System.out.println("[TIP] Must be 2-50 characters, contain letters, and use only valid characters");
                    return null;
                }
            }
        }

        return input;
    }

    private static String handleEtcInput(String fieldName) {
        String[] brands = getDeviceBrands(fieldName);
        if (brands != null) {
            displayBrandList(fieldName, brands);
            System.out.print("Now enter your choice from the list above: ");
            return getSmartValidatedInput(fieldName, brands);
        }
        return null;
    }

    private static String getSmartValidatedInput(String fieldName, String[] validOptions) {
        while (true) {
            String input = getValidatedInput("");
            if (input == null) {
                return null;
            }

            for (String option : validOptions) {
                if (option.equalsIgnoreCase(input.trim())) {
                    return input.trim();
                }
            }

            if (isValidCustomEntry(input, fieldName)) {
                System.out.println("[INFO] Using custom " + fieldName.toLowerCase() + ": " + input);
                return input.trim();
            } else {
                System.out.print("Invalid " + fieldName.toLowerCase() + ". Please enter a valid option or try again: ");
            }
        }
    }

    private static boolean isValidCustomEntry(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        input = input.trim();

        if (input.length() < 2 || input.length() > 50) {
            return false;
        }

        if ("Room Name".equalsIgnoreCase(fieldName)) {
            return input.matches("[A-Za-z0-9\\s'\\-&\\.]+") && input.matches(".*[A-Za-z]+.*");
        }

        return input.matches("[A-Za-z0-9\\s'\\-&\\.\\+]+") && input.matches(".*[A-Za-z]+.*");
    }

    private static String[] getDeviceBrands(String fieldName) {
        String normalizedField = fieldName.toLowerCase().replace(" ", "");

        switch (normalizedField) {
            case "tvmodel/brand":
            case "tvmodel":
                return TV_BRANDS;
            case "acmodel":
                return AC_BRANDS;
            case "fanmodel":
                return FAN_BRANDS;
            case "smartspeakermodel":
                return SPEAKER_BRANDS;
            case "airpurifiermodel":
                return AIR_PURIFIER_BRANDS;
            case "smartthermostatmodel":
                return THERMOSTAT_BRANDS;
            case "smartlightmodel":
                return LIGHT_BRANDS;
            case "smartswitchmodel":
                return SWITCH_BRANDS;
            case "securitycameramodel":
                return CAMERA_BRANDS;
            case "smartlockmodel":
                return LOCK_BRANDS;
            case "smartdoorbellmodel":
                return DOORBELL_BRANDS;
            case "refrigeratormodel":
                return REFRIGERATOR_BRANDS;
            case "microwavemodel":
                return MICROWAVE_BRANDS;
            case "washingmachinemodel":
                return WASHING_MACHINE_BRANDS;
            case "geysermodel":
                return GEYSER_BRANDS;
            case "waterpurifiermodel":
                return WATER_PURIFIER_BRANDS;
            case "roboticvacuummodel":
                return VACUUM_BRANDS;
            case "roomname":
                return ROOM_NAMES;
            default:
                return null;
        }
    }

    private static void displayBrandList(String fieldName, String[] brands) {
        System.out.println("\n=== Complete List of " + fieldName + "s ===");

        int columns = 3;
        int rows = (int) Math.ceil((double) brands.length / columns);

        for (int row = 0; row < rows; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < columns; col++) {
                int index = row + col * rows;
                if (index < brands.length) {
                    line.append(String.format("%-25s", brands[index]));
                }
            }
            System.out.println(line.toString().trim());
        }

        System.out.println("\n[INFO] Total " + brands.length + " options available");

        if ("Room Name".equalsIgnoreCase(fieldName)) {
            System.out.println("[NOTE] You can enter any room name, not limited to this list");
        } else {
            System.out.println("[NOTE] You can enter any brand name, not limited to this list");
        }
        System.out.println();
    }

}