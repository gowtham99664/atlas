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
import java.util.ArrayList;

public class SmartHomeServiceCoreTest {

    private SmartHomeService smartHomeService;
    private String testEmail;
    private static final String TEST_NAME = "Test User";
    private static final String TEST_PASSWORD = "TestPass123!@#";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String WEAK_PASSWORD = "123";

    @BeforeEach
    @DisplayName("Setup core test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();
        testEmail = "test" + System.currentTimeMillis() + "@smarthome.com";
        System.out.println("Setting up core test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup core tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            List<Gadget> devices = smartHomeService.viewGadgets();
            if (devices != null) {
                for (Gadget device : new ArrayList<>(devices)) {
                    smartHomeService.deleteDevice(device.getType(), device.getRoomName());
                }
            }
            smartHomeService.logout();
        }
        System.out.println("Cleaned up core test environment");
    }


    @Test
    @DisplayName("Test 1: Valid Customer Registration")
    void testValidCustomerRegistration() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        assertTrue(result, "Valid customer registration should succeed");
        System.out.println(" Valid customer registration test passed");
    }

    @Test
    @DisplayName("Test 2: Registration with Mismatched Passwords")
    void testRegistrationMismatchedPasswords() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, "DifferentPass123!");
        assertFalse(result, "Registration with mismatched passwords should fail");
        System.out.println(" Mismatched passwords test passed");
    }

    @Test
    @DisplayName("Test 3: Registration with Invalid Email")
    void testRegistrationInvalidEmail() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, INVALID_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(result, "Registration with invalid email should fail");
        System.out.println(" Invalid email test passed");
    }

    @Test
    @DisplayName("Test 4: Registration with Weak Password")
    void testRegistrationWeakPassword() {
        boolean result = smartHomeService.registerCustomer(TEST_NAME, testEmail, WEAK_PASSWORD, WEAK_PASSWORD);
        assertFalse(result, "Registration with weak password should fail");
        System.out.println(" Weak password test passed");
    }

    @Test
    @DisplayName("Test 5: Registration with Invalid Name")
    void testRegistrationInvalidName() {
        boolean result = smartHomeService.registerCustomer("X", testEmail, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(result, "Registration with invalid name should fail");
        System.out.println(" Invalid name test passed");
    }

    @Test
    @DisplayName("Test 6: Duplicate Email Registration")
    void testDuplicateEmailRegistration() {
        boolean firstRegistration = smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        assertTrue(firstRegistration, "First registration should succeed");

        boolean secondRegistration = smartHomeService.registerCustomer("Another User", testEmail, TEST_PASSWORD, TEST_PASSWORD);
        assertFalse(secondRegistration, "Duplicate email registration should fail");
        System.out.println(" Duplicate email test passed");
    }


    @Test
    @DisplayName("Test 7: Valid Customer Login")
    void testValidCustomerLogin() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);

        boolean loginResult = smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        assertTrue(loginResult, "Valid customer login should succeed");
        assertTrue(smartHomeService.isLoggedIn(), "User should be logged in after successful login");

        Customer currentUser = smartHomeService.getCurrentUser();
        assertNotNull(currentUser, "Current user should not be null after login");
        assertEquals(testEmail, currentUser.getEmail(), "Current user email should match");
        System.out.println(" Valid login test passed");
    }

    @Test
    @DisplayName("Test 8: Login with Wrong Password")
    void testLoginWrongPassword() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);

        boolean loginResult = smartHomeService.loginCustomer(testEmail, "WrongPassword123!");
        assertFalse(loginResult, "Login with wrong password should fail");
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in after failed login");
        System.out.println(" Wrong password test passed");
    }

    @Test
    @DisplayName("Test 9: Login with Non-existent Email")
    void testLoginNonExistentEmail() {
        boolean loginResult = smartHomeService.loginCustomer("nonexistent@email.com", TEST_PASSWORD);
        assertFalse(loginResult, "Login with non-existent email should fail");
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in");
        System.out.println(" Non-existent email test passed");
    }

    @Test
    @DisplayName("Test 10: Logout Functionality")
    void testLogoutFunctionality() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        assertTrue(smartHomeService.isLoggedIn(), "User should be logged in initially");

        smartHomeService.logout();
        assertFalse(smartHomeService.isLoggedIn(), "User should not be logged in after logout");
        assertNull(smartHomeService.getCurrentUser(), "Current user should be null after logout");
        System.out.println(" Logout test passed");
    }


    @Test
    @DisplayName("Test 11: Connect Valid Device")
    void testConnectValidDevice() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        boolean result = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertTrue(result, "Connecting valid device should succeed");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertNotNull(devices, "Device list should not be null");
        assertEquals(1, devices.size(), "Should have one device connected");

        Gadget device = devices.get(0);
        assertEquals("TV", device.getType(), "Device type should match");
        assertEquals("Samsung", device.getModel(), "Device model should match");
        assertEquals("Living Room", device.getRoomName(), "Room name should match");
        System.out.println(" Connect valid device test passed");
    }

    @Test
    @DisplayName("Test 12: Connect Device Without Login")
    void testConnectDeviceWithoutLogin() {
        boolean result = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertFalse(result, "Connecting device without login should fail");
        System.out.println(" Connect without login test passed");
    }

    @Test
    @DisplayName("Test 13: Connect Duplicate Device in Same Room")
    void testConnectDuplicateDevice() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        boolean firstResult = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        assertTrue(firstResult, "First device connection should succeed");

        boolean secondResult = smartHomeService.connectToGadget("TV", "LG", "Living Room");
        assertFalse(secondResult, "Duplicate device type in same room should fail");

        List<Gadget> devices = smartHomeService.viewGadgets();
        assertEquals(1, devices.size(), "Should still have only one device");
        System.out.println(" Duplicate device test passed");
    }


    @Test
    @DisplayName("Test 15: Connect Invalid Device")
    void testConnectInvalidDevice() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        boolean result = smartHomeService.connectToGadget("TV", "InvalidBrand", "Living Room");
        assertFalse(result, "Connecting device with invalid model should fail");

        boolean result2 = smartHomeService.connectToGadget("TV", "Samsung", "Invalid Room");
        assertFalse(result2, "Connecting device with invalid room should fail");
        System.out.println(" Invalid device test passed");
    }


    @Test
    @DisplayName("Test 16: Change Device Status")
    void testChangeDeviceStatus() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        List<Gadget> devices = smartHomeService.viewGadgets();
        Gadget device = devices.get(0);
        String initialStatus = device.getStatus();

        boolean result = smartHomeService.changeGadgetStatus("TV");
        assertTrue(result, "Changing device status should succeed");

        devices = smartHomeService.viewGadgets();
        device = devices.get(0);
        String newStatus = device.getStatus();
        assertNotEquals(initialStatus, newStatus, "Device status should have changed");
        System.out.println(" Change device status test passed");
    }



    @Test
    @DisplayName("Test 18: Show Current User Info")
    void testShowCurrentUserInfo() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        assertDoesNotThrow(() -> smartHomeService.showCurrentUserInfo(),
                          "Showing current user info should not throw exception");
        assertDoesNotThrow(() -> smartHomeService.showDetailedUserInfo(),
                          "Showing detailed user info should not throw exception");
        System.out.println(" Show user info test passed");
    }

    @Test
    @DisplayName("Test 19: Update User Full Name")
    void testUpdateUserFullName() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        String newName = "Updated Test User";
        boolean result = smartHomeService.updateUserFullName(newName);
        assertTrue(result, "Updating user full name should succeed");

        Customer currentUser = smartHomeService.getCurrentUser();
        assertEquals(newName, currentUser.getFullName(), "User name should be updated");
        System.out.println(" Update user name test passed");
    }

    @Test
    @DisplayName("Test 20: Update User Email")
    void testUpdateUserEmail() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        String newEmail = "updated@smarthome.com";
        boolean result = smartHomeService.updateUserEmail(newEmail);
        assertTrue(result, "Updating user email should succeed");

        Customer currentUser = smartHomeService.getCurrentUser();
        assertEquals(newEmail, currentUser.getEmail(), "User email should be updated");
        System.out.println(" Update user email test passed");
    }

    @Test
    @DisplayName("Test 21: Update User Password")
    void testUpdateUserPassword() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        String newPassword = "NewTestPass456!@#";
        boolean result = smartHomeService.updateUserPassword(newPassword, newPassword);
        assertTrue(result, "Updating user password should succeed");
        System.out.println(" Update user password test passed");
    }

    @Test
    @DisplayName("Test 22: Verify Current Password")
    void testVerifyCurrentPassword() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.loginCustomer(testEmail, TEST_PASSWORD);

        boolean correctResult = smartHomeService.verifyCurrentPassword(TEST_PASSWORD);
        assertTrue(correctResult, "Verifying correct password should succeed");

        boolean wrongResult = smartHomeService.verifyCurrentPassword("WrongPassword123!");
        assertFalse(wrongResult, "Verifying wrong password should fail");
        System.out.println(" Verify current password test passed");
    }


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
        System.out.println(" Service getters test passed");
    }


    @Test
    @DisplayName("Test 24: Password Reset Flow")
    void testPasswordResetFlow() {
        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);

        boolean initiateResult = smartHomeService.initiatePasswordReset(testEmail);
        assertTrue(initiateResult, "Password reset initiation should succeed");

        String newPassword = "NewResetPass789!@#";
        boolean resetResult = smartHomeService.resetPassword(testEmail, newPassword);
        assertTrue(resetResult, "Password reset should succeed");

        boolean loginResult = smartHomeService.loginCustomer(testEmail, newPassword);
        assertTrue(loginResult, "Login with new password should succeed");
        System.out.println(" Password reset flow test passed");
    }

    @Test
    @DisplayName("Test 25: Email Availability Check")
    void testEmailAvailabilityCheck() {
        boolean available = smartHomeService.checkEmailAvailability("available@test.com");
        assertTrue(available, "New email should be available");

        smartHomeService.registerCustomer(TEST_NAME, testEmail, TEST_PASSWORD, TEST_PASSWORD);

        boolean notAvailable = smartHomeService.checkEmailAvailability(testEmail);
        assertFalse(notAvailable, "Registered email should not be available");
        System.out.println(" Email availability check test passed");
    }
}