package com.draw.suckhoe.helper;

import static com.draw.suckhoe.utils.MyConstants.NOTIFICATION_ID_BASE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.draw.suckhoe.model.Reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private List<Reminder> reminders = new ArrayList<>();
    private int nextNotificationId = NOTIFICATION_ID_BASE; // Danh sách lời nhắc nhở

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Xử lý khi báo thức kích hoạt
        String title = intent.getStringExtra("TITLE");
        String message = intent.getStringExtra("MESSAGE");

        Intent foregroundServiceIntent = new Intent(context, ForegroundService.class);
        foregroundServiceIntent.putExtra("TITLE", title);
        foregroundServiceIntent.putExtra("MESSAGE", message);
        Log.d("listReminders", reminders.size()+"");
        context.startForegroundService(foregroundServiceIntent);
    }

    public void scheduleReminders(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;
        if (alarmManager != null) {
            for (Reminder reminder : reminders) {
                if(reminder.getIsSwitchOn() == 1){
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("TITLE", "Nhắc nhở");
                    intent.putExtra("MESSAGE", reminder.getNameReminder());
                    intent.putExtra("NOTIFICATION_ID", nextNotificationId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
                    } else {
                        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    long triggerTime = calculateTriggerTime(reminder); // Thời gian kích hoạt của lời nhắc nhở

                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
                nextNotificationId++;
            }
        }
    }

    public void removeScheduler(Reminder reminder)
    {
        reminders.remove(reminder);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_BASE, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private long calculateTriggerTime(Reminder reminder) {
        // Tính thời gian kích hoạt của lời nhắc nhở dựa trên thời gian của lời nhắc nhở
        Calendar calendar = Calendar.getInstance();
        String time = reminder.getTimeReminder();
        Pair<Integer, Integer> timeInt = convertTimeToInt(time);
        calendar.set(Calendar.HOUR_OF_DAY, timeInt.first);
        calendar.set(Calendar.MINUTE, timeInt.second);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private Pair<Integer, Integer> convertTimeToInt(String time)
    {
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        return new Pair<>(hour, minute);
    }

}
