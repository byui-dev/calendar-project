package com.obakeng.scheduler.model;

import java.time.LocalDateTime;

/**
 * Represents a single scheduled task with a title, date/time, priority, and
 * completion status.
 */
public class Task {
    private String title;
    private LocalDateTime dateTime;
    private String priority; // "Low", "Medium", "High"
    private boolean completed; // true if task is done

    // Constructor
    public Task(String title, LocalDateTime dateTime, String priority) {
        this.title = title;
        this.dateTime = dateTime;
        this.priority = priority;
        this.completed = false; // default to not completed
    }

    // Getters
    public String getTitle() {
        return title;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getPriority() {
        return priority;
    }
    
    public boolean isCompleted() {
        return completed;
    } 
        
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return (completed ? "[✓] " : "[ ] ") + title + " (" + priority + ") @ " + dateTime;
    }    
}
