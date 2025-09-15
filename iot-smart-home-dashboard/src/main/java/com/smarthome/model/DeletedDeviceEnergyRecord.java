package com.smarthome.model;

import java.time.LocalDateTime;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class DeletedDeviceEnergyRecord {

    private String deviceType;
    private String roomName;
    private String deviceModel;
    private double totalEnergyConsumedKWh;
    private long totalUsageMinutes;
    private LocalDateTime deletionTime;
    private LocalDateTime deviceCreationTime;
    private double powerRatingWatts;
    private String deletionMonth; // Format: "YYYY-MM" for monthly grouping

    public DeletedDeviceEnergyRecord() {
    }

    public DeletedDeviceEnergyRecord(Gadget device) {
        this.deviceType = device.getType();
        this.roomName = device.getRoomName();
        this.deviceModel = device.getModel();
        this.totalEnergyConsumedKWh = device.getTotalEnergyConsumedKWh();
        this.totalUsageMinutes = device.getTotalUsageMinutes();
        this.deletionTime = LocalDateTime.now();
        this.powerRatingWatts = device.getPowerRatingWatts();

        // Calculate current session energy if device is currently on
        if (device.isOn() && device.getLastOnTime() != null) {
            double currentSessionHours = device.getCurrentSessionUsageHours();
            double currentSessionEnergy = (device.getPowerRatingWatts() / 1000.0) * currentSessionHours;
            this.totalEnergyConsumedKWh += currentSessionEnergy;
            this.totalUsageMinutes += (long)(currentSessionHours * 60);
        }

        // Set deletion month for easy querying
        this.deletionMonth = deletionTime.getYear() + "-" +
                           String.format("%02d", deletionTime.getMonthValue());
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

    @DynamoDbAttribute("deviceModel")
    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    @DynamoDbAttribute("totalEnergyConsumedKWh")
    public double getTotalEnergyConsumedKWh() {
        return totalEnergyConsumedKWh;
    }

    public void setTotalEnergyConsumedKWh(double totalEnergyConsumedKWh) {
        this.totalEnergyConsumedKWh = totalEnergyConsumedKWh;
    }

    @DynamoDbAttribute("totalUsageMinutes")
    public long getTotalUsageMinutes() {
        return totalUsageMinutes;
    }

    public void setTotalUsageMinutes(long totalUsageMinutes) {
        this.totalUsageMinutes = totalUsageMinutes;
    }

    @DynamoDbAttribute("deletionTime")
    public LocalDateTime getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(LocalDateTime deletionTime) {
        this.deletionTime = deletionTime;
    }

    @DynamoDbAttribute("deviceCreationTime")
    public LocalDateTime getDeviceCreationTime() {
        return deviceCreationTime;
    }

    public void setDeviceCreationTime(LocalDateTime deviceCreationTime) {
        this.deviceCreationTime = deviceCreationTime;
    }

    @DynamoDbAttribute("powerRatingWatts")
    public double getPowerRatingWatts() {
        return powerRatingWatts;
    }

    public void setPowerRatingWatts(double powerRatingWatts) {
        this.powerRatingWatts = powerRatingWatts;
    }

    @DynamoDbAttribute("deletionMonth")
    public String getDeletionMonth() {
        return deletionMonth;
    }

    public void setDeletionMonth(String deletionMonth) {
        this.deletionMonth = deletionMonth;
    }

    public String getFormattedUsageTime() {
        long hours = totalUsageMinutes / 60;
        long minutes = totalUsageMinutes % 60;

        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    @Override
    public String toString() {
        return String.format("DeletedDevice{%s %s in %s, Energy: %.3f kWh, Usage: %s, Deleted: %s}",
                deviceType, deviceModel, roomName, totalEnergyConsumedKWh,
                getFormattedUsageTime(), deletionTime.toString());
    }
}