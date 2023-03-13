package com.nocial.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.widget.ImageView;


public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText = new MutableLiveData<>();


//    public ProfileViewModel() {
//    }

    public MutableLiveData<String> getTextLiveData() {
        return mText;
    }

    public void setText(String text) {
        mText.postValue(text);
    }
}