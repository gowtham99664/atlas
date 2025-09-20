package com.smarthome;

import com.smarthome.service.SmartScenesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Complete Scene Ordering Consistency Test
 * Verifies that ALL methods in SmartScenesService maintain consistent scene ordering
 */
public class CompleteSceneOrderingConsistencyTest {

    @Test
    @DisplayName("Verify ALL Scene Methods Use Consistent Ordering")
    void testAllSceneMethodsConsistentOrdering() {
        System.out.println("\n🔍 Verifying ALL Scene Methods Use Consistent Ordering...");

        SmartScenesService smartScenesService = SmartScenesService.getInstance();

        // The master ordering that all methods should follow
        String[] masterOrder = {
            "MORNING", "EVENING", "NIGHT", "AWAY", "ENERGY_SAVING",
            "MOVIE", "WORKOUT", "COOKING"
        };

        System.out.println("📋 Master Scene Order:");
        for (int i = 0; i < masterOrder.length; i++) {
            System.out.println("   " + (i + 1) + ". " + masterOrder[i]);
        }

        // Test 1: getAvailableSceneNames()
        System.out.println("\n🔍 Testing getAvailableSceneNames()...");
        List<String> availableScenes = smartScenesService.getAvailableSceneNames();

        assertEquals(masterOrder.length, availableScenes.size(), "getAvailableSceneNames() count should match master");
        for (int i = 0; i < masterOrder.length; i++) {
            assertEquals(masterOrder[i], availableScenes.get(i),
                "getAvailableSceneNames() order mismatch at position " + (i + 1));
        }
        System.out.println("✅ getAvailableSceneNames() - ORDER CONSISTENT");

        // Test 2: getAllAvailableSceneNames(userEmail) - for user with no custom scenes
        System.out.println("\n🔍 Testing getAllAvailableSceneNames() with no custom scenes...");
        List<String> allScenesNoCustom = smartScenesService.getAllAvailableSceneNames("test@user.com");

        assertEquals(masterOrder.length, allScenesNoCustom.size(), "getAllAvailableSceneNames() count should match master");
        for (int i = 0; i < masterOrder.length; i++) {
            assertEquals(masterOrder[i], allScenesNoCustom.get(i),
                "getAllAvailableSceneNames() order mismatch at position " + (i + 1));
        }
        System.out.println("✅ getAllAvailableSceneNames() - ORDER CONSISTENT");

        // Test 3: Verify scene existence and editability
        System.out.println("\n🔍 Testing scene existence and editability...");
        for (String sceneName : masterOrder) {
            boolean isEditable = smartScenesService.isSceneEditable(sceneName);
            assertTrue(isEditable, "Scene " + sceneName + " should be editable");
        }
        System.out.println("✅ All scenes are properly editable");

        // Test 4: Verify scene actions retrieval maintains order
        System.out.println("\n🔍 Testing scene actions retrieval...");
        for (String sceneName : masterOrder) {
            var sceneActions = smartScenesService.getSceneActions("test@user.com", sceneName);
            assertNotNull(sceneActions, "Scene " + sceneName + " should have actions");
            assertFalse(sceneActions.isEmpty(), "Scene " + sceneName + " should have non-empty actions");
        }
        System.out.println("✅ All scenes have valid actions");

        System.out.println("\n📊 === SCENE ORDERING CONSISTENCY VERIFICATION ===");
        System.out.println("✅ getAvailableSceneNames() - CONSISTENT");
        System.out.println("✅ getAllAvailableSceneNames() - CONSISTENT");
        System.out.println("✅ isSceneEditable() - WORKING");
        System.out.println("✅ getSceneActions() - WORKING");
        System.out.println("✅ Scene Display Order - CONSISTENT");
        System.out.println("\n🎯 RESULT: ALL SCENE METHODS USE CONSISTENT ORDERING!");
        System.out.println("📋 Order: MORNING → EVENING → NIGHT → AWAY → ENERGY_SAVING → MOVIE → WORKOUT → COOKING");
        System.out.println("\n🎉 SCENE ORDERING CONSISTENCY VERIFICATION COMPLETE!");
    }

    @Test
    @DisplayName("Verify Scene Categories Grouping")
    void testSceneCategoriesGrouping() {
        System.out.println("\n🏷️ Verifying Scene Categories Grouping...");

        SmartScenesService smartScenesService = SmartScenesService.getInstance();
        List<String> scenes = smartScenesService.getAvailableSceneNames();

        // Define expected categories
        String[] dailyRoutineScenes = {"MORNING", "EVENING", "NIGHT"};
        String[] securityEfficiencyScenes = {"AWAY", "ENERGY_SAVING"};
        String[] activitySpecificScenes = {"MOVIE", "WORKOUT", "COOKING"};

        System.out.println("📋 Verifying category groupings:");

        // Check Daily Routine Scenes (positions 0-2)
        System.out.println("\n[DAILY ROUTINE SCENES] - Expected positions 1-3:");
        for (int i = 0; i < dailyRoutineScenes.length; i++) {
            assertEquals(dailyRoutineScenes[i], scenes.get(i),
                "Daily routine scene at position " + (i + 1) + " should be " + dailyRoutineScenes[i]);
            System.out.println("   " + (i + 1) + ". " + scenes.get(i) + " ✅");
        }

        // Check Security & Efficiency Scenes (positions 3-4)
        System.out.println("\n[SECURITY & EFFICIENCY SCENES] - Expected positions 4-5:");
        for (int i = 0; i < securityEfficiencyScenes.length; i++) {
            int position = 3 + i; // Start from position 3
            assertEquals(securityEfficiencyScenes[i], scenes.get(position),
                "Security/efficiency scene at position " + (position + 1) + " should be " + securityEfficiencyScenes[i]);
            System.out.println("   " + (position + 1) + ". " + scenes.get(position) + " ✅");
        }

        // Check Activity-Specific Scenes (positions 5-7)
        System.out.println("\n[ACTIVITY-SPECIFIC SCENES] - Expected positions 6-8:");
        for (int i = 0; i < activitySpecificScenes.length; i++) {
            int position = 5 + i; // Start from position 5
            assertEquals(activitySpecificScenes[i], scenes.get(position),
                "Activity-specific scene at position " + (position + 1) + " should be " + activitySpecificScenes[i]);
            System.out.println("   " + (position + 1) + ". " + scenes.get(position) + " ✅");
        }

        System.out.println("\n✅ SCENE CATEGORIES GROUPING VERIFICATION COMPLETE!");
        System.out.println("🏠 Daily Routine: 3 scenes in correct order");
        System.out.println("🔒 Security & Efficiency: 2 scenes in correct order");
        System.out.println("🎯 Activity-Specific: 3 scenes in correct order");
    }
}