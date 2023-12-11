package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.Items;
import com.draw.suckhoe.model.Titles;
import com.draw.suckhoe.repository.TitleRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemViewModel extends ViewModel {

    private final TitleRepository repository;
    private final MutableLiveData<List<Items>> liveData = new MutableLiveData<>();

    public ItemViewModel(Application application)
    {
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new TitleRepository(healthDB);
    }

    public MutableLiveData<List<Items>> getLiveData() {
        return liveData;
    }

    public void getAllData(int titleId)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try{
                List<Items> list = repository.getAllDataItem(titleId);
                liveData.postValue(list);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        });
    }
}
