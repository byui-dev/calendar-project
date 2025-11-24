package com.obakeng.scheduler.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

/*
 * CalendarModel manages tasks by date and persists them to a JSON file.
 */
public class CalendarModel {
    private Map<LocalDate, List<Task>> tasks = new HashMap<>()
    private static final String FILE_NAME = "tasks.json";
    
    public CalendarModel() {
        loadTasks(); // Load tasks on startup
    }

    // Add a new task and persist immediately
    public void addTask(LocalDate date, Task task) {
        tasks.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
        saveTasks();
    }
    
    // Get tasks for a given date
    public List<Task> getTasks(LocalDate date) {
        return tasks.getOrDefault(date, new ArrayList<>());
    }

    // Mark a task completed and persist
    public void markTaskCompleted(LocalDate date, Task task) {
        task.setCompleted(true);
        saveTasks();
    }

    // Save tasks to JSON file
    private void saveTasks() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new Gsonbuilder().setPrettyPrinting().create();
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Load tasks from JSON file
    public void loadTasks() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<LocalDate, List<Task>>>() {
            }.getType();
            tasks = gson.fromJson(reader, type);

            if (tasks == null) {
                tasks = new HashMap<>();
            }
        } catch (IOException e) {
            tasks = new HashMap<>(); // start fresh if file missing
        }
    }
    
    // Delete Task
    public void deleteTask(LocalDate date, Task task) {
        List<Task> dayTasks = tasks.get(date);
        if (dayTasks != null) {
            dayTasks.remove(task);
            if (dayTasks.isEmpty()) {
                tasks.remove(date); // clean up empty date entry
            }
            saveTasks(); // persist changes
        }
    }
}
