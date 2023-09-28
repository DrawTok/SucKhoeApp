package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.Weight;

import java.util.List;
@Dao
public interface WeightDAO {

    @Insert
    void insertWeight(Weight weight);

    @Query("SELECT * FROM weightBMI")
    List<Weight> getAllRecordWeight();
}
