package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.service.SmartHomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Core functionality tests for SmartHomeService
 * Tests: Registration, Login, Basic Device Management, User Profile
 */
public class SmartHomeServiceCoreTest {

    private SmartHomeService smartHomeService;
    private static final String TEST_EMAIL = "test@smarthome.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String WEAK_PASSWORD = "123";

    @BeforeEach
    @DisplayName("Setup core test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        System.out.println("🔧 Setting up core test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup core tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up core test environment");
    }

    // ==================== REGISTRATION TESTS ====================

    @Test
    @DisplayName("Test 1: Valid Customer Registration")
    void testValidCustomerRegistration() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertTrue(result, "Valid customer registration should succeed");
        System.out.println("✅ Valid customer registration test passed");
    }

    @Test
    @DisplayName("Test 2: Registration with Mismatched Passwords")
    void testRegistrationMismatchedPasswords() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, "DifferentPass123!");
        assertFalse(result, "Registration with mismatched passwords should fail");
        System.out.println("✅ Mismatched passwords test passed");
    }

    @Test
    @DisplayName("Test 3: Registration with Invalid Email")
    void testRegistrationInvalidEmail() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, INVALID_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(result, "Registration with invalid email should fail");
        System.out.println("✅ Invalid email test passed");
    }

    @Test
    @DisplayName("Test 4: Registration with Weak Password")
    void testRegistrationWeakPassword() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, WEAK_PASSWORD, WEAK_PASSWORD);
        assertFalse(result, "Registration with weak password should fail");
        System.out.println("✅ Weak password test passed");
    }

    @Test
    @DisplayName("Test 5: Registration with Invalid Name")
    void testRegistrationInvalidName() {
        boolean result = smartHomeService.registerCustomer("X", TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(result, "Registration with invalid name should fail");
        System.out.println("✅ Invalid name test passed");
    }

    @Test
    @DisplayName("Test 6: Duplicate Email Registration")
    void testDuplicateEmailRegistration() {
        // First registration should succeed
        boolean firstRegistration = smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertTrue(firstRegistration, "First registration should succeed");

        // Second registration with same email should fail
        boolean secondRegistration = smartHomeService.registerCustomer("Another User", TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(secondRegistration, "Duplicate email registration should fail");
        System.out.println("✅ Duplicate email test passed");
    }

    // ==================== LOGIN TESTS ====================

    @Test
    @DisplayName("Test 7: Valid Customer Login")
    void testValidCustomerLogin() {
        // First register a customer
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        // Then attempt login
        boolean loginResult = smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(loginResult, "Valid customer login should succeed");
        assertTrue(smartHomeService.isLoggedIn(), "User should be logged in after successful login");

        Customer currentUser = smartHomeService.getCurrentUser();
        assertNotNull(currentUser, "Current user should not be null after login");
        assertEquals(TEST_EMAIL, currentUser.getEmail(), "Current user email should match");
        System.out.println("✅ Valid login test passed");
    }

    @Test
    @DisplayName("Test 8: Login with Wrong Password")
    void testLoginWrongPassword() {
        // First register a customer
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        // Attempt login with wrong password
        boolean loginResult = smartHomeService.loginCustomer(TEST_EMAIL, "WrongPassword123!");
        assertFalse(loginResult, "Login with wrong password should fail");
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in after failed login");
        System.out.println("✅ Wrong password test passed");
    }

    @Test
    @DisplayName("Test 9: Login with Non-existent Email")
    void testLoginNonExistentEmail() {
        boolean loginResult = smartHomeService.loginCustomer("nonexistent@email.com", TEST_PASSWORD);
        assertFalse(loginResult, "Login with non-existent email should fail");
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in");
        System.out.println("✅ Non-existent email test passed");
    }

    @Test
    @DisplayName("Test 10: Logout Functionality")
    void testLogoutFunctionality() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(smartHomeService.isLoggedIn(), "User should be logged in initially");

        // Logout
        smartHomeService.logout();
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in after logout");
        assertNull(smartHomeService.getCurrentUser(), "Current user should be null after logout");
        System.out.println("✅ Logout test passed");
    }

    // ==================== DEVICE CONNECTION TESTS ====================

    @Test
    @DisplayName("Test 11: Connect Valid Device")
    void testConnectValidDevice() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Connect a device
        boolean result = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertTrue(result, "Connecting valid device should succeed");

        // Verify device was added
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertNotNull(devices, "Device list should not be null");
        assertEquals(1, devices.size(), "Should have one device connected");

        Gadget device = devices.get(0);
        assertEquals("TV", device.getType(), "Device type should match");
        assertEquals("Samsung", device.getModel(), "Device model should match");
        assertEquals("Living Room", device.getRoomName(), "Room name should match");
        System.out.println("✅ Connect valid device test passed");
    }

    @Test
    @DisplayName("Test 12: Connect Device Without Login")
    void testConnectDeviceWithoutLogin() {
        boolean result = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertFalse(result, "Connecting device without login should fail");
        System.out.println("✅ Connect without login test passed");
    }

    @Test
    @DisplayName("Test 13: Connect Duplicate Device in Same Room")
    void testConnectDuplicateDevice() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Connect first device
        boolean firstResult = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertTrue(firstResult, "First device connection should succeed");

        // Try to connect another TV in same room
        boolean secondResult = smartHomeService.connectToGadget("TV", "LG", "Living Room");
        assertFalse(secondResult, "Duplicate device type in same room should fail");

        // Should still have only one device
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(1, devices.size(), "Should still have only one device");
        System.out.println("✅ Duplicate device test passed");
    }

    @Test
    @DisplayName("Test 14: Connect Multiple Different Devices")
    void testConnectMultipleDifferentDevices() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Connect multiple different devices
        assertTrue(smartHomeService.connectToGadget("TV", "Samsung", "Living Room"));
        assertTrue(smartHomeService.connectToGadget("AC", "LG", "Living Room"));
        assertTrue(smartHomeService.connectToGadget("TV", "Sony", "Bedroom"));
        assertTrue(smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen"));

        // Verify all devices were added
        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(4, devices.size(), "Should have four different devices");
        System.out.println("✅ Multiple devices test passed");
    }

    @Test
    @DisplayName("Test 15: Connect Invalid Device")
    void testConnectInvalidDevice() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Try to connect device with invalid model
        boolean result = smartHomeService.connectToGadget("TV", "InvalidBrand", "Living Room");
        assertFalse(result, "Connecting device with invalid model should fail");

        // Try to connect device with invalid room
        boolean result2 = smartHomeService.connectToGadget("TV", "Samsung", "Invalid Room");
        assertFalse(result2, "Connecting device with invalid room should fail");
        System.out.println("✅ Invalid device test passed");
    }

    // ==================== DEVICE STATUS TESTS ====================

    @Test
    @DisplayName("Test 16: Change Device Status")
    void testChangeDeviceStatus() {
        // Register, login and connect device
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Get initial status
        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget device = devices.get(0);
        String initialStatus = device.getStatus();

        // Change status
        boolean result = smartHomeService.changeGadgetStatus("TV");
        assertTrue(result, "Changing device status should succeed");

        // Verify status changed
        devices = smartHomeService.viewGadgets();
        device = devices.get(0);
        String newStatus = device.getStatus();
        assertNotEquals(initialStatus, newStatus, "Device status should have changed");
        System.out.println("✅ Change device status test passed");
    }

    @Test
    @DisplayName("Test 17: Change Specific Device Status")
    void testChangeSpecificDeviceStatus() {
        // Register, login and connect multiple devices
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("TV", "LG", "Bedroom");

        // Change specific device status
        boolean result = smartHomeService.changeSpecificGadgetStatus("TV", "Bedroom");
        assertTrue(result, "Changing specific device status should succeed");
        System.out.println("✅ Change specific device status test passed");
    }

    // ==================== USER PROFILE TESTS ====================

    @Test
    @DisplayName("Test 18: Show Current User Info")
    void testShowCurrentUserInfo() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // This should not throw an exception
        assertDoesNotThrow(() -> smartHomeService.showCurrentUserInfo(),
                          "Showing current user info should not throw exception");
        assertDoesNotThrow(() -> smartHomeService.showDetailedUserInfo(),
                          "Showing detailed user info should not throw exception");
        System.out.println("✅ Show user info test passed");
    }

    @Test
    @DisplayName("Test 19: Update User Full Name")
    void testUpdateUserFullName() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Update name
        String newName = "Updated Test User";
        boolean result = smartHomeService.updateUserFullName(newName);
        assertTrue(result, "Updating user full name should succeed");

        // Verify name was updated
        Customer currentUser = smartHomeService.getCurrentUser();
        assertEquals(newName, currentUser.getFullName(), "User name should be updated");
        System.out.println("✅ Update user name test passed");
    }

    @Test
    @DisplayName("Test 20: Update User Email")
    void testUpdateUserEmail() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Update email
        String newEmail = "updated@smarthome.com";
        boolean result = smartHomeService.updateUserEmail(newEmail);
        assertTrue(result, "Updating user email should succeed");

        // Verify email was updated
        Customer currentUser = smartHomeService.getCurrentUser();
        assertEquals(newEmail, currentUser.getEmail(), "User email should be updated");
        System.out.println("✅ Update user email test passed");
    }

    @Test
    @DisplayName("Test 21: Update User Password")
    void testUpdateUserPassword() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Update password
        String newPassword = "NewTestPass456!@#";
        boolean result = smartHomeService.updateUserPassword(newPassword, newPassword);
        assertTrue(result, "Updating user password should succeed");
        System.out.println("✅ Update user password test passed");
    }

    @Test
    @DisplayName("Test 22: Verify Current Password")
    void testVerifyCurrentPassword() {
        // Register and login
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(TEST_EMAIL, TEST_PASSWORD);

        // Verify correct password
        boolean correctResult = smartHomeService.verifyCurrentPassword(TEST_PASSWORD);
        assertTrue(correctResult, "Verifying correct password should succeed");

        // Verify wrong password
        boolean wrongResult = smartHomeService.verifyCurrentPassword("WrongPassword123!");
        assertFalse(wrongResult, "Verifying wrong password should fail");
        System.out.println("✅ Verify current password test passed");
    }

    // ==================== SERVICE GETTER TESTS ====================

    @Test
    @DisplayName("Test 23: Service Getters")
    void testServiceGetters() {
        assertNotNull(smartHomeService.getGadgetService(), "GadgetService should not be null");
        assertNotNull(smartHomeService.getTimerService(), "TimerService should not be null");
        assertNotNull(smartHomeService.getCalendarService(), "CalendarService should not be null");
        assertNotNull(smartHomeService.getWeatherService(), "WeatherService should not be null");
        assertNotNull(smartHomeService.getEnergyService(), "EnergyService should not be null");
        assertNotNull(smartHomeService.getSmartScenesService(), "SmartScenesService should not be null");
        assertNotNull(smartHomeService.getDeviceHealthService(), "DeviceHealthService should not be null");
        System.out.println("✅ Service getters test passed");
    }

    // ==================== PASSWORD RESET TESTS ====================

    @Test
    @DisplayName("Test 24: Password Reset Flow")
    void testPasswordResetFlow() {
        // Register a customer
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        // Initiate password reset
        boolean initiateResult = smartHomeService.initiatePasswordReset(TEST_EMAIL);
        assertTrue(initiateResult, "Password reset initiation should succeed");

        // Reset password
        String newPassword = "NewResetPass789!@#";
        boolean resetResult = smartHomeService.resetPassword(TEST_EMAIL, newPassword);
        assertTrue(resetResult, "Password reset should succeed");

        // Verify can login with new password
        boolean loginResult = smartHomeService.loginCustomer(TEST_EMAIL, newPassword);
        assertTrue(loginResult, "Login with new password should succeed");
        System.out.println("✅ Password reset flow test passed");
    }

    @Test
    @DisplayName("Test 25: Email Availability Check")
    void testEmailAvailabilityCheck() {
        // Check availability of new email
        boolean available = smartHomeService.checkEmailAvailability("available@test.com");
        assertTrue(available, "New email should be available");

        // Register with an email
        smartHomeService.registerCustomer(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        // Check availability of registered email
        boolean notAvailable = smartHomeService.checkEmailAvailability(TEST_EMAIL);
        assertFalse(notAvailable, "Registered email should not be available");
        System.out.println("✅ Email availability check test passed");
    }
}