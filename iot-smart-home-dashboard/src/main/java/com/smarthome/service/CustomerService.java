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
            System.out.println("[INFO] Running in DEMO MODE - data won't persist between sessions");
            this.customerTable = null;
            this.isDemoMode = true;
            this.demoCustomers = new HashMap<>();
        }
    }
    private void createTableIfNotExists() {
        if (!isDemoMode) {
            try {
                customerTable.describeTable();
                System.out.println("[INFO] DynamoDB table 'customers' already exists");
            } catch (ResourceNotFoundException e) {
                System.out.println("[INFO] Creating DynamoDB table 'customers'...");
                customerTable.createTable();
                System.out.println(" Successfully created 'customers' table in DynamoDB");
            } catch (Exception e) {
                System.err.println(" Error checking/creating table: " + e.getMessage());
                throw e;
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
                System.out.println("[INFO] Customer registered in DEMO mode (data will not persist)");
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
                System.out.println("[ERROR] Email not registered. Please create an account first.");
                addSecurityDelay(1);
                return null;
            }
            if (customer.isAccountLocked()) {
                LocalDateTime lockUntil = customer.getAccountLockedUntil();
                System.out.println("[LOCKED] Account is locked until: " + 
                    lockUntil.toString().replace("T", " ") + 
                    ". Please try again later.");
                return null;
            }
            if (password != null && BCrypt.checkpw(password, customer.getPassword())) {
                if (customer.getFailedLoginAttempts() > 0) {
                    customer.resetFailedAttempts();
                    updateCustomer(customer);
                    System.out.println("[SUCCESS] Login successful! Previous failed attempts have been cleared.");
                } else {
                    System.out.println("[SUCCESS] Login successful! Welcome back, " + customer.getFullName() + "!");
                }
                return customer;
            } else {
                addSecurityDelay(customer.getFailedLoginAttempts() + 1);
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
            Customer customer = null;
            if (isDemoMode) {
                customer = demoCustomers.get(email);
            } else {
                Key key = Key.builder().partitionValue(email).build();
                customer = customerTable.getItem(key);
            }
            if (customer != null && customer.getGadgets() != null) {
                for (com.smarthome.model.Gadget gadget : customer.getGadgets()) {
                    gadget.ensurePowerRating();
                }
            }
            return customer;
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
               "- At least one uppercase letter (A-Z)\n" +
               "- At least one lowercase letter (a-z)\n" +
               "- At least one number (0-9)\n" +
               "- At least one special character (!@#$%^&*)\n" +
               "- Cannot be a common password\n" +
               "- Cannot have more than 2 repeating characters";
    }
    private int calculateLockoutMinutes(int failedAttempts) {
        if (failedAttempts >= 10) {
            return 1440; 
        } else if (failedAttempts >= 7) {
            return 240;  
        } else if (failedAttempts >= 5) {
            return 60;   
        } else if (failedAttempts >= 3) {
            return 15;   
        } else if (failedAttempts >= 2) {
            return 5;    
        }
        return 0;
    }
    private void handleFailedLogin(Customer customer) {
        customer.incrementFailedAttempts();
        int attempts = customer.getFailedLoginAttempts();
        System.out.println("[SECURITY] Failed login attempt #" + attempts + " for account: " +
                         customer.getEmail() + " at " + java.time.LocalDateTime.now().toString().replace("T", " "));
        int lockoutMinutes = calculateLockoutMinutes(attempts);
        if (lockoutMinutes > 0) {
            customer.lockAccount(lockoutMinutes);
            if (lockoutMinutes >= 1440) {
                System.out.println("[SECURITY ALERT] Account PERMANENTLY LOCKED for 24 hours due to " +
                                 attempts + " failed attempts. Contact administrator if legitimate.");
            } else if (lockoutMinutes >= 240) {
                System.out.println("[HIGH SECURITY RISK] Account locked for " + (lockoutMinutes/60) + " hours due to " +
                                 attempts + " repeated failed attempts.");
            } else {
                System.out.println("[LOCKED] Account locked for " + lockoutMinutes + " minutes due to " +
                                 attempts + " failed login attempts.");
            }
        } else {
            System.out.println("[WARNING] Invalid password. This is attempt " + attempts + " of 2 allowed before lockout.");
            System.out.println("[SECURITY] Account will be locked after 2 failed attempts for security.");
        }
        updateCustomer(customer);
    }
    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        name = name.trim();
        return name.length() >= 2 && name.matches("^[A-Za-z\\s]+$");
    }
    public boolean initiatePasswordReset(String email) {
        try {
            Customer customer = findCustomerByEmail(email);
            if (customer == null) {
                System.out.println("[ERROR] No account found with this email address.");
                return false;
            }
            System.out.println("[SUCCESS] Account found! You can now reset your password.");
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
                System.out.println("[ERROR] Account not found.");
                return false;
            }
            if (!isValidPassword(newPassword)) {
                System.out.println("[ERROR] New password does not meet security requirements:");
                System.out.println(getPasswordRequirements());
                return false;
            }
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            customer.setPassword(hashedPassword);
            customer.resetFailedAttempts();
                boolean updated = updateCustomer(customer);
            if (updated) {
                System.out.println("[SUCCESS] Password reset successful! You can now login with your new password.");
            } else {
                System.out.println("[ERROR] Failed to update password. Please try again.");
            }
            return updated;
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }
    public boolean isEmailAvailable(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            if (!isValidEmail(email)) {
                return false;
            }
            Customer existingCustomer = findCustomerByEmail(email);
            return existingCustomer == null;
        } catch (Exception e) {
            System.err.println("Error checking email availability: " + e.getMessage());
            return false;
        }
    }
    public boolean verifyPassword(String email, String password) {
        try {
            Customer customer = findCustomerByEmail(email);
            if (customer == null) {
                return false;
            }
            return password != null && BCrypt.checkpw(password, customer.getPassword());
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    public boolean updateCustomerEmail(String oldEmail, Customer customer) {
        try {
            if (isDemoMode) {
                demoCustomers.remove(oldEmail.toLowerCase());
                demoCustomers.put(customer.getEmail().toLowerCase(), customer);
            } else {
                Key oldKey = Key.builder().partitionValue(oldEmail.toLowerCase()).build();
                customerTable.deleteItem(oldKey);
                customerTable.putItem(customer);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error updating customer email: " + e.getMessage());
            return false;
        }
    }
    public boolean updatePassword(String email, String newPassword) {
        try {
            Customer customer = findCustomerByEmail(email);
            if (customer == null) {
                System.out.println("[ERROR] Customer not found.");
                return false;
            }
            if (!isValidPassword(newPassword)) {
                System.out.println("[ERROR] New password does not meet security requirements:");
                System.out.println(getPasswordRequirements());
                return false;
            }
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            customer.setPassword(hashedPassword);
            customer.resetFailedAttempts();
                boolean updated = updateCustomer(customer);
            if (!updated) {
                System.out.println("[ERROR] Failed to update password in database.");
            }
            return updated;
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
    private void addSecurityDelay(int failedAttempts) {
        try {
            int delaySeconds;
            switch (failedAttempts) {
                case 1:
                    delaySeconds = 1;  
                    break;
                case 2:
                    delaySeconds = 3;  
                    break;
                case 3:
                    delaySeconds = 5;  
                    break;
                case 4:
                    delaySeconds = 10; 
                    break;
                default:
                    delaySeconds = 15; 
                    break;
            }
            if (delaySeconds > 1) {
                System.out.println("[SECURITY] Implementing " + delaySeconds + " second delay due to failed attempts...");
            }
            Thread.sleep(delaySeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[SECURITY] Security delay interrupted");
        }
    }
}
