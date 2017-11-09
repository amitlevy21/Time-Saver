package com.example.amit.timesaver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private int MID = 0;
    private ArrayList<Task> tasks = TaskManager.getInstance().getPendingTasks();
    boolean notify = false;
    private MyDate day;
    private Calendar calendar;
    private int tomorrow1, tomorrow2;
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


        calendar = Calendar.getInstance();
        tomorrow1 = (calendar.get(Calendar.YEAR)) * (calendar.get(Calendar.MONTH)+1) * (calendar.get(Calendar.DAY_OF_MONTH)+1);


        for(int i = 0; i < tasks.size(); i++){
            day = tasks.get(i).getDueDate();
            tomorrow2 = ((day.getDay()) * (day.getMonth()+1) * (day.getYear()));
            if(tomorrow1 == tomorrow2)
                notify = true;
        }


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.logo_in_app)
                .setContentTitle("Don't forget to finish your tasks for tomorrow")
                .setContentText("Click here to enter Dashboard").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent);

        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;
        notify = false;

    }
}


