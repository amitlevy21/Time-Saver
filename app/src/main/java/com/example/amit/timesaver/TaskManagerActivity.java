package com.example.amit.timesaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
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

        buildDrawer();
        DrawerLayout.LayoutParams dlp  = (DrawerLayout.LayoutParams)findViewById(R.id.activity_task_manager).getLayoutParams();
        dlp.setMargins(50,50,50,50);


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

        //add task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTask = new Intent(getApplicationContext(), AddTaskActivity.class);
                addTask.putExtra(Keys.SEMESTER, semesterRelated);
                startActivityForResult(addTask, REQUEST_ADD_TASK);
            }
        });
        //remove task
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (int i = 0; i < taskManager.getNumOfPendingTasks(); i++) {
                    if(taskManager.getPendingTaskAtIndex(i).isDone()) {
                        tasksAdapter.removeItem(i);
                    }
                }

                return true;
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
        Course c = new Course("course" + i);
        Task taskToAdd = new Task(c, new MyDate(1994,2,14), "description " + i);
        taskList.add(taskToAdd);
        semesterRelated.addCourse(c);
        taskManager.addTask(taskToAdd);
    }
    tasksAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) { // data can be null if user pressed back
            Bundle taskAddedBundle = data.getExtras();
            Task task = (Task) taskAddedBundle.getSerializable(Keys.TASK_ADDED);
            taskList.add(task);
            tasksAdapter.notifyDataSetChanged();
            // TODO: 11/2/2017 add to firebase
        }
    }
}
