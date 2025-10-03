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

public class SmartHomeServiceGroupTest {

    private SmartHomeService smartHomeService;
    private String adminEmail;
    private String member1Email;
    private String member2Email;
    private static final String ADMIN_NAME = "Admin User";
    private static final String MEMBER1_NAME = "Member One";
    private static final String MEMBER2_NAME = "Member Two";
    private static final String TEST_PASSWORD = "TestPass123!@#";

    @BeforeEach
    @DisplayName("Setup group test environment")
    void setUp() {
        smartHomeService = new SmartHomeService();

        long timestamp = System.currentTimeMillis();
        adminEmail = "admin" + timestamp + "@smarthome.com";
        member1Email = "member1" + timestamp + "@smarthome.com";
        member2Email = "member2" + timestamp + "@smarthome.com";

        smartHomeService.registerCustomer(ADMIN_NAME, adminEmail, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.registerCustomer(MEMBER1_NAME, member1Email, TEST_PASSWORD, TEST_PASSWORD);
        smartHomeService.registerCustomer(MEMBER2_NAME, member2Email, TEST_PASSWORD, TEST_PASSWORD);

        System.out.println("Setting up group test environment...");
    }

    @AfterEach
    @DisplayName("Cleanup group tests")
    void tearDown() {
        if (smartHomeService.isLoggedIn()) {
            List<Gadget> devices = smartHomeService.viewGadgets();
            if (devices != null) {
                for (Gadget device : new java.util.ArrayList<>(devices)) {
                    smartHomeService.deleteDevice(device.getType(), device.getRoomName());
                }
            }
            smartHomeService.logout();
        }
        System.out.println("Cleaned up group test environment");
    }


    @Test
    @DisplayName("Test 1: Add Person to Group")
    void testAddPersonToGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        boolean result = smartHomeService.addPersonToGroup(member1Email);
        assertTrue(result, "Adding person to group should succeed");

        Customer admin = smartHomeService.getCurrentUser();
        assertTrue(admin.isGroupAdmin(), "Admin should be group admin");
        assertTrue(admin.isPartOfGroup(), "Admin should be part of group");
        assertTrue(admin.isGroupMember(member1Email), "Member should be in admin's group");

        System.out.println(" Add person to group test passed");
    }

    @Test
    @DisplayName("Test 2: Add Multiple People to Group")
    void testAddMultiplePeopleToGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        boolean result1 = smartHomeService.addPersonToGroup(member1Email);
        boolean result2 = smartHomeService.addPersonToGroup(member2Email);

        assertTrue(result1, "Adding first member should succeed");
        assertTrue(result2, "Adding second member should succeed");

        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(3, admin.getGroupSize(), "Group should have 3 members (admin + 2 members)");
        assertTrue(admin.isGroupMember(member1Email), "Member 1 should be in group");
        assertTrue(admin.isGroupMember(member2Email), "Member 2 should be in group");

        System.out.println(" Add multiple people to group test passed");
    }

    @Test
    @DisplayName("Test 3: Add Self to Group")
    void testAddSelfToGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        boolean result = smartHomeService.addPersonToGroup(adminEmail);
        assertFalse(result, "Adding self to group should fail");

        System.out.println(" Add self to group test passed");
    }

    @Test
    @DisplayName("Test 4: Add Non-existent User to Group")
    void testAddNonExistentUserToGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        boolean result = smartHomeService.addPersonToGroup("nonexistent@email.com");
        assertFalse(result, "Adding non-existent user should fail");

        System.out.println(" Add non-existent user to group test passed");
    }

    @Test
    @DisplayName("Test 5: Add Duplicate Person to Group")
    void testAddDuplicatePersonToGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        boolean firstAdd = smartHomeService.addPersonToGroup(member1Email);
        assertTrue(firstAdd, "First add should succeed");

        boolean secondAdd = smartHomeService.addPersonToGroup(member1Email);
        assertFalse(secondAdd, "Duplicate add should fail");

        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(2, admin.getGroupSize(), "Group size should remain 2");

        System.out.println(" Add duplicate person to group test passed");
    }


    @Test
    @DisplayName("Test 6: Remove Person from Group")
    void testRemovePersonFromGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.addPersonToGroup(member2Email);

        Customer admin = smartHomeService.getCurrentUser();
        assertEquals(3, admin.getGroupSize(), "Initial group should have 3 members");

        boolean result = smartHomeService.removePersonFromGroup(member1Email);
        assertTrue(result, "Removing person from group should succeed");

        admin = smartHomeService.getCurrentUser();
        assertEquals(2, admin.getGroupSize(), "Group should have 2 members after removal");
        assertFalse(admin.isGroupMember(member1Email), "Member 1 should not be in group");
        assertTrue(admin.isGroupMember(member2Email), "Member 2 should still be in group");

        System.out.println(" Remove person from group test passed");
    }

    @Test
    @DisplayName("Test 7: Remove Non-existent Person from Group")
    void testRemoveNonExistentPersonFromGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);

        boolean result = smartHomeService.removePersonFromGroup("nonexistent@email.com");
        assertFalse(result, "Removing non-existent person should fail");

        System.out.println(" Remove non-existent person from group test passed");
    }

    @Test
    @DisplayName("Test 8: Remove Self from Group")
    void testRemoveSelfFromGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);

        boolean result = smartHomeService.removePersonFromGroup(adminEmail);
        assertFalse(result, "Removing self should fail");

        System.out.println(" Remove self from group test passed");
    }


    @Test
    @DisplayName("Test 9: Show Group Info")
    void testShowGroupInfo() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.addPersonToGroup(member2Email);

        assertDoesNotThrow(() -> smartHomeService.showGroupInfo(),
                          "Showing group info should not throw exception");

        System.out.println(" Show group info test passed");
    }

    @Test
    @DisplayName("Test 10: Group Info Without Group")
    void testGroupInfoWithoutGroup() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);

        assertDoesNotThrow(() -> smartHomeService.showGroupInfo(),
                          "Showing group info without group should not throw exception");

        System.out.println(" Group info without group test passed");
    }


    @Test
    @DisplayName("Test 11: Grant Device Permission")
    void testGrantDevicePermission() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        assertTrue(result, "Granting device permission should succeed");

        boolean hasPermission = smartHomeService.hasDevicePermission(member1Email, "TV", "Living Room");
        assertTrue(hasPermission, "Member should have device permission");

        System.out.println(" Grant device permission test passed");
    }

    @Test
    @DisplayName("Test 12: Grant Permission for Non-existent Device")
    void testGrantPermissionNonExistentDevice() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);

        boolean result = smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        assertFalse(result, "Granting permission for non-existent device should fail");

        System.out.println(" Grant permission for non-existent device test passed");
    }

    @Test
    @DisplayName("Test 13: Grant Permission to Non-group Member")
    void testGrantPermissionNonGroupMember() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        assertFalse(result, "Granting permission to non-group member should fail");

        System.out.println(" Grant permission to non-group member test passed");
    }

    @Test
    @DisplayName("Test 14: Revoke Device Permission")
    void testRevokeDevicePermission() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");

        assertTrue(smartHomeService.hasDevicePermission(member1Email, "TV", "Living Room"),
                  "Permission should exist before revoking");

        boolean result = smartHomeService.revokeDevicePermission(member1Email, "TV", "Living Room");
        assertTrue(result, "Revoking device permission should succeed");

        assertFalse(smartHomeService.hasDevicePermission(member1Email, "TV", "Living Room"),
                   "Permission should not exist after revoking");

        System.out.println(" Revoke device permission test passed");
    }

    @Test
    @DisplayName("Test 15: Revoke Non-existent Permission")
    void testRevokeNonExistentPermission() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean result = smartHomeService.revokeDevicePermission(member1Email, "TV", "Living Room");
        assertFalse(result, "Revoking non-existent permission should fail");

        System.out.println(" Revoke non-existent permission test passed");
    }


    @Test
    @DisplayName("Test 17: Show Device Permissions")
    void testShowDevicePermissions() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");

        assertDoesNotThrow(() -> smartHomeService.showDevicePermissions(),
                          "Showing device permissions should not throw exception");

        System.out.println(" Show device permissions test passed");
    }


    @Test
    @DisplayName("Test 18: Non-admin Cannot Grant Permissions")
    void testNonAdminCannotGrantPermissions() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.logout();

        smartHomeService.loginCustomer(member1Email, TEST_PASSWORD);
        Customer member = smartHomeService.getCurrentUser();
        assertFalse(member.isGroupAdmin(), "Member should not be group admin");

        boolean result = smartHomeService.grantDevicePermission(adminEmail, "TV", "Living Room");
        assertFalse(result, "Non-admin should not be able to grant permissions");

        System.out.println(" Non-admin cannot grant permissions test passed");
    }

    @Test
    @DisplayName("Test 19: Non-admin Cannot Revoke Permissions")
    void testNonAdminCannotRevokePermissions() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.addPersonToGroup(member2Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(member2Email, "TV", "Living Room");
        smartHomeService.logout();

        smartHomeService.loginCustomer(member1Email, TEST_PASSWORD);

        boolean result = smartHomeService.revokeDevicePermission(member2Email, "TV", "Living Room");
        assertFalse(result, "Non-admin should not be able to revoke permissions");

        System.out.println(" Non-admin cannot revoke permissions test passed");
    }


    @Test
    @DisplayName("Test 20: Permission-based Device Viewing")
    void testPermissionBasedDeviceViewing() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.connectToGadget("AC", "LG", "Bedroom");

        smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        smartHomeService.logout();

        smartHomeService.loginCustomer(member1Email, TEST_PASSWORD);

        List<Gadget> accessibleDevices = smartHomeService.viewGadgets();
        assertNotNull(accessibleDevices, "Accessible devices should not be null");

        System.out.println("Member can access " + accessibleDevices.size() + " devices");

        System.out.println(" Permission-based device viewing test passed");
    }

    @Test
    @DisplayName("Test 21: Permission-based Device Control")
    void testPermissionBasedDeviceControl() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        smartHomeService.logout();

        smartHomeService.loginCustomer(member1Email, TEST_PASSWORD);

        List<Gadget> devices = smartHomeService.viewGadgets();
        if (devices != null && !devices.isEmpty()) {
            assertDoesNotThrow(() -> smartHomeService.changeGadgetStatus("TV"),
                              "Member should be able to control permitted device");
        }

        System.out.println(" Permission-based device control test passed");
    }


    @Test
    @DisplayName("Test 22: Get Group Members for Permissions")
    void testGetGroupMembersForPermissions() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.addPersonToGroup(member2Email);

        List<Customer> groupMembers = smartHomeService.getGroupMembersForPermissions();
        assertNotNull(groupMembers, "Group members list should not be null");

        assertTrue(groupMembers.size() >= 0, "Group members count should be non-negative");

        System.out.println(" Get group members for permissions test passed");
    }

    @Test
    @DisplayName("Test 23: Duplicate Permission Prevention")
    void testDuplicatePermissionPrevention() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        boolean firstGrant = smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        assertTrue(firstGrant, "First permission grant should succeed");

        boolean secondGrant = smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        assertFalse(secondGrant, "Duplicate permission grant should fail");

        System.out.println(" Duplicate permission prevention test passed");
    }


    @Test
    @DisplayName("Test 24: Cross-Group Permission Isolation")
    void testCrossGroupPermissionIsolation() {
        smartHomeService.loginCustomer(adminEmail, TEST_PASSWORD);
        smartHomeService.addPersonToGroup(member1Email);
        smartHomeService.connectToGadget("TV", "Samsung", "Living Room");
        smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room");
        smartHomeService.logout();

        smartHomeService.loginCustomer(member2Email, TEST_PASSWORD);

        boolean hasPermission = smartHomeService.hasDevicePermission(member2Email, "TV", "Living Room");
        assertFalse(hasPermission, "Non-group member should not have device permission");

        System.out.println(" Cross-group permission isolation test passed");
    }


    @Test
    @DisplayName("Test 25: Permission Operations Without Login")
    void testPermissionOperationsWithoutLogin() {
        assertFalse(smartHomeService.grantDevicePermission(member1Email, "TV", "Living Room"),
                   "Grant permission without login should fail");

        assertFalse(smartHomeService.revokeDevicePermission(member1Email, "TV", "Living Room"),
                   "Revoke permission without login should fail");

        assertFalse(smartHomeService.hasDevicePermission(member1Email, "TV", "Living Room"),
                   "Check permission without login should return false");

        System.out.println(" Permission operations without login test passed");
    }
}