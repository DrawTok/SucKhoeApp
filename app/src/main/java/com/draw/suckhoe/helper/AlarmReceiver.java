package com.draw.suckhoe.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.draw.suckhoe.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1; // ID của thông báo

    @Override
    public void onReceive(Context context, Intent intent) {
        // Xử lý khi báo thức kích hoạt
        showNotification(context, "Báo thức", "Đã đến lúc thức dậy!");
        Intent alarmSoundIntent = new Intent(context, AlarmSoundService.class);
        context.startService(alarmSoundIntent);
    }

    public void setAlarm(Context context, long triggerTime) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        }
    }




    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "SucKhoe"; // Sử dụng channelId của kênh thông báo đã tạo
        String channelName = "Theo dõi sức khỏe";

        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        stopIntent.setAction("STOP_ALARM");
        stopIntent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);

        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Action stopAction = new NotificationCompat.Action.Builder
                (R.drawable.baseline_access_alarm_24, "Tắt", stopPendingIntent).build();

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(stopAction)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
