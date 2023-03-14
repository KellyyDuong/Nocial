package com.nocial.ui.profile;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nocial.R;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView mTextView;
    private TextView mAppUsageTextView;
    private ProfileViewModel profileViewModel;
    private String user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        mTextView = root.findViewById(R.id.text_profile);
        mAppUsageTextView = root.findViewById(R.id.app_usage_text_view);

        user = "dtsela"; // example userName --> get this user's data

        profileViewModel.getUserTextLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mTextView.setText(s);
            }
        });

        profileViewModel.getAppUsageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mAppUsageTextView.setText(s);
            }
        });

        if (!checkUsageStatsPermission()){
            requestUsageStatsPermission();
        } else {
            showAppUsage();
        }

//        makeGetRequest();

        return root;
    }

    private void makeGetRequest() {
        OkHttpClient client = new OkHttpClient();

        String userUrl = ("http://10.0.2.2:5000/").concat(user);
        Request request = new Request.Builder().url(userUrl).build();

        // make async HTTP request to server
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code:" + response);
                }

                String responseText = response.body().string();
                profileViewModel.setUserText(responseText);
            }
        });
    }


    private boolean checkUsageStatsPermission(){
        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        return usageStatsManager != null;
    }

    private void requestUsageStatsPermission(){
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void showAppUsage(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        StringBuilder stringBuilder = new StringBuilder();

        // relevant package names for apps
        // instagram: com.instagram.android
        // youtube: com.google.android.youtube
        // snapchat: com.snapchat.android
        // twitter: com.twitter.android
        // reddit: com.reddit.android
        for (UsageStats usageStats : usageStatsList){
            if (usageStats.getPackageName().equals("com.android.chrome") || usageStats.getPackageName().equals("com.google.android.youtube")) {
                stringBuilder.append(usageStats.getPackageName()).append(": ").append(usageStats.getTotalTimeInForeground() / 1000).append(" seconds\n");
            }
        }
        profileViewModel.setmAppUsage(stringBuilder.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showAppUsage();
        }
    }



//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}