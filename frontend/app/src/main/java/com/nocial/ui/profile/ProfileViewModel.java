package com.nocial.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.widget.ImageView;


public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mAppUsage;

    public ProfileViewModel() {
        mText= new MutableLiveData<>();
        mAppUsage = new MutableLiveData<>();
    }

    public MutableLiveData<String> getUserTextLiveData() {
        return mText;
    }

    public MutableLiveData<String> getAppUsageLiveData() {
        return mAppUsage;
    }

    public void setUserText(String text) {
        mText.postValue(text);
    }

    public void setmAppUsage(String text) {
        mAppUsage.postValue(text);
    }


}