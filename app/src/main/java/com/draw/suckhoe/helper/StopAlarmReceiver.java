package com.draw.suckhoe.helper;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StopAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationID = intent.getIntExtra("NOTIFICATION_ID", -1);
        Intent stopAlarmIntent = new Intent(context, ForegroundService.class);
        context.stopService(stopAlarmIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationID);
    }
}
