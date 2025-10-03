package com.smarthome;

import com.smarthome.service.SmartScenesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SceneOrderingVerificationTest {

    @Test
    @DisplayName("Verify Smart Scenes Display Order")
    void testSmartScenesDisplayOrder() {
        System.out.println("\n Verifying Smart Scenes Display Order...");

        SmartScenesService smartScenesService = SmartScenesService.getInstance();
        List<String> sceneNames = smartScenesService.getAvailableSceneNames();

        String[] expectedOrder = {
            "MORNING", "EVENING", "NIGHT", "AWAY", "ENERGY_SAVING",
            "MOVIE", "WORKOUT", "COOKING"
        };

        System.out.println("Expected Order:");
        for (int i = 0; i < expectedOrder.length; i++) {
            System.out.println("   " + (i + 1) + ". " + expectedOrder[i]);
        }

        System.out.println("\nActual Order:");
        for (int i = 0; i < sceneNames.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + sceneNames.get(i));
        }

        assertEquals(expectedOrder.length, sceneNames.size(), "Scene count should match expected count");

        for (int i = 0; i < expectedOrder.length; i++) {
            assertEquals(expectedOrder[i], sceneNames.get(i),
                "Scene at position " + (i + 1) + " should be " + expectedOrder[i]);
        }

        System.out.println("\n SCENE ORDERING VERIFICATION:");
        System.out.println("   Scene Count: " + sceneNames.size() + " (CORRECT)");
        System.out.println("   Order Match: PERFECT");
        System.out.println("   User Request: FULFILLED");
        System.out.println("\n SMART SCENES ORDERING FIX VERIFIED SUCCESSFULLY!");
    }
}