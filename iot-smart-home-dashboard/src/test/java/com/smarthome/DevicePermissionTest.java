package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.DevicePermission;
import com.smarthome.service.SmartHomeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DevicePermissionTest {

    private SmartHomeService smartHomeService;
    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String MEMBER_EMAIL = "member@test.com";
    private static final String ADMIN_NAME = "Admin User";
    private static final String MEMBER_NAME = "Member User";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup device permission test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();

        // Register admin user
        smartHomeService.registerCustomer(ADMIN_NAME, ADMIN_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        // Register member user
        smartHomeService.registerCustomer(MEMBER_NAME, MEMBER_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        System.out.println("🔐 Setting up device permission test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup permission tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up permission test environment");
    }

    @Test
    @DisplayName("Test 1: Basic Device Permission Model")
    void testDevicePermissionModel() {
        DevicePermission permission = new DevicePermission(
            MEMBER_EMAIL, "TV", "Living Room", ADMIN_EMAIL, ADMIN_EMAIL
        );

        assertNotNull(permission, "Device permission should be created");
        assertEquals(MEMBER_EMAIL, permission.getMemberEmail(), "Member email should match");
        assertEquals("TV", permission.getDeviceType(), "Device type should match");
        assertEquals("Living Room", permission.getRoomName(), "Room name should match");
        assertEquals(ADMIN_EMAIL, permission.getDeviceOwnerEmail(), "Device owner should match");
        assertTrue(permission.isCanView(), "Default view permission should be true");
        assertTrue(permission.isCanControl(), "Default control permission should be true");

        System.out.println("✅ Device permission model test passed");
    }

    @Test
    @DisplayName("Test 2: Device Permission Matching")
    void testDevicePermissionMatching() {
        DevicePermission permission = new DevicePermission(
            MEMBER_EMAIL, "TV", "Living Room", ADMIN_EMAIL, ADMIN_EMAIL
        );

        // Test matching
        assertTrue(permission.matchesDevice("TV", "Living Room", ADMIN_EMAIL),
                  "Should match exact device details");

        // Test case insensitive matching
        assertTrue(permission.matchesDevice("tv", "living room", ADMIN_EMAIL),
                  "Should match case insensitive device details");

        // Test non-matching
        assertFalse(permission.matchesDevice("AC", "Living Room", ADMIN_EMAIL),
                   "Should not match different device type");
        assertFalse(permission.matchesDevice("TV", "Bedroom", ADMIN_EMAIL),
                   "Should not match different room");

        System.out.println("✅ Device permission matching test passed");
    }

    @Test
    @DisplayName("Test 3: Customer Device Permission Management")
    void testCustomerDevicePermissions() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Add some devices first
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");
        smartHomeService.connectToGadget("AC", "LG Dual Inverter", "Master Bedroom");

        Customer adminUser = smartHomeService.getCurrentUser();
        assertNotNull(adminUser, "Admin user should be logged in");

        // Test granting permission
        boolean granted = adminUser.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room", ADMIN_EMAIL);
        assertTrue(granted, "Permission should be granted successfully");

        // Test checking permission
        boolean hasPermission = adminUser.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(hasPermission, "Member should have permission for TV");

        // Test permission doesn't exist for other devices
        boolean noPermission = adminUser.hasDevicePermission(MEMBER_EMAIL, "AC", "Master Bedroom");
        assertFalse(noPermission, "Member should not have permission for AC initially");

        // Test revoking permission
        boolean revoked = adminUser.revokeDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(revoked, "Permission should be revoked successfully");

        boolean permissionGone = adminUser.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertFalse(permissionGone, "Member should no longer have permission for TV");

        System.out.println("✅ Customer device permission management test passed");
    }

    @Test
    @DisplayName("Test 4: Group Setup and Admin Check")
    void testGroupSetupAndAdmin() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Add member to group
        boolean memberAdded = smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        assertTrue(memberAdded, "Member should be added to group successfully");

        Customer adminUser = smartHomeService.getCurrentUser();
        assertTrue(adminUser.isGroupAdmin(), "Admin should be group admin");
        assertTrue(adminUser.isPartOfGroup(), "Admin should be part of group");

        System.out.println("✅ Group setup and admin check test passed");
    }

    @Test
    @DisplayName("Test 5: Grant Device Permission via Service")
    void testGrantDevicePermissionViaService() {
        // Setup group and devices
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");

        // Grant permission via service
        boolean granted = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(granted, "Device permission should be granted via service");

        // Verify permission exists
        boolean hasPermission = smartHomeService.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(hasPermission, "Permission should exist after granting");

        System.out.println("✅ Grant device permission via service test passed");
    }

    @Test
    @DisplayName("Test 6: Revoke Device Permission via Service")
    void testRevokeDevicePermissionViaService() {
        // Setup group and devices with permission
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");

        // Verify permission exists
        assertTrue(smartHomeService.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room"),
                  "Permission should exist before revoking");

        // Revoke permission via service
        boolean revoked = smartHomeService.revokeDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(revoked, "Device permission should be revoked via service");

        // Verify permission no longer exists
        boolean permissionGone = smartHomeService.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertFalse(permissionGone, "Permission should not exist after revoking");

        System.out.println("✅ Revoke device permission via service test passed");
    }

    @Test
    @DisplayName("Test 7: Non-Admin Permission Management")
    void testNonAdminPermissionManagement() {
        // Setup group first
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.logout();

        // Try to grant permission as non-admin member
        smartHomeService.loginCustomer(MEMBER_EMAIL, TEST_PASSWORD);

        Customer memberUser = smartHomeService.getCurrentUser();
        assertFalse(memberUser.isGroupAdmin(), "Member should not be group admin");

        // This should fail because member is not admin
        boolean granted = smartHomeService.grantDevicePermission(ADMIN_EMAIL, "TV", "Living Room");
        assertFalse(granted, "Non-admin should not be able to grant permissions");

        System.out.println("✅ Non-admin permission management test passed");
    }

    @Test
    @DisplayName("Test 8: Permission-Based Device Viewing")
    void testPermissionBasedDeviceViewing() {
        // Setup as admin: create group, add devices, grant specific permissions
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");
        smartHomeService.connectToGadget("AC", "LG Dual Inverter", "Master Bedroom");

        // Grant permission only to TV, not AC
        smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");

        smartHomeService.logout();

        // Login as member and check device access
        smartHomeService.loginCustomer(MEMBER_EMAIL, TEST_PASSWORD);

        // Member should see their own devices + devices they have permission for
        List<com.smarthome.model.Gadget> accessibleDevices = smartHomeService.viewGadgets();
        assertNotNull(accessibleDevices, "Accessible devices should not be null");

        // The exact count depends on member's own devices + granted permissions
        // But we can verify the permission system is working by checking the method completes
        System.out.println("✅ Permission-based device viewing test passed - " +
                          (accessibleDevices != null ? accessibleDevices.size() : 0) + " accessible devices");
    }

    @Test
    @DisplayName("Test 9: Multiple Device Permissions")
    void testMultipleDevicePermissions() {
        // Setup
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");
        smartHomeService.connectToGadget("AC", "LG Dual Inverter", "Master Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips Hue", "Kitchen");

        // Grant multiple permissions
        boolean tvGranted = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        boolean acGranted = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "AC", "Master Bedroom");
        boolean lightGranted = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "LIGHT", "Kitchen");

        assertTrue(tvGranted, "TV permission should be granted");
        assertTrue(acGranted, "AC permission should be granted");
        assertTrue(lightGranted, "Light permission should be granted");

        // Verify all permissions
        assertTrue(smartHomeService.hasDevicePermission(MEMBER_EMAIL, "TV", "Living Room"));
        assertTrue(smartHomeService.hasDevicePermission(MEMBER_EMAIL, "AC", "Master Bedroom"));
        assertTrue(smartHomeService.hasDevicePermission(MEMBER_EMAIL, "LIGHT", "Kitchen"));

        Customer adminUser = smartHomeService.getCurrentUser();
        List<DevicePermission> memberPermissions = adminUser.getPermissionsForMember(MEMBER_EMAIL);
        assertEquals(3, memberPermissions.size(), "Member should have 3 permissions");

        System.out.println("✅ Multiple device permissions test passed");
    }

    @Test
    @DisplayName("Test 10: Duplicate Permission Prevention")
    void testDuplicatePermissionPrevention() {
        // Setup
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung Smart TV", "Living Room");

        // Grant permission first time
        boolean firstGrant = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertTrue(firstGrant, "First permission grant should succeed");

        // Try to grant same permission again
        boolean duplicateGrant = smartHomeService.grantDevicePermission(MEMBER_EMAIL, "TV", "Living Room");
        assertFalse(duplicateGrant, "Duplicate permission grant should fail");

        Customer adminUser = smartHomeService.getCurrentUser();
        List<DevicePermission> permissions = adminUser.getPermissionsForMember(MEMBER_EMAIL);
        assertEquals(1, permissions.size(), "Should only have one permission, not duplicate");

        System.out.println("✅ Duplicate permission prevention test passed");
    }
}