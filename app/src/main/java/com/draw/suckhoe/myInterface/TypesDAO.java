package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;

import com.draw.suckhoe.model.Types;

@Dao
public interface TypesDAO {
    @Insert
    void insertType(Types types);
}
