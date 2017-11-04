package com.example.amit.timesaver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    private int MID = 0;
    private ArrayList<Task> tasks = TaskManager.getInstance().getPendingTasks();
    boolean notify = false;
    private MyDate day;
    public static final String MY_ACTION = "com.sample.myaction";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, DashboardActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        day = new MyDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH+1);

        for(int i = 0; i < tasks.size(); i++){
            if((tasks.get(i).getDueDate().compareTo(day))== 0)
                notify = true;
        }


            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.splash_icon)
                    .setContentTitle("Don't forget to finish you tasks for tomorrow")
                    .setContentText("Events to be Performed").setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            notificationManager.notify(MID, mNotifyBuilder.build());
            MID++;
            notify = false;

        }
    }


