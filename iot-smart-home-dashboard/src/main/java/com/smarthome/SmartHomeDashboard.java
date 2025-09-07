package com.smarthome;

import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import com.smarthome.util.DynamoDBConfig;

import java.util.List;
import java.util.Scanner;

public class SmartHomeDashboard {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final SmartHomeService smartHomeService = new SmartHomeService();
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to IoT Smart Home Dashboard ===\n");
        
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
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Customer Register");
            System.out.println("2. Customer Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Control Gadgets");
            System.out.println("5. View Gadgets");
            System.out.println("6. Change Gadget Status");
            
            System.out.println("7. Exit");
            
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                int exitOption = 7;
                
                switch (choice) {
                    case 1:
                        registerCustomer();
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
                        System.out.println("Thank you for using IoT Smart Home Dashboard!");
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-7.");
            }
        }
    }
    
    private static void registerCustomer() {
        System.out.println("\n=== Customer Registration ===");
        
        try {
            System.out.print("Enter your full Name: ");
            String fullName = getValidatedInput("Full Name");
            if (fullName == null) return;
            
            System.out.print("Enter your email: ");
            String email = getValidatedInput("Email");
            if (email == null) return;
            
            System.out.println("\n📋 Password Requirements:");
            System.out.println("• 8-128 characters long");
            System.out.println("• At least one uppercase letter (A-Z)");
            System.out.println("• At least one lowercase letter (a-z)");
            System.out.println("• At least one number (0-9)");
            System.out.println("• At least one special character (!@#$%^&*)");
            System.out.println("• Cannot be a common password");
            System.out.println("• Cannot have more than 2 repeating characters");
            System.out.print("\nChoose Your password: ");
            String password = getValidatedInput("Password");
            if (password == null) return;
            
            System.out.print("Choose Your password again: ");
            String confirmPassword = getValidatedInput("Confirm Password");
            if (confirmPassword == null) return;
            
            boolean success = smartHomeService.registerCustomer(fullName, email, password, confirmPassword);
            
            if (success) {
                System.out.println("\n✅ Registration successful! You can now login with your credentials.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed due to input error. Please try again.");
        }
    }
    
    private static void loginCustomer() {
        System.out.println("\n=== Customer Login ===");
        
        try {
            System.out.print("Email: ");
            String email = getValidatedInput("Email");
            if (email == null) return;
            
            System.out.print("Password: ");
            String password = getValidatedInput("Password");
            if (password == null) return;
            
            smartHomeService.loginCustomer(email, password);
        } catch (Exception e) {
            System.out.println("Login failed due to input error. Please try again.");
        }
    }
    
    private static boolean checkLoginStatus() {
        if (!smartHomeService.isLoggedIn()) {
            System.out.println("Please login first!");
            return false;
        }
        return true;
    }
    
    private static void showGadgetControlMenu() {
        while (true) {
            System.out.println("\n=== Control Gadgets Menu ===");
            System.out.println("Entertainment Devices:");
            System.out.println("1. Control TV");
            System.out.println("2. Control Smart Speaker");
            
            System.out.println("\nClimate Control:");
            System.out.println("3. Control AC");
            System.out.println("4. Control Fan");
            System.out.println("5. Control Air Purifier");
            System.out.println("6. Control Smart Thermostat");
            
            System.out.println("\nLighting & Switches:");
            System.out.println("7. Control Smart Light");
            System.out.println("8. Control Smart Switch");
            
            System.out.println("\nSecurity & Safety:");
            System.out.println("9. Control Security Camera");
            System.out.println("10. Control Smart Door Lock");
            System.out.println("11. Control Smart Doorbell");
            
            System.out.println("\nKitchen & Appliances:");
            System.out.println("12. Control Refrigerator");
            System.out.println("13. Control Microwave");
            System.out.println("14. Control Washing Machine");
            System.out.println("15. Control Water Heater/Geyser");
            System.out.println("16. Control Water Purifier");
            
            System.out.println("\nCleaning:");
            System.out.println("17. Control Robotic Vacuum");
            
            System.out.println("\n18. Exit");
            System.out.print("Choose an option (1-18): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: controlTV(); break;
                    case 2: controlSpeaker(); break;
                    case 3: controlAC(); break;
                    case 4: controlFan(); break;
                    case 5: controlAirPurifier(); break;
                    case 6: controlThermostat(); break;
                    case 7: controlLight(); break;
                    case 8: controlSwitch(); break;
                    case 9: controlCamera(); break;
                    case 10: controlLock(); break;
                    case 11: controlDoorbell(); break;
                    case 12: controlRefrigerator(); break;
                    case 13: controlMicrowave(); break;
                    case 14: controlWashingMachine(); break;
                    case 15: controlGeyser(); break;
                    case 16: controlWaterPurifier(); break;
                    case 17: controlVacuum(); break;
                    case 18: return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-18.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-18.");
            }
        }
    }
    
    private static void controlTV() {
        System.out.println("\n=== Control TV ===");
        
        try {
            String model = getValidatedGadgetInput("TV Model", "Samsung, Sony, LG, TCL, Hisense, Panasonic, Philips, MI, OnePlus, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("TV", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control TV. Please try again.");
        }
    }
    
    private static void controlAC() {
        System.out.println("\n=== Control AC ===");
        
        try {
            String model = getValidatedGadgetInput("AC Model", "LG, Voltas, Blue Star, Samsung, Daikin, Hitachi, Panasonic, Carrier, Godrej, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("AC", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control AC. Please try again.");
        }
    }
    
    private static void controlFan() {
        System.out.println("\n=== Control Fan ===");
        
        try {
            String model = getValidatedGadgetInput("Fan Model", "Atomberg, Crompton, Havells, Bajaj, Usha, Orient, Luminous, Polycab, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("FAN", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Fan. Please try again.");
        }
    }
    
    private static void controlSpeaker() {
        System.out.println("\n=== Control Smart Speaker ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Speaker Model", "Amazon Echo, Google Home, MI, Realme, JBL, Sony, Boat, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("SPEAKER", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Speaker. Please try again.");
        }
    }
    
    private static void controlAirPurifier() {
        System.out.println("\n=== Control Air Purifier ===");
        
        try {
            String model = getValidatedGadgetInput("Air Purifier Model", "MI, Realme, Honeywell, Philips, Kent, Eureka Forbes, Sharp, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("AIR_PURIFIER", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Air Purifier. Please try again.");
        }
    }
    
    private static void controlThermostat() {
        System.out.println("\n=== Control Smart Thermostat ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Thermostat Model", "Honeywell, Johnson Controls, Schneider Electric, Siemens, Nest, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("THERMOSTAT", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Thermostat. Please try again.");
        }
    }
    
    private static void controlLight() {
        System.out.println("\n=== Control Smart Light ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Light Model", "Philips Hue, MI, Syska, Havells, Wipro, Bajaj, Orient, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("LIGHT", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Light. Please try again.");
        }
    }
    
    private static void controlSwitch() {
        System.out.println("\n=== Control Smart Switch ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Switch Model", "Anchor, Havells, Legrand, Schneider Electric, Wipro, Polycab, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("SWITCH", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Switch. Please try again.");
        }
    }
    
    private static void controlCamera() {
        System.out.println("\n=== Control Security Camera ===");
        
        try {
            String model = getValidatedGadgetInput("Security Camera Model", "MI, Realme, TP-Link, D-Link, Hikvision, CP Plus, Godrej, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("CAMERA", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Security Camera. Please try again.");
        }
    }
    
    private static void controlLock() {
        System.out.println("\n=== Control Smart Door Lock ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Lock Model", "Godrej, Yale, Samsung, Philips, MI, Realme, Ultraloq, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("DOOR_LOCK", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Door Lock. Please try again.");
        }
    }
    
    private static void controlDoorbell() {
        System.out.println("\n=== Control Smart Doorbell ===");
        
        try {
            String model = getValidatedGadgetInput("Smart Doorbell Model", "MI, Realme, Godrej, Yale, Ring, Honeywell, CP Plus, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("DOORBELL", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Smart Doorbell. Please try again.");
        }
    }
    
    private static void controlRefrigerator() {
        System.out.println("\n=== Control Refrigerator ===");
        
        try {
            String model = getValidatedGadgetInput("Refrigerator Model", "LG, Samsung, Whirlpool, Godrej, Haier, Panasonic, Bosch, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("REFRIGERATOR", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Refrigerator. Please try again.");
        }
    }
    
    private static void controlMicrowave() {
        System.out.println("\n=== Control Microwave ===");
        
        try {
            String model = getValidatedGadgetInput("Microwave Model", "LG, Samsung, IFB, Panasonic, Whirlpool, Godrej, Bajaj, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("MICROWAVE", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Microwave. Please try again.");
        }
    }
    
    private static void controlWashingMachine() {
        System.out.println("\n=== Control Washing Machine ===");
        
        try {
            String model = getValidatedGadgetInput("Washing Machine Model", "LG, Samsung, Whirlpool, IFB, Bosch, Godrej, Haier, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("WASHING_MACHINE", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Washing Machine. Please try again.");
        }
    }
    
    private static void controlGeyser() {
        System.out.println("\n=== Control Water Heater/Geyser ===");
        
        try {
            String model = getValidatedGadgetInput("Geyser Model", "Bajaj, Havells, Crompton, V-Guard, Racold, AO Smith, Haier, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("GEYSER", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Water Heater/Geyser. Please try again.");
        }
    }
    
    private static void controlWaterPurifier() {
        System.out.println("\n=== Control Water Purifier ===");
        
        try {
            String model = getValidatedGadgetInput("Water Purifier Model", "Kent, Aquaguard, Pureit, LivPure, Blue Star, Havells, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("WATER_PURIFIER", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Water Purifier. Please try again.");
        }
    }
    
    private static void controlVacuum() {
        System.out.println("\n=== Control Robotic Vacuum ===");
        
        try {
            String model = getValidatedGadgetInput("Robotic Vacuum Model", "MI Robot, Realme TechLife, Eureka Forbes, Kent, Black+Decker, etc.");
            if (model == null) return;
            
            String roomName = getValidatedGadgetInput("Room Name", "Living Room, Master Bedroom, Kitchen, Balcony, Study Room, etc.");
            if (roomName == null) return;
            
            smartHomeService.connectToGadget("VACUUM", model, roomName);
        } catch (Exception e) {
            System.out.println("Failed to control Robotic Vacuum. Please try again.");
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
        System.out.println("\n=== Change Gadget Status ===");
        
        List<Gadget> gadgets = smartHomeService.viewGadgets();
        
        if (gadgets != null && !gadgets.isEmpty()) {
            System.out.print("\nEnter gadget type to change status: ");
            System.out.println("Available types: TV, AC, FAN, LIGHT, SWITCH, CAMERA, DOOR_LOCK, GEYSER, DOORBELL, VACUUM, AIR_PURIFIER, SPEAKER, WATER_PURIFIER, THERMOSTAT, WASHING_MACHINE, REFRIGERATOR, MICROWAVE");
            String gadgetType = getValidatedInput("Gadget Type");
            
            if (gadgetType != null) {
                smartHomeService.changeGadgetStatus(gadgetType);
            }
        }
    }
    
    private static String getValidatedInput(String fieldName) {
        try {
            if (!scanner.hasNextLine()) {
                System.out.println("\nInput stream ended. Returning to main menu.");
                return null;
            }
            
            String input = scanner.nextLine();
            if (input == null) {
                System.out.println(fieldName + " cannot be null. Please try again.");
                return null;
            }
            
            input = input.trim();
            if (input.isEmpty()) {
                System.out.println(fieldName + " cannot be empty. Please try again.");
                return null;
            }
            
            return input;
            
        } catch (Exception e) {
            System.out.println("Error reading input for " + fieldName + ". Please try again.");
            return null;
        }
    }
    
    private static String getValidatedGadgetInput(String fieldName, String validOptions) {
        String input = getValidatedInput(fieldName);
        if (input == null) return null;
        
        System.out.println("Valid options: " + validOptions);
        return input;
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
            
            System.out.println("\n📋 New Password Requirements:");
            System.out.println("• 8-128 characters long");
            System.out.println("• At least one uppercase letter (A-Z)");
            System.out.println("• At least one lowercase letter (a-z)");
            System.out.println("• At least one number (0-9)");
            System.out.println("• At least one special character (!@#$%^&*)");
            System.out.println("• Cannot be a common password");
            System.out.println("• Cannot have more than 2 repeating characters");
            System.out.print("\nEnter your new password: ");
            String newPassword = getValidatedInput("New Password");
            if (newPassword == null) return;
            
            System.out.print("Confirm your new password: ");
            String confirmPassword = getValidatedInput("Confirm Password");
            if (confirmPassword == null) return;
            
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("❌ Passwords do not match!");
                return;
            }
            
            boolean success = smartHomeService.resetPassword(email, newPassword);
            if (success) {
                System.out.println("\n🎉 You can now login with your new password!");
            }
            
        } catch (Exception e) {
            System.out.println("Password reset failed due to an error. Please try again.");
        }
    }
    
}