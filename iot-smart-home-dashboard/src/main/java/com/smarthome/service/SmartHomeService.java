package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.util.SessionManager;

import java.util.List;

public class SmartHomeService {
    
    private final CustomerService customerService;
    private final GadgetService gadgetService;
    private final SessionManager sessionManager;
    
    public SmartHomeService() {
        this.customerService = new CustomerService();
        this.gadgetService = new GadgetService();
        this.sessionManager = SessionManager.getInstance();
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
            System.out.println("❌ Invalid password! Please ensure your password meets all requirements:");
            System.out.println(customerService.getPasswordRequirements());
            return false;
        }
        
        boolean success = customerService.registerCustomer(fullName, email, password);
        if (success) {
            System.out.println("Thank you! Customer registration successful.");
        } else {
            System.out.println("Registration failed! Email might already be registered.");
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
        sessionManager.logout();
        System.out.println("Logged out successfully!");
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
            currentUser.addGadget(gadget);
            
            boolean updated = customerService.updateCustomer(currentUser);
            
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                System.out.println("Successfully connected to " + gadget.getType() + " " + gadget.getModel() + " in " + gadget.getRoomName());
                return true;
            } else {
                System.out.println("Failed to update customer data!");
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
        List<Gadget> gadgets = currentUser.getGadgets();
        
        if (gadgets == null || gadgets.isEmpty()) {
            System.out.println("No gadgets found! Please connect to some gadgets first.");
            return gadgets;
        }
        
        System.out.println("\n=== Your Gadgets ===");
        for (int i = 0; i < gadgets.size(); i++) {
            Gadget gadget = gadgets.get(i);
            System.out.println((i + 1) + ". " + gadget.toString());
        }
        
        return gadgets;
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
            List<Gadget> gadgets = currentUser.getGadgets();
            
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
            
            String previousStatus = targetGadget.getStatus();
            targetGadget.toggleStatus();
            String newStatus = targetGadget.getStatus();
            
            boolean updated = customerService.updateCustomer(currentUser);
            
            if (updated) {
                sessionManager.updateCurrentUser(currentUser);
                
                if ("ON".equals(newStatus)) {
                    System.out.println("Switched on successful");
                } else {
                    System.out.println("Switched off successful");
                }
                
                System.out.println("\n=== All Gadgets Status ===");
                viewGadgets();
                return true;
            } else {
                targetGadget.setStatus(previousStatus);
                System.out.println("Failed to update gadget status!");
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
}