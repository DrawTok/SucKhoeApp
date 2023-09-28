package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.draw.suckhoe.model.Water;

import java.util.List;
@Dao
public interface WaterDAO {

    @Insert
    void insertAmountOfWater(Water water);

    @Update
    void updateAmountOfWater(Water water);

    @Query("SELECT * FROM water")
    List<Water> getAllRecordWater();
}
