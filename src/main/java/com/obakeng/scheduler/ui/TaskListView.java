package com.obakeng.scheduler.ui;

import com.obakeng.scheduler.model.Task;
import com.obakeng.scheduler.model.CalendarModel;
import javafx.scene.control.ListView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Panel that displays tasks for a selected day.
 * Integrates with CalendarModel to fetch tasks, mark them completed/incomplete,
 * and delete them with confirmation.
 */
public class TaskListView extends VBox {
    private final CalendarModel model;
    private final ListView<Task> listView;
    private LocalDate currentDate;

    public TaskListView(CalendarModel model) {
        this.model = model;
        this.listView = new ListView<>();
        this.getChildren().add(listView);

        // Custom cell factory for styling
        listView.setCellFactory(lv -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(task.toString());
                    if (task.isCompleted()) {
                         // Gray text + strike-through for completed tasks
                         setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
                    } else {
                        // Normal Style for incomplete tasks
                        setStyle("-fx-text-fill: black; -fx-strikethrough: false;");
                    }
                }
            }
        });

        ContextMenu contextMenu = new ContextMenu();

        // Mark Completed option
        MenuItem markCompleted = new MenuItem("Mark Completed");
        markCompleted.setOnAction(e -> {
            Task selectedTask = listView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && currentDate != null) {
                model.markTaskCompleted(currentDate, selectedTask);
                showTasks(currentDate);
            }
        });

        // Mark Incomplete option
        MenuItem markIncomplete = new MenuItem("Mark Incomplete");
        markIncomplete.setOnAction(e -> {
            Task selectedTask = listView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && currentDate != null) {
                model.markTaskIncomplete(currentDate, selectedTask);
                showTasks(currentDate);
            }
        });

        // Delete Task option with confirmation
        MenuItem deleteTask = new MenuItem("Delete Task");
        deleteTask.setOnAction(e -> {
            Task selectedTask = listView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && currentDate != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Task");
                alert.setHeaderText("Are you sure you want to delete this task?");
                alert.setContentText(selectedTask.toString());

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    model.deleteTask(currentDate, selectedTask);
                    showTasks(currentDate);
                }
            }
        });

        // Add all three menu items
        contextMenu.getItems().addAll(markCompleted, markIncomplete, deleteTask);
        listView.setContextMenu(contextMenu);
    }

    public void showTasks(LocalDate date) {
        currentDate = date;
        listView.getItems().clear();
        List<Task> tasks = model.getTasks(date);
        listView.getItems().addAll(tasks);
    }

    public void setDate(LocalDate date) {
        showTasks(date);
    }
}
