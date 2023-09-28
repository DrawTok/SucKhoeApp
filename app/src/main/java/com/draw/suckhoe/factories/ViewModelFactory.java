package com.draw.suckhoe.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.view.viewModels.RecordBPFRMViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecordBPFRMViewModel.class)) {
            return (T) new RecordBPFRMViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
