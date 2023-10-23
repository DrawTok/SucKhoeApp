package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.BloodGlucose;

import java.util.List;

@Dao
public interface BloodGlucoseDAO {

    @Insert
    void insertLevelBGlucose(BloodGlucose bloodGlucose);

    @Delete
    void deleteBGlucose(BloodGlucose bloodGlucose);

    @Query("SELECT * FROM bloodGlucose")
    List<BloodGlucose> getAllRecordBGlucose();

    @Query("SELECT * FROM bloodGlucose ORDER BY time DESC LIMIT 4")
    List<BloodGlucose> getLatestRecordBGlucose();
}
