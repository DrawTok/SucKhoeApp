package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Insert;

import com.draw.suckhoe.model.Titles;

@Dao
public interface TitlesDAO {
    @Insert
    void insertTitle(Titles titles);
}
