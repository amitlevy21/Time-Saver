package com.example.amit.timesaver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class BaseActivity extends AppCompatActivity {

    protected static final int DASHBOARD_DRAWER_POSITION = 0;
    protected static final int TASK_MANAGER_DRAWER_POSITION = 1;
    protected static final int ADD_SEMESTER_DRAWER_POSITION = 2;
    protected static final int ADD_COURSE_DRAWER_POSITION = 3;
    protected static final int ADD_INSTANCE_DRAWER_POSITION = 4;

    public static final String MY_ACTION = "com.sample.myaction";

    DrawerBuilder drawerBuilder;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(BaseActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BaseActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) BaseActivity.this.getSystemService(BaseActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        drawerBuilder = new DrawerBuilder().withActivity(this)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .withSelectedItem(-1) //no default selection
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Dashboard").withIcon(R.drawable.dashboard_menu_ic).withSelectable(false),
                        new PrimaryDrawerItem().withName("Task Manager").withIcon(R.drawable.task_manager_menu_ic).withSelectable(false),
                        new SecondaryDrawerItem().withName("Add Semester").withIcon(R.drawable.semester_menu_ic).withSelectable(false),
                        new SecondaryDrawerItem().withName("Add Course").withIcon(R.drawable.course_menu_ic).withSelectable(false),
                        new SecondaryDrawerItem().withName("Add Course Instance").withIcon(R.drawable.instance_menu_ic).withSelectable(false)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {

                            Intent intent = null;
                            switch (position) {
                                case DASHBOARD_DRAWER_POSITION: {
                                    intent = new Intent(BaseActivity.this, DashboardActivity.class);
                                    break;
                                }
                                case TASK_MANAGER_DRAWER_POSITION: {
                                    intent = new Intent(BaseActivity.this, TaskManagerActivity.class);
                                    break;
                                }
                                case ADD_SEMESTER_DRAWER_POSITION: {
                                    intent = new Intent(BaseActivity.this, AddSemesterActivity.class);
                                    break;
                                }
                                case ADD_COURSE_DRAWER_POSITION: {
                                    intent = new Intent(BaseActivity.this, AddCourseActivity.class);
                                    break;
                                }
                                case ADD_INSTANCE_DRAWER_POSITION: {
                                    intent = new Intent(BaseActivity.this, AddCourseInstanceActivity.class);
                                    break;
                                }
                            }
                            if (intent != null) {
                                BaseActivity.this.startActivity(intent);
                            }

                        }
                        return false;
                    }
                });


    }

    protected void buildDrawer() {
        drawer = drawerBuilder.build();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}

