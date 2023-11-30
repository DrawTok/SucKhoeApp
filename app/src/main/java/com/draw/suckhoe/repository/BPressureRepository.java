package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.myInterface.BloodPressureDAO;

import java.util.List;

public class BPressureRepository {
    private final BloodPressureDAO bloodPressureDAO;

    public BPressureRepository(HealthDB healthDB) {
        bloodPressureDAO = healthDB.getBPressureDAO();
    }

    public void insertBPressure(BloodPressure bloodPressure) {
        bloodPressureDAO.insertBPressure(bloodPressure);
    }

    public void deleteBPressure(BloodPressure bloodPressure) {
        bloodPressureDAO.deleteBPressure(bloodPressure);
    }

    public List<BloodPressure> getAllDataBPressure() {
        return bloodPressureDAO.getAllRecordBPressure();
    }

}
