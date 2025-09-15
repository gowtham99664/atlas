package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.time.LocalDateTime;

@DynamoDbBean
public class DevicePermission {

    private String memberEmail;
    private String deviceType;
    private String roomName;
    private String deviceOwnerEmail;
    private boolean canControl;
    private boolean canView;
    private LocalDateTime grantedAt;
    private String grantedBy;

    public DevicePermission() {
        this.canControl = true;
        this.canView = true;
        this.grantedAt = LocalDateTime.now();
    }

    public DevicePermission(String memberEmail, String deviceType, String roomName,
                           String deviceOwnerEmail, String grantedBy) {
        this.memberEmail = memberEmail;
        this.deviceType = deviceType;
        this.roomName = roomName;
        this.deviceOwnerEmail = deviceOwnerEmail;
        this.grantedBy = grantedBy;
        this.canControl = true;
        this.canView = true;
        this.grantedAt = LocalDateTime.now();
    }

    @DynamoDbAttribute("memberEmail")
    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    @DynamoDbAttribute("deviceType")
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @DynamoDbAttribute("roomName")
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @DynamoDbAttribute("deviceOwnerEmail")
    public String getDeviceOwnerEmail() {
        return deviceOwnerEmail;
    }

    public void setDeviceOwnerEmail(String deviceOwnerEmail) {
        this.deviceOwnerEmail = deviceOwnerEmail;
    }

    @DynamoDbAttribute("canControl")
    public boolean isCanControl() {
        return canControl;
    }

    public void setCanControl(boolean canControl) {
        this.canControl = canControl;
    }

    @DynamoDbAttribute("canView")
    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    @DynamoDbAttribute("grantedAt")
    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }

    @DynamoDbAttribute("grantedBy")
    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    /**
     * Check if this permission matches a specific device
     */
    public boolean matchesDevice(String deviceType, String roomName, String ownerEmail) {
        return this.deviceType.equalsIgnoreCase(deviceType) &&
               this.roomName.equalsIgnoreCase(roomName) &&
               this.deviceOwnerEmail.equalsIgnoreCase(ownerEmail);
    }

    /**
     * Get a unique identifier for this device
     */
    public String getDeviceId() {
        return deviceOwnerEmail + ":" + deviceType + ":" + roomName;
    }

    @Override
    public String toString() {
        return "DevicePermission{" +
                "memberEmail='" + memberEmail + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", roomName='" + roomName + '\'' +
                ", deviceOwnerEmail='" + deviceOwnerEmail + '\'' +
                ", canControl=" + canControl +
                ", canView=" + canView +
                ", grantedBy='" + grantedBy + '\'' +
                '}';
    }
}