package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.myInterface.BMIModelDAO;

import java.util.List;

public class BMIRepository {
    private final BMIModelDAO BMIModelDAO;

    public BMIRepository(HealthDB healthDB) {
        this.BMIModelDAO = healthDB.getWeightDAO();
    }

    public void insertDataBMI(BMIModel BMIModel)
    {
        BMIModelDAO.insertDataBMI(BMIModel);
    }

    public void deleteDataBMI(BMIModel BMIModel)
    {
        BMIModelDAO.deleteDataBMI(BMIModel);
    }

    public List<BMIModel> getAllDataBGlucose()
    {
        return BMIModelDAO.getAllRecordBMI();
    }
}
