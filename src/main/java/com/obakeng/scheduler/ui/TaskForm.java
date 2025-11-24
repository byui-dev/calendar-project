package com.obakeng.scheduler.ui;

import com.obakeng.scheduler.model.Task;
import com.obakeng.scheduler.model.CalendarModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Popup dialog for adding a new task.
 * Integrates with CalendarModel to save tasks.
 */
public class TaskForm {
    private final CalendarModel model; // Reference to the data model

    // Constructor
    public TaskForm(CalendarModel model) {
        this.model = model;
    }

    // Show the form for a given date
    public void show(LocalDate date) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL); // Block until closed
        dialog.setTitle("Add Task");

        // Input fields
        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        DatePicker datePicker = new DatePicker(date); // Defaults to clicked day

        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm (24h format)");

        // Priority dropdown
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("Low", "Medium", "High");
        priorityBox.setValue("Medium"); // default

        // Completion checkbox
        CheckBox completedBox = new CheckBox("Completed");

        // Save button
        Button saveButton = new Button("Save Task");
        saveButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                LocalDate selectedDate = datePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText());
                String priority = priorityBox.getValue();
                boolean completed = completedBox.isSelected();

                // Create and add task
                Task task = new Task(title, LocalDateTime.of(selectedDate, time), priority);
                task.setCompleted(completed);

                model.addTask(selectedDate, task);

                dialog.close();
            } catch (Exception ex) {
                // Handle invalid input gracefully
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please check your values.");
                alert.showAndWait();
            }
        });

        // Layout
        VBox layout = new VBox(10, new Label("Enter Task:"), titleField, datePicker, timeField, new Label("Priority:"),
                priorityBox, completedBox, saveButton);
        layout.setPadding(new Insets(10));

        dialog.setScene(new Scene(layout, 300, 250));
        dialog.showAndWait();
    }
}