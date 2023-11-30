package com.draw.suckhoe.view.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Character> idFragment = new MutableLiveData<>();



    public LiveData<Character> getIdFragment()
    {
        return idFragment;
    }

    public void setIdFragment(char idFRM)
    {
        idFragment.setValue(idFRM);
    }
}
