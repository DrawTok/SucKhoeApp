package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.Water;
import com.draw.suckhoe.repository.WaterRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WaterViewModel extends ViewModel {
    private final WaterRepository repository;

    private final MutableLiveData<List<Water>> listLiveData = new MutableLiveData<>();

    public WaterViewModel(Application application) {
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new WaterRepository(healthDB);
    }

    public MutableLiveData<List<Water>> getListLiveData() {
        return listLiveData;
    }

    public void insertDataWater(Water water)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                repository.insertDataWater(water);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        });
    }

    public void deleteDataWater(Water water)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                repository.deleteDataWater(water);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        });
    }

    public Water getLatestWater() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Water> future = executor.submit(() -> {
                Water water = null;
                try {
                    water = repository.getLatestWater();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return water;
            });

            // Đợi kết quả từ Future
            Water water = future.get();

            // Đóng executor sau khi sử dụng xong
            executor.shutdown();

            return water;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void getListDataWater(String startDate, String endDate)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                List<Water> list = repository.getAllDataBGlucose(startDate, endDate);
                listLiveData.postValue(list);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        });
    }
}
