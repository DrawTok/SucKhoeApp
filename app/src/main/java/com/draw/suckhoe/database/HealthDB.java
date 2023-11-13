package com.draw.suckhoe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.model.Step;
import com.draw.suckhoe.model.Water;
import com.draw.suckhoe.myInterface.BloodGlucoseDAO;
import com.draw.suckhoe.myInterface.BloodPressureDAO;
import com.draw.suckhoe.myInterface.ReminderDAO;
import com.draw.suckhoe.myInterface.StepDAO;
import com.draw.suckhoe.myInterface.WaterDAO;
import com.draw.suckhoe.myInterface.BMIModelDAO;

@Database(entities = {Step.class, Water.class, BMIModel.class,
                BloodGlucose.class, BloodPressure.class, Reminder.class}, version = 1, exportSchema = false)
public abstract class HealthDB extends RoomDatabase {

    private static HealthDB healthDB;

    public static synchronized HealthDB getInstance(Context context)
    {
        if(healthDB == null) {
            healthDB = Room.databaseBuilder
                    (context, HealthDB.class, "HealthDB").build();
        }
        return healthDB;
    }

    public abstract StepDAO getStepDAO();
    public abstract WaterDAO getWaterDAO();

    public abstract BloodPressureDAO getBPressureDAO();
    public abstract BloodGlucoseDAO getBGlucoseDAO();
    public abstract BMIModelDAO getWeightDAO();
    public abstract ReminderDAO getReminderDAO();
}

/*Phương thức getInstance(Context context):
Phương thức này tạo một đối tượng HealthDB sử dụng
mô hình Singleton để đảm bảo rằng chỉ có một đối tượng
cơ sở dữ liệu duy nhất được tạo ra trong suốt vòng đời của ứng dụng.
Nếu đối tượng đã được tạo, nó sẽ được trả về, ngược lại, nó sẽ được tạo ra
và lưu lại để sử dụng cho các hoạt động khác của ứng dụng.*/
