package com.nocial.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.widget.ImageView;


public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mUserName;
    private MutableLiveData<String> mFullName;
    private MutableLiveData<String> mTotalScore;
    private MutableLiveData<String> mAppUsage;
    private MutableLiveData<String> progressBar;

    public ProfileViewModel() {
        mFullName = new MutableLiveData<>();
        mUserName = new MutableLiveData<>();
        mTotalScore = new MutableLiveData<>();
        mAppUsage = new MutableLiveData<>();
        progressBar = new MutableLiveData<>();
    }

    public MutableLiveData<String> getUserNameLiveData() { return mUserName; }
    public MutableLiveData<String> getFullNameLiveData() { return mFullName; }
    public MutableLiveData<String> getTotalScoreLiveData() {
        return mTotalScore;
    }
    public MutableLiveData<String> getAppUsageLiveData() {
        return mAppUsage;
    }

    public void setmTotalScore(String text) {
        mTotalScore.postValue(text);
    }
    public void setmUserName(String text) { mUserName.postValue(text);}
    public void setmFullName(String text) { mFullName.postValue(text);}
    public void setmAppUsage(String text) { mAppUsage.postValue(text);}
    public void setProgressBar(String text) { progressBar.postValue(text);}
}