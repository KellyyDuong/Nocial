package com.nocial.ui.profile;

import android.app.AppOpsManager;
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
import androidx.lifecycle.ViewModelProvider;

import com.nocial.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

public class ProfileFragment extends Fragment {

    private TextView mTextView;
    private TextView mAppUsageTextView;
    private ProfileViewModel profileViewModel;
    private String user;
    final private OkHttpClient client = new OkHttpClient();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        mTextView = root.findViewById(R.id.text_profile);
        mAppUsageTextView = root.findViewById(R.id.app_usage_text_view);

        user = "dtsela"; // example userName --> get this user's data

        profileViewModel.getUserTextLiveData().observe(getViewLifecycleOwner(), s -> mTextView.setText(s));

        profileViewModel.getAppUsageLiveData().observe(getViewLifecycleOwner(), s -> mAppUsageTextView.setText(s));

        Context context = requireContext();
        if (!checkUsageStatsPermission(context)) {
            requestUsageStatsPermission();
        } else {
            showAppUsage();
        }

        makeGetRequest();

        return root;
    }

    /**
     * Connects to Flask server and displays user data in TextView in ProfileViewModel
     */
    private void makeGetRequest() {

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
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                }

                String responseText = response.body().string();
                profileViewModel.setUserText(responseText);
            }
        });
    }


    /**
     * Returns boolean value for whether or not app has permission to gather data from API
     * @return the permission state
     */
    private boolean checkUsageStatsPermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    /**
     * Requests permission from Android OS to gather app usage data
     */
    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    /**
     * Sets String contents in ViewModel to display package name and seconds in foreground used
     */
    private void showAppUsage() {

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
        List<String> appsToCheck = new ArrayList<>();
        appsToCheck.add("Reddit");
        appsToCheck.add("Instagram");
        appsToCheck.add("TikTok");
        appsToCheck.add("SnapChat");
        appsToCheck.add("Twitter");

        for (UsageStats usageStats : usageStatsList) {
            for (int i = 0; i < appsToCheck.size(); i++) {
                if ( usageStats.getPackageName().toLowerCase().contains( appsToCheck.get(i).toLowerCase() ) ) {
                    String packageName = appsToCheck.get(i);
                    stringBuilder.append(packageName).append(": ").append(usageStats.getTotalTimeInForeground() / 1000).append(" seconds\n");
                }
            }
        }
        profileViewModel.setmAppUsage(stringBuilder.toString());

        RequestBody formBody = new FormBody.Builder().add("userData", stringBuilder.toString()).build();
        Request request = new Request.Builder().url("http://10.0.2.2:5000/updateTotalScore/"+user).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                }
            }
        });

    }

}