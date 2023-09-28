package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.Step;

import java.util.List;

@Dao
public interface StepDAO {
    @Insert
    void insertStep(Step step);

    @Query("SELECT * FROM step")
    List<Step> getAllRecordStep();
}
