package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.myInterface.BloodGlucoseDAO;

import java.util.List;

public class BGlucoseRepository {
    private final BloodGlucoseDAO bloodGlucoseDAO;

    public BGlucoseRepository(HealthDB healthDB) {
        this.bloodGlucoseDAO = healthDB.getBGlucoseDAO();
    }

    public void insertBGlucose(BloodGlucose bloodGlucose)
    {
        bloodGlucoseDAO.insertLevelBGlucose(bloodGlucose);
    }

    public void deleteBGlucose(BloodGlucose bloodGlucose)
    {
        bloodGlucoseDAO.deleteBGlucose(bloodGlucose);
    }

    public List<BloodGlucose> getAllDataBGlucose()
    {
        return bloodGlucoseDAO.getAllRecordBGlucose();
    }

    public List<BloodGlucose> getLatestDataBGlucose()
    {
        return bloodGlucoseDAO.getLatestRecordBGlucose();
    }
}
