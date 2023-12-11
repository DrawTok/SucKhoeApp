package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.Titles;

import java.util.List;

@Dao
public interface TitlesDAO {
    @Insert
    void insertTitle(Titles titles);

    @Query("SELECT * FROM titles WHERE typeId = :typeId")
    List<Titles> getAllData(int typeId);
}
