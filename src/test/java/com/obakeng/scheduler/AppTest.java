package com.obakeng.scheduler;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;

/**
 * GUI tests for the App using TestFX.
 * <p>
 * TestFX allows us to launch the JavaFX application in a headless
 * test environment and verify that UI elements appear as expected.
 */
public class AppTest extends ApplicationTest {

    private Stage stage;

    /**
     * Called before each test. Launches the App and sets up the stage.
     * 
     * @param stage the primary stage provided by TestFX
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        new App().start(stage); // Launch the application manually
    }

    /**
     * Verify that the stage (window) is shown after startup.
     */
    @Test
    public void testStageIsShown() {
        assertTrue(stage.isShowing(), "The primary stage should be visible after startup.");
    }

    /**
     * Verify that the label with the expected text is present in the scene.
     * Uses TestFX's lookup API to safely query the label instead of casting.
     */
    @Test
    public void testLabelText() {
        // Query the first label node in the scene
        Label label = lookup(".label").query();

        // Verify that the label text matches the expected greeting
        assertEquals("Hello, Scheduler!", label.getText(), "The label should display the correct greeting text.");
    }

    /**
     * Simulate clicking the button and verify that the label text changes.
     */ 
     @Test
     public void testButtonClickChangesLabel() {
         // Click the button with the text "Click Me"
         clickOn(".button");

         // Query the label again to check its updated text
         Label label = lookup(".label").query();

         // Verify that the label text has changed after the button click
         assertEquals("Button Clicked!", label.getText(), "The label text should change after button click.");
     }
}
