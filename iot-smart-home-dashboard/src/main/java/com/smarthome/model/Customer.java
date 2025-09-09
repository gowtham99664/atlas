package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

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
    private String groupCreator; // Email of the person who created the group (admin)
    
    private int failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime lastFailedLoginTime;
    
    
    public Customer() {
        this.gadgets = new ArrayList<>();
        this.groupMembers = new ArrayList<>();
        this.groupCreator = null;
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