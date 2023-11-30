package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.myInterface.BloodPressureDAO;
import com.draw.suckhoe.myInterface.ReminderDAO;

import java.util.List;

public class ReminderRepository {
    private final ReminderDAO reminderDAO;

    public ReminderRepository(HealthDB healthDB) {
        reminderDAO = healthDB.getReminderDAO();
    }

    public void insertReminder(Reminder reminder) {
        reminderDAO.insertReminder(reminder);
    }

    public void updateReminder(Reminder reminder)
    {
        reminderDAO.updateReminder(reminder);
    }

    public void deleteReminder(int id)
    {
        reminderDAO.deleteReminder(id);
    }

    public List<Reminder> getAllDataReminder() {
        return reminderDAO.getAllReminder();
    }

}
