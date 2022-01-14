package com.wiut12860.recipemanagement.ui.WebAPI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WebAPIViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WebAPIViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}