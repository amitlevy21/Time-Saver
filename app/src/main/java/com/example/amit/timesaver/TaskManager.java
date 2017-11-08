package com.example.amit.timesaver;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by amit on 13/10/17.
 */

public class TaskManager {

    private static TaskManager taskManagerInstance = new TaskManager();
    private ArrayList<Task> pendingTasks = new ArrayList<>();

    private TaskManager() {}

    public static TaskManager getInstance() {
        return  taskManagerInstance;
    }

    public ArrayList<Task> getPendingTasks() {
        return pendingTasks;
    }

    public Task getPendingTaskAtIndex(int index) {
        return pendingTasks.get(index);
    }

    public int getNumOfPendingTasks() {
        return pendingTasks.size();
    }

    public void setPendingTasks(ArrayList<Task> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public void markTaskAsDone(Task task) {
        int index = pendingTasks.indexOf(task);
        pendingTasks.get(index).markAsDone();
    }

    public void markTaskAsUndone(Task task) {
        int index = pendingTasks.indexOf(task);
        pendingTasks.get(index).markAsUndone();
    }

    public void addTask(Task task) {
        pendingTasks.add(task);
    }

    public void sortTasksByDate() {
       Collections.sort(pendingTasks, new TaskComparator());
    }
}
