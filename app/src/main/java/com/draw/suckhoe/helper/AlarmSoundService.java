package com.draw.suckhoe.helper;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.draw.suckhoe.R;

public class AlarmSoundService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        mediaPlayer = MediaPlayer.create(this, soundUri);

        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isPlaying)
        {
            mediaPlayer.start();
            isPlaying = true;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isPlaying)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            isPlaying = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
/*
START_STICKY là một trong các giá trị trả về từ phương thức onStartCommand
của một dịch vụ Android. Nó thể hiện một hành vi cho biết dịch vụ nên
được khởi động lại tự động bởi hệ thống Android trong trường hợp nó bị kết thúc.
Khi bạn trả về START_STICKY, dịch vụ sẽ được khởi động lại bởi hệ thống và thử lại
 việc thực thi của nó sau khi nó bị dừng.*/
