package com.example.amit.timesaver;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DashboardActivity extends BaseActivity {

    private Dashboard dashboard = Dashboard.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String userID;

    private MyDate day;
    private Calendar calendar;
    private int today = 0, tomorrow = 0, taskDay = 0;
    private int tasksForToday = 0, tasksForTomorrow = 0, numOfTasksDone = 0, numOfTotalCourses = 0;
    private ArrayList<Task> tasks;

    private ArrayList<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        buildDrawer();

        DrawerLayout.LayoutParams dlp  = (DrawerLayout.LayoutParams)findViewById(R.id.activity_dashboard).getLayoutParams();
        dlp.setMargins(50,50,50,50);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        tasks = TaskManager.getInstance().getPendingTasks();
        TaskManager.getInstance().setPendingTasks(tasks);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        courses = dashboard.getCourses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSemestersFromFireBase();
        updateDashboardStats();

    }

    private void loadSemestersFromFireBase() {
        DatabaseReference semesters = databaseReference.child("users").child(userID).child("semesters");
        semesters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Semester>> t = new GenericTypeIndicator<HashMap<String, Semester>>() {};

                HashMap<String, Semester> semestersFromFireBase = dataSnapshot.getValue(t);

                if (semestersFromFireBase != null) {
                    for (Map.Entry<String, Semester> entry : semestersFromFireBase.entrySet()) {
                        if(!dashboard.getSemesters().contains(entry.getValue()))
                            dashboard.getSemesters().add(entry.getValue());

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference tasksRef = databaseReference.child("users").child(userID).child("tasks");
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, Task>> t = new GenericTypeIndicator<HashMap<String, Task>>() {};

                    HashMap<String, Task> tasksFromFireBase = dataSnapshot.getValue(t);

                    if (tasksFromFireBase != null) {
                        for (Map.Entry<String,Task> entry : tasksFromFireBase.entrySet()) {
                            dashboard.getTasks().add(entry.getValue());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDashboardStats(){
        calendar = Calendar.getInstance();
        today = (calendar.get(Calendar.YEAR)) * (calendar.get(Calendar.MONTH)+1) * (calendar.get(Calendar.DAY_OF_MONTH));
        tomorrow = (calendar.get(Calendar.YEAR)) * (calendar.get(Calendar.MONTH)+1) * (calendar.get(Calendar.DAY_OF_MONTH)+1);

        numOfTotalCourses = courses.size();

        for(int i = 0; i < tasks.size(); i++){
            if(!(tasks.get(i).isDone()) ){
                day = tasks.get(i).getDueDate();
                taskDay = ((day.getDay()) * (day.getMonth() + 1) * (day.getYear()));
                if (today == taskDay) {
                    tasksForToday++;
                }
                else if (tomorrow == taskDay) {
                    tasksForTomorrow++;
                }
            }else numOfTasksDone++;
        }

        TextView tasksForTodayText = (TextView)findViewById(R.id.dashboard_num_of_tasks_due_today);
        tasksForTodayText.setText(String.valueOf(tasksForToday));

        TextView tasksDoneText = (TextView)findViewById(R.id.dashboard_total_tasks_done);
        tasksDoneText.setText(String.valueOf(numOfTasksDone));

        TextView tasksForTomorrowText = (TextView)findViewById(R.id.dashboard_num_of_tasks_due_tomorrow);
        tasksForTomorrowText.setText(String.valueOf(tasksForTomorrow));

        TextView tatalCoursesText = (TextView)findViewById(R.id.dashboard_num_of_classes);
        tatalCoursesText.setText(String.valueOf(numOfTotalCourses));

    }
}