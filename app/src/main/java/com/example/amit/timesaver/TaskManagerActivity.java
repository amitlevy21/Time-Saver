package com.example.amit.timesaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.List;

public class TaskManagerActivity extends BaseActivity {

    private static final int REQUEST_ADD_TASK = 0;
    private TaskManager taskManager;
    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;

    private Semester semesterRelated;

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


        //to delete
        semesterRelated = new Semester(1994, new MyDate(1994,12,28), new MyDate(1995,12,28), Semester.eSemesterType.A);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.task_manager_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTask = new Intent(getApplicationContext(), AddTaskActivity.class);
                addTask.putExtra(Keys.SEMESTER, semesterRelated);
                startActivityForResult(addTask, REQUEST_ADD_TASK);
            }
        });

        //after we add a task need to call
        //tasksAdapter.notifyDataSetChanged();
        testRecyclerView();

    }


    // TODO: 10/25/2017 Remove method and build dialog that takes input and a fab button maybe
    /** test method */
    public void testRecyclerView() {

    for (int i = 0; i < 20; i++) {
        Course c = new Course("course" + i, semesterRelated);
        Task taskToAdd = new Task(c, new MyDate(1994,2,14), "description " + i);
        taskList.add(taskToAdd);
        semesterRelated.addCourse(c);
    }
    tasksAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle taskAddedBundle = data.getExtras();
        Task task =(Task) taskAddedBundle.getSerializable(Keys.TASK_ADDED);
        taskList.add(task);
        tasksAdapter.notifyDataSetChanged();
    }
}
