package com.obakeng.scheduler.ui;

import com.obakeng.scheduler.model.CalendarModel;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.YearMonth;

/*
 * Main calendar view with month navigation and date selection 
 * Displays a grid of day buttons, navigation controls, and integrates with TaskListView.
 */
public class CalendarView extends BorderPane {
    private final CalendarModel calendarModel; // Reference to the model (stores tasks)
    private YearMonth currentYearMonth; // Tracks which month is displayed
    private final GridPane calendarGrid; // Grid of day buttons
    private final Label monthYearLabel; // Label showing current month/year
    private final TaskListView taskListView; // Panel showing tasks for selected day

    public CalendarView(CalendarModel calendarModel) {
        this.calendarModel = calendarModel;
        this.currentYearMonth = YearMonth.now(); // start with current month
        this.calendarGrid = new GridPane();
        this.monthYearLabel = new Label();
        this.taskListView = new TaskListView(calendarModel);
        

        // Navigation buttons 
        Button prevButton = new Button("<");
        prevButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        // Top bar layout: prev button | month label | next button
        BorderPane topPane = new BorderPane();
        topPane.setLeft(prevButton);
        topPane.setCenter(monthYearLabel);
        topPane.setRight(nextButton);

        // Place components in the BorderPane
        this.setTop(topPane);
        this.setCenter(calendarGrid);
        this.setRight(taskListView); // or setBottom(taskListView) based on design

        updateCalendar(); // Initial calendar setup
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int row = 0;
        int col = dayOfWeekValue % 7; // Start week on Sunday 

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Button dayButton = new Button(String.valueOf(day));

        // On day button click, update task list view
        dayButton.setOnAction(e -> {
            taskListView.showTasks(date); // show existing tasks
            TaskForm form = new TaskForm(calendarModel);
            form.show(date); // open popup to add new task  
            taskListView.showTasks(date); // refresh after adding 
        });
            
        calendarGrid.add(dayButton, col, row);

        col++;
        if (col > 6) { // Move to next row after Saturday
            col = 0;
            row++;
            }
        }
    }
}