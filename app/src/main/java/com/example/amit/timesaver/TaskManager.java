package com.example.amit.timesaver;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by amit on 13/10/17.
 */

public class TaskManager {

    private static TaskManager taskManagerInstance = new TaskManager();
    private ArrayList<Task> pendingTasks = new ArrayList<>();
    private int numOfPendingTasks;

    private TaskManager() {}

    public static TaskManager getInstance() {
        return  taskManagerInstance;
    }

    public Task getPendingTaskAtIndex(int index) {
        return pendingTasks.get(index);
    }

    public int getNumOfPendingTasks() {
        return numOfPendingTasks;
    }

    public void markTaskAsDone(Task task) {
        int index = pendingTasks.indexOf(task);
        pendingTasks.get(index).markAsDone();
    }

    public void markTaskAsUndone(Task task) {
        int index = pendingTasks.indexOf(task);
        pendingTasks.get(index).markAsUndone();
    }

    public void sortTasksByDate() {
       Collections.sort(pendingTasks, new TaskComparator());
    }
}
