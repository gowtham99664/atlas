package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

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
    
    public Gadget() {
        this.status = GadgetStatus.OFF.name();
    }
    
    public Gadget(String type, String model, String roomName) {
        this.type = type;
        this.model = model;
        this.roomName = roomName;
        this.status = GadgetStatus.OFF.name();
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
        this.status = GadgetStatus.ON.name();
    }
    
    public void turnOff() {
        this.status = GadgetStatus.OFF.name();
    }
    
    public void toggleStatus() {
        this.status = isOn() ? GadgetStatus.OFF.name() : GadgetStatus.ON.name();
    }
    
    @Override
    public String toString() {
        return String.format("%s %s in %s - %s", type, model, roomName, status);
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