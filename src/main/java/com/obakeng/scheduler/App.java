package com.obakeng.scheduler;

import com.obakeng.scheduler.model.CalendarModel;
import com.obakeng.scheduler.ui.CalendarView;
import javafx.application.Application;
import javafx.scene.Scene;  
import javafx.stage.Stage;

/**
 * Entry point of the Scheduler Application.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        CalendarModel calendarModel = new CalendarModel();
        CalendarView calendarView = new CalendarView(calendarModel);

        Scene scene = new Scene(calendarView, 800, 600);

        // Attach application stylesheet (placed in src/main/resources/styles.css)
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception ignored) {
            // If the stylesheet isn't found at runtime, continue without it.
        }

        primaryStage.setTitle("Scheduler Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Standard Java entry point.
     * Delegates to the JavaFX runtime which will eventually call start().
     * @param args
    */
    public static void main(String[] args) {
        launch(args);
    }
}