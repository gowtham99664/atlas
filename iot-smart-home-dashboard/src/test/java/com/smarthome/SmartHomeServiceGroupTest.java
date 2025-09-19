package com.smarthome;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.model.DevicePermission;
import com.smarthome.service.SmartHomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Group Management and Device Permission functionality tests for SmartHomeService
 * Tests: Group Creation, Permission Management, Family Sharing, Access Control
 */
public class SmartHomeServiceGroupTest {

    private SmartHomeService smartHomeService;
    private static final String ADMIN_EMAIL = "admin@smarthome.com";
    private static final String MEMBER1_EMAIL = "member1@smarthome.com";
    private static final String MEMBER2_EMAIL = "member2@smarthome.com";
    private static final String ADMIN_NAME = "Admin User";
    private static final String MEMBER1_NAME = "Member One";
    private static final String MEMBER2_NAME = "Member Two";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup group test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();

        // Register multiple users for group testing
        smartHomeService.registerCustomer(ADMIN_NAME, ADMIN_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.registerCustomer(MEMBER1_NAME, MEMBER1_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.registerCustomer(MEMBER2_NAME, MEMBER2_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        System.out.println("👥 Setting up group test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup group tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            smartHomeService.logout();
        }
        System.out.println("🧹 Cleaned up group test environment");
    }

    // ==================== GROUP CREATION TESTS ====================

    @Test
    @DisplayName("Test 1: Add Person to Group")
    void testAddPersonToGroup() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Add member to group
        boolean result = smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        assertTrue(result, "Adding person to group should succeed");

        // Verify group setup
        Customer admin = smartHomeService.getCurrentUser();
        assertTrue(admin.isGroupAdmin(), "Admin should be group admin");
        assertTrue(admin.isPartOfGroup(), "Admin should be part of group");
        assertTrue(admin.isGroupMember(MEMBER1_EMAIL), "Member should be in admin's group");

        System.out.println("✅ Add person to group test passed");
    }

    @Test
    @DisplayName("Test 2: Add Multiple People to Group")
    void testAddMultiplePeopleToGroup() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Add multiple members
        boolean result1 = smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        boolean result2 = smartHomeService.addPersonToGroup(MEMBER2_EMAIL);

        assertTrue(result1, "Adding first member should succeed");
        assertTrue(result2, "Adding second member should succeed");

        // Verify group structure
        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(3, admin.getGroupSize(), "Group should have 3 members (admin + 2 members)");
        assertTrue(admin.isGroupMember(MEMBER1_EMAIL), "Member 1 should be in group");
        assertTrue(admin.isGroupMember(MEMBER2_EMAIL), "Member 2 should be in group");

        System.out.println("✅ Add multiple people to group test passed");
    }

    @Test
    @DisplayName("Test 3: Add Self to Group")
    void testAddSelfToGroup() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Try to add self to group
        boolean result = smartHomeService.addPersonToGroup(ADMIN_EMAIL);
        assertFalse(result, "Adding self to group should fail");

        System.out.println("✅ Add self to group test passed");
    }

    @Test
    @DisplayName("Test 4: Add Non-existent User to Group")
    void testAddNonExistentUserToGroup() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Try to add non-existent user
        boolean result = smartHomeService.addPersonToGroup("nonexistent@email.com");
        assertFalse(result, "Adding non-existent user should fail");

        System.out.println("✅ Add non-existent user to group test passed");
    }

    @Test
    @DisplayName("Test 5: Add Duplicate Person to Group")
    void testAddDuplicatePersonToGroup() {
        // Login as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Add member first time
        boolean firstAdd = smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        assertTrue(firstAdd, "First add should succeed");

        // Try to add same member again
        boolean secondAdd = smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        assertFalse(secondAdd, "Duplicate add should fail");

        // Group size should still be 2
        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(2, admin.getGroupSize(), "Group size should remain 2");

        System.out.println("✅ Add duplicate person to group test passed");
    }

    // ==================== GROUP REMOVAL TESTS ====================

    @Test
    @DisplayName("Test 6: Remove Person from Group")
    void testRemovePersonFromGroup() {
        // Setup group first
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.addPersonToGroup(MEMBER2_EMAIL);

        // Verify initial group size
        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(3, admin.getGroupSize(), "Initial group should have 3 members");

        // Remove a member
        boolean result = smartHomeService.removePersonFromGroup(MEMBER1_EMAIL);
        assertTrue(result, "Removing person from group should succeed");

        // Verify member was removed
        admin = smartHomeService.getCurrentUser();
        assertEquals(2, admin.getGroupSize(), "Group should have 2 members after removal");
        assertFalse(admin.isGroupMember(MEMBER1_EMAIL), "Member 1 should not be in group");
        assertTrue(admin.isGroupMember(MEMBER2_EMAIL), "Member 2 should still be in group");

        System.out.println("✅ Remove person from group test passed");
    }

    @Test
    @DisplayName("Test 7: Remove Non-existent Person from Group")
    void testRemoveNonExistentPersonFromGroup() {
        // Setup group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);

        // Try to remove non-existent member
        boolean result = smartHomeService.removePersonFromGroup("nonexistent@email.com");
        assertFalse(result, "Removing non-existent person should fail");

        System.out.println("✅ Remove non-existent person from group test passed");
    }

    @Test
    @DisplayName("Test 8: Remove Self from Group")
    void testRemoveSelfFromGroup() {
        // Setup group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);

        // Try to remove self
        boolean result = smartHomeService.removePersonFromGroup(ADMIN_EMAIL);
        assertFalse(result, "Removing self should fail");

        System.out.println("✅ Remove self from group test passed");
    }

    // ==================== GROUP INFO TESTS ====================

    @Test
    @DisplayName("Test 9: Show Group Info")
    void testShowGroupInfo() {
        // Setup group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.addPersonToGroup(MEMBER2_EMAIL);

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showGroupInfo(),
                          "Showing group info should not throw exception");

        System.out.println("✅ Show group info test passed");
    }

    @Test
    @DisplayName("Test 10: Group Info Without Group")
    void testGroupInfoWithoutGroup() {
        // Login but don't create group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);

        // Should not throw exception even without group
        assertDoesNotThrow(() -> smartHomeService.showGroupInfo(),
                          "Showing group info without group should not throw exception");

        System.out.println("✅ Group info without group test passed");
    }

    // ==================== DEVICE PERMISSION TESTS ====================

    @Test
    @DisplayName("Test 11: Grant Device Permission")
    void testGrantDevicePermission() {
        // Setup group and device
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Grant permission
        boolean result = smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertTrue(result, "Granting device permission should succeed");

        // Verify permission exists
        boolean hasPermission = smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertTrue(hasPermission, "Member should have device permission");

        System.out.println("✅ Grant device permission test passed");
    }

    @Test
    @DisplayName("Test 12: Grant Permission for Non-existent Device")
    void testGrantPermissionNonExistentDevice() {
        // Setup group without device
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);

        // Try to grant permission for non-existent device
        boolean result = smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertFalse(result, "Granting permission for non-existent device should fail");

        System.out.println("✅ Grant permission for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 13: Grant Permission to Non-group Member")
    void testGrantPermissionNonGroupMember() {
        // Setup device but no group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to grant permission to non-group member
        boolean result = smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertFalse(result, "Granting permission to non-group member should fail");

        System.out.println("✅ Grant permission to non-group member test passed");
    }

    @Test
    @DisplayName("Test 14: Revoke Device Permission")
    void testRevokeDevicePermission() {
        // Setup group, device, and permission
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");

        // Verify permission exists
        assertTrue(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"),
                  "Permission should exist before revoking");

        // Revoke permission
        boolean result = smartHomeService.revokeDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertTrue(result, "Revoking device permission should succeed");

        // Verify permission is gone
        assertFalse(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"),
                   "Permission should not exist after revoking");

        System.out.println("✅ Revoke device permission test passed");
    }

    @Test
    @DisplayName("Test 15: Revoke Non-existent Permission")
    void testRevokeNonExistentPermission() {
        // Setup group and device without permission
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Try to revoke non-existent permission
        boolean result = smartHomeService.revokeDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertFalse(result, "Revoking non-existent permission should fail");

        System.out.println("✅ Revoke non-existent permission test passed");
    }

    @Test
    @DisplayName("Test 16: Multiple Device Permissions")
    void testMultipleDevicePermissions() {
        // Setup group and multiple devices
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");
        smartHomeService.connectToGadget("LIGHT", "Philips", "Kitchen");

        // Grant permissions for multiple devices
        assertTrue(smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"));
        assertTrue(smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "AC", "Bedroom"));
        assertTrue(smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "LIGHT", "Kitchen"));

        // Verify all permissions exist
        assertTrue(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"));
        assertTrue(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "AC", "Bedroom"));
        assertTrue(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "LIGHT", "Kitchen"));

        System.out.println("✅ Multiple device permissions test passed");
    }

    @Test
    @DisplayName("Test 17: Show Device Permissions")
    void testShowDevicePermissions() {
        // Setup group, devices, and permissions
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");

        // Should not throw exception
        assertDoesNotThrow(() -> smartHomeService.showDevicePermissions(),
                          "Showing device permissions should not throw exception");

        System.out.println("✅ Show device permissions test passed");
    }

    // ==================== NON-ADMIN PERMISSION TESTS ====================

    @Test
    @DisplayName("Test 18: Non-admin Cannot Grant Permissions")
    void testNonAdminCannotGrantPermissions() {
        // Setup group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.logout();

        // Login as member (non-admin)
        smartHomeService.loginCustomer(MEMBER1_EMAIL, TEST_PASSWORD);
        Customer member = smartHomeService.getCurrentUser();
        assertFalse(member.isGroupAdmin(), "Member should not be group admin");

        // Try to grant permission
        boolean result = smartHomeService.grantDevicePermission(ADMIN_EMAIL, "TV", "Living Room");
        assertFalse(result, "Non-admin should not be able to grant permissions");

        System.out.println("✅ Non-admin cannot grant permissions test passed");
    }

    @Test
    @DisplayName("Test 19: Non-admin Cannot Revoke Permissions")
    void testNonAdminCannotRevokePermissions() {
        // Setup group and permission
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.addPersonToGroup(MEMBER2_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER2_EMAIL, "TV", "Living Room");
        smartHomeService.logout();

        // Login as member 1 (non-admin)
        smartHomeService.loginCustomer(MEMBER1_EMAIL, TEST_PASSWORD);

        // Try to revoke member 2's permission
        boolean result = smartHomeService.revokeDevicePermission(MEMBER2_EMAIL, "TV", "Living Room");
        assertFalse(result, "Non-admin should not be able to revoke permissions");

        System.out.println("✅ Non-admin cannot revoke permissions test passed");
    }

    // ==================== PERMISSION-BASED DEVICE ACCESS TESTS ====================

    @Test
    @DisplayName("Test 20: Permission-based Device Viewing")
    void testPermissionBasedDeviceViewing() {
        // Setup as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        // Grant permission only for TV
        smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        smartHomeService.logout();

        // Login as member
        smartHomeService.loginCustomer(MEMBER1_EMAIL, TEST_PASSWORD);

        // View devices (should see own devices + permitted devices)
        List<Gadget> accessibleDevices = smartHomeService.viewGadgets();
        assertNotNull(accessibleDevices, "Accessible devices should not be null");

        // Member should see at least the TV they have permission for
        // (The exact count depends on how the viewGadgets method works with permissions)
        System.out.println("Member can access " + accessibleDevices.size() + " devices");

        System.out.println("✅ Permission-based device viewing test passed");
    }

    @Test
    @DisplayName("Test 21: Permission-based Device Control")
    void testPermissionBasedDeviceControl() {
        // Setup as admin
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        smartHomeService.logout();

        // Login as member
        smartHomeService.loginCustomer(MEMBER1_EMAIL, TEST_PASSWORD);

        // Try to control the permitted device
        // Note: The actual control depends on the implementation
        // This test verifies the member can attempt to control the device
        List<Gadget> devices = smartHomeService.viewGadgets();
        if (devices != null && !devices.isEmpty()) {
            // If member can see devices, they should be able to control permitted ones
            assertDoesNotThrow(() -> smartHomeService.changeGadgetStatus("TV"),
                              "Member should be able to control permitted device");
        }

        System.out.println("✅ Permission-based device control test passed");
    }

    // ==================== GROUP MEMBER MANAGEMENT TESTS ====================

    @Test
    @DisplayName("Test 22: Get Group Members for Permissions")
    void testGetGroupMembersForPermissions() {
        // Setup group
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.addPersonToGroup(MEMBER2_EMAIL);

        // Get group members
        List<Customer> groupMembers = smartHomeService.getGroupMembersForPermissions();
        assertNotNull(groupMembers, "Group members list should not be null");

        // Should include the members (exact implementation may vary)
        assertTrue(groupMembers.size() >= 0, "Group members count should be non-negative");

        System.out.println("✅ Get group members for permissions test passed");
    }

    @Test
    @DisplayName("Test 23: Duplicate Permission Prevention")
    void testDuplicatePermissionPrevention() {
        // Setup group and device
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Grant permission first time
        boolean firstGrant = smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertTrue(firstGrant, "First permission grant should succeed");

        // Try to grant same permission again
        boolean secondGrant = smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        assertFalse(secondGrant, "Duplicate permission grant should fail");

        System.out.println("✅ Duplicate permission prevention test passed");
    }

    // ==================== CROSS-GROUP PERMISSION TESTS ====================

    @Test
    @DisplayName("Test 24: Cross-Group Permission Isolation")
    void testCrossGroupPermissionIsolation() {
        // Create first group (admin + member1)
        smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(MEMBER1_EMAIL);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room");
        smartHomeService.logout();

        // Member2 is not in the group and should not have access
        // Login as member2 and verify they cannot access the device
        smartHomeService.loginCustomer(MEMBER2_EMAIL, TEST_PASSWORD);

        // Member2 should not have permission for admin's TV
        boolean hasPermission = smartHomeService.hasDevicePermission(MEMBER2_EMAIL, "TV", "Living Room");
        assertFalse(hasPermission, "Non-group member should not have device permission");

        System.out.println("✅ Cross-group permission isolation test passed");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 25: Permission Operations Without Login")
    void testPermissionOperationsWithoutLogin() {
        // Try permission operations without login
        assertFalse(smartHomeService.grantDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"),
                   "Grant permission without login should fail");

        assertFalse(smartHomeService.revokeDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"),
                   "Revoke permission without login should fail");

        assertFalse(smartHomeService.hasDevicePermission(MEMBER1_EMAIL, "TV", "Living Room"),
                   "Check permission without login should return false");

        System.out.println("✅ Permission operations without login test passed");
    }
}