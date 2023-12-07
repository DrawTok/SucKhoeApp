package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;

import com.draw.suckhoe.model.Items;

@Dao
public interface ItemsDAO {

    @Insert
    void insertItems(Items items);
}
