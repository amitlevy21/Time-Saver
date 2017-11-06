package com.example.amit.timesaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TaskManagerActivity extends BaseActivity {

    private static final int REQUEST_ADD_TASK = 0;
    private TaskManager taskManager;
    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;

    private Semester semesterRelated;

    private List<Task> taskList;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference databaseReference;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        buildDrawer();
        DrawerLayout.LayoutParams dlp  = (DrawerLayout.LayoutParams)findViewById(R.id.activity_task_manager).getLayoutParams();
        dlp.setMargins(50,50,50,50);


        mFireBaseDataBase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = mFireBaseDataBase.getReference();

        userID = mAuth.getUid();

        taskManager = TaskManager.getInstance();
        recyclerView = findViewById(R.id.task_manager_recycler_view);
        taskList = taskManager.getPendingTasks();
        tasksAdapter = new TasksAdapter(taskList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tasksAdapter);


        //to delete
        semesterRelated = new Semester(1994, new MyDate(1994,12,28), new MyDate(1995,12,28), Semester.eSemesterType.A);
        FloatingActionButton fab = findViewById(R.id.task_manager_fab);

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference tasksFromFireBase = databaseReference.child("users").child(userID).child("tasks");
        tasksFromFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Task> t  = new GenericTypeIndicator<Task>() {};
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(t);
                    if(!taskList.contains(task)) {
                        taskList.add(task);
                        tasksAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) { // data can be null if user pressed back
            Bundle taskAddedBundle = data.getExtras();
            Task task = (Task) taskAddedBundle.getSerializable(Keys.TASK_ADDED);
            if (!taskList.contains(task)) {
                taskList.add(task);
                tasksAdapter.notifyDataSetChanged();
                databaseReference.child("users").child(userID).child("tasks").push().setValue(task);
            }

        }
    }
}
