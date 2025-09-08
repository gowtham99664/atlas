package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@DynamoDbBean
public class Gadget {
    
    public enum GadgetType {
        TV, AC, FAN, ROBO_VAC_MOP
    }
    
    public enum GadgetStatus {
        ON, OFF
    }
    
    private String type;
    private String model;
    private String roomName;
    private String status;
    private double powerRatingWatts;
    private LocalDateTime lastOnTime;
    private LocalDateTime lastOffTime;
    private long totalUsageMinutes;
    private double totalEnergyConsumedKWh;
    private LocalDateTime scheduledOnTime;
    private LocalDateTime scheduledOffTime;
    private boolean timerEnabled;
    
    public Gadget() {
        this.status = GadgetStatus.OFF.name();
        this.powerRatingWatts = 0.0;
        this.totalUsageMinutes = 0L;
        this.totalEnergyConsumedKWh = 0.0;
        this.timerEnabled = false;
    }
    
    public Gadget(String type, String model, String roomName) {
        this.type = type;
        this.model = model;
        this.roomName = roomName;
        this.status = GadgetStatus.OFF.name();
        this.powerRatingWatts = getDefaultPowerRating(type);
        this.totalUsageMinutes = 0L;
        this.totalEnergyConsumedKWh = 0.0;
        this.timerEnabled = false;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
        if (this.powerRatingWatts == 0.0) {
            this.powerRatingWatts = getDefaultPowerRating(type);
        }
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isOn() {
        return GadgetStatus.ON.name().equals(this.status);
    }
    
    public void turnOn() {
        if (!isOn()) {
            this.lastOnTime = LocalDateTime.now();
            updateUsageAndEnergy();
        }
        this.status = GadgetStatus.ON.name();
    }
    
    public void turnOff() {
        if (isOn()) {
            this.lastOffTime = LocalDateTime.now();
            updateUsageAndEnergy();
        }
        this.status = GadgetStatus.OFF.name();
    }
    
    public void toggleStatus() {
        if (isOn()) {
            turnOff();
        } else {
            turnOn();
        }
    }
    
    private void updateUsageAndEnergy() {
        if (lastOnTime != null && isOn()) {
            long minutesUsed = ChronoUnit.MINUTES.between(lastOnTime, LocalDateTime.now());
            totalUsageMinutes += minutesUsed;
            
            double hoursUsed = minutesUsed / 60.0;
            double energyUsed = (powerRatingWatts / 1000.0) * hoursUsed;
            totalEnergyConsumedKWh += energyUsed;
        }
    }
    
    private static double getDefaultPowerRating(String deviceType) {
        switch (deviceType.toUpperCase()) {
            case "TV": return 150.0;
            case "AC": return 1500.0;
            case "FAN": return 75.0;
            case "LIGHT": return 60.0;
            case "SPEAKER": return 30.0;
            case "AIR_PURIFIER": return 45.0;
            case "THERMOSTAT": return 5.0;
            case "SWITCH": return 2.0;
            case "CAMERA": return 15.0;
            case "DOOR_LOCK": return 12.0;
            case "DOORBELL": return 8.0;
            case "REFRIGERATOR": return 200.0;
            case "MICROWAVE": return 1200.0;
            case "WASHING_MACHINE": return 500.0;
            case "GEYSER": return 2000.0;
            case "WATER_PURIFIER": return 25.0;
            case "VACUUM": return 1400.0;
            default: return 50.0;
        }
    }
    
    public double getPowerRatingWatts() {
        return powerRatingWatts;
    }
    
    public void setPowerRatingWatts(double powerRatingWatts) {
        this.powerRatingWatts = powerRatingWatts;
    }
    
    public LocalDateTime getLastOnTime() {
        return lastOnTime;
    }
    
    public void setLastOnTime(LocalDateTime lastOnTime) {
        this.lastOnTime = lastOnTime;
    }
    
    public LocalDateTime getLastOffTime() {
        return lastOffTime;
    }
    
    public void setLastOffTime(LocalDateTime lastOffTime) {
        this.lastOffTime = lastOffTime;
    }
    
    public long getTotalUsageMinutes() {
        return totalUsageMinutes;
    }
    
    public void setTotalUsageMinutes(long totalUsageMinutes) {
        this.totalUsageMinutes = totalUsageMinutes;
    }
    
    public double getTotalEnergyConsumedKWh() {
        return totalEnergyConsumedKWh;
    }
    
    public void setTotalEnergyConsumedKWh(double totalEnergyConsumedKWh) {
        this.totalEnergyConsumedKWh = totalEnergyConsumedKWh;
    }
    
    public LocalDateTime getScheduledOnTime() {
        return scheduledOnTime;
    }
    
    public void setScheduledOnTime(LocalDateTime scheduledOnTime) {
        this.scheduledOnTime = scheduledOnTime;
    }
    
    public LocalDateTime getScheduledOffTime() {
        return scheduledOffTime;
    }
    
    public void setScheduledOffTime(LocalDateTime scheduledOffTime) {
        this.scheduledOffTime = scheduledOffTime;
    }
    
    public boolean isTimerEnabled() {
        return timerEnabled;
    }
    
    public void setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
    }
    
    public String getUsageTimeFormatted() {
        long hours = totalUsageMinutes / 60;
        long minutes = totalUsageMinutes % 60;
        return String.format("%dh %02dm", hours, minutes);
    }
    
    public double getCurrentSessionUsageHours() {
        if (isOn() && lastOnTime != null) {
            long currentMinutes = ChronoUnit.MINUTES.between(lastOnTime, LocalDateTime.now());
            return currentMinutes / 60.0;
        }
        return 0.0;
    }
    
    public double getCurrentTotalEnergyConsumedKWh() {
        double baseEnergy = totalEnergyConsumedKWh;
        if (isOn() && lastOnTime != null) {
            double currentSessionHours = getCurrentSessionUsageHours();
            double currentSessionEnergy = (powerRatingWatts / 1000.0) * currentSessionHours;
            baseEnergy += currentSessionEnergy;
        }
        return baseEnergy;
    }
    
    public long getCurrentTotalUsageMinutes() {
        long baseUsage = totalUsageMinutes;
        if (isOn() && lastOnTime != null) {
            long currentMinutes = ChronoUnit.MINUTES.between(lastOnTime, LocalDateTime.now());
            baseUsage += currentMinutes;
        }
        return baseUsage;
    }
    
    public String getCurrentUsageTimeFormatted() {
        long totalCurrentMinutes = getCurrentTotalUsageMinutes();
        long hours = totalCurrentMinutes / 60;
        long minutes = totalCurrentMinutes % 60;
        return String.format("%dh %02dm", hours, minutes);
    }
    
    @Override
    public String toString() {
        return String.format("%s %s in %s - %s (%.1fW)", type, model, roomName, status, powerRatingWatts);
    }
    
    public void ensurePowerRating() {
        if (this.powerRatingWatts == 0.0 && this.type != null) {
            this.powerRatingWatts = getDefaultPowerRating(this.type);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Gadget gadget = (Gadget) obj;
        return type.equals(gadget.type) && roomName.equals(gadget.roomName);
    }
    
    @Override
    public int hashCode() {
        return (type + roomName).hashCode();
    }
}