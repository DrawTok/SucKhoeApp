package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.Water;
import com.draw.suckhoe.myInterface.WaterDAO;

import java.util.List;

public class WaterRepository {

    private final WaterDAO waterDAO;

    public WaterRepository(HealthDB healthDB) {
        this.waterDAO = healthDB.getWaterDAO();
    }

    public void insertDataWater(Water water)
    {
        waterDAO.insertAmountOfWater(water);
    }

    public void deleteDataWater(Water water)
    {
        waterDAO.deleteAmountOfWater(water);
    }

    public Water getLatestWater()
    {
        return waterDAO.getLatestWater();
    }

    public List<Water> getAllDataBGlucose()
    {
        return waterDAO.getAllRecordWater();
    }
}
