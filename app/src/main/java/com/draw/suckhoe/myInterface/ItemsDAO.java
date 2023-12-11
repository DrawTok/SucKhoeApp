package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.draw.suckhoe.model.Items;

import java.util.List;

@Dao
public interface ItemsDAO {

    @Insert
    void insertItems(Items items);

    @Query("SELECT * FROM items WHERE titleId = :titleId")
    List<Items> getAllData(int titleId);
}
