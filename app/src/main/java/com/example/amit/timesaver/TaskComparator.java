package com.example.amit.timesaver;

import java.util.Comparator;

/**
 * Created by amit on 13/10/17.
 */

public class TaskComparator implements Comparator <Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.compareTo(task2);
    }
}
