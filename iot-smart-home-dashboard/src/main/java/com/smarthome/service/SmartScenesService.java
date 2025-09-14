package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmartScenesService {
    
    private static SmartScenesService instance;
    private final Map<String, List<SceneAction>> predefinedScenes;
    private final Map<String, Map<String, List<SceneAction>>> userCustomScenes;
    
    private SmartScenesService() {
        this.predefinedScenes = new HashMap<>();
        this.userCustomScenes = new HashMap<>();
        initializePredefinedScenes();
    }
    
    public static synchronized SmartScenesService getInstance() {
        if (instance == null) {
            instance = new SmartScenesService();
        }
        return instance;
    }
    
    public static class SceneAction {
        private String deviceType;
        private String roomName;
        private String action;
        private String description;
        
        public SceneAction(String deviceType, String roomName, String action, String description) {
            this.deviceType = deviceType;
            this.roomName = roomName;
            this.action = action;
            this.description = description;
        }
        
        public String getDeviceType() { return deviceType; }
        public String getRoomName() { return roomName; }
        public String getAction() { return action; }
        public String getDescription() { return description; }

        public void setAction(String action) { this.action = action; }
        public void setDescription(String description) { this.description = description; }
    }
    
    public static class SceneExecutionResult {
        private String sceneName;
        private int totalActions;
        private int successfulActions;
        private int failedActions;
        private List<String> executionLog;
        
        public SceneExecutionResult(String sceneName) {
            this.sceneName = sceneName;
            this.totalActions = 0;
            this.successfulActions = 0;
            this.failedActions = 0;
            this.executionLog = new ArrayList<>();
        }
        
        public void addSuccess(String action) {
            totalActions++;
            successfulActions++;
            executionLog.add("[OK] " + action);
        }
        
        public void addFailure(String action, String reason) {
            totalActions++;
            failedActions++;
            executionLog.add("[FAIL] " + action + " - " + reason);
        }
        
        public String getSceneName() { return sceneName; }
        public int getTotalActions() { return totalActions; }
        public int getSuccessfulActions() { return successfulActions; }
        public int getFailedActions() { return failedActions; }
        public List<String> getExecutionLog() { return executionLog; }
        
        public boolean isFullySuccessful() { return failedActions == 0; }
    }
    
    private void initializePredefinedScenes() {
        // Morning Scene
        List<SceneAction> morningActions = Arrays.asList(
            new SceneAction("LIGHT", "Living Room", "ON", "Turn on living room lights"),
            new SceneAction("LIGHT", "Kitchen", "ON", "Turn on kitchen lights"),
            new SceneAction("GEYSER", "Kitchen", "ON", "Heat water for morning use"),
            new SceneAction("TV", "Living Room", "ON", "Turn on TV for morning news"),
            new SceneAction("AC", "Master Bedroom", "OFF", "Turn off bedroom AC"),
            new SceneAction("MICROWAVE", "Kitchen", "OFF", "Ensure microwave is ready")
        );
        predefinedScenes.put("MORNING", morningActions);
        
        // Evening Scene
        List<SceneAction> eveningActions = Arrays.asList(
            new SceneAction("LIGHT", "Living Room", "ON", "Turn on living room ambiance"),
            new SceneAction("LIGHT", "Master Bedroom", "ON", "Turn on bedroom lights"),
            new SceneAction("TV", "Living Room", "ON", "Turn on entertainment"),
            new SceneAction("AC", "Living Room", "ON", "Cool down after work"),
            new SceneAction("SPEAKER", "Living Room", "ON", "Play evening music"),
            new SceneAction("GEYSER", "Kitchen", "ON", "Prepare hot water for evening")
        );
        predefinedScenes.put("EVENING", eveningActions);
        
        // Night/Sleep Scene
        List<SceneAction> nightActions = Arrays.asList(
            new SceneAction("LIGHT", "Living Room", "OFF", "Turn off living room lights"),
            new SceneAction("LIGHT", "Kitchen", "OFF", "Turn off kitchen lights"),
            new SceneAction("TV", "Living Room", "OFF", "Turn off entertainment"),
            new SceneAction("SPEAKER", "Living Room", "OFF", "Turn off music"),
            new SceneAction("AC", "Master Bedroom", "ON", "Cool bedroom for sleep"),
            new SceneAction("DOOR_LOCK", "Living Room", "ON", "Secure the house"),
            new SceneAction("CAMERA", "Living Room", "ON", "Activate security monitoring")
        );
        predefinedScenes.put("NIGHT", nightActions);
        
        // Away/Security Scene
        List<SceneAction> awayActions = Arrays.asList(
            new SceneAction("LIGHT", "Living Room", "OFF", "Turn off all lights to save energy"),
            new SceneAction("LIGHT", "Kitchen", "OFF", "Turn off kitchen lights"),
            new SceneAction("LIGHT", "Master Bedroom", "OFF", "Turn off bedroom lights"),
            new SceneAction("TV", "Living Room", "OFF", "Turn off all entertainment"),
            new SceneAction("SPEAKER", "Living Room", "OFF", "Turn off speakers"),
            new SceneAction("AC", "Living Room", "OFF", "Save energy while away"),
            new SceneAction("AC", "Master Bedroom", "OFF", "Turn off bedroom AC"),
            new SceneAction("DOOR_LOCK", "Living Room", "ON", "Lock all doors"),
            new SceneAction("CAMERA", "Living Room", "ON", "Activate security cameras"),
            new SceneAction("GEYSER", "Kitchen", "OFF", "Turn off geyser for safety")
        );
        predefinedScenes.put("AWAY", awayActions);
        
        // Movie Night Scene
        List<SceneAction> movieActions = Arrays.asList(
            new SceneAction("TV", "Living Room", "ON", "Turn on entertainment system"),
            new SceneAction("LIGHT", "Living Room", "OFF", "Dim lights for movie ambiance"),
            new SceneAction("AC", "Living Room", "ON", "Ensure comfortable temperature"),
            new SceneAction("SPEAKER", "Living Room", "ON", "Enable surround sound"),
            new SceneAction("MICROWAVE", "Kitchen", "OFF", "Prepare for popcorn later")
        );
        predefinedScenes.put("MOVIE", movieActions);
        
        // Workout Scene
        List<SceneAction> workoutActions = Arrays.asList(
            new SceneAction("FAN", "Living Room", "ON", "Increase air circulation"),
            new SceneAction("SPEAKER", "Living Room", "ON", "Play workout music"),
            new SceneAction("LIGHT", "Living Room", "ON", "Ensure good visibility"),
            new SceneAction("AIR_PURIFIER", "Living Room", "ON", "Clean air during workout"),
            new SceneAction("AC", "Living Room", "OFF", "Use fan instead of AC")
        );
        predefinedScenes.put("WORKOUT", workoutActions);
        
        // Cooking Scene
        List<SceneAction> cookingActions = Arrays.asList(
            new SceneAction("LIGHT", "Kitchen", "ON", "Ensure good kitchen lighting"),
            new SceneAction("AIR_PURIFIER", "Kitchen", "ON", "Keep air clean while cooking"),
            new SceneAction("GEYSER", "Kitchen", "ON", "Ensure hot water availability"),
            new SceneAction("MICROWAVE", "Kitchen", "OFF", "Ready microwave for use")
        );
        predefinedScenes.put("COOKING", cookingActions);
        
        // Energy Saving Scene
        List<SceneAction> energySavingActions = Arrays.asList(
            new SceneAction("LIGHT", "Living Room", "OFF", "Turn off unnecessary lights"),
            new SceneAction("LIGHT", "Kitchen", "OFF", "Save energy in kitchen"),
            new SceneAction("TV", "Living Room", "OFF", "Turn off entertainment"),
            new SceneAction("AC", "Living Room", "OFF", "Use fans instead of AC"),
            new SceneAction("GEYSER", "Kitchen", "OFF", "Turn off water heating"),
            new SceneAction("MICROWAVE", "Kitchen", "OFF", "Turn off appliances")
        );
        predefinedScenes.put("ENERGY_SAVING", energySavingActions);
    }
    
    public SceneExecutionResult executeScene(String sceneName, Customer user, CustomerService customerService) {
        SceneExecutionResult result = new SceneExecutionResult(sceneName);
        
        List<SceneAction> actions = predefinedScenes.get(sceneName.toUpperCase());
        if (actions == null) {
            // Check for custom scenes
            Map<String, List<SceneAction>> userScenes = userCustomScenes.get(user.getEmail());
            if (userScenes != null) {
                actions = userScenes.get(sceneName.toUpperCase());
            }
        }
        
        if (actions == null) {
            result.addFailure("Scene execution", "Scene '" + sceneName + "' not found");
            return result;
        }
        
        for (SceneAction action : actions) {
            try {
                Gadget device = user.findGadget(action.getDeviceType(), action.getRoomName());
                
                if (device == null) {
                    result.addFailure(action.getDescription(), 
                        action.getDeviceType() + " in " + action.getRoomName() + " not found");
                    continue;
                }
                
                boolean currentStatus = device.isOn();
                boolean targetStatus = action.getAction().equalsIgnoreCase("ON");
                
                if (currentStatus == targetStatus) {
                    result.addSuccess(action.getDescription() + " (already in correct state)");
                } else {
                    if (targetStatus) {
                        device.turnOn();
                    } else {
                        device.turnOff();
                    }
                    result.addSuccess(action.getDescription());
                }
                
            } catch (Exception e) {
                result.addFailure(action.getDescription(), "Error: " + e.getMessage());
            }
        }
        
        // Update user data
        try {
            customerService.updateCustomer(user);
        } catch (Exception e) {
            result.addFailure("Save changes", "Failed to save device states");
        }
        
        return result;
    }
    
    public void displaySceneExecutionResult(SceneExecutionResult result) {
        System.out.println("\n=== Scene Execution Report ===");
        System.out.println("Scene: " + result.getSceneName());
        System.out.println("Executed at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println();
        
        System.out.printf("[Summary] %d Total | [OK] %d Success | [FAIL] %d Failed\n", 
                         result.getTotalActions(), result.getSuccessfulActions(), result.getFailedActions());
        System.out.println();
        
        System.out.println("Execution Details:");
        for (String log : result.getExecutionLog()) {
            System.out.println("  " + log);
        }
        
        if (result.isFullySuccessful()) {
            System.out.println("\n[SUCCESS] Scene executed successfully! All devices are now in the desired state.");
        } else if (result.getSuccessfulActions() > 0) {
            System.out.println("\n[WARNING] Scene partially executed. Some devices may need manual adjustment.");
        } else {
            System.out.println("\n[ERROR] Scene execution failed. Please check your devices and try again.");
        }
    }
    
    public void displayAvailableScenes() {
        System.out.println("\n=== Smart Scenes Available ===");
        
        List<String> sceneNames = getAvailableSceneNames();
        
        System.out.println("\n[DAILY ROUTINE SCENES]:");
        System.out.println("1. MORNING - Start your day right");
        System.out.println("   - Turn on lights in living room & kitchen");
        System.out.println("   - Heat water for morning use");
        System.out.println("   - Turn on TV for news");
        
        System.out.println("\n2. EVENING - Relax after work");
        System.out.println("   - Set ambient lighting");
        System.out.println("   - Turn on entertainment & music");
        System.out.println("   - Cool down the house");
        
        System.out.println("\n3. NIGHT - Prepare for sleep");
        System.out.println("   - Turn off all lights except bedroom");
        System.out.println("   - Activate security systems");
        System.out.println("   - Set comfortable sleeping temperature");
        
        System.out.println("\n[SECURITY & EFFICIENCY SCENES]:");
        System.out.println("4. AWAY - Secure and save energy");
        System.out.println("   - Turn off all lights and entertainment");
        System.out.println("   - Activate all security devices");
        System.out.println("   - Enable energy saving mode");
        
        System.out.println("\n5. ENERGY_SAVING - Minimize consumption");
        System.out.println("   - Turn off non-essential devices");
        System.out.println("   - Optimize power usage");
        
        System.out.println("\n[ACTIVITY-SPECIFIC SCENES]:");
        System.out.println("6. MOVIE - Perfect cinema experience");
        System.out.println("   - Dim lights for ambiance");
        System.out.println("   - Turn on entertainment system");
        System.out.println("   - Set comfortable temperature");
        
        System.out.println("\n7. WORKOUT - Exercise environment");
        System.out.println("   - Increase air circulation");
        System.out.println("   - Play energizing music");
        System.out.println("   - Ensure proper lighting");
        
        System.out.println("\n8. COOKING - Kitchen preparation");
        System.out.println("   - Optimize kitchen lighting");
        System.out.println("   - Activate air purification");
        System.out.println("   - Ensure hot water availability");
        
        System.out.println("\n[TIP] Each scene intelligently controls multiple devices with one command!");
    }
    
    public List<String> getAvailableSceneNames() {
        return new ArrayList<>(predefinedScenes.keySet());
    }
    
    public boolean createCustomScene(String userEmail, String sceneName, List<SceneAction> actions) {
        try {
            userCustomScenes.computeIfAbsent(userEmail, k -> new HashMap<>())
                           .put(sceneName.toUpperCase(), actions);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void displaySceneDetails(String sceneName) {
        List<SceneAction> actions = predefinedScenes.get(sceneName.toUpperCase());
        if (actions == null) {
            System.out.println("Scene not found: " + sceneName);
            return;
        }
        
        System.out.println("\n=== " + sceneName.toUpperCase() + " Scene Details ===");
        System.out.println("This scene will perform the following actions:");
        System.out.println();
        
        for (int i = 0; i < actions.size(); i++) {
            SceneAction action = actions.get(i);
            System.out.printf("%d. %s\n", (i + 1), action.getDescription());
            System.out.printf("   Device: %s in %s → %s\n", 
                            action.getDeviceType(), action.getRoomName(), action.getAction());
        }
        
        System.out.println("\n[TIP] This scene coordinates " + actions.size() + " devices for optimal automation!");
    }

    // Scene Editing Methods
    public List<SceneAction> getSceneActions(String userEmail, String sceneName) {
        String sceneKey = sceneName.toUpperCase();

        // Check user custom scenes first
        Map<String, List<SceneAction>> userScenes = userCustomScenes.get(userEmail);
        if (userScenes != null && userScenes.containsKey(sceneKey)) {
            return new ArrayList<>(userScenes.get(sceneKey));
        }

        // Check predefined scenes
        List<SceneAction> predefinedActions = predefinedScenes.get(sceneKey);
        if (predefinedActions != null) {
            return new ArrayList<>(predefinedActions);
        }

        return null;
    }

    public boolean editScene(String userEmail, String sceneName, List<SceneAction> newActions) {
        try {
            String sceneKey = sceneName.toUpperCase();

            // Create or update custom scene
            userCustomScenes.computeIfAbsent(userEmail, k -> new HashMap<>())
                           .put(sceneKey, new ArrayList<>(newActions));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addDeviceToScene(String userEmail, String sceneName, SceneAction newAction) {
        List<SceneAction> actions = getSceneActions(userEmail, sceneName);
        if (actions == null) {
            return false;
        }

        // Check if device already exists in scene
        boolean deviceExists = actions.stream()
                .anyMatch(action -> action.getDeviceType().equals(newAction.getDeviceType()) &&
                                  action.getRoomName().equals(newAction.getRoomName()));

        if (deviceExists) {
            return false; // Device already in scene
        }

        actions.add(newAction);
        return editScene(userEmail, sceneName, actions);
    }

    public boolean removeDeviceFromScene(String userEmail, String sceneName, String deviceType, String roomName) {
        List<SceneAction> actions = getSceneActions(userEmail, sceneName);
        if (actions == null) {
            return false;
        }

        boolean removed = actions.removeIf(action ->
            action.getDeviceType().equals(deviceType) && action.getRoomName().equals(roomName));

        if (removed) {
            return editScene(userEmail, sceneName, actions);
        }

        return false;
    }

    public boolean changeDeviceAction(String userEmail, String sceneName, String deviceType, String roomName, String newAction) {
        List<SceneAction> actions = getSceneActions(userEmail, sceneName);
        if (actions == null) {
            return false;
        }

        for (SceneAction action : actions) {
            if (action.getDeviceType().equals(deviceType) && action.getRoomName().equals(roomName)) {
                action.setAction(newAction);
                String newDescription = generateActionDescription(deviceType, roomName, newAction);
                action.setDescription(newDescription);
                return editScene(userEmail, sceneName, actions);
            }
        }

        return false; // Device not found in scene
    }

    private String generateActionDescription(String deviceType, String roomName, String action) {
        String verb = action.equalsIgnoreCase("ON") ? "Turn on" : "Turn off";
        return String.format("%s %s in %s", verb, deviceType.toLowerCase(), roomName);
    }

    public void displayEditableSceneDetails(String userEmail, String sceneName) {
        List<SceneAction> actions = getSceneActions(userEmail, sceneName);
        if (actions == null) {
            System.out.println("Scene not found: " + sceneName);
            return;
        }

        System.out.println("\n=== " + sceneName.toUpperCase() + " Scene (Editable View) ===");

        // Check if this is a custom scene or modified predefined scene
        Map<String, List<SceneAction>> userScenes = userCustomScenes.get(userEmail);
        boolean isCustom = userScenes != null && userScenes.containsKey(sceneName.toUpperCase());

        if (isCustom) {
            System.out.println("[STATUS] Custom/Modified Scene");
        } else {
            System.out.println("[STATUS] Original Predefined Scene");
        }

        System.out.println("This scene will perform the following actions:");
        System.out.println();

        for (int i = 0; i < actions.size(); i++) {
            SceneAction action = actions.get(i);
            System.out.printf("%d. %s\n", (i + 1), action.getDescription());
            System.out.printf("   Device: %s in %s → %s\n",
                            action.getDeviceType(), action.getRoomName(), action.getAction());
        }

        System.out.println("\n[EDIT OPTIONS] You can:");
        System.out.println("• Add new devices to this scene");
        System.out.println("• Remove existing devices from this scene");
        System.out.println("• Change device actions (ON ↔ OFF)");
        System.out.println("• Reset to original (for predefined scenes)");

        System.out.println("\n[TIP] This scene coordinates " + actions.size() + " devices for optimal automation!");
    }

    public boolean resetSceneToOriginal(String userEmail, String sceneName) {
        String sceneKey = sceneName.toUpperCase();

        // Check if it's a predefined scene
        if (!predefinedScenes.containsKey(sceneKey)) {
            return false; // Cannot reset custom scenes
        }

        // Remove custom version to restore original
        Map<String, List<SceneAction>> userScenes = userCustomScenes.get(userEmail);
        if (userScenes != null) {
            userScenes.remove(sceneKey);
            if (userScenes.isEmpty()) {
                userCustomScenes.remove(userEmail);
            }
        }

        return true;
    }

    public boolean isSceneEditable(String sceneName) {
        // All scenes are editable (predefined scenes become custom when edited)
        return predefinedScenes.containsKey(sceneName.toUpperCase()) ||
               userCustomScenes.values().stream()
                   .anyMatch(scenes -> scenes.containsKey(sceneName.toUpperCase()));
    }

    public List<String> getAllAvailableSceneNames(String userEmail) {
        Set<String> allScenes = new HashSet<>(predefinedScenes.keySet());

        Map<String, List<SceneAction>> userScenes = userCustomScenes.get(userEmail);
        if (userScenes != null) {
            allScenes.addAll(userScenes.keySet());
        }

        return new ArrayList<>(allScenes);
    }
}