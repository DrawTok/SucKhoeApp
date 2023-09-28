package com.draw.suckhoe.view.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private char id;
    private final MutableLiveData<Character> idFragment = new MutableLiveData<>();

    public HomeViewModel(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public LiveData<Character> getIdFragment()
    {
        return idFragment;
    }

    public void setIdFragment(char idFRM)
    {
        idFragment.setValue(idFRM);
    }
}
