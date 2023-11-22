package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.draw.suckhoe.model.Water;

import java.util.List;
@Dao
public interface WaterDAO {

    @Insert
    void insertAmountOfWater(Water water);

    @Delete
    void deleteAmountOfWater(Water water);

    @Query("SELECT * FROM water ORDER BY id DESC LIMIT 1")
    Water getLatestWater();
    @Query("SELECT * FROM water WHERE time BETWEEN :")
    List<Water> getAllRecordWater();
}
