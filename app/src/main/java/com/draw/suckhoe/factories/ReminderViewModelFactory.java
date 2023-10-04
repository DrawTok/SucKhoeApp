package com.draw.suckhoe.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;
import com.draw.suckhoe.view.viewModels.ReminderViewModel;

public class ReminderViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ReminderViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReminderViewModel.class)) {
            return (T) new ReminderViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
