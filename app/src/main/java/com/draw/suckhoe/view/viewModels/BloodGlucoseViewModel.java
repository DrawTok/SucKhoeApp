package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.R;
import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.utils.LevelResult;
import com.draw.suckhoe.repository.BGlucoseRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BloodGlucoseViewModel extends ViewModel {
    private final BGlucoseRepository repository;

    private final Application application;

    private final MutableLiveData<Float> valueLv = new MutableLiveData<>();
    private final MutableLiveData<List<BloodGlucose>> bloodGlucoseListLiveData = new MutableLiveData<>();
    private final MutableLiveData<LevelResult> levelResultLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>();

    public BloodGlucoseViewModel(Application application) {
        this.application = application;
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new BGlucoseRepository(healthDB);
    }

    public LiveData<Float> getValueLv() {
        return valueLv;
    }

    public void setGlucoseLevel(float value) {
        valueLv.setValue(value);
        calculateLevelResult(); // Gọi hàm tính LevelResult sau khi giá trị selectSYS được thay đổi
    }

    public LiveData<Boolean> getInsertResultLiveData() {
        return isSuccessLiveData;
    }

    private void calculateLevelResult() {
        float value = 0;
        if (valueLv.getValue() != null)
            value = valueLv.getValue();
        String name, level;
        int type;
        if (value < 4f) {
            name = application.getString(R.string.bGlucose_low);
            level = application.getString(R.string.bGlucose_level_low);
            type = 1;
        } else if (value < 5.5) {
            name = application.getString(R.string.bGlucose_normal);
            level = application.getString(R.string.bGlucose_level_normal);
            type = 2;
        } else if (value < 7f) {
            name = application.getString(R.string.bGlucose_preDiabetes);
            level = application.getString(R.string.bGlucose_level_preDiabetes);
            type = 3;
        } else {
            name = application.getString(R.string.bGlucose_diabetes);
            level = application.getString(R.string.bGlucose_level_diabetes);
            type = 4;
        }
        LevelResult result = new LevelResult(name, level, type);
        levelResultLiveData.setValue(result);

    }

    public LiveData<LevelResult> getLevelResultLiveData() {
        return levelResultLiveData;
    }

    public LiveData<List<BloodGlucose>> getBloodGlucoseListLiveData() {
        return bloodGlucoseListLiveData;
    }

    public void insertBGlucose(BloodGlucose bloodGlucose) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                repository.insertBGlucose(bloodGlucose);
                isSuccessLiveData.postValue(true);
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessLiveData.postValue(false);
            } finally {
                executorService.shutdown();
            }
        });
    }

    public void deleteBGlucose(BloodGlucose bloodGlucose) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                repository.deleteBGlucose(bloodGlucose);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        });
    }

    public void getListDataGlucose() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                List<BloodGlucose> bloodGlucoseList = repository.getAllDataBGlucose();
                bloodGlucoseListLiveData.postValue(bloodGlucoseList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        });
    }
}
