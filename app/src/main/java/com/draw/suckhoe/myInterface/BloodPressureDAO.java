package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.BloodPressure;

import java.util.List;

@Dao
public interface BloodPressureDAO {

    @Insert
    void insertBPressure(BloodPressure bloodPressure);

    @Query("SELECT * FROM bloodPressure")
    List<BloodPressure> getAllRecordBPressure();
}
