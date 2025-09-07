package com.smarthome.service;

import com.smarthome.model.Gadget;
import java.util.Arrays;
import java.util.List;

public class GadgetService {
    
    private static final List<String> VALID_ROOMS = Arrays.asList(
        "Living Room", "Hall", "Drawing Room", "Family Room", "Sitting Room",
        "Master Bedroom", "Bedroom 1", "Bedroom 2", "Bedroom 3", "Guest Room", 
        "Kids Room", "Parents Room", "Childrens Room",
        "Kitchen", "Dining Room", "Breakfast Area", "Pantry", "Store Room",
        "Master Bathroom", "Common Bathroom", "Guest Bathroom", "Powder Room",
        "Balcony", "Terrace", "Garden", "Courtyard", "Entrance", "Porch", 
        "Garage", "Parking", "Utility Room", "Laundry Room",
        "Study Room", "Office Room", "Library", "Home Office",
        "Home Theater", "Entertainment Room", "Prayer Room", "Pooja Room",
        "Servant Room", "Driver Room", "Security Room", "Storage Room",
        "Verandah", "Chowk", "Angan", "Baithak", "Otla"
    );
    
    private static final List<String> VALID_TV_MODELS = Arrays.asList(
        "MI", "Realme", "OnePlus", "TCL", "Samsung", "LG", "Sony", "Panasonic",
        "Haier", "Thomson", "Kodak", "Motorola", "Nokia", "Videocon", "BPL",
        "Micromax", "Intex", "Vu", "Shinco", "Daiwa", "Akai", "Sanyo", "Lloyd",
        "Hitachi", "Toshiba", "Sharp", "Philips", "Iffalcon", "Coocaa", "MarQ"
    );
    
    private static final List<String> VALID_AC_MODELS = Arrays.asList(
        "Voltas", "Blue Star", "Godrej", "Lloyd", "Carrier", "Daikin", "Hitachi",
        "LG", "Samsung", "Panasonic", "Whirlpool", "O General", "Mitsubishi",
        "Haier", "IFB", "Sansui", "Koryo", "MarQ", "Onida", "Kelvinator",
        "Bajaj", "Usha", "Orient", "Maharaja Whiteline", "Hindware", "Havells",
        "Videocon", "BPL", "Electrolux", "Bosch", "Siemens", "Midea", "Gree"
    );
    
    private static final List<String> VALID_FAN_MODELS = Arrays.asList(
        "Havells", "Bajaj", "Usha", "Orient", "Crompton", "Luminous", "Atomberg",
        "Polycab", "Anchor", "Syska", "V-Guard", "Khaitan", "Surya", "Almonard",
        "Maharaja Whiteline", "Hindware", "Rico", "GM", "Standard", "Activa",
        "Singer", "Orpat", "Seema", "Citron", "Lazer", "Finolex", "Replay"
    );
    
    private static final List<String> VALID_LIGHT_MODELS = Arrays.asList(
        "Philips Hue", "MI", "Syska", "Havells", "Wipro", "Bajaj", "Orient",
        "Crompton", "Polycab", "V-Guard", "Anchor", "Luminous", "Eveready",
        "Osram", "Godrej", "Schneider Electric", "Legrand", "Panasonic"
    );
    
    private static final List<String> VALID_SWITCH_MODELS = Arrays.asList(
        "Anchor", "Havells", "Legrand", "Schneider Electric", "Wipro", "Polycab",
        "V-Guard", "Orient", "Syska", "Bajaj", "Godrej", "MI", "Realme",
        "Panasonic", "Philips", "Simon", "Roma", "Crabtree", "MK Electric"
    );
    
    private static final List<String> VALID_CAMERA_MODELS = Arrays.asList(
        "MI", "Realme", "TP-Link", "D-Link", "Hikvision", "CP Plus", "Godrej",
        "Honeywell", "Dahua", "Panasonic", "Sony", "Samsung", "LG", "Zebronics",
        "Imou", "Qubo", "Alert Eyes", "Digisol", "Reolink", "Kent Cam"
    );
    
    private static final List<String> VALID_LOCK_MODELS = Arrays.asList(
        "Godrej", "Yale", "Samsung", "Philips", "MI", "Realme", "Ultraloq",
        "Kaadas", "Harfo", "Dorset", "IPSA", "Atom", "Ozone", "Plantex",
        "Hafele", "Hettich", "Cleveland", "Ebco", "Dorma", "Honeywell"
    );
    
    private static final List<String> VALID_GEYSER_MODELS = Arrays.asList(
        "Bajaj", "Havells", "Crompton", "V-Guard", "Racold", "AO Smith", 
        "Haier", "Whirlpool", "LG", "Samsung", "Godrej", "Orient", "Usha",
        "Maharaja Whiteline", "Hindware", "Venus", "Singer", "Morphy Richards"
    );
    
    private static final List<String> VALID_DOORBELL_MODELS = Arrays.asList(
        "MI", "Realme", "Godrej", "Yale", "Ring", "Honeywell", "CP Plus",
        "Hikvision", "D-Link", "TP-Link", "Qubo", "Alert Eyes", "Zebronics"
    );
    
    private static final List<String> VALID_VACUUM_MODELS = Arrays.asList(
        "MI Robot", "Realme TechLife", "Eureka Forbes", "Kent", "Black+Decker",
        "Karcher", "Philips", "LG", "Samsung", "Inalsa", "American Micronic",
        "Prestige", "Panasonic", "Bosch", "Dyson", "Shark", "Bissell"
    );
    
    private static final List<String> VALID_PURIFIER_MODELS = Arrays.asList(
        "MI", "Realme", "Honeywell", "Philips", "Kent", "Eureka Forbes",
        "Sharp", "LG", "Samsung", "Panasonic", "Godrej", "Havells", "Bajaj",
        "Blue Star", "Crompton", "Orient", "Coway", "Dyson", "IQAir"
    );
    
    private static final List<String> VALID_SPEAKER_MODELS = Arrays.asList(
        "Amazon Echo", "Google Home", "MI", "Realme", "JBL", "Sony", "Philips",
        "Boat", "Zebronics", "Portronics", "Motorola", "Samsung", "LG"
    );
    
    private static final List<String> VALID_WATER_PURIFIER_MODELS = Arrays.asList(
        "Kent", "Aquaguard", "Pureit", "LivPure", "Blue Star", "Havells",
        "Eureka Forbes", "AO Smith", "Faber", "V-Guard", "Godrej", "HUL"
    );
    
    private static final List<String> VALID_THERMOSTAT_MODELS = Arrays.asList(
        "Honeywell", "Johnson Controls", "Schneider Electric", "Siemens",
        "Nest", "Ecobee", "MI", "Realme", "Godrej", "Blue Star", "Carrier"
    );
    
    private static final List<String> VALID_WASHING_MACHINE_MODELS = Arrays.asList(
        "LG", "Samsung", "Whirlpool", "IFB", "Bosch", "Godrej", "Haier",
        "Panasonic", "Videocon", "BPL", "Onida", "Kelvinator", "Lloyd"
    );
    
    private static final List<String> VALID_REFRIGERATOR_MODELS = Arrays.asList(
        "LG", "Samsung", "Whirlpool", "Godrej", "Haier", "Panasonic", "Bosch",
        "IFB", "Videocon", "BPL", "Onida", "Kelvinator", "Lloyd", "Hitachi"
    );
    
    private static final List<String> VALID_MICROWAVE_MODELS = Arrays.asList(
        "LG", "Samsung", "IFB", "Panasonic", "Whirlpool", "Godrej", "Bajaj",
        "Morphy Richards", "Haier", "Bosch", "Onida", "BPL", "Videocon"
    );
    
    public boolean isValidRoom(String roomName) {
        return roomName != null && VALID_ROOMS.stream()
                .anyMatch(room -> room.equalsIgnoreCase(roomName));
    }
    
    public boolean isValidTVModel(String model) {
        return model != null && VALID_TV_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidACModel(String model) {
        return model != null && VALID_AC_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidFanModel(String model) {
        return model != null && VALID_FAN_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidLightModel(String model) {
        return model != null && VALID_LIGHT_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidSwitchModel(String model) {
        return model != null && VALID_SWITCH_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidCameraModel(String model) {
        return model != null && VALID_CAMERA_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidLockModel(String model) {
        return model != null && VALID_LOCK_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidGeyserModel(String model) {
        return model != null && VALID_GEYSER_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidDoorbellModel(String model) {
        return model != null && VALID_DOORBELL_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidVacuumModel(String model) {
        return model != null && VALID_VACUUM_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidPurifierModel(String model) {
        return model != null && VALID_PURIFIER_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidSpeakerModel(String model) {
        return model != null && VALID_SPEAKER_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidWaterPurifierModel(String model) {
        return model != null && VALID_WATER_PURIFIER_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidThermostatModel(String model) {
        return model != null && VALID_THERMOSTAT_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidWashingMachineModel(String model) {
        return model != null && VALID_WASHING_MACHINE_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidRefrigeratorModel(String model) {
        return model != null && VALID_REFRIGERATOR_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidMicrowaveModel(String model) {
        return model != null && VALID_MICROWAVE_MODELS.stream()
                .anyMatch(validModel -> validModel.equalsIgnoreCase(model));
    }
    
    public boolean isValidModel(String type, String model) {
        if (type == null || model == null) {
            return false;
        }
        
        switch (type.toUpperCase()) {
            case "TV":
                return isValidTVModel(model);
            case "AC":
            case "AIR_CONDITIONER":
                return isValidACModel(model);
            case "FAN":
                return isValidFanModel(model);
            case "LIGHT":
            case "SMART_LIGHT":
                return isValidLightModel(model);
            case "SWITCH":
            case "SMART_SWITCH":
                return isValidSwitchModel(model);
            case "CAMERA":
            case "SECURITY_CAMERA":
                return isValidCameraModel(model);
            case "DOOR_LOCK":
            case "SMART_LOCK":
                return isValidLockModel(model);
            case "GEYSER":
            case "WATER_HEATER":
                return isValidGeyserModel(model);
            case "DOORBELL":
            case "SMART_DOORBELL":
                return isValidDoorbellModel(model);
            case "VACUUM":
            case "ROBOTIC_VACUUM":
            case "ROBO_VAC_MOP":
                return isValidVacuumModel(model);
            case "AIR_PURIFIER":
            case "PURIFIER":
                return isValidPurifierModel(model);
            case "SPEAKER":
            case "SMART_SPEAKER":
                return isValidSpeakerModel(model);
            case "WATER_PURIFIER":
                return isValidWaterPurifierModel(model);
            case "THERMOSTAT":
            case "SMART_THERMOSTAT":
                return isValidThermostatModel(model);
            case "WASHING_MACHINE":
                return isValidWashingMachineModel(model);
            case "REFRIGERATOR":
            case "FRIDGE":
                return isValidRefrigeratorModel(model);
            case "MICROWAVE":
            case "MICROWAVE_OVEN":
                return isValidMicrowaveModel(model);
            default:
                return false;
        }
    }
    
    public Gadget createGadget(String type, String model, String roomName) {
        if (!isValidRoom(roomName)) {
            throw new IllegalArgumentException("Invalid room name. Valid rooms: " + VALID_ROOMS);
        }
        
        if (!isValidModel(type, model)) {
            throw new IllegalArgumentException("Invalid model for " + type);
        }
        
        return new Gadget(type.toUpperCase(), model, roomName);
    }
    
    public List<String> getValidRooms() {
        return VALID_ROOMS;
    }
    
    public List<String> getValidModelsForType(String type) {
        if (type == null) {
            return Arrays.asList();
        }
        
        switch (type.toUpperCase()) {
            case "TV":
                return VALID_TV_MODELS;
            case "AC":
            case "AIR_CONDITIONER":
                return VALID_AC_MODELS;
            case "FAN":
                return VALID_FAN_MODELS;
            case "LIGHT":
            case "SMART_LIGHT":
                return VALID_LIGHT_MODELS;
            case "SWITCH":
            case "SMART_SWITCH":
                return VALID_SWITCH_MODELS;
            case "CAMERA":
            case "SECURITY_CAMERA":
                return VALID_CAMERA_MODELS;
            case "DOOR_LOCK":
            case "SMART_LOCK":
                return VALID_LOCK_MODELS;
            case "GEYSER":
            case "WATER_HEATER":
                return VALID_GEYSER_MODELS;
            case "DOORBELL":
            case "SMART_DOORBELL":
                return VALID_DOORBELL_MODELS;
            case "VACUUM":
            case "ROBOTIC_VACUUM":
            case "ROBO_VAC_MOP":
                return VALID_VACUUM_MODELS;
            case "AIR_PURIFIER":
            case "PURIFIER":
                return VALID_PURIFIER_MODELS;
            case "SPEAKER":
            case "SMART_SPEAKER":
                return VALID_SPEAKER_MODELS;
            case "WATER_PURIFIER":
                return VALID_WATER_PURIFIER_MODELS;
            case "THERMOSTAT":
            case "SMART_THERMOSTAT":
                return VALID_THERMOSTAT_MODELS;
            case "WASHING_MACHINE":
                return VALID_WASHING_MACHINE_MODELS;
            case "REFRIGERATOR":
            case "FRIDGE":
                return VALID_REFRIGERATOR_MODELS;
            case "MICROWAVE":
            case "MICROWAVE_OVEN":
                return VALID_MICROWAVE_MODELS;
            default:
                return Arrays.asList();
        }
    }
    
    public String getFormattedValidRooms() {
        return String.join(", ", VALID_ROOMS);
    }
    
    public String getFormattedValidModels(String type) {
        List<String> models = getValidModelsForType(type);
        return models.isEmpty() ? "No valid models" : String.join(", ", models);
    }
}