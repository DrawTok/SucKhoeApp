package com.draw.suckhoe.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.view.viewModels.BMIViewModel;
import com.draw.suckhoe.view.viewModels.BloodGlucoseViewModel;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;
import com.draw.suckhoe.view.viewModels.ItemViewModel;
import com.draw.suckhoe.view.viewModels.ReminderViewModel;
import com.draw.suckhoe.view.viewModels.TitleViewModel;
import com.draw.suckhoe.view.viewModels.WaterViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == BloodPressureViewModel.class) {
            return (T) new BloodPressureViewModel(application);
        }else if (modelClass == ReminderViewModel.class) {
            return (T) new ReminderViewModel(application);
        }else if(modelClass.isAssignableFrom(BloodGlucoseViewModel.class))
            return (T) new BloodGlucoseViewModel(application);
        else if(modelClass.isAssignableFrom(BMIViewModel.class))
            return (T) new BMIViewModel(application);
        else if(modelClass.isAssignableFrom(WaterViewModel.class))
            return (T) new WaterViewModel(application);
        else if(modelClass.isAssignableFrom(TitleViewModel.class))
            return (T) new TitleViewModel(application);
        else if(modelClass.isAssignableFrom(ItemViewModel.class))
            return (T) new ItemViewModel(application);
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
