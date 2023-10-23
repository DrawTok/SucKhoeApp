package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.repository.BPressureRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BloodPressureViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectSYS = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectDIA = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectPulse = new MutableLiveData<>();
    private final BPressureRepository repository;
    private final Application application;
    private final MutableLiveData<List<BloodPressure>> bloodPressureListLiveData = new MutableLiveData<>();
    private final MutableLiveData<LevelResult> levelResultLiveData = new MutableLiveData<>(); // Thêm LiveData cho LevelResult

    public void setSelectSYS(int valueSYS) {
        selectSYS.setValue(valueSYS);
        calculateBPLevel(); // Gọi hàm tính LevelResult sau khi giá trị selectSYS được thay đổi
    }

    public LiveData<Integer> getSelectSYS() {
        return selectSYS;
    }

    public void setSelectDIA(int valueDIA) {
        selectDIA.setValue(valueDIA);
        calculateBPLevel(); // Gọi hàm tính LevelResult sau khi giá trị selectDIA được thay đổi
    }

    public LiveData<Integer> getSelectDIA() {
        return selectDIA;
    }

    public void setSelectPulse(int valuePulse) {
        selectPulse.setValue(valuePulse);
    }

    public LiveData<Integer> getSelectPulse() {
        return selectPulse;
    }

    public LiveData<LevelResult> getLevelResultLiveData() {
        return levelResultLiveData;
    }

    private void calculateBPLevel() {
        Integer valueSYS = selectSYS.getValue();
        Integer valueDIA = selectDIA.getValue();
        String nameRes = "";
        String levelRes = "";
        int type = -1;
        if (valueSYS != null && valueDIA != null) {
            if (valueSYS < 90 || valueDIA < 60) {
                nameRes = application.getString(R.string.bPressure_low);
                levelRes = application.getString(R.string.bPressure_level_low);
                type = 1;
            } else if (valueSYS < 120 && valueDIA < 80) {
                nameRes = application.getString(R.string.bPressure_normal);
                levelRes = application.getString(R.string.bPressure_level_normal);
                type = 2;
            } else if (valueSYS < 130 || valueDIA < 80) {
                nameRes = application.getString(R.string.bPressure_high);
                levelRes = application.getString(R.string.bPressure_level_high);
                type = 3;
            } else if (valueSYS < 140 || valueDIA < 90) {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage1);
                type = 4;
            } else if (valueSYS <= 180 || valueDIA <= 120) {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage2);
                type = 5;
            } else {
                nameRes = application.getString(R.string.bPressure_stage);
                levelRes = application.getString(R.string.bPressure_level_stage3);
                type = 6;
            }
        }
        LevelResult result = new LevelResult(nameRes, levelRes, type);
        levelResultLiveData.setValue(result); // Cập nhật LiveData cho LevelResult
    }

    public BloodPressureViewModel(Application application) {
        this.application = application;
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new BPressureRepository(healthDB);
    }

    public LiveData<List<BloodPressure>> getBloodPressureListLiveData() {
        return bloodPressureListLiveData;
    }

    public void insertBPressure(BloodPressure bloodPressure) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->
                repository.insertBPressure(bloodPressure));
        executorService.shutdown();
    }

    public void deleteBPressure(BloodPressure bloodPressure) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->
        {
            try{
                repository.deleteBPressure(bloodPressure);
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                executorService.shutdown();
            }
        });
    }

    public void getListDataBPressure() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<BloodPressure> bloodPressureList = repository.getAllDataBPressure();
            bloodPressureListLiveData.postValue(bloodPressureList);
        });
    }
}
