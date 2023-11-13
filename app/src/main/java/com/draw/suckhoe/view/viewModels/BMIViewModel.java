package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BMIModel;
import com.draw.suckhoe.repository.BMIRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BMIViewModel extends ViewModel {

    private final BMIRepository bmiRepository;

    private final MutableLiveData<List<BMIModel>> listLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public BMIViewModel(Application application) {
        HealthDB healthDB = HealthDB.getInstance(application);
        bmiRepository = new BMIRepository(healthDB);
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<List<BMIModel>> getListLiveData() {
        return listLiveData;
    }


    public void insertDataBMI(BMIModel BMIModel)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                bmiRepository.insertDataBMI(BMIModel);
                isSuccess.postValue(true);
            }catch (Exception e)
            {
                e.printStackTrace();
                isSuccess.postValue(false);
            }finally {
                executor.shutdown();
            }
        });
    }

    public void deleteDataBMI(BMIModel BMIModel)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                bmiRepository.deleteDataBMI(BMIModel);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        });
    }

    public void getListDataBMI()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->
        {
            try{
                List<BMIModel> list = bmiRepository.getAllDataBGlucose();
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
