package com.draw.suckhoe.view.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draw.suckhoe.database.HealthDB;
import com.draw.suckhoe.model.Reminder;
import com.draw.suckhoe.repository.ReminderRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderViewModel extends ViewModel {

    private final ReminderRepository repository;
    private final MutableLiveData<List<Reminder>> reminderListLiveData = new MutableLiveData<>();

    public ReminderViewModel(Application application) {
        HealthDB healthDB = HealthDB.getInstance(application);
        repository = new ReminderRepository(healthDB);
    }

    public LiveData<List<Reminder>> getReminderListLiveData() {
        return reminderListLiveData;
    }

    public void insertReminder(Reminder reminder)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            repository.insertReminder(reminder);
            getListDataReminder();
        });
        executorService.shutdown();
    }

    public void updateReminder(Reminder reminder)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()-> repository.updateReminder(reminder));
        executorService.shutdown();
    }

    public void deleteReminder(int id)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()-> {
            repository.deleteReminder(id);
            getListDataReminder();
        });
        executorService.shutdown();
    }

    public void getListDataReminder()
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<Reminder> list = repository.getAllDataReminder();
            reminderListLiveData.postValue(list);
        });
    }
}
