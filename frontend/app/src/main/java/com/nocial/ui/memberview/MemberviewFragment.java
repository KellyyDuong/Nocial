package com.nocial.ui.memberview;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.nocial.R;
import com.nocial.data.LoginRepository;
import com.nocial.ui.customize.CustomizeFragment;

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

import org.w3c.dom.Text;

public class MemberviewFragment extends Fragment {

    private TextView mFullName;
    private TextView mUserName;
    private TextView mTotalScore;
    private TextView mAppUsage;

    private String user;
    final private OkHttpClient client = new OkHttpClient();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_memberview, container, false);


        return root;
    }

    /**
     * Connects to Flask server and displays user data in TextView in ProfileViewModel
     */




    /**
     * This method is for displaying Group data
     */


}
