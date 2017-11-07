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
import java.util.Map;


public class DashboardActivity extends BaseActivity {

    private Dashboard dashboard = Dashboard.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String userID;

    private MyDate day;
    private Calendar calendar;
    private int today = 0, tomorrow = 0, taskDay = 0;
    int tasksForToday = 0, tasksForTomorrow = 0, numOfTasksDone = 0;
    private ArrayList<Task> tasks;


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

        tasks = dashboard.getTasks();
        TaskManager.getInstance().setPendingTasks(tasks);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSemestersFromFireBase();
        updateStats();
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

    private void updateStats() {
        int[] quantityToday = {dashboard.getNumOfTodaysTasksDue(), dashboard.getNumOfTodaysClasses(), dashboard.getNumOfTotalTasksDone()};
        int[] quantityTomorrow = {dashboard.getNumOfTomorrowsTasksDue(), dashboard.getNumOfTomorrowsClasses()};

        TableLayout tableLayoutToday = findViewById(R.id.dashboard_table_today);

        for (int i = 0; i < quantityToday.length; i++) {
            TableRow row = (TableRow) tableLayoutToday.getChildAt(i);
            TextView textView = (TextView) row.getChildAt(1);
            textView.setText(String.valueOf(quantityToday[i]));

        }

        TableLayout tableLayoutTomorrow = findViewById(R.id.dashboard_table_tomorrow);

        for (int i = 0; i < quantityTomorrow.length; i++) {
            TableRow row = (TableRow) tableLayoutTomorrow.getChildAt(i);
            TextView textView = (TextView) row.getChildAt(1);
            textView.setText(String.valueOf(quantityToday[i]));
        }
    }

    private void updateDashboardStats(){
        calendar = Calendar.getInstance();
        today = (calendar.get(Calendar.YEAR)) * (calendar.get(Calendar.MONTH)+1) * (calendar.get(Calendar.DAY_OF_MONTH));
        tomorrow = (calendar.get(Calendar.YEAR)) * (calendar.get(Calendar.MONTH)+1) * (calendar.get(Calendar.DAY_OF_MONTH)+1);

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

        TextView coursesForTodayText = (TextView)findViewById(R.id.dashboard_num_of_classes_today);
        coursesForTodayText.setText("s");

        TextView tasksDoneText = (TextView)findViewById(R.id.dashboard_total_tasks_done);
        tasksDoneText.setText(String.valueOf(numOfTasksDone));

        TextView tasksForTomorrowText = (TextView)findViewById(R.id.dashboard_num_of_tasks_due_tomorrow);
        tasksForTomorrowText.setText(String.valueOf(tasksForTomorrow));

        TextView coursesForTomorrowText = (TextView)findViewById(R.id.dashboard_num_of_classes_tomorrow);
        coursesForTomorrowText.setText("s");

    }
}