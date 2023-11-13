package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.BMIModel;

import java.util.List;
@Dao
public interface BMIModelDAO {

    @Insert
    void insertDataBMI(BMIModel BMIModel);

    @Delete
    void deleteDataBMI(BMIModel BMIModel);

    @Query("SELECT * FROM bmimodel")
    List<BMIModel> getAllRecordBMI();
}
