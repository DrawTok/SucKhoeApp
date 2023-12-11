package com.draw.suckhoe.repository;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.Items;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.model.Types;
import com.draw.suckhoe.myInterface.ItemsDAO;
import com.draw.suckhoe.myInterface.TitlesDAO;
import com.draw.suckhoe.myInterface.TypesDAO;

import java.util.List;

public class TitleRepository {

    private final TitlesDAO titlesDAO;
    private final ItemsDAO itemsDAO;

    public TitleRepository(HealthDB healthDB) {
        this.titlesDAO = healthDB.getTitlesDAO();
        this.itemsDAO = healthDB.getItemsDAO();
    }

    public List<Titles> getAllData(int typeId)
    {
        return titlesDAO.getAllData(typeId);
    }

    public List<Items> getAllDataItem(int titleId)
    {
        return itemsDAO.getAllData(titleId);
    }

}
