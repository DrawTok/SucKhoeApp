package com.draw.suckhoe.myInterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.draw.suckhoe.model.Reminder;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Query("DELETE FROM reminder WHERE idReminder = :id")
    void deleteReminder(int id);

    @Query("SELECT * FROM reminder")
    List<Reminder> getAllReminder();
}
