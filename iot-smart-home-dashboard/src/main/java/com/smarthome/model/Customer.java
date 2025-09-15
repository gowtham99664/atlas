package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
public class Customer {
    
    private String email;
    private String fullName;
    private String password;
    private List<Gadget> gadgets;
    private List<String> groupMembers;
    private String groupCreator;
    private List<DeletedDeviceEnergyRecord> deletedDeviceEnergyRecords;
    private List<DevicePermission> devicePermissions;

    private int failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime lastFailedLoginTime;
    
    
    public Customer() {
        this.gadgets = new ArrayList<>();
        this.groupMembers = new ArrayList<>();
        this.groupCreator = null;
        this.deletedDeviceEnergyRecords = new ArrayList<>();
        this.devicePermissions = new ArrayList<>();
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
        this.lastFailedLoginTime = null;
    }

    public Customer(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.gadgets = new ArrayList<>();
        this.groupMembers = new ArrayList<>();
        this.groupCreator = null;
        this.deletedDeviceEnergyRecords = new ArrayList<>();
        this.devicePermissions = new ArrayList<>();
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
        this.lastFailedLoginTime = null;
    }
    
    @DynamoDbPartitionKey
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Gadget> getGadgets() {
        return gadgets;
    }
    
    public void setGadgets(List<Gadget> gadgets) {
        this.gadgets = gadgets != null ? gadgets : new ArrayList<>();
    }
    
    public void addGadget(Gadget gadget) {
        if (this.gadgets == null) {
            this.gadgets = new ArrayList<>();
        }
        
        boolean exists = this.gadgets.stream()
                .anyMatch(g -> g.getType().equalsIgnoreCase(gadget.getType()) && 
                              g.getRoomName().equalsIgnoreCase(gadget.getRoomName()));
        
        if (!exists) {
            this.gadgets.add(gadget);
        }
    }
    
    public Gadget findGadget(String type, String roomName) {
        if (this.gadgets == null) {
            return null;
        }
        
        return this.gadgets.stream()
                .filter(g -> g.getType().equalsIgnoreCase(type) && 
                           g.getRoomName().equalsIgnoreCase(roomName))
                .findFirst()
                .orElse(null);
    }
    
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
    
    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }
    
    public LocalDateTime getAccountLockedUntil() {
        return accountLockedUntil;
    }
    
    public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
        this.accountLockedUntil = accountLockedUntil;
    }
    
    public LocalDateTime getLastFailedLoginTime() {
        return lastFailedLoginTime;
    }
    
    public void setLastFailedLoginTime(LocalDateTime lastFailedLoginTime) {
        this.lastFailedLoginTime = lastFailedLoginTime;
    }
    
    public boolean isAccountLocked() {
        return accountLockedUntil != null && LocalDateTime.now().isBefore(accountLockedUntil);
    }
    
    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        this.lastFailedLoginTime = LocalDateTime.now();
    }
    
    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
        this.lastFailedLoginTime = null;
    }
    
    public void lockAccount(int minutes) {
        this.accountLockedUntil = LocalDateTime.now().plusMinutes(minutes);
    }
    
    public List<String> getGroupMembers() {
        return groupMembers;
    }
    
    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers != null ? groupMembers : new ArrayList<>();
    }
    
    public void addGroupMember(String memberEmail) {
        if (this.groupMembers == null) {
            this.groupMembers = new ArrayList<>();
        }
        
        if (!this.groupMembers.contains(memberEmail.toLowerCase().trim())) {
            this.groupMembers.add(memberEmail.toLowerCase().trim());
        }
    }
    
    public void removeGroupMember(String memberEmail) {
        if (this.groupMembers != null) {
            this.groupMembers.remove(memberEmail.toLowerCase().trim());
        }
    }
    
    public boolean isPartOfGroup() {
        return this.groupMembers != null && !this.groupMembers.isEmpty();
    }
    
    public boolean isGroupMember(String memberEmail) {
        return this.groupMembers != null && this.groupMembers.contains(memberEmail.toLowerCase().trim());
    }
    
    public String getGroupCreator() {
        return groupCreator;
    }
    
    public void setGroupCreator(String groupCreator) {
        this.groupCreator = groupCreator;
    }
    
    public boolean isGroupAdmin() {
        return this.groupCreator != null && this.groupCreator.equalsIgnoreCase(this.email);
    }
    
    public boolean isGroupAdmin(String email) {
        return this.groupCreator != null && this.groupCreator.equalsIgnoreCase(email);
    }
    
    public int getGroupSize() {
        int size = 0;
        if (this.groupMembers != null) {
            size += this.groupMembers.size();
        }
        return size + 1; // +1 for the current user
    }

    @DynamoDbAttribute("deletedDeviceEnergyRecords")
    public List<DeletedDeviceEnergyRecord> getDeletedDeviceEnergyRecords() {
        if (deletedDeviceEnergyRecords == null) {
            deletedDeviceEnergyRecords = new ArrayList<>();
        }
        return deletedDeviceEnergyRecords;
    }

    public void setDeletedDeviceEnergyRecords(List<DeletedDeviceEnergyRecord> deletedDeviceEnergyRecords) {
        this.deletedDeviceEnergyRecords = deletedDeviceEnergyRecords;
    }

    public void addDeletedDeviceRecord(DeletedDeviceEnergyRecord record) {
        if (deletedDeviceEnergyRecords == null) {
            deletedDeviceEnergyRecords = new ArrayList<>();
        }
        deletedDeviceEnergyRecords.add(record);
    }

    public double getTotalDeletedDeviceEnergyForCurrentMonth() {
        if (deletedDeviceEnergyRecords == null || deletedDeviceEnergyRecords.isEmpty()) {
            return 0.0;
        }

        LocalDateTime now = LocalDateTime.now();
        String currentMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());

        return deletedDeviceEnergyRecords.stream()
                .filter(record -> currentMonth.equals(record.getDeletionMonth()))
                .mapToDouble(DeletedDeviceEnergyRecord::getTotalEnergyConsumedKWh)
                .sum();
    }

    @DynamoDbAttribute("devicePermissions")
    public List<DevicePermission> getDevicePermissions() {
        if (devicePermissions == null) {
            devicePermissions = new ArrayList<>();
        }
        return devicePermissions;
    }

    public void setDevicePermissions(List<DevicePermission> devicePermissions) {
        this.devicePermissions = devicePermissions != null ? devicePermissions : new ArrayList<>();
    }

    /**
     * Grant permission for a specific device to a group member
     */
    public boolean grantDevicePermission(String memberEmail, String deviceType, String roomName, String grantedBy) {
        // Check if device exists
        Gadget device = findGadget(deviceType, roomName);
        if (device == null) {
            return false;
        }

        // Check if permission already exists
        if (hasDevicePermission(memberEmail, deviceType, roomName)) {
            return false; // Permission already exists
        }

        // Create new permission
        DevicePermission permission = new DevicePermission(memberEmail, deviceType, roomName, this.email, grantedBy);
        getDevicePermissions().add(permission);
        return true;
    }

    /**
     * Revoke permission for a specific device from a group member
     */
    public boolean revokeDevicePermission(String memberEmail, String deviceType, String roomName) {
        return getDevicePermissions().removeIf(permission ->
            permission.getMemberEmail().equalsIgnoreCase(memberEmail) &&
            permission.matchesDevice(deviceType, roomName, this.email));
    }

    /**
     * Check if a member has permission for a specific device
     */
    public boolean hasDevicePermission(String memberEmail, String deviceType, String roomName) {
        return getDevicePermissions().stream()
            .anyMatch(permission ->
                permission.getMemberEmail().equalsIgnoreCase(memberEmail) &&
                permission.matchesDevice(deviceType, roomName, this.email));
    }

    /**
     * Get all devices that a specific member has permission to access
     */
    public List<DevicePermission> getPermissionsForMember(String memberEmail) {
        return getDevicePermissions().stream()
            .filter(permission -> permission.getMemberEmail().equalsIgnoreCase(memberEmail))
            .toList();
    }

    /**
     * Get all devices that this user can access from other group members (based on permissions granted to this user)
     * This method checks what devices OTHER users have given THIS user permission to access
     */
    public List<Gadget> getAccessibleGroupDevices(List<Customer> groupMembers) {
        List<Gadget> accessibleDevices = new ArrayList<>();

        for (Customer member : groupMembers) {
            if (member.getEmail().equalsIgnoreCase(this.email)) {
                continue; // Skip self
            }

            // Check what permissions this member has granted to current user
            List<DevicePermission> permissionsForMe = member.getPermissionsForMember(this.email);

            for (DevicePermission permission : permissionsForMe) {
                if (permission.isCanView()) {
                    Gadget device = member.findGadget(permission.getDeviceType(), permission.getRoomName());
                    if (device != null) {
                        accessibleDevices.add(device);
                    }
                }
            }
        }

        return accessibleDevices;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gadgetsCount=" + (gadgets != null ? gadgets.size() : 0) +
                ", failedAttempts=" + failedLoginAttempts +
                ", isLocked=" + isAccountLocked() +
                '}';
    }
}