package com.example.amit.timesaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class BaseActivity extends AppCompatActivity {

    DrawerBuilder drawerBuilder;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);



        drawerBuilder = new DrawerBuilder().withActivity(this)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Dashboard").withIcon(R.drawable.dashboard_menu_ic),
                        new PrimaryDrawerItem().withName("Task Manager").withIcon(R.drawable.task_manager_menu_ic),
                        new SecondaryDrawerItem().withName("Add Semester").withIcon(R.drawable.semester_menu_ic),
                        new SecondaryDrawerItem().withName("Add Course").withIcon(R.drawable.course_menu_ic),
                        new SecondaryDrawerItem().withName("Add Course Instance").withIcon(R.drawable.instance_menu_ic)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {

                            Intent intent = null;
                            switch (position) {
                                case 0: {
                                    intent = new Intent(BaseActivity.this, DashboardActivity.class);
                                    break;
                                }
                                case 1: {
                                    intent = new Intent(BaseActivity.this, TaskManagerActivity.class);
                                    break;
                                }
                                case 2: {
                                    intent = new Intent(BaseActivity.this, AddSemesterActivity.class);
                                    break;
                                }
                                case 3: {
                                    intent = new Intent(BaseActivity.this, AddCourseActivity.class);
                                    break;
                                }
                                case 4: {
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

