package com.draw.suckhoe.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.model.Items;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.model.Step;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.model.Types;
import com.draw.suckhoe.model.Water;
import com.draw.suckhoe.myInterface.BMIModelDAO;
import com.draw.suckhoe.myInterface.BloodGlucoseDAO;
import com.draw.suckhoe.myInterface.BloodPressureDAO;
import com.draw.suckhoe.myInterface.ItemsDAO;
import com.draw.suckhoe.myInterface.ReminderDAO;
import com.draw.suckhoe.myInterface.StepDAO;
import com.draw.suckhoe.myInterface.TitlesDAO;
import com.draw.suckhoe.myInterface.TypesDAO;
import com.draw.suckhoe.myInterface.WaterDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Step.class, Water.class, BMIModel.class,
        BloodGlucose.class, BloodPressure.class, Reminder.class,
        Types.class, Titles.class, Items.class}, version = 1, exportSchema = false)
public abstract class HealthDB extends RoomDatabase {

    private static HealthDB healthDB;
    private static final Executor databaseExecutor = Executors.newSingleThreadExecutor();

    public static synchronized HealthDB getInstance(Context context) {
        if (healthDB == null) {
            healthDB = Room.databaseBuilder(context.getApplicationContext(), HealthDB.class, "HealthDB")
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseExecutor.execute(() -> populateInitialData(context));
                        }
                    })
                    .build();
        }
        return healthDB;
    }

    private static void populateInitialData(Context context) {
        readTypesFile(context);
        readItemsFile(context);
        readTitleFile(context);
    }

    private static void readTitleFile(Context context) {
        try (InputStream is = context.getAssets().open("title.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataTitle = line.split(";");

                Titles titles= new Titles();
                titles.setTitleId(Integer.parseInt(dataTitle[0]));
                titles.setTitleName(dataTitle[1]);
                titles.setImageUrl(dataTitle[2]);
                titles.setTypeId(Integer.parseInt(dataTitle[3]));

                healthDB.getTitlesDAO().insertTitle(titles);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readItemsFile(Context context) {
        try (InputStream is = context.getAssets().open("items.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataItem = line.split(";");

                Items items = new Items();
                items.setItemId(Integer.parseInt(dataItem[0]));
                items.setItemName(dataItem[1]);
                items.setContent(dataItem[2]);
                items.setTitleId(Integer.parseInt(dataItem[3]));

                healthDB.getItemsDAO().insertItems(items);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readTypesFile(Context context) {
        try (InputStream is = context.getAssets().open("types.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");

                Types types = new Types();
                types.setTypeId(Integer.parseInt(data[0]));
                types.setTypeName(data[1]);

                healthDB.getTypesDAO().insertType(types);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Define your DAO methods here
    public abstract StepDAO getStepDAO();

    public abstract WaterDAO getWaterDAO();

    public abstract BloodPressureDAO getBPressureDAO();

    public abstract BloodGlucoseDAO getBGlucoseDAO();

    public abstract BMIModelDAO getWeightDAO();

    public abstract ReminderDAO getReminderDAO();
    public abstract ItemsDAO getItemsDAO();
    public abstract TypesDAO getTypesDAO();
    public abstract TitlesDAO getTitlesDAO();
}


/*Phương thức getInstance(Context context):
Phương thức này tạo một đối tượng HealthDB sử dụng
mô hình Singleton để đảm bảo rằng chỉ có một đối tượng
cơ sở dữ liệu duy nhất được tạo ra trong suốt vòng đời của ứng dụng.
Nếu đối tượng đã được tạo, nó sẽ được trả về, ngược lại, nó sẽ được tạo ra
và lưu lại để sử dụng cho các hoạt động khác của ứng dụng.*/
