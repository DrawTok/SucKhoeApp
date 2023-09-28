package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.BloodGlucose;

import java.util.List;

@Dao
public interface BloodGlucoseDAO {

    @Insert
    void insertLevelBGlucose(BloodGlucose bloodGlucose);

    @Query("SELECT * FROM bloodGlucose")
    List<BloodGlucose> getAllRecordBGlucose();
}
