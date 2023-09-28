package com.draw.suckhoe.view.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class DetailsViewModel extends ViewModel {

    private String title;
    private final MutableLiveData<Boolean> isVisibility = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LiveData<Boolean> getIsVisibility() {
        return isVisibility;
    }

    public void setIsVisibility(Boolean visibility) {
        isVisibility.setValue(visibility);
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public void onClickBack()
    {
        navigateBack.setValue(true);
    }
}
