package com.draw.suckhoe.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.draw.suckhoe.R;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "MyForegroundServiceChannel";
    private int notificationID = -1;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        mediaPlayer = MediaPlayer.create(this, soundUri);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent.getStringExtra("TITLE");
        String message = intent.getStringExtra("MESSAGE");
        notificationID = intent.getIntExtra("NOTIFICATION_ID", -1);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            showNotification(title, message);
        }
        return START_NOT_STICKY;
    }


    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo một intent để mở ứng dụng của bạn khi thông báo được nhấn
        Intent notificationIntent = new Intent(this, DetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);

        Intent stopIntent = new Intent(getApplicationContext(), StopAlarmReceiver.class);
        stopIntent.setAction("STOP_ALARM");
        stopIntent.putExtra("NOTIFICATION_ID", notificationID);

        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, stopIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Action stopAction = new NotificationCompat.Action.Builder(
                R.drawable.baseline_access_alarm_24, "Tắt", stopPendingIntent).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent) // Mở ứng dụng khi thông báo được nhấn
                .addAction(stopAction);

        CharSequence name = "My Notification Channel";
        String description = "Description of My Notification Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
        startForeground(notificationID, builder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
