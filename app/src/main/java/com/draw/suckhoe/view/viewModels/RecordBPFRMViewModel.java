package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.model.LevelResult;
import com.draw.suckhoe.repository.BPressureRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordBPFRMViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectSYS = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectDIA = new MutableLiveData<>();

    private final MutableLiveData<Integer> selectPulse = new MutableLiveData<>();
    private final BPressureRepository repository;
    private final Application application;

    public void setSelectSYS(int valueSYS)
    {
        selectSYS.setValue(valueSYS);
    }

    public LiveData<Integer> getSelectSYS()
    {
        return selectSYS;
    }

    public void setSelectDIA(int valueDIA)
    {
        selectDIA.setValue(valueDIA);
    }

    public LiveData<Integer> getSelectDIA()
    {
        return selectDIA;
    }

    public void setSelectPulse(int valuePulse)
    {
        selectPulse.setValue(valuePulse);
    }

    public LiveData<Integer> getSelectPulse()
    {
        return selectPulse;
    }

    public LevelResult getBPLevel()
    {
        Integer valueSYS = selectSYS.getValue();
        Integer valueDIA = selectDIA.getValue();
        String nameRes = "";
        String levelRes = "";
        if(valueSYS != null && valueDIA != null)
        {
            if(valueSYS < 90 || valueDIA < 60)
            {
                nameRes = application.getString(R.string.bPressure_low);
                levelRes = application.getString(R.string.bPressure_level_low);
            }
            else if(valueSYS < 120 && valueDIA < 80)
            {
                nameRes = application.getString(R.string.bPressure_normal);
                levelRes = application.getString(R.string.bPressure_level_normal);
            }
            else if(valueSYS < 130  || valueDIA < 80)
            {
                nameRes = application.getString(R.string.bPressure_high);
                levelRes = application.getString(R.string.bPressure_level_high);
            }
            else if(valueSYS < 140 || valueDIA < 90)
            {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage1);
            }
            else if(valueSYS <= 180 || valueDIA <= 120)
            {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage2);
            }
            else
            {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage3);
            }
        }
        return new LevelResult(nameRes, levelRes);
    }

    public RecordBPFRMViewModel(Application application) {
        this.application= application;
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new BPressureRepository(healthDB);
    }

    public void insertBPressure(BloodPressure bloodPressure) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->
                repository.insertBPressure(bloodPressure));
        executorService.shutdown();
    }
}
