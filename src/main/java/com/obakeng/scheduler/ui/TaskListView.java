package com.obakeng.scheduler.ui;

import com.obakeng.scheduler.model.Task;
import com.obakeng.scheduler.model.CalendarModel;
import javafx.scene.control.ListView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

/**
 * Panel that displays tasks for a selected day.
 * Integrates with CalendarModel to fetch tasks and mark them completed.
 */
public class TaskListView extends VBox {
    private final CalendarModel model; // Reference to the data model
    private final ListView<Task> listView; // UI component to list tasks
    private LocalDate currentDate; // Tracks which date is being shown

    public TaskListView(CalendarModel model) {
        this.model = model;
        this.listView = new ListView<>();
        this.getChildren().add(listView);

        // Context menu for marking tasks completed
        ContextMenu contextMenu = new ContextMenu();

        // Mark completed action
        MenuItem markCompleted = new MenuItem("Mark Completed");
        markCompleted.setOnAction(e -> {
            Task selectedTask = listView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && currentDate != null) {
                model.markTaskCompleted(currentDate, selectedTask);
                showTasks(currentDate); // refresh view
            }
        });

        // Delete Task option
        MenuItem deleteTask = new MenuItem("Delete Task");
        deleteTask.setOnAction(e -> {
            Task selectedTask = listView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && currentDate != null) {
                model.deleteTask(currentDate, selectedTask);
                showTasks(currentDate); // refresh view
        }

        contextMenu.getItems().addAll(markCompleted, deleteTask);
        listView.setContextMenu(contextMenu);
    }

    // Show tasks for a given date
    public void showTasks(LocalDate date) {
        currentDate = date;
        listView.getItems().clear();
        List<Task> tasks = model.getTasks(date);
        listView.getItems().addAll(tasks);
    }

    // Convenience method if CalendarView calls setDate()
    public void setDate(LocalDate date) {
        showTasks(date);
    }
}
