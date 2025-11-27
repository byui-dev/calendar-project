package com.obakeng.scheduler.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/*
 * CalendarModel manages tasks by date and persists them to a JSON file.
 */
public class CalendarModel {
    private Map<LocalDate, List<Task>> tasks = new HashMap<>();
    private static final String FILE_NAME = "tasks.json";

    // Custom adapter for LocalDate
    static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value.toString()); // ISO-8601 format (e.g. 2025-11-26)
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        } 
    }

    // Custom adapter for LocalDateTime
    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value.toString()); // ISO-8601 format (e.g. 2025-11-27T12:51:33)
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString());
        }
    }

    private final Gson gson;

    public CalendarModel() {
        // Register adapters for LocalDate and LocalDateTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting() // Format JSON nicely
                .create();
        loadTasks(); // Load tasks from file when model is created     
    }
    
    // Add a new task for a given date
    public void addTask(LocalDate date, Task task) {
        tasks.computeIfAbsent(date, d -> new ArrayList<>()).add(task);
        saveTasks();
    }
    
    // Get tasks for a given date
    public List<Task> getTasks(LocalDate date) {
        // Return the list of tasks for the requested date, or an empty list
        // if there are no tasks for that date. Use diamond operator for brevity.
        return tasks.getOrDefault(date, new ArrayList<>());
    }

    // Mark a task as completed
    public void markTaskCompleted(LocalDate date, Task task) {
        task.setCompleted(true);
        saveTasks();
    }
    
    // Mark a task as incomplete
    public void markTaskIncomplete(LocalDate date, Task task) {
        task.setCompleted(false);
        saveTasks();
    }
    
    // Delete a task from a given date
    public void deleteTask(LocalDate date, Task task) {
        List<Task> dayTasks = tasks.get(date);
        if (dayTasks != null) {
            dayTasks.remove(task);
            if (dayTasks.isEmpty()) {
                tasks.remove(date); // Remove date entry if no tasks left
            }
            saveTasks();
        }
    }
    
    // Save tasks to JSON file
    private void saveTasks() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(tasks, writer); // Serialize tasks map to JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load tasks from JSON file
    private void loadTasks() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type type = new TypeToken<Map<LocalDate, List<Task>>>() {}.getType();
            tasks = gson.fromJson(reader, type); // Deserialize JSON back into Map
            if (tasks == null)
                tasks = new HashMap<>(); // Start fresh if file empty
        } catch (IOException e) {
            tasks = new HashMap<>(); // Start fresh if file missing
        }
    }    
}