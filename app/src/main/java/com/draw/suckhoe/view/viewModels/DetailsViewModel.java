package com.draw.suckhoe.view.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class DetailsViewModel extends ViewModel {

    public final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isVisibility = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();


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

    public LiveData<String> getLiveDataTitle()
    {
        return title;
    }

    public void setTitle(String titleName) {
        title.setValue(titleName);
    }
}
