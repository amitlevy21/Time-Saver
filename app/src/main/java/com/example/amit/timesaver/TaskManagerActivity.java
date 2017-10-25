package com.example.amit.timesaver;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class TaskManagerActivity extends BaseActivity {

    private TaskManager taskManager;
    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;

    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskManager = TaskManager.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.task_manager_recycler_view);
        taskList = taskManager.getPendingTasks();
        tasksAdapter = new TasksAdapter(taskList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tasksAdapter);

        //after we add a task need to call
        //tasksAdapter.notifyDataSetChanged();
        testRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Task task = taskList.get(position);
                // TODO: 10/25/2017 call dialog to edit task
                Toast.makeText(getApplicationContext(), task.getDescription() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    // TODO: 10/25/2017 Remove method and build dialog that takes input and a fab button maybe
    /** test method */
    public void testRecyclerView() {
        for (int i = 0; i < 20; i++) {
            Semester s = new Semester(1994, new MyDate(1994,12,28), new MyDate(1995,12,28), Semester.eSemesterType.A);
            Course c = new Course("course" + i, s);
            Task taskToAdd = new Task(c, new MyDate(1994,2,14), "description " + i);
            taskList.add(taskToAdd);
        }
        tasksAdapter.notifyDataSetChanged();


    }
}
