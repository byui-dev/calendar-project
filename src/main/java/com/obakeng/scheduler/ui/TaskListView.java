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
import javafx.scene.text.Text;
import javafx.beans.binding.BooleanBinding;

import java.util.function.Consumer;

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

        // Custom cell factory for styling. Use a Text node and CSS classes
        // instead of inline styles so styling is centralized in a stylesheet.
        listView.setCellFactory(lv -> new ListCell<Task>() {
            private final Text text = new Text();

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    text.setText(task.toString());
                    // Ensure the style class is kept in sync with completion state
                    text.getStyleClass().remove("task-completed");
                    if (task.isCompleted()) {
                        text.getStyleClass().add("task-completed");
                    }
                    setText(null); // we use graphic instead
                    setGraphic(text);
                }
            }
        });

        ContextMenu contextMenu = new ContextMenu();

        // Helper to perform an action on the selected task and refresh view
        // Reduces duplication for mark/delete actions.
        Consumer<Task> refreshingAction = t -> showTasks(currentDate);

        MenuItem markCompleted = new MenuItem("Mark Completed");
        markCompleted.setOnAction(e -> operateOnSelectedTask(t -> {
            model.markTaskCompleted(currentDate, t);
            refreshingAction.accept(t);
        }));

        MenuItem markIncomplete = new MenuItem("Mark Incomplete");
        markIncomplete.setOnAction(e -> operateOnSelectedTask(t -> {
            model.markTaskIncomplete(currentDate, t);
            refreshingAction.accept(t);
        }));

        MenuItem deleteTask = new MenuItem("Delete Task");
        deleteTask.setOnAction(e -> {
            Task sel = listView.getSelectionModel().getSelectedItem();
            if (sel == null || currentDate == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Are you sure you want to delete this task?");
            alert.setContentText(sel.toString());
            // If possible, set the owner so dialogs are centered on the app window
            if (getScene() != null && getScene().getWindow() != null) {
                alert.initOwner(getScene().getWindow());
            }
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                model.deleteTask(currentDate, sel);
                showTasks(currentDate);
            }
        });

        // Disable menu items when nothing is selected
        BooleanBinding noSelection = listView.getSelectionModel().selectedItemProperty().isNull();
        markCompleted.disableProperty().bind(noSelection);
        markIncomplete.disableProperty().bind(noSelection);
        deleteTask.disableProperty().bind(noSelection);

        // Add all three menu items
        contextMenu.getItems().addAll(markCompleted, markIncomplete, deleteTask);
        listView.setContextMenu(contextMenu);
    }

    public void showTasks(LocalDate date) {
        currentDate = date;
        // Replace the list contents with tasks for the selected date.
        // For a future improvement we could expose an ObservableList from the model
        // and bind directly to it so changes are reflected automatically.
        listView.getItems().setAll(model.getTasks(date));
    }

    public void setDate(LocalDate date) {
        showTasks(date);
    }

    // Helper to run an action on the currently selected task and refresh view
    private void operateOnSelectedTask(Consumer<Task> action) {
        Task sel = listView.getSelectionModel().getSelectedItem();
        if (sel != null && currentDate != null) {
            action.accept(sel);
            showTasks(currentDate);
        }
    }
}
