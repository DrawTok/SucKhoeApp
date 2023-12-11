package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.Types;

import java.util.List;

@Dao
public interface TypesDAO {
    @Insert
    void insertType(Types types);

    @Query("SELECT * FROM types")
    List<Types> getAllData();
}
