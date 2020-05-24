package com.example.uberapp.ui.ui_driver.gallery_drive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel_drive extends ViewModel {

    private MutableLiveData<String> mText;

    public GalleryViewModel_drive() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}