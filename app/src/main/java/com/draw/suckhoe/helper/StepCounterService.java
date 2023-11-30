package com.draw.suckhoe.helper;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int stepCount = 0;

    private static final float ALPHA = 0.8f;
    private static final float STEP_THRESHOLD = 6.0f;

    private final float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = ALPHA;
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linearAcceleration[0] = event.values[0] - gravity[0];
        linearAcceleration[1] = event.values[1] - gravity[1];
        linearAcceleration[2] = event.values[2] - gravity[2];
        if (isStepDetected(linearAcceleration)) {
            stepCount++;
            broadcastStepCount(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void broadcastStepCount(int count) {
        Intent intent = new Intent("stepCount");
        intent.putExtra("count", count);
        sendBroadcast(intent);
    }

    private boolean isStepDetected(float[] values) {
        // Tính tổng gia tốc
        float magnitude = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);

        // Kiểm tra xem tổng gia tốc có vượt qua ngưỡng không
        return magnitude > STEP_THRESHOLD;
    }
}
