package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.util.DynamoDBConfig;
import org.mindrot.jbcrypt.BCrypt;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService {
    
    private final DynamoDbTable<Customer> customerTable;
    private final boolean isDemoMode;
    private final Map<String, Customer> demoCustomers;
    
    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
        "password", "123456", "password123", "admin", "qwerty", "abc123", 
        "123456789", "welcome", "monkey", "1234567890", "dragon", "letmein",
        "india123", "mumbai", "delhi", "bangalore", "chennai", "kolkata",
        "password1", "admin123", "root", "toor", "pass", "test", "guest",
        "user", "demo", "sample", "temp", "change", "changeme", "default",
        "india", "bharat", "hindustan", "cricket", "bollywood", "iloveyou"
    );
    
    
    public CustomerService() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBConfig.getEnhancedClient();
        
        if (enhancedClient != null) {
            this.customerTable = enhancedClient.table("customers", TableSchema.fromBean(Customer.class));
            this.isDemoMode = false;
            this.demoCustomers = null;
            createTableIfNotExists();
        } else {
            System.out.println("🎮 Running in DEMO MODE - data won't persist between sessions");
            this.customerTable = null;
            this.isDemoMode = true;
            this.demoCustomers = new HashMap<>();
        }
    }
    
    private void createTableIfNotExists() {
        if (!isDemoMode) {
            try {
                customerTable.describeTable();
            } catch (ResourceNotFoundException e) {
                customerTable.createTable();
                System.out.println("Created 'customers' table");
            }
        }
    }
    
    public boolean registerCustomer(String fullName, String email, String password) {
        try {
            email = email.trim().toLowerCase();
            
            if (findCustomerByEmail(email) != null) {
                return false;
            }
            
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Customer customer = new Customer(email, fullName.trim(), hashedPassword);
            
            if (isDemoMode) {
                demoCustomers.put(email, customer);
            } else {
                customerTable.putItem(customer);
            }
            return true;
            
        } catch (Exception e) {
            System.err.println("Error registering customer: " + e.getMessage());
            return false;
        }
    }
    
    public Customer authenticateCustomer(String email, String password) {
        try {
            email = email.trim().toLowerCase();
            Customer customer = findCustomerByEmail(email);
            
            if (customer == null) {
                System.out.println("❌ Invalid email or password!");
                return null;
            }
            
            if (customer.isAccountLocked()) {
                LocalDateTime lockUntil = customer.getAccountLockedUntil();
                System.out.println("🔒 Account is locked until: " + 
                    lockUntil.toString().replace("T", " ") + 
                    ". Please try again later.");
                return null;
            }
            
            if (password != null && BCrypt.checkpw(password, customer.getPassword())) {
                if (customer.getFailedLoginAttempts() > 0) {
                    customer.resetFailedAttempts();
                    updateCustomer(customer);
                    System.out.println("✅ Login successful! Previous failed attempts have been cleared.");
                } else {
                    System.out.println("✅ Login successful! Welcome back, " + customer.getFullName() + "!");
                }
                return customer;
            } else {
                handleFailedLogin(customer);
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("Error authenticating customer: " + e.getMessage());
            return null;
        }
    }
    
    public Customer findCustomerByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return null;
            }
            
            email = email.trim().toLowerCase();
            
            if (isDemoMode) {
                return demoCustomers.get(email);
            } else {
                Key key = Key.builder().partitionValue(email).build();
                return customerTable.getItem(key);
            }
            
        } catch (Exception e) {
            System.err.println("Error finding customer: " + e.getMessage());
            return null;
        }
    }
    
    public boolean updateCustomer(Customer customer) {
        try {
            if (isDemoMode) {
                demoCustomers.put(customer.getEmail().toLowerCase(), customer);
            } else {
                customerTable.updateItem(customer);
            }
            return true;
            
        } catch (Exception e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        email = email.trim().toLowerCase();
        return email.matches("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    }
    
    public boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        if (password.length() < 8) {
            return false;
        }
        
        if (password.length() > 128) {
            return false;
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            return false;
        }
        
        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            return false;
        }
        
        if (password.matches("(.)\\1{2,}")) {
            return false;
        }
        
        return true;
    }
    
    public String getPasswordRequirements() {
        return "Password must be 8-128 characters long and contain:\n" +
               "• At least one uppercase letter (A-Z)\n" +
               "• At least one lowercase letter (a-z)\n" +
               "• At least one number (0-9)\n" +
               "• At least one special character (!@#$%^&*)\n" +
               "• Cannot be a common password\n" +
               "• Cannot have more than 2 repeating characters";
    }
    
    private int calculateLockoutMinutes(int failedAttempts) {
        if (failedAttempts >= 7) {
            return 60;
        } else if (failedAttempts >= 5) {
            return 15;
        } else if (failedAttempts >= 3) {
            return 5;
        }
        return 0; // No lockout for less than 3 attempts
    }
    
    private void handleFailedLogin(Customer customer) {
        customer.incrementFailedAttempts();
        
        int lockoutMinutes = calculateLockoutMinutes(customer.getFailedLoginAttempts());
        if (lockoutMinutes > 0) {
            customer.lockAccount(lockoutMinutes);
            System.out.println("🔒 Account locked for " + lockoutMinutes + " minutes due to " + 
                             customer.getFailedLoginAttempts() + " failed login attempts.");
        } else {
            System.out.println("⚠️  Invalid credentials. Failed attempts: " + 
                             customer.getFailedLoginAttempts() + "/3 before lockout.");
        }
        
        // Update customer record
        updateCustomer(customer);
    }
    
    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        name = name.trim();
        return name.length() >= 2 && name.matches("^[A-Za-z\\s]+$");
    }
    
    
    
    // Password Reset Functionality
    public boolean initiatePasswordReset(String email) {
        try {
            Customer customer = findCustomerByEmail(email);
            if (customer == null) {
                System.out.println("❌ No account found with this email address.");
                return false;
            }
            
            System.out.println("✅ Account found! You can now reset your password.");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error initiating password reset: " + e.getMessage());
            return false;
        }
    }
    
    public boolean resetPassword(String email, String newPassword) {
        try {
            Customer customer = findCustomerByEmail(email);
            if (customer == null) {
                System.out.println("❌ Account not found.");
                return false;
            }
            
            // Validate new password
            if (!isValidPassword(newPassword)) {
                System.out.println("❌ New password does not meet security requirements:");
                System.out.println(getPasswordRequirements());
                return false;
            }
            
            // Hash and update password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            customer.setPassword(hashedPassword);
            
            // Reset failed login attempts on successful password reset
            customer.resetFailedAttempts();
            
            // Update customer record
            boolean updated = updateCustomer(customer);
            if (updated) {
                System.out.println("✅ Password reset successful! You can now login with your new password.");
            } else {
                System.out.println("❌ Failed to update password. Please try again.");
            }
            
            return updated;
            
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }
    
}